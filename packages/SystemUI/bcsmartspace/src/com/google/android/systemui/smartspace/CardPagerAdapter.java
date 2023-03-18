package com.google.android.systemui.smartspace;

import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceTarget;
import android.app.smartspace.SmartspaceTargetEvent;
import android.app.smartspace.SmartspaceUtils;
import android.app.smartspace.uitemplatedata.BaseTemplateData;
import android.app.smartspace.uitemplatedata.TapAction;
import android.content.ComponentName;
import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.slice.view.R$plurals;
import androidx.viewpager.widget.PagerAdapter;
import com.android.internal.graphics.ColorUtils;
import com.android.launcher3.icons.GraphicsUtils;
import com.android.systemui.R;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.google.android.systemui.smartspace.logging.BcSmartspaceCardLoggerUtil;
import com.google.android.systemui.smartspace.logging.BcSmartspaceCardLoggingInfo;
import com.google.android.systemui.smartspace.logging.BcSmartspaceSubcardLoggingInfo;
import com.google.android.systemui.smartspace.uitemplate.BaseTemplateCard;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;
/* loaded from: classes2.dex */
public final class CardPagerAdapter extends PagerAdapter {
    public static final int MAX_FEATURE_TYPE = 41;
    public static final int MIN_FEATURE_TYPE = -2;
    public int mCurrentTextColor;
    public BcSmartspaceDataPlugin mDataProvider;
    public int mPrimaryTextColor;
    public final View mRoot;
    public ArrayList mSmartspaceTargets = new ArrayList();
    public ArrayList mAODTargets = new ArrayList();
    public ArrayList mLockscreenTargets = new ArrayList();
    public final ArrayList mMediaTargets = new ArrayList();
    public final SparseArray<ViewHolder> mViewHolders = new SparseArray<>();
    public final LazyServerFlagLoader mEnableCardRecycling = new LazyServerFlagLoader("enable_card_recycling");
    public final LazyServerFlagLoader mEnableReducedCardRecycling = new LazyServerFlagLoader("enable_reduced_card_recycling");
    public final SparseArray<BaseTemplateCard> mRecycledCards = new SparseArray<>();
    public final SparseArray<BcSmartspaceCard> mRecycledLegacyCards = new SparseArray<>();
    public boolean mIsDreaming = false;
    public String mUiSurface = null;
    public float mDozeAmount = 0.0f;
    public int mDozeColor = -1;
    public String mDndDescription = null;
    public Drawable mDndImage = null;
    public BcNextAlarmData mNextAlarmData = new BcNextAlarmData();
    public boolean mKeyguardBypassEnabled = false;
    public boolean mHasDifferentTargets = false;
    public boolean mHasAodLockscreenTransition = false;

    public static int getBaseLegacyCardRes(int i) {
        return i != 1 ? R.layout.smartspace_card : R.layout.smartspace_card_date;
    }

    public static int getLegacySecondaryCardRes(int i) {
        if (i != -2) {
            if (i != -1) {
                if (i != 3) {
                    if (i != 4) {
                        if (i != 9) {
                            if (i != 10) {
                                if (i != 13) {
                                    if (i != 14) {
                                        if (i != 18) {
                                            if (i == 20 || i == 30) {
                                                return R.layout.smartspace_card_doorbell;
                                            }
                                            return 0;
                                        }
                                        return R.layout.smartspace_card_generic_landscape_image;
                                    }
                                    return R.layout.smartspace_card_loyalty;
                                }
                                return R.layout.smartspace_card_shopping_list;
                            }
                            return R.layout.smartspace_card_weather_forecast;
                        }
                        return R.layout.smartspace_card_sports;
                    }
                    return R.layout.smartspace_card_flight;
                }
                return R.layout.smartspace_card_generic_landscape_image;
            }
            return R.layout.smartspace_card_combination;
        }
        return R.layout.smartspace_card_combination_at_store;
    }

