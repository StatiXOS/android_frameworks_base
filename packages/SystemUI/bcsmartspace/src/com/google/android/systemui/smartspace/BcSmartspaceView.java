package com.google.android.systemui.smartspace;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceTarget;
import android.app.smartspace.SmartspaceTargetEvent;
import android.content.ComponentName;
import android.content.Context;
import android.database.ContentObserver;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.graphics.drawable.Drawable;
import android.os.Debug;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.slice.view.R$plurals;
import androidx.viewpager.widget.ViewPager;
import com.android.settingslib.media.MediaDevice;
import com.android.systemui.R;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.plugins.FalsingManager;
import com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda20;
import com.google.android.systemui.smartspace.CardPagerAdapter;
import com.google.android.systemui.smartspace.logging.BcSmartspaceCardLogger;
import com.google.android.systemui.smartspace.logging.BcSmartspaceCardLoggerUtil;
import com.google.android.systemui.smartspace.logging.BcSmartspaceCardLoggingInfo;
import com.google.android.systemui.smartspace.logging.BcSmartspaceSubcardLoggingInfo;
import com.google.android.systemui.smartspace.uitemplate.BaseTemplateCard;
import java.time.DateTimeException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
/* loaded from: classes2.dex */
public class BcSmartspaceView extends FrameLayout implements BcSmartspaceDataPlugin.SmartspaceTargetListener, BcSmartspaceDataPlugin.SmartspaceView {
    public static final boolean DEBUG = Log.isLoggable("BcSmartspaceView", 3);
    public final CardPagerAdapter mAdapter;
    public boolean mAnimateSmartspaceUpdate;
    public final AnonymousClass1 mAodObserver;
    public int mCardPosition;
    public BcSmartspaceDataPlugin mDataProvider;
    public boolean mIsAodEnabled;
    public ArraySet<String> mLastReceivedTargets;
    public final AnonymousClass2 mOnPageChangeListener;
    public PageIndicator mPageIndicator;
    public List<? extends Parcelable> mPendingTargets;
    public float mPreviousDozeAmount;
    public Animator mRunningAnimation;
    public int mScrollState;
    public ViewPager mViewPager;