    public static boolean useRecycledViewForAction(SmartspaceAction smartspaceAction, SmartspaceAction smartspaceAction2) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        if (smartspaceAction == null && smartspaceAction2 == null) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            return true;
        }
        if (smartspaceAction != null && smartspaceAction2 != null) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (z2) {
            Bundle extras = smartspaceAction.getExtras();
            Bundle extras2 = smartspaceAction2.getExtras();
            if (extras == null && extras2 == null) {
                z3 = true;
            } else {
                z3 = false;
            }
            if (z3) {
                return true;
            }
            Bundle extras3 = smartspaceAction.getExtras();
            Bundle extras4 = smartspaceAction2.getExtras();
            if (extras3 != null && extras4 != null) {
                z4 = true;
            } else {
                z4 = false;
            }
            if (z4 && smartspaceAction.getExtras().keySet().equals(smartspaceAction2.getExtras().keySet())) {
                return true;
            }
        }
        return false;
    }

    public static boolean useRecycledViewForActionsList(final List<SmartspaceAction> list, final List<SmartspaceAction> list2) {
        boolean z;
        boolean z2;
        if (list == null && list2 == null) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            return true;
        }
        if (list != null && list2 != null) {
            z2 = true;
        } else {
            z2 = false;
        }
        return z2 && list.size() == list2.size() && IntStream.range(0, list.size()).allMatch(new IntPredicate() { // from class: com.google.android.systemui.smartspace.CardPagerAdapter$$ExternalSyntheticLambda0
            @Override // java.util.function.IntPredicate
            public final boolean test(int i) {
                return CardPagerAdapter.useRecycledViewForAction((SmartspaceAction) list.get(i), (SmartspaceAction) list2.get(i));
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x0070  */
    /* JADX WARN: Removed duplicated region for block: B:39:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean useRecycledViewForNewTarget(android.app.smartspace.SmartspaceTarget r4, android.app.smartspace.SmartspaceTarget r5) {
        /*
            r0 = 0
            r1 = 1
            if (r5 == 0) goto L71
            java.lang.String r2 = r4.getSmartspaceTargetId()
            java.lang.String r3 = r5.getSmartspaceTargetId()
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L71
            android.app.smartspace.SmartspaceAction r2 = r4.getHeaderAction()
            android.app.smartspace.SmartspaceAction r3 = r5.getHeaderAction()
            boolean r2 = useRecycledViewForAction(r2, r3)
            if (r2 == 0) goto L71
            android.app.smartspace.SmartspaceAction r2 = r4.getBaseAction()
            android.app.smartspace.SmartspaceAction r3 = r5.getBaseAction()
            boolean r2 = useRecycledViewForAction(r2, r3)
            if (r2 == 0) goto L71
            java.util.List r2 = r4.getActionChips()
            java.util.List r3 = r5.getActionChips()
            boolean r2 = useRecycledViewForActionsList(r2, r3)
            if (r2 == 0) goto L71
            java.util.List r2 = r4.getIconGrid()
            java.util.List r3 = r5.getIconGrid()
            boolean r2 = useRecycledViewForActionsList(r2, r3)
            if (r2 == 0) goto L71
            android.app.smartspace.uitemplatedata.BaseTemplateData r4 = r4.getTemplateData()
            android.app.smartspace.uitemplatedata.BaseTemplateData r5 = r5.getTemplateData()
            if (r4 != 0) goto L58
            if (r5 != 0) goto L58
            r2 = r1
            goto L59
        L58:
            r2 = r0
        L59:
            if (r2 != 0) goto L6d
            if (r4 == 0) goto L61
            if (r5 == 0) goto L61
            r2 = r1
            goto L62
        L61:
            r2 = r0
        L62:
            if (r2 == 0) goto L6b
            boolean r4 = r4.equals(r5)
            if (r4 == 0) goto L6b
            goto L6d
        L6b:
            r4 = r0
            goto L6e
        L6d:
            r4 = r1
        L6e:
            if (r4 == 0) goto L71
            r0 = r1
        L71:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.smartspace.CardPagerAdapter.useRecycledViewForNewTarget(android.app.smartspace.SmartspaceTarget, android.app.smartspace.SmartspaceTarget):boolean");
    }

    public final void refreshCards() {
        for (int i = 0; i < this.mViewHolders.size(); i++) {
            SparseArray<ViewHolder> sparseArray = this.mViewHolders;
            ViewHolder viewHolder = sparseArray.get(sparseArray.keyAt(i));
            if (viewHolder != null) {
                onBindViewHolder(viewHolder);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class ViewHolder {
        public final BaseTemplateCard mCard;
        public final BcSmartspaceCard mLegacyCard;
        public final int mPosition;
        public SmartspaceTarget mTarget;

        public ViewHolder(int i, BcSmartspaceCard bcSmartspaceCard, SmartspaceTarget smartspaceTarget, BaseTemplateCard baseTemplateCard) {
            this.mPosition = i;
            this.mLegacyCard = bcSmartspaceCard;
            this.mTarget = smartspaceTarget;
            this.mCard = baseTemplateCard;
        }
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public final void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        ViewHolder viewHolder = (ViewHolder) obj;
        if (viewHolder == null) {
            return;
        }
        BcSmartspaceCard bcSmartspaceCard = viewHolder.mLegacyCard;
        if (bcSmartspaceCard != null) {
            SmartspaceTarget smartspaceTarget = bcSmartspaceCard.mTarget;
            if (smartspaceTarget != null && this.mEnableCardRecycling.get()) {
                this.mRecycledLegacyCards.put(getFeatureType(smartspaceTarget), bcSmartspaceCard);
            }
            viewGroup.removeView(bcSmartspaceCard);
        }
        BaseTemplateCard baseTemplateCard = viewHolder.mCard;
        if (baseTemplateCard != null) {
            SmartspaceTarget smartspaceTarget2 = baseTemplateCard.mTarget;
            if (smartspaceTarget2 != null && this.mEnableCardRecycling.get()) {
                this.mRecycledCards.put(smartspaceTarget2.getFeatureType(), baseTemplateCard);
            }
            viewGroup.removeView(viewHolder.mCard);
        }
        if (this.mViewHolders.get(i) == viewHolder) {
            this.mViewHolders.remove(i);
        }
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public final int getCount() {
        return this.mSmartspaceTargets.size();
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public final int getItemPosition(Object obj) {
        ViewHolder viewHolder = (ViewHolder) obj;
        SmartspaceTarget targetAtPosition = getTargetAtPosition(viewHolder.mPosition);
        if (viewHolder.mTarget == targetAtPosition) {
            return -1;
        }
        if (targetAtPosition != null && getFeatureType(targetAtPosition) == getFeatureType(viewHolder.mTarget) && Objects.equals(targetAtPosition.getSmartspaceTargetId(), viewHolder.mTarget.getSmartspaceTargetId())) {
            viewHolder.mTarget = targetAtPosition;
            onBindViewHolder(viewHolder);
            return -1;
        }
        return -2;
    }

    public final SmartspaceTarget getTargetAtPosition(int i) {
        if (!this.mSmartspaceTargets.isEmpty() && i >= 0 && i < this.mSmartspaceTargets.size()) {
            return (SmartspaceTarget) this.mSmartspaceTargets.get(i);
        }
        return null;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public final Object instantiateItem(int i, ViewGroup viewGroup) {
        BcSmartspaceCard bcSmartspaceCard;
        ViewHolder viewHolder;
        BaseTemplateCard baseTemplateCard;
        BaseTemplateData.SubItemInfo subItemInfo;
        int i2;
        int secondaryCardRes;
        SmartspaceTarget smartspaceTarget = (SmartspaceTarget) this.mSmartspaceTargets.get(i);
        if (BcSmartspaceCardLoggerUtil.containsValidTemplateType(smartspaceTarget.getTemplateData())) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline0.m("Use UI template for the feature: ");
            m.append(smartspaceTarget.getFeatureType());
            Log.i("SsCardPagerAdapter", m.toString());
            if (this.mEnableCardRecycling.get()) {
                baseTemplateCard = (BaseTemplateCard) this.mRecycledCards.removeReturnOld(smartspaceTarget.getFeatureType());
            } else {
                baseTemplateCard = null;
            }
            if (baseTemplateCard == null || (this.mEnableReducedCardRecycling.get() && !useRecycledViewForNewTarget(smartspaceTarget, baseTemplateCard.mTarget))) {
                BaseTemplateData templateData = smartspaceTarget.getTemplateData();
                if (templateData != null) {
                    subItemInfo = templateData.getPrimaryItem();
                } else {
                    subItemInfo = null;
                }
                if (subItemInfo != null && (!SmartspaceUtils.isEmpty(subItemInfo.getText()) || subItemInfo.getIcon() != null)) {
                    i2 = R.layout.smartspace_base_template_card;
                } else {
                    i2 = R.layout.smartspace_base_template_card_with_date;
                }
                LayoutInflater from = LayoutInflater.from(viewGroup.getContext());
                BaseTemplateCard baseTemplateCard2 = (BaseTemplateCard) from.inflate(i2, viewGroup, false);
                String str = this.mUiSurface;
                baseTemplateCard2.getClass();
                if (baseTemplateCard2.mDateView != null && TextUtils.equals(str, BcSmartspaceDataPlugin.UI_SURFACE_LOCK_SCREEN_AOD)) {
                    IcuDateTextView icuDateTextView = baseTemplateCard2.mDateView;
                    if (!icuDateTextView.isAttachedToWindow()) {
                        icuDateTextView.mUpdatesOnAod = true;
                    } else {
                        throw new IllegalStateException("Must call before attaching view to window.");
                    }
                }
                if (templateData != null && (secondaryCardRes = BcSmartspaceTemplateDataUtils.getSecondaryCardRes(templateData.getTemplateType())) != 0) {
                    BcSmartspaceCardSecondary bcSmartspaceCardSecondary = (BcSmartspaceCardSecondary) from.inflate(secondaryCardRes, (ViewGroup) baseTemplateCard2, false);
                    if (bcSmartspaceCardSecondary != null) {
                        Log.i("SsCardPagerAdapter", "Secondary card is found");
                    }
                    ViewGroup viewGroup2 = baseTemplateCard2.mSecondaryCardPane;
                    if (viewGroup2 != null) {
                        baseTemplateCard2.mSecondaryCard = bcSmartspaceCardSecondary;
                        BcSmartspaceTemplateDataUtils.updateVisibility(viewGroup2, 8);
                        baseTemplateCard2.mSecondaryCardPane.removeAllViews();
                        if (bcSmartspaceCardSecondary != null) {
                            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(-2, baseTemplateCard2.getResources().getDimensionPixelSize(R.dimen.enhanced_smartspace_card_height));
                            layoutParams.setMarginStart(baseTemplateCard2.getResources().getDimensionPixelSize(R.dimen.enhanced_smartspace_secondary_card_start_margin));
                            layoutParams.startToStart = 0;
                            layoutParams.topToTop = 0;
                            layoutParams.bottomToBottom = 0;
                            baseTemplateCard2.mSecondaryCardPane.addView(bcSmartspaceCardSecondary, layoutParams);
                        }
                    }
                }
                baseTemplateCard = baseTemplateCard2;
            }
            viewHolder = new ViewHolder(i, null, smartspaceTarget, baseTemplateCard);
            viewGroup.addView(baseTemplateCard);
        } else {
            if (this.mEnableCardRecycling.get()) {
                bcSmartspaceCard = (BcSmartspaceCard) this.mRecycledLegacyCards.removeReturnOld(getFeatureType(smartspaceTarget));
            } else {
                bcSmartspaceCard = null;
            }
            if (bcSmartspaceCard == null || (this.mEnableReducedCardRecycling.get() && !useRecycledViewForNewTarget(smartspaceTarget, bcSmartspaceCard.mTarget))) {
                int featureType = getFeatureType(smartspaceTarget);
                LayoutInflater from2 = LayoutInflater.from(viewGroup.getContext());
                BcSmartspaceCard bcSmartspaceCard2 = (BcSmartspaceCard) from2.inflate(getBaseLegacyCardRes(featureType), viewGroup, false);
                String str2 = this.mUiSurface;
                bcSmartspaceCard2.getClass();
                if (bcSmartspaceCard2.mDateView != null && TextUtils.equals(str2, BcSmartspaceDataPlugin.UI_SURFACE_LOCK_SCREEN_AOD)) {
                    IcuDateTextView icuDateTextView2 = bcSmartspaceCard2.mDateView;
                    if (!icuDateTextView2.isAttachedToWindow()) {
                        icuDateTextView2.mUpdatesOnAod = true;
                    } else {
                        throw new IllegalStateException("Must call before attaching view to window.");
                    }
                }
                int legacySecondaryCardRes = getLegacySecondaryCardRes(featureType);
                if (legacySecondaryCardRes != 0) {
                    BcSmartspaceCardSecondary bcSmartspaceCardSecondary2 = (BcSmartspaceCardSecondary) from2.inflate(legacySecondaryCardRes, (ViewGroup) bcSmartspaceCard2, false);
                    ViewGroup viewGroup3 = bcSmartspaceCard2.mSecondaryCardGroup;
                    if (viewGroup3 != null) {
                        bcSmartspaceCard2.mSecondaryCard = bcSmartspaceCardSecondary2;
                        BcSmartspaceTemplateDataUtils.updateVisibility(viewGroup3, 8);
                        bcSmartspaceCard2.mSecondaryCardGroup.removeAllViews();
                        if (bcSmartspaceCardSecondary2 != null) {
                            ConstraintLayout.LayoutParams layoutParams2 = new ConstraintLayout.LayoutParams(-2, bcSmartspaceCard2.getResources().getDimensionPixelSize(R.dimen.enhanced_smartspace_card_height));
                            layoutParams2.setMarginStart(bcSmartspaceCard2.getResources().getDimensionPixelSize(R.dimen.enhanced_smartspace_secondary_card_start_margin));
                            layoutParams2.startToStart = 0;
                            layoutParams2.topToTop = 0;
                            layoutParams2.bottomToBottom = 0;
                            bcSmartspaceCard2.mSecondaryCardGroup.addView(bcSmartspaceCardSecondary2, layoutParams2);
                        }
                    }
                }
                bcSmartspaceCard = bcSmartspaceCard2;
            }
            viewHolder = new ViewHolder(i, bcSmartspaceCard, smartspaceTarget, null);
            viewGroup.addView(bcSmartspaceCard);
        }
        onBindViewHolder(viewHolder);
        this.mViewHolders.put(i, viewHolder);
        return viewHolder;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public final boolean isViewFromObject(View view, Object obj) {
        ViewHolder viewHolder = (ViewHolder) obj;
        if (view != viewHolder.mLegacyCard && view != viewHolder.mCard) {
            return false;
        }
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v45, types: [com.google.android.systemui.smartspace.CardPagerAdapter$$ExternalSyntheticLambda1] */
    public final void onBindViewHolder(ViewHolder viewHolder) {
        BcSmartspaceSubcardLoggingInfo createSubcardLoggingInfo;
        BcSmartspaceDataPlugin.SmartspaceEventNotifier smartspaceEventNotifier;
        boolean z;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        String uuid;
        Drawable drawable;
        int i6;
        int i7;
        int i8;
        boolean z2;
        int i9;
        boolean z3;
        boolean z4;
        boolean z5;
        CardPagerAdapter$$ExternalSyntheticLambda1 cardPagerAdapter$$ExternalSyntheticLambda1;
        boolean z6;
        String uuid2;
        String str;
        int i10;
        BcNextAlarmData bcNextAlarmData;
        int i11;
        String str2;
        TapAction tapAction;
        boolean z7;
        int i12;
        SmartspaceTarget smartspaceTarget = (SmartspaceTarget) this.mSmartspaceTargets.get(viewHolder.mPosition);
        boolean containsValidTemplateType = BcSmartspaceCardLoggerUtil.containsValidTemplateType(smartspaceTarget.getTemplateData());
        BcSmartspaceCardLoggingInfo.Builder builder = new BcSmartspaceCardLoggingInfo.Builder();
        builder.mInstanceId = R$plurals.create(smartspaceTarget);
        builder.mFeatureType = smartspaceTarget.getFeatureType();
        builder.mDisplaySurface = BcSmartSpaceUtil.getLoggingDisplaySurface(this.mRoot.getContext().getPackageName(), this.mIsDreaming, this.mDozeAmount);
        builder.mRank = viewHolder.mPosition;
        builder.mCardinality = this.mSmartspaceTargets.size();
        builder.mUid = BcSmartspaceCardLoggerUtil.getUid(this.mRoot.getContext().getPackageManager(), smartspaceTarget);
        if (containsValidTemplateType) {
            createSubcardLoggingInfo = BcSmartspaceCardLoggerUtil.createSubcardLoggingInfo(smartspaceTarget.getTemplateData());
        } else {
            createSubcardLoggingInfo = BcSmartspaceCardLoggerUtil.createSubcardLoggingInfo(smartspaceTarget);
        }
        builder.mSubcardInfo = createSubcardLoggingInfo;
        builder.mDimensionalInfo = BcSmartspaceCardLoggerUtil.createDimensionalLoggingInfo(smartspaceTarget.getTemplateData());
        BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo = new BcSmartspaceCardLoggingInfo(builder);
        int i13 = 8;
        if (containsValidTemplateType) {
            BcSmartspaceCardLoggerUtil.tryForcePrimaryFeatureTypeOrUpdateLogInfoFromTemplateData(bcSmartspaceCardLoggingInfo, smartspaceTarget.getTemplateData());
            BaseTemplateCard baseTemplateCard = viewHolder.mCard;
            if (baseTemplateCard == null) {
                Log.w("SsCardPagerAdapter", "No ui-template card view can be binded");
                return;
            }
            baseTemplateCard.mIsDreaming = this.mIsDreaming;
            final BcSmartspaceDataPlugin bcSmartspaceDataPlugin = this.mDataProvider;
            if (bcSmartspaceDataPlugin == null) {
                cardPagerAdapter$$ExternalSyntheticLambda1 = null;
            } else {
                cardPagerAdapter$$ExternalSyntheticLambda1 = new BcSmartspaceDataPlugin.SmartspaceEventNotifier() { // from class: com.google.android.systemui.smartspace.CardPagerAdapter$$ExternalSyntheticLambda1
                    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceEventNotifier
                    public final void notifySmartspaceEvent(SmartspaceTargetEvent smartspaceTargetEvent) {
                        BcSmartspaceDataPlugin.this.notifySmartspaceEvent(smartspaceTargetEvent);
                    }
                };
            }
            if (this.mSmartspaceTargets.size() > 1) {
                z6 = true;
            } else {
                z6 = false;
            }
            BcNextAlarmData bcNextAlarmData2 = this.mNextAlarmData;
            if (!smartspaceTarget.getSmartspaceTargetId().equals(baseTemplateCard.mPrevSmartspaceTargetId)) {
                baseTemplateCard.mTarget = null;
                baseTemplateCard.mTemplateData = null;
                baseTemplateCard.mFeatureType = 0;
                baseTemplateCard.mLoggingInfo = null;
                baseTemplateCard.setOnClickListener(null);
                baseTemplateCard.resetTextView(baseTemplateCard.mTitleTextView);
                baseTemplateCard.resetTextView(baseTemplateCard.mSubtitleTextView);
                baseTemplateCard.resetTextView(baseTemplateCard.mSubtitleSupplementalView);
                baseTemplateCard.resetTextView(baseTemplateCard.mSupplementalLineTextView);
                baseTemplateCard.resetTextView(baseTemplateCard.mNextAlarmTextView);
                ImageView imageView = baseTemplateCard.mNextAlarmImageView;
                if (imageView != null) {
                    imageView.setImageDrawable(null);
                }
                BcSmartspaceTemplateDataUtils.updateVisibility(baseTemplateCard.mTitleTextView, 8);
                BcSmartspaceTemplateDataUtils.updateVisibility(baseTemplateCard.mSubtitleTextView, 8);
                BcSmartspaceTemplateDataUtils.updateVisibility(baseTemplateCard.mSubtitleSupplementalView, 8);
                BcSmartspaceTemplateDataUtils.updateVisibility(baseTemplateCard.mSecondaryCardPane, 8);
                BcSmartspaceTemplateDataUtils.updateVisibility(baseTemplateCard.mDndImageView, 8);
                BcSmartspaceTemplateDataUtils.updateVisibility(baseTemplateCard.mNextAlarmImageView, 8);
                BcSmartspaceTemplateDataUtils.updateVisibility(baseTemplateCard.mNextAlarmTextView, 8);
                BcSmartspaceTemplateDataUtils.updateVisibility(baseTemplateCard.mExtrasGroup, 8);
            }
            baseTemplateCard.mPrevSmartspaceTargetId = smartspaceTarget.getSmartspaceTargetId();
            baseTemplateCard.mTarget = smartspaceTarget;
            baseTemplateCard.mTemplateData = smartspaceTarget.getTemplateData();
            baseTemplateCard.mFeatureType = smartspaceTarget.getFeatureType();
            baseTemplateCard.mLoggingInfo = bcSmartspaceCardLoggingInfo;
            baseTemplateCard.mShouldShowPageIndicator = z6;
            baseTemplateCard.mValidSecondaryCard = false;
            ViewGroup viewGroup = baseTemplateCard.mTextGroup;
            if (viewGroup != null) {
                viewGroup.setTranslationX(0.0f);
            }
            if (baseTemplateCard.mTemplateData == null) {
                i10 = 0;
            } else {
                BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo2 = baseTemplateCard.mLoggingInfo;
                if (bcSmartspaceCardLoggingInfo2 == null) {
                    BcSmartspaceCardLoggingInfo.Builder builder2 = new BcSmartspaceCardLoggingInfo.Builder();
                    builder2.mDisplaySurface = BcSmartSpaceUtil.getLoggingDisplaySurface(baseTemplateCard.getContext().getPackageName(), baseTemplateCard.mIsDreaming, baseTemplateCard.mDozeAmount);
                    builder2.mFeatureType = baseTemplateCard.mFeatureType;
                    builder2.mUid = BcSmartspaceCardLoggerUtil.getUid(baseTemplateCard.getContext().getPackageManager(), baseTemplateCard.mTarget);
                    builder2.mDimensionalInfo = BcSmartspaceCardLoggerUtil.createDimensionalLoggingInfo(baseTemplateCard.mTarget.getTemplateData());
                    bcSmartspaceCardLoggingInfo2 = new BcSmartspaceCardLoggingInfo(builder2);
                }
                baseTemplateCard.mLoggingInfo = bcSmartspaceCardLoggingInfo2;
                if (baseTemplateCard.mSecondaryCard != null) {
                    Log.i("SsBaseTemplateCard", "Secondary card is not null");
                    BcSmartspaceCardSecondary bcSmartspaceCardSecondary = baseTemplateCard.mSecondaryCard;
                    String smartspaceTargetId = smartspaceTarget.getSmartspaceTargetId();
                    if (!bcSmartspaceCardSecondary.mPrevSmartspaceTargetId.equals(smartspaceTargetId)) {
                        bcSmartspaceCardSecondary.mPrevSmartspaceTargetId = smartspaceTargetId;
                        bcSmartspaceCardSecondary.resetUi();
                    }
                    baseTemplateCard.mValidSecondaryCard = baseTemplateCard.mSecondaryCard.setSmartspaceActions(smartspaceTarget, cardPagerAdapter$$ExternalSyntheticLambda1, baseTemplateCard.mLoggingInfo);
                }
                ViewGroup viewGroup2 = baseTemplateCard.mSecondaryCardPane;
                if (viewGroup2 != null) {
                    if (baseTemplateCard.mDozeAmount != 1.0f && baseTemplateCard.mValidSecondaryCard) {
                        z7 = false;
                    } else {
                        z7 = true;
                    }
                    if (z7) {
                        i12 = 8;
                    } else {
                        i12 = 0;
                    }
                    BcSmartspaceTemplateDataUtils.updateVisibility(viewGroup2, i12);
                }
                BaseTemplateData.SubItemInfo primaryItem = baseTemplateCard.mTemplateData.getPrimaryItem();
                if (baseTemplateCard.mDateView == null) {
                    str = "SsBaseTemplateCard";
                    i10 = 0;
                    bcNextAlarmData = bcNextAlarmData2;
                    i11 = 8;
                } else {
                    if (primaryItem != null && primaryItem.getTapAction() != null) {
                        uuid2 = primaryItem.getTapAction().getId().toString();
                    } else {
                        uuid2 = UUID.randomUUID().toString();
                    }
                    str = "SsBaseTemplateCard";
                    i10 = 0;
                    bcNextAlarmData = bcNextAlarmData2;
                    i11 = 8;
                    BcSmartSpaceUtil.setOnClickListener$1(baseTemplateCard, baseTemplateCard.mTarget, new TapAction.Builder(uuid2).setIntent(BcSmartSpaceUtil.getOpenCalendarIntent()).build(), cardPagerAdapter$$ExternalSyntheticLambda1, "SsBaseTemplateCard", bcSmartspaceCardLoggingInfo, 0);
                }
                baseTemplateCard.setUpTextView(baseTemplateCard.mTitleTextView, baseTemplateCard.mTemplateData.getPrimaryItem(), cardPagerAdapter$$ExternalSyntheticLambda1);
                baseTemplateCard.setUpTextView(baseTemplateCard.mSubtitleTextView, baseTemplateCard.mTemplateData.getSubtitleItem(), cardPagerAdapter$$ExternalSyntheticLambda1);
                baseTemplateCard.setUpTextView(baseTemplateCard.mSubtitleSupplementalView, baseTemplateCard.mTemplateData.getSubtitleSupplementalItem(), cardPagerAdapter$$ExternalSyntheticLambda1);
                BaseTemplateData.SubItemInfo supplementalAlarmItem = baseTemplateCard.mTemplateData.getSupplementalAlarmItem();
                ImageView imageView2 = baseTemplateCard.mNextAlarmImageView;
                if (imageView2 != null && baseTemplateCard.mNextAlarmTextView != null) {
                    if (bcNextAlarmData.mImage == null) {
                        BcSmartspaceTemplateDataUtils.updateVisibility(imageView2, i11);
                        BcSmartspaceTemplateDataUtils.updateVisibility(baseTemplateCard.mNextAlarmTextView, i11);
                        BcSmartspaceTemplateDataUtils.offsetImageViewForIcon(baseTemplateCard.mNextAlarmImageView, null);
                    } else {
                        DoubleShadowIconDrawable doubleShadowIconDrawable = new DoubleShadowIconDrawable(baseTemplateCard.getContext());
                        doubleShadowIconDrawable.setIcon(bcNextAlarmData.mImage);
                        baseTemplateCard.mNextAlarmImageView.setImageDrawable(doubleShadowIconDrawable);
                        BcSmartspaceTemplateDataUtils.offsetImageViewForIcon(baseTemplateCard.mNextAlarmImageView, doubleShadowIconDrawable);
                        BcSmartspaceTemplateDataUtils.updateVisibility(baseTemplateCard.mNextAlarmImageView, i10);
                        String description = bcNextAlarmData.getDescription(supplementalAlarmItem);
                        DoubleShadowTextView doubleShadowTextView = baseTemplateCard.mNextAlarmTextView;
                        Context context = baseTemplateCard.getContext();
                        Object[] objArr = new Object[1];
                        objArr[i10] = description;
                        doubleShadowTextView.setContentDescription(context.getString(R.string.accessibility_next_alarm, objArr));
                        baseTemplateCard.mNextAlarmTextView.setText(description);
                        BcSmartspaceTemplateDataUtils.updateVisibility(baseTemplateCard.mNextAlarmTextView, i10);
                        if (supplementalAlarmItem == null) {
                            tapAction = null;
                        } else {
                            tapAction = supplementalAlarmItem.getTapAction();
                        }
                        bcNextAlarmData.setOnClickListener(baseTemplateCard.mNextAlarmImageView, tapAction, cardPagerAdapter$$ExternalSyntheticLambda1, BcSmartSpaceUtil.getLoggingDisplaySurface(baseTemplateCard.getContext().getPackageName(), baseTemplateCard.mIsDreaming, baseTemplateCard.mDozeAmount));
                        bcNextAlarmData.setOnClickListener(baseTemplateCard.mNextAlarmTextView, tapAction, cardPagerAdapter$$ExternalSyntheticLambda1, BcSmartSpaceUtil.getLoggingDisplaySurface(baseTemplateCard.getContext().getPackageName(), baseTemplateCard.mIsDreaming, baseTemplateCard.mDozeAmount));
                    }
                }
                baseTemplateCard.setUpTextView(baseTemplateCard.mSupplementalLineTextView, baseTemplateCard.mTemplateData.getSupplementalLineItem(), cardPagerAdapter$$ExternalSyntheticLambda1);
                baseTemplateCard.updateZenVisibility();
                if (baseTemplateCard.mTemplateData.getPrimaryItem() != null && baseTemplateCard.mTemplateData.getPrimaryItem().getTapAction() != null) {
                    str2 = str;
                    i13 = i11;
                    BcSmartSpaceUtil.setOnClickListener$1(baseTemplateCard, smartspaceTarget, baseTemplateCard.mTemplateData.getPrimaryItem().getTapAction(), cardPagerAdapter$$ExternalSyntheticLambda1, "SsBaseTemplateCard", baseTemplateCard.mLoggingInfo, 0);
                } else {
                    str2 = str;
                    i13 = i11;
                }
                ViewGroup viewGroup3 = baseTemplateCard.mSecondaryCardPane;
                if (viewGroup3 == null) {
                    Log.i(str2, "Secondary card pane is null");
                } else {
                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) viewGroup3.getLayoutParams();
                    layoutParams.matchConstraintMaxWidth = baseTemplateCard.getWidth() / 2;
                    baseTemplateCard.mSecondaryCardPane.setLayoutParams(layoutParams);
                }
            }
            Drawable drawable2 = this.mDndImage;
            String str3 = this.mDndDescription;
            ImageView imageView3 = baseTemplateCard.mDndImageView;
            if (imageView3 != null) {
                if (drawable2 == null) {
                    BcSmartspaceTemplateDataUtils.updateVisibility(imageView3, i13);
                    BcSmartspaceTemplateDataUtils.offsetImageViewForIcon(baseTemplateCard.mDndImageView, null);
                } else {
                    DoubleShadowIconDrawable doubleShadowIconDrawable2 = new DoubleShadowIconDrawable(baseTemplateCard.getContext());
                    doubleShadowIconDrawable2.setIcon(drawable2.mutate());
                    baseTemplateCard.mDndImageView.setImageDrawable(doubleShadowIconDrawable2);
                    baseTemplateCard.mDndImageView.setContentDescription(str3);
                    BcSmartspaceTemplateDataUtils.updateVisibility(baseTemplateCard.mDndImageView, i10);
                    BcSmartspaceTemplateDataUtils.offsetImageViewForIcon(baseTemplateCard.mDndImageView, doubleShadowIconDrawable2);
                }
                baseTemplateCard.updateZenVisibility();
            }
            baseTemplateCard.setPrimaryTextColor(this.mCurrentTextColor);
            baseTemplateCard.setDozeAmount(this.mDozeAmount);
            return;
        }
        BcSmartspaceCardLoggerUtil.tryForcePrimaryFeatureTypeAndInjectWeatherSubcard(bcSmartspaceCardLoggingInfo, smartspaceTarget);
        BcSmartspaceCard bcSmartspaceCard = viewHolder.mLegacyCard;
        if (bcSmartspaceCard == null) {
            Log.w("SsCardPagerAdapter", "No legacy card view can be binded");
            return;
        }
        bcSmartspaceCard.mIsDreaming = this.mIsDreaming;
        final BcSmartspaceDataPlugin bcSmartspaceDataPlugin2 = this.mDataProvider;
        if (bcSmartspaceDataPlugin2 == null) {
            smartspaceEventNotifier = null;
        } else {
            smartspaceEventNotifier = new BcSmartspaceDataPlugin.SmartspaceEventNotifier() { // from class: com.google.android.systemui.smartspace.CardPagerAdapter$$ExternalSyntheticLambda1
                @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceEventNotifier
                public final void notifySmartspaceEvent(SmartspaceTargetEvent smartspaceTargetEvent) {
                    BcSmartspaceDataPlugin.this.notifySmartspaceEvent(smartspaceTargetEvent);
                }
            };
        }
        if (this.mSmartspaceTargets.size() > 1) {
            z = true;
        } else {
            z = false;
        }
        String smartspaceTargetId2 = smartspaceTarget.getSmartspaceTargetId();
        if (!bcSmartspaceCard.mPrevSmartspaceTargetId.equals(smartspaceTargetId2)) {
            bcSmartspaceCard.mPrevSmartspaceTargetId = smartspaceTargetId2;
            bcSmartspaceCard.mEventNotifier = null;
            BcSmartspaceTemplateDataUtils.updateVisibility(bcSmartspaceCard.mSecondaryCardGroup, 8);
            bcSmartspaceCard.mIconDrawable.setIcon(null);
            bcSmartspaceCard.updateZenVisibility();
            bcSmartspaceCard.setTitle(null, null, false);
            bcSmartspaceCard.setSubtitle(null, null, false);
            bcSmartspaceCard.updateIconTint();
            bcSmartspaceCard.setOnClickListener(null);
        }
        bcSmartspaceCard.mTarget = smartspaceTarget;
        bcSmartspaceCard.mEventNotifier = smartspaceEventNotifier;
        SmartspaceAction headerAction = smartspaceTarget.getHeaderAction();
        SmartspaceAction baseAction = smartspaceTarget.getBaseAction();
        bcSmartspaceCard.mUsePageIndicatorUi = z;
        bcSmartspaceCard.mValidSecondaryCard = false;
        ViewGroup viewGroup4 = bcSmartspaceCard.mTextGroup;
        if (viewGroup4 != null) {
            viewGroup4.setTranslationX(0.0f);
        }
        if (headerAction != null) {
            BcSmartspaceCardSecondary bcSmartspaceCardSecondary2 = bcSmartspaceCard.mSecondaryCard;
            if (bcSmartspaceCardSecondary2 != null) {
                String smartspaceTargetId3 = smartspaceTarget.getSmartspaceTargetId();
                if (!bcSmartspaceCardSecondary2.mPrevSmartspaceTargetId.equals(smartspaceTargetId3)) {
                    bcSmartspaceCardSecondary2.mPrevSmartspaceTargetId = smartspaceTargetId3;
                    bcSmartspaceCardSecondary2.resetUi();
                }
                bcSmartspaceCard.mValidSecondaryCard = bcSmartspaceCard.mSecondaryCard.setSmartspaceActions(smartspaceTarget, bcSmartspaceCard.mEventNotifier, bcSmartspaceCardLoggingInfo);
            }
            ViewGroup viewGroup5 = bcSmartspaceCard.mSecondaryCardGroup;
            if (bcSmartspaceCard.mDozeAmount != 1.0f && bcSmartspaceCard.mValidSecondaryCard) {
                z2 = false;
            } else {
                z2 = true;
            }
            if (z2) {
                i9 = 8;
            } else {
                i9 = 0;
            }
            BcSmartspaceTemplateDataUtils.updateVisibility(viewGroup5, i9);
            Drawable iconDrawable = BcSmartSpaceUtil.getIconDrawable(bcSmartspaceCard.getContext(), headerAction.getIcon());
            if (iconDrawable != null) {
                z3 = true;
            } else {
                z3 = false;
            }
            bcSmartspaceCard.mIconDrawable.setIcon(iconDrawable);
            CharSequence title = headerAction.getTitle();
            CharSequence subtitle = headerAction.getSubtitle();
            if (smartspaceTarget.getFeatureType() != 1 && TextUtils.isEmpty(title)) {
                z4 = false;
            } else {
                z4 = true;
            }
            boolean z8 = !TextUtils.isEmpty(subtitle);
            bcSmartspaceCard.updateZenVisibility();
            if (!z4) {
                title = subtitle;
            }
            CharSequence contentDescription = headerAction.getContentDescription();
            if (z4 != z8 && z3) {
                z5 = true;
            } else {
                z5 = false;
            }
            bcSmartspaceCard.setTitle(title, contentDescription, z5);
            if (!z4 || !z8) {
                subtitle = null;
            }
            bcSmartspaceCard.setSubtitle(subtitle, headerAction.getContentDescription(), z3);
            bcSmartspaceCard.updateIconTint();
        }
        if (bcSmartspaceCard.mBaseActionIconSubtitleView != null) {
            if (baseAction != null && baseAction.getIcon() != null) {
                drawable = BcSmartSpaceUtil.getIconDrawable(bcSmartspaceCard.getContext(), baseAction.getIcon());
            } else {
                drawable = null;
            }
            if (baseAction != null && baseAction.getIcon() != null && drawable != null) {
                drawable.setTintList(null);
                bcSmartspaceCard.mBaseActionIconSubtitleView.setText(baseAction.getSubtitle());
                bcSmartspaceCard.mBaseActionIconSubtitleView.setCompoundDrawablesRelative(drawable, null, null, null);
                BcSmartspaceTemplateDataUtils.updateVisibility(bcSmartspaceCard.mBaseActionIconSubtitleView, 0);
                if (baseAction.getExtras() != null && !baseAction.getExtras().isEmpty()) {
                    i6 = baseAction.getExtras().getInt("subcardType", -1);
                } else {
                    i6 = -1;
                }
                if (i6 != -1) {
                    i8 = BcSmartspaceCard.getClickedIndex(bcSmartspaceCardLoggingInfo, i6);
                    i7 = 1;
                } else {
                    i7 = 1;
                    Log.d("BcSmartspaceCard", String.format("Subcard expected but missing type. loggingInfo=%s, baseAction=%s", bcSmartspaceCardLoggingInfo.toString(), baseAction.toString()));
                    i8 = 0;
                }
                i2 = i7;
                i = 4;
                BcSmartSpaceUtil.setOnClickListener(bcSmartspaceCard.mBaseActionIconSubtitleView, smartspaceTarget, baseAction, bcSmartspaceCard.mEventNotifier, "BcSmartspaceCard", bcSmartspaceCardLoggingInfo, i8);
                bcSmartspaceCard.setFormattedContentDescription(bcSmartspaceCard.mBaseActionIconSubtitleView, baseAction.getSubtitle(), baseAction.getContentDescription());
            } else {
                i = 4;
                i2 = 1;
                BcSmartspaceTemplateDataUtils.updateVisibility(bcSmartspaceCard.mBaseActionIconSubtitleView, 4);
                bcSmartspaceCard.mBaseActionIconSubtitleView.setOnClickListener(null);
                bcSmartspaceCard.mBaseActionIconSubtitleView.setContentDescription(null);
            }
        } else {
            i = 4;
            i2 = 1;
        }
        if (bcSmartspaceCard.mDateView != null) {
            if (headerAction != null) {
                uuid = headerAction.getId();
            } else if (baseAction != null) {
                uuid = baseAction.getId();
            } else {
                uuid = UUID.randomUUID().toString();
            }
            BcSmartSpaceUtil.setOnClickListener(bcSmartspaceCard.mDateView, smartspaceTarget, new SmartspaceAction.Builder(uuid, "unusedTitle").setIntent(BcSmartSpaceUtil.getOpenCalendarIntent()).build(), bcSmartspaceCard.mEventNotifier, "BcSmartspaceCard", bcSmartspaceCardLoggingInfo, 0);
        }
        if (headerAction != null && (headerAction.getIntent() != null || headerAction.getPendingIntent() != null)) {
            i3 = i2;
        } else {
            i3 = 0;
        }
        if (i3 != 0) {
            if (smartspaceTarget.getFeatureType() == i2 && bcSmartspaceCardLoggingInfo.mFeatureType == 39) {
                i5 = BcSmartspaceCard.getClickedIndex(bcSmartspaceCardLoggingInfo, i2);
            } else {
                i5 = 0;
            }
            BcSmartSpaceUtil.setOnClickListener(bcSmartspaceCard, smartspaceTarget, headerAction, bcSmartspaceCard.mEventNotifier, "BcSmartspaceCard", bcSmartspaceCardLoggingInfo, i5);
        } else {
            if (baseAction != null && (baseAction.getIntent() != null || baseAction.getPendingIntent() != null)) {
                i4 = i2;
            } else {
                i4 = 0;
            }
            if (i4 != 0) {
                BcSmartSpaceUtil.setOnClickListener(bcSmartspaceCard, smartspaceTarget, baseAction, bcSmartspaceCard.mEventNotifier, "BcSmartspaceCard", bcSmartspaceCardLoggingInfo, 0);
            } else {
                BcSmartSpaceUtil.setOnClickListener(bcSmartspaceCard, smartspaceTarget, headerAction, bcSmartspaceCard.mEventNotifier, "BcSmartspaceCard", bcSmartspaceCardLoggingInfo, 0);
            }
        }
        ViewGroup viewGroup6 = bcSmartspaceCard.mSecondaryCardGroup;
        if (viewGroup6 != null) {
            ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) viewGroup6.getLayoutParams();
            if (getFeatureType(smartspaceTarget) == -2) {
                layoutParams2.matchConstraintMaxWidth = (bcSmartspaceCard.getWidth() * 3) / i;
            } else {
                layoutParams2.matchConstraintMaxWidth = bcSmartspaceCard.getWidth() / 2;
            }
            bcSmartspaceCard.mSecondaryCardGroup.setLayoutParams(layoutParams2);
        }
        bcSmartspaceCard.setPrimaryTextColor(this.mCurrentTextColor);
        bcSmartspaceCard.setDozeAmount(this.mDozeAmount);
        Drawable drawable3 = this.mDndImage;
        String str4 = this.mDndDescription;
        ImageView imageView4 = bcSmartspaceCard.mDndImageView;
        if (imageView4 != null) {
            if (drawable3 == null) {
                BcSmartspaceTemplateDataUtils.updateVisibility(imageView4, 8);
                BcSmartspaceTemplateDataUtils.offsetImageViewForIcon(bcSmartspaceCard.mDndImageView, null);
            } else {
                bcSmartspaceCard.mDndIconDrawable.setIcon(drawable3.mutate());
                bcSmartspaceCard.mDndImageView.setImageDrawable(bcSmartspaceCard.mDndIconDrawable);
                bcSmartspaceCard.mDndImageView.setContentDescription(str4);
                BcSmartspaceTemplateDataUtils.offsetImageViewForIcon(bcSmartspaceCard.mDndImageView, bcSmartspaceCard.mDndIconDrawable);
                BcSmartspaceTemplateDataUtils.updateVisibility(bcSmartspaceCard.mDndImageView, 0);
            }
            bcSmartspaceCard.updateZenVisibility();
        }
        BcNextAlarmData bcNextAlarmData3 = this.mNextAlarmData;
        ImageView imageView5 = bcSmartspaceCard.mNextAlarmImageView;
        if (imageView5 != null && bcSmartspaceCard.mNextAlarmTextView != null) {
            Drawable drawable4 = bcNextAlarmData3.mImage;
            if (drawable4 == null) {
                BcSmartspaceTemplateDataUtils.updateVisibility(imageView5, 8);
                BcSmartspaceTemplateDataUtils.updateVisibility(bcSmartspaceCard.mNextAlarmTextView, 8);
                BcSmartspaceTemplateDataUtils.offsetImageViewForIcon(bcSmartspaceCard.mNextAlarmImageView, null);
            } else {
                bcSmartspaceCard.mNextAlarmIconDrawable.setIcon(drawable4);
                bcSmartspaceCard.mNextAlarmImageView.setImageDrawable(bcSmartspaceCard.mNextAlarmIconDrawable);
                BcSmartspaceTemplateDataUtils.offsetImageViewForIcon(bcSmartspaceCard.mNextAlarmImageView, bcSmartspaceCard.mNextAlarmIconDrawable);
                BcSmartspaceTemplateDataUtils.updateVisibility(bcSmartspaceCard.mNextAlarmImageView, 0);
                String description2 = bcNextAlarmData3.getDescription(null);
                TextView textView = bcSmartspaceCard.mNextAlarmTextView;
                Context context2 = bcSmartspaceCard.getContext();
                Object[] objArr2 = new Object[i2];
                objArr2[0] = description2;
                textView.setContentDescription(context2.getString(R.string.accessibility_next_alarm, objArr2));
                bcSmartspaceCard.mNextAlarmTextView.setText(description2);
                BcSmartspaceTemplateDataUtils.updateVisibility(bcSmartspaceCard.mNextAlarmTextView, 0);
                bcNextAlarmData3.setOnClickListener(bcSmartspaceCard.mNextAlarmImageView, null, bcSmartspaceCard.mEventNotifier, BcSmartSpaceUtil.getLoggingDisplaySurface(bcSmartspaceCard.getContext().getPackageName(), bcSmartspaceCard.mIsDreaming, bcSmartspaceCard.mDozeAmount));
                bcNextAlarmData3.setOnClickListener(bcSmartspaceCard.mNextAlarmTextView, null, bcSmartspaceCard.mEventNotifier, BcSmartSpaceUtil.getLoggingDisplaySurface(bcSmartspaceCard.getContext().getPackageName(), bcSmartspaceCard.mIsDreaming, bcSmartspaceCard.mDozeAmount));
            }
            bcSmartspaceCard.updateZenVisibility();
        }
    }

    public final void setDozeAmount(float f) {
        this.mCurrentTextColor = ColorUtils.blendARGB(this.mPrimaryTextColor, this.mDozeColor, f);
        this.mDozeAmount = f;
        updateTargetVisibility();
        for (int i = 0; i < this.mViewHolders.size(); i++) {
            SparseArray<ViewHolder> sparseArray = this.mViewHolders;
            ViewHolder viewHolder = sparseArray.get(sparseArray.keyAt(i));
            if (viewHolder != null) {
                BcSmartspaceCard bcSmartspaceCard = viewHolder.mLegacyCard;
                if (bcSmartspaceCard != null) {
                    bcSmartspaceCard.setPrimaryTextColor(this.mCurrentTextColor);
                    bcSmartspaceCard.setDozeAmount(this.mDozeAmount);
                }
                BaseTemplateCard baseTemplateCard = viewHolder.mCard;
                if (baseTemplateCard != null) {
                    baseTemplateCard.setPrimaryTextColor(this.mCurrentTextColor);
                    baseTemplateCard.setDozeAmount(this.mDozeAmount);
                }
            }
        }
    }

    public final void updateTargetVisibility() {
        ArrayList arrayList;
        ArrayList arrayList2;
        ArrayList arrayList3;
        boolean z;
        ArrayList arrayList4;
        if (Float.compare(this.mDozeAmount, 1.0f) == 0) {
            if (isMediaPreferred(this.mAODTargets)) {
                arrayList4 = this.mMediaTargets;
            } else if (this.mHasDifferentTargets) {
                arrayList4 = this.mAODTargets;
            } else {
                arrayList4 = this.mLockscreenTargets;
            }
            this.mSmartspaceTargets = arrayList4;
            notifyDataSetChanged();
        } else if (Float.compare(this.mDozeAmount, 0.0f) == 0) {
            if (isMediaPreferred(this.mLockscreenTargets) && this.mKeyguardBypassEnabled) {
                arrayList = this.mMediaTargets;
            } else {
                arrayList = this.mLockscreenTargets;
            }
            this.mSmartspaceTargets = arrayList;
            notifyDataSetChanged();
        }
        if (isMediaPreferred(this.mAODTargets)) {
            arrayList2 = this.mMediaTargets;
        } else if (this.mHasDifferentTargets) {
            arrayList2 = this.mAODTargets;
        } else {
            arrayList2 = this.mLockscreenTargets;
        }
        if (isMediaPreferred(this.mLockscreenTargets) && this.mKeyguardBypassEnabled) {
            arrayList3 = this.mMediaTargets;
        } else {
            arrayList3 = this.mLockscreenTargets;
        }
        if (arrayList2 != arrayList3) {
            z = true;
        } else {
            z = false;
        }
        this.mHasAodLockscreenTransition = z;
    }

    public CardPagerAdapter(View view) {
        this.mRoot = view;
        int attrColor = GraphicsUtils.getAttrColor(16842806, view.getContext());
        this.mPrimaryTextColor = attrColor;
        this.mCurrentTextColor = attrColor;
    }

    public static int getFeatureType(SmartspaceTarget smartspaceTarget) {
        List actionChips = smartspaceTarget.getActionChips();
        int featureType = smartspaceTarget.getFeatureType();
        if (actionChips != null && !actionChips.isEmpty()) {
            if (featureType != 13 || actionChips.size() != 1) {
                return -1;
            }
            return -2;
        }
        return featureType;
    }

    public final void addDefaultDateCardIfEmpty(ArrayList arrayList) {
        if (arrayList.isEmpty()) {
            arrayList.add(new SmartspaceTarget.Builder("date_card_794317_92634", new ComponentName(this.mRoot.getContext(), CardPagerAdapter.class), this.mRoot.getContext().getUser()).setFeatureType(1).build());
        }
    }

    public final boolean isMediaPreferred(ArrayList arrayList) {
        boolean z;
        if (arrayList.size() == 1 && ((SmartspaceTarget) arrayList.get(0)).getFeatureType() == 1) {
            z = true;
        } else {
            z = false;
        }
        if (z && !this.mMediaTargets.isEmpty()) {
            return true;
        }
        return false;
    }
}