    public final void animateSmartspaceUpdate(final ConstraintLayout constraintLayout) {
        if (this.mRunningAnimation == null && constraintLayout.getParent() == null) {
            final ViewGroup viewGroup = (ViewGroup) this.mViewPager.getParent();
            constraintLayout.measure(View.MeasureSpec.makeMeasureSpec(this.mViewPager.getWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(this.mViewPager.getHeight(), 1073741824));
            constraintLayout.layout(this.mViewPager.getLeft(), this.mViewPager.getTop(), this.mViewPager.getRight(), this.mViewPager.getBottom());
            AnimatorSet animatorSet = new AnimatorSet();
            float dimension = getContext().getResources().getDimension(R.dimen.enhanced_smartspace_dismiss_margin);
            animatorSet.play(ObjectAnimator.ofFloat(constraintLayout, View.TRANSLATION_Y, 0.0f, (-getHeight()) - dimension));
            animatorSet.play(ObjectAnimator.ofFloat(constraintLayout, View.ALPHA, 1.0f, 0.0f));
            animatorSet.play(ObjectAnimator.ofFloat(this.mViewPager, View.TRANSLATION_Y, getHeight() + dimension, 0.0f));
            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.smartspace.BcSmartspaceView.3
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    constraintLayout.setTranslationY(0.0f);
                    constraintLayout.setAlpha(1.0f);
                    viewGroup.getOverlay().remove(constraintLayout);
                    BcSmartspaceView bcSmartspaceView = BcSmartspaceView.this;
                    bcSmartspaceView.mRunningAnimation = null;
                    bcSmartspaceView.mAnimateSmartspaceUpdate = false;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationStart(Animator animator) {
                    viewGroup.getOverlay().add(constraintLayout);
                }
            });
            this.mRunningAnimation = animatorSet;
            animatorSet.start();
        }
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public final int getCurrentCardTopPadding() {
        BcSmartspaceCard bcSmartspaceCard;
        BaseTemplateCard baseTemplateCard;
        CardPagerAdapter.ViewHolder viewHolder = this.mAdapter.mViewHolders.get(this.mViewPager.mCurItem);
        ViewGroup viewGroup = null;
        if (viewHolder == null) {
            bcSmartspaceCard = null;
        } else {
            bcSmartspaceCard = viewHolder.mLegacyCard;
        }
        if (bcSmartspaceCard != null) {
            CardPagerAdapter.ViewHolder viewHolder2 = this.mAdapter.mViewHolders.get(this.mViewPager.mCurItem);
            if (viewHolder2 != null) {
                viewGroup = viewHolder2.mLegacyCard;
            }
            return viewGroup.getPaddingTop();
        }
        CardPagerAdapter.ViewHolder viewHolder3 = this.mAdapter.mViewHolders.get(this.mViewPager.mCurItem);
        if (viewHolder3 == null) {
            baseTemplateCard = null;
        } else {
            baseTemplateCard = viewHolder3.mCard;
        }
        if (baseTemplateCard != null) {
            CardPagerAdapter.ViewHolder viewHolder4 = this.mAdapter.mViewHolders.get(this.mViewPager.mCurItem);
            if (viewHolder4 != null) {
                viewGroup = viewHolder4.mCard;
            }
            return viewGroup.getPaddingTop();
        }
        return 0;
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public final int getSelectedPage() {
        return this.mViewPager.mCurItem;
    }

    public final void logSmartspaceEvent(SmartspaceTarget smartspaceTarget, int i, BcSmartspaceEvent bcSmartspaceEvent) {
        int i2;
        BcSmartspaceSubcardLoggingInfo createSubcardLoggingInfo;
        if (bcSmartspaceEvent == BcSmartspaceEvent.SMARTSPACE_CARD_RECEIVED) {
            try {
                i2 = (int) Instant.now().minusMillis(smartspaceTarget.getCreationTimeMillis()).toEpochMilli();
            } catch (ArithmeticException | DateTimeException e) {
                Log.e("BcSmartspaceView", "received_latency_millis will be -1 due to exception ", e);
                i2 = -1;
            }
        } else {
            i2 = 0;
        }
        boolean containsValidTemplateType = BcSmartspaceCardLoggerUtil.containsValidTemplateType(smartspaceTarget.getTemplateData());
        BcSmartspaceCardLoggingInfo.Builder builder = new BcSmartspaceCardLoggingInfo.Builder();
        builder.mInstanceId = R$plurals.create(smartspaceTarget);
        builder.mFeatureType = smartspaceTarget.getFeatureType();
        String packageName = getContext().getPackageName();
        CardPagerAdapter cardPagerAdapter = this.mAdapter;
        builder.mDisplaySurface = BcSmartSpaceUtil.getLoggingDisplaySurface(packageName, cardPagerAdapter.mIsDreaming, cardPagerAdapter.mDozeAmount);
        builder.mRank = i;
        builder.mCardinality = this.mAdapter.getCount();
        builder.mReceivedLatency = i2;
        builder.mUid = BcSmartspaceCardLoggerUtil.getUid(getContext().getPackageManager(), smartspaceTarget);
        if (containsValidTemplateType) {
            createSubcardLoggingInfo = BcSmartspaceCardLoggerUtil.createSubcardLoggingInfo(smartspaceTarget.getTemplateData());
        } else {
            createSubcardLoggingInfo = BcSmartspaceCardLoggerUtil.createSubcardLoggingInfo(smartspaceTarget);
        }
        builder.mSubcardInfo = createSubcardLoggingInfo;
        builder.mDimensionalInfo = BcSmartspaceCardLoggerUtil.createDimensionalLoggingInfo(smartspaceTarget.getTemplateData());
        BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo = new BcSmartspaceCardLoggingInfo(builder);
        if (containsValidTemplateType) {
            BcSmartspaceCardLoggerUtil.tryForcePrimaryFeatureTypeOrUpdateLogInfoFromTemplateData(bcSmartspaceCardLoggingInfo, smartspaceTarget.getTemplateData());
        } else {
            BcSmartspaceCardLoggerUtil.tryForcePrimaryFeatureTypeAndInjectWeatherSubcard(bcSmartspaceCardLoggingInfo, smartspaceTarget);
        }
        BcSmartspaceCardLogger.log(bcSmartspaceEvent, bcSmartspaceCardLoggingInfo);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mViewPager.setAdapter(this.mAdapter);
        ViewPager viewPager = this.mViewPager;
        AnonymousClass2 anonymousClass2 = this.mOnPageChangeListener;
        if (viewPager.mOnPageChangeListeners == null) {
            viewPager.mOnPageChangeListeners = new ArrayList();
        }
        viewPager.mOnPageChangeListeners.add(anonymousClass2);
        this.mPageIndicator.setNumPages(this.mAdapter.getCount(), isLayoutRtl());
        try {
            boolean z = false;
            getContext().getContentResolver().registerContentObserver(Settings.Secure.getUriFor("doze_always_on"), false, this.mAodObserver, -1);
            Context context = getContext();
            if (Settings.Secure.getIntForUser(context.getContentResolver(), "doze_always_on", 0, context.getUserId()) == 1) {
                z = true;
            }
            this.mIsAodEnabled = z;
        } catch (Exception e) {
            Log.w("BcSmartspaceView", "Unable to register Doze Always on content observer.", e);
        }
        BcSmartspaceDataPlugin bcSmartspaceDataPlugin = this.mDataProvider;
        if (bcSmartspaceDataPlugin != null) {
            registerDataProvider(bcSmartspaceDataPlugin);
        }
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceTargetListener
    public final void onSmartspaceTargetsUpdated(List<? extends Parcelable> list) {
        int i;
        BaseTemplateCard baseTemplateCard;
        BcSmartspaceCard bcSmartspaceCard;
        if (DEBUG) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline0.m("@");
            m.append(Integer.toHexString(hashCode()));
            m.append(", onTargetsAvailable called. Callers = ");
            m.append(Debug.getCallers(5));
            Log.d("BcSmartspaceView", m.toString());
            Log.d("BcSmartspaceView", "    targets.size() = " + list.size());
            Log.d("BcSmartspaceView", "    targets = " + list.toString());
        }
        if (this.mScrollState != 0 && this.mAdapter.getCount() > 1) {
            this.mPendingTargets = list;
            return;
        }
        this.mPendingTargets = null;
        boolean isLayoutRtl = isLayoutRtl();
        int i2 = this.mViewPager.mCurItem;
        if (isLayoutRtl) {
            i = this.mAdapter.getCount() - i2;
            ArrayList arrayList = new ArrayList(list);
            Collections.reverse(arrayList);
            list = arrayList;
        } else {
            i = i2;
        }
        CardPagerAdapter.ViewHolder viewHolder = this.mAdapter.mViewHolders.get(i2);
        if (viewHolder == null) {
            baseTemplateCard = null;
        } else {
            baseTemplateCard = viewHolder.mCard;
        }
        CardPagerAdapter.ViewHolder viewHolder2 = this.mAdapter.mViewHolders.get(i2);
        if (viewHolder2 == null) {
            bcSmartspaceCard = null;
        } else {
            bcSmartspaceCard = viewHolder2.mLegacyCard;
        }
        CardPagerAdapter cardPagerAdapter = this.mAdapter;
        cardPagerAdapter.mAODTargets.clear();
        cardPagerAdapter.mLockscreenTargets.clear();
        cardPagerAdapter.mHasDifferentTargets = false;
        cardPagerAdapter.mNextAlarmData.mHolidayAlarmsTarget = null;
        list.forEach(new WifiPickerTracker$$ExternalSyntheticLambda20(4, cardPagerAdapter));
        cardPagerAdapter.addDefaultDateCardIfEmpty(cardPagerAdapter.mAODTargets);
        cardPagerAdapter.addDefaultDateCardIfEmpty(cardPagerAdapter.mLockscreenTargets);
        cardPagerAdapter.updateTargetVisibility();
        cardPagerAdapter.notifyDataSetChanged();
        int count = this.mAdapter.getCount();
        PageIndicator pageIndicator = this.mPageIndicator;
        if (pageIndicator != null) {
            pageIndicator.setNumPages(count, isLayoutRtl);
        }
        if (isLayoutRtl) {
            int max = Math.max(0, Math.min(count - 1, count - i));
            this.mViewPager.setCurrentItem(max, false);
            this.mPageIndicator.setPageOffset(max, 0.0f);
        }
        if (this.mAnimateSmartspaceUpdate) {
            if (baseTemplateCard != null) {
                animateSmartspaceUpdate(baseTemplateCard);
            } else if (bcSmartspaceCard != null) {
                animateSmartspaceUpdate(bcSmartspaceCard);
            }
        }
        for (int i3 = 0; i3 < count; i3++) {
            SmartspaceTarget targetAtPosition = this.mAdapter.getTargetAtPosition(i3);
            if (!this.mLastReceivedTargets.contains(targetAtPosition.getSmartspaceTargetId())) {
                logSmartspaceEvent(targetAtPosition, i3, BcSmartspaceEvent.SMARTSPACE_CARD_RECEIVED);
                SmartspaceTargetEvent.Builder builder = new SmartspaceTargetEvent.Builder(8);
                builder.setSmartspaceTarget(targetAtPosition);
                SmartspaceAction baseAction = targetAtPosition.getBaseAction();
                if (baseAction != null) {
                    builder.setSmartspaceActionId(baseAction.getId());
                }
                this.mDataProvider.notifySmartspaceEvent(builder.build());
            }
        }
        this.mLastReceivedTargets.clear();
        this.mLastReceivedTargets.addAll((Collection) this.mAdapter.mSmartspaceTargets.stream().map(new Function() { // from class: com.android.systemui.media.dialog.MediaOutputController$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                switch (r1) {
                    case 0:
                        return new MediaItem((MediaDevice) obj);
                    default:
                        return ((SmartspaceTarget) obj).getSmartspaceTargetId();
                }
            }
        }).collect(Collectors.toList()));
        this.mAdapter.notifyDataSetChanged();
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public final void registerDataProvider(BcSmartspaceDataPlugin bcSmartspaceDataPlugin) {
        BcSmartspaceDataPlugin bcSmartspaceDataPlugin2 = this.mDataProvider;
        if (bcSmartspaceDataPlugin2 != null) {
            bcSmartspaceDataPlugin2.unregisterListener(this);
        }
        this.mDataProvider = bcSmartspaceDataPlugin;
        bcSmartspaceDataPlugin.registerListener(this);
        this.mAdapter.mDataProvider = this.mDataProvider;
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public final void setDnd(Drawable drawable, String str) {
        CardPagerAdapter cardPagerAdapter = this.mAdapter;
        cardPagerAdapter.mDndImage = drawable;
        cardPagerAdapter.mDndDescription = str;
        cardPagerAdapter.refreshCards();
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public final void setDozeAmount(float f) {
        boolean z;
        int i;
        CardPagerAdapter cardPagerAdapter = this.mAdapter;
        ArrayList arrayList = cardPagerAdapter.mSmartspaceTargets;
        cardPagerAdapter.setDozeAmount(f);
        this.mPageIndicator.setNumPages(this.mAdapter.getCount(), isLayoutRtl());
        if (this.mAdapter.mHasAodLockscreenTransition) {
            float f2 = this.mPreviousDozeAmount;
            if (f2 > f) {
                setAlpha(f);
                z = true;
            } else {
                if (f2 < f) {
                    setAlpha(1.0f - f);
                }
                z = false;
            }
            if (getAlpha() == 0.0f) {
                this.mPageIndicator.setAlpha(1.0f);
                animate().alpha(1.0f).setDuration(100L).start();
            }
        } else {
            setAlpha(1.0f);
            z = false;
        }
        if (z && f != 0.0f) {
            this.mPageIndicator.setAlpha(0.0f);
        } else {
            this.mPageIndicator.setAlpha(1.0f - f);
        }
        this.mPreviousDozeAmount = f;
        CardPagerAdapter cardPagerAdapter2 = this.mAdapter;
        if (cardPagerAdapter2.mHasDifferentTargets && cardPagerAdapter2.mSmartspaceTargets != arrayList && cardPagerAdapter2.getCount() > 0) {
            if (isLayoutRtl()) {
                i = this.mAdapter.getCount() - 1;
            } else {
                i = 0;
            }
            this.mViewPager.setCurrentItem(i, false);
            this.mPageIndicator.setPageOffset(i, 0.0f);
        }
        String packageName = getContext().getPackageName();
        CardPagerAdapter cardPagerAdapter3 = this.mAdapter;
        int loggingDisplaySurface = BcSmartSpaceUtil.getLoggingDisplaySurface(packageName, cardPagerAdapter3.mIsDreaming, cardPagerAdapter3.mDozeAmount);
        if (loggingDisplaySurface == -1) {
            return;
        }
        if (loggingDisplaySurface == 3 && !this.mIsAodEnabled) {
            return;
        }
        if (DEBUG) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline0.m("@");
            m.append(Integer.toHexString(hashCode()));
            m.append(", setDozeAmount: Logging SMARTSPACE_CARD_SEEN, currentSurface = ");
            m.append(loggingDisplaySurface);
            Log.d("BcSmartspaceView", m.toString());
        }
        BcSmartspaceEvent bcSmartspaceEvent = BcSmartspaceEvent.SMARTSPACE_CARD_SEEN;
        SmartspaceTarget targetAtPosition = this.mAdapter.getTargetAtPosition(this.mCardPosition);
        if (targetAtPosition == null) {
            Log.w("BcSmartspaceView", "Current card is not present in the Adapter; cannot log.");
        } else {
            logSmartspaceEvent(targetAtPosition, this.mCardPosition, bcSmartspaceEvent);
        }
        if (this.mAdapter.mNextAlarmData.mImage != null) {
            logSmartspaceEvent(new SmartspaceTarget.Builder("upcoming_alarm_card_94510_12684", new ComponentName(getContext(), getClass()), getContext().getUser()).setFeatureType(23).build(), 0, bcSmartspaceEvent);
            if (!TextUtils.isEmpty(this.mAdapter.mNextAlarmData.getHolidayAlarmText(null))) {
                logSmartspaceEvent(this.mAdapter.mNextAlarmData.mHolidayAlarmsTarget, 0, bcSmartspaceEvent);
            }
        }
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public final void setIsDreaming(boolean z) {
        this.mAdapter.mIsDreaming = z;
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public final void setKeyguardBypassEnabled(boolean z) {
        CardPagerAdapter cardPagerAdapter = this.mAdapter;
        cardPagerAdapter.mKeyguardBypassEnabled = z;
        cardPagerAdapter.updateTargetVisibility();
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public final void setMediaTarget(SmartspaceTarget smartspaceTarget) {
        CardPagerAdapter cardPagerAdapter = this.mAdapter;
        cardPagerAdapter.mMediaTargets.clear();
        if (smartspaceTarget != null) {
            cardPagerAdapter.mMediaTargets.add(smartspaceTarget);
        }
        cardPagerAdapter.updateTargetVisibility();
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public final void setNextAlarm(Drawable drawable, String str) {
        CardPagerAdapter cardPagerAdapter = this.mAdapter;
        BcNextAlarmData bcNextAlarmData = cardPagerAdapter.mNextAlarmData;
        bcNextAlarmData.mImage = drawable;
        if (drawable != null) {
            drawable.mutate();
        }
        bcNextAlarmData.mDescription = str;
        cardPagerAdapter.refreshCards();
    }

    @Override // android.view.View
    public final void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.mViewPager.setOnLongClickListener(onLongClickListener);
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public final void setPrimaryTextColor(int i) {
        CardPagerAdapter cardPagerAdapter = this.mAdapter;
        cardPagerAdapter.mPrimaryTextColor = i;
        cardPagerAdapter.setDozeAmount(cardPagerAdapter.mDozeAmount);
        PageIndicator pageIndicator = this.mPageIndicator;
        pageIndicator.mPrimaryColor = i;
        for (int i2 = 0; i2 < pageIndicator.getChildCount(); i2++) {
            ((ImageView) pageIndicator.getChildAt(i2)).getDrawable().setTint(pageIndicator.mPrimaryColor);
        }
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public final void setUiSurface(String str) {
        this.mAdapter.mUiSurface = str;
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [com.google.android.systemui.smartspace.BcSmartspaceView$1] */
    /* JADX WARN: Type inference failed for: r1v5, types: [com.google.android.systemui.smartspace.BcSmartspaceView$2] */
    public BcSmartspaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mLastReceivedTargets = new ArraySet<>();
        this.mIsAodEnabled = false;
        this.mCardPosition = 0;
        this.mPreviousDozeAmount = 0.0f;
        this.mAnimateSmartspaceUpdate = false;
        this.mScrollState = 0;
        this.mAodObserver = new ContentObserver(new Handler()) { // from class: com.google.android.systemui.smartspace.BcSmartspaceView.1
            @Override // android.database.ContentObserver
            public final void onChange(boolean z) {
                BcSmartspaceView bcSmartspaceView = BcSmartspaceView.this;
                boolean z2 = BcSmartspaceView.DEBUG;
                Context context2 = bcSmartspaceView.getContext();
                boolean z3 = false;
                if (Settings.Secure.getIntForUser(context2.getContentResolver(), "doze_always_on", 0, context2.getUserId()) == 1) {
                    z3 = true;
                }
                bcSmartspaceView.mIsAodEnabled = z3;
            }
        };
        this.mAdapter = new CardPagerAdapter(this);
        this.mOnPageChangeListener = new ViewPager.OnPageChangeListener() { // from class: com.google.android.systemui.smartspace.BcSmartspaceView.2
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public final void onPageScrollStateChanged(int i) {
                List<? extends Parcelable> list;
                BcSmartspaceView bcSmartspaceView = BcSmartspaceView.this;
                bcSmartspaceView.mScrollState = i;
                if (i == 0 && (list = bcSmartspaceView.mPendingTargets) != null) {
                    bcSmartspaceView.onSmartspaceTargetsUpdated(list);
                }
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public final void onPageScrolled(float f, int i, int i2) {
                PageIndicator pageIndicator = BcSmartspaceView.this.mPageIndicator;
                if (pageIndicator != null) {
                    pageIndicator.setPageOffset(i, f);
                }
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public final void onPageSelected(int i) {
                BcSmartspaceView bcSmartspaceView = BcSmartspaceView.this;
                SmartspaceTarget targetAtPosition = bcSmartspaceView.mAdapter.getTargetAtPosition(bcSmartspaceView.mCardPosition);
                BcSmartspaceView bcSmartspaceView2 = BcSmartspaceView.this;
                bcSmartspaceView2.mCardPosition = i;
                SmartspaceTarget targetAtPosition2 = bcSmartspaceView2.mAdapter.getTargetAtPosition(i);
                BcSmartspaceView bcSmartspaceView3 = BcSmartspaceView.this;
                bcSmartspaceView3.logSmartspaceEvent(targetAtPosition2, bcSmartspaceView3.mCardPosition, BcSmartspaceEvent.SMARTSPACE_CARD_SEEN);
                if (BcSmartspaceView.this.mDataProvider == null) {
                    Log.w("BcSmartspaceView", "Cannot notify target hidden/shown smartspace events: data provider null");
                    return;
                }
                if (targetAtPosition == null) {
                    Log.w("BcSmartspaceView", "Cannot notify target hidden smartspace event: previous target is null.");
                } else {
                    SmartspaceTargetEvent.Builder builder = new SmartspaceTargetEvent.Builder(3);
                    builder.setSmartspaceTarget(targetAtPosition);
                    SmartspaceAction baseAction = targetAtPosition.getBaseAction();
                    if (baseAction != null) {
                        builder.setSmartspaceActionId(baseAction.getId());
                    }
                    BcSmartspaceView.this.mDataProvider.notifySmartspaceEvent(builder.build());
                }
                SmartspaceTargetEvent.Builder builder2 = new SmartspaceTargetEvent.Builder(2);
                builder2.setSmartspaceTarget(targetAtPosition2);
                SmartspaceAction baseAction2 = targetAtPosition2.getBaseAction();
                if (baseAction2 != null) {
                    builder2.setSmartspaceActionId(baseAction2.getId());
                }
                BcSmartspaceView.this.mDataProvider.notifySmartspaceEvent(builder2.build());
            }
        };
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getContext().getContentResolver().unregisterContentObserver(this.mAodObserver);
        BcSmartspaceDataPlugin bcSmartspaceDataPlugin = this.mDataProvider;
        if (bcSmartspaceDataPlugin != null) {
            bcSmartspaceDataPlugin.unregisterListener(this);
        }
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mViewPager = (ViewPager) findViewById(R.id.smartspace_card_pager);
        this.mPageIndicator = (PageIndicator) findViewById(R.id.smartspace_page_indicator);
    }

    @Override // android.widget.FrameLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i2);
        int dimensionPixelSize = getContext().getResources().getDimensionPixelSize(R.dimen.enhanced_smartspace_height);
        if (size > 0 && size < dimensionPixelSize) {
            float f = size;
            float f2 = dimensionPixelSize;
            float f3 = f / f2;
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(Math.round(View.MeasureSpec.getSize(i) / f3), 1073741824), View.MeasureSpec.makeMeasureSpec(dimensionPixelSize, 1073741824));
            setScaleX(f3);
            setScaleY(f3);
            setPivotX(0.0f);
            setPivotY(f2 / 2.0f);
            return;
        }
        super.onMeasure(i, i2);
        setScaleX(1.0f);
        setScaleY(1.0f);
        resetPivot();
    }

    @Override // android.view.View
    public final void onVisibilityAggregated(boolean z) {
        int i;
        super.onVisibilityAggregated(z);
        BcSmartspaceDataPlugin bcSmartspaceDataPlugin = this.mDataProvider;
        if (bcSmartspaceDataPlugin != null) {
            if (z) {
                i = 6;
            } else {
                i = 7;
            }
            bcSmartspaceDataPlugin.notifySmartspaceEvent(new SmartspaceTargetEvent.Builder(i).build());
        }
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public final void setFalsingManager(FalsingManager falsingManager) {
        BcSmartSpaceUtil.sFalsingManager = falsingManager;
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceView
    public final void setIntentStarter(BcSmartspaceDataPlugin.IntentStarter intentStarter) {
        BcSmartSpaceUtil.sIntentStarter = intentStarter;
    }
}
