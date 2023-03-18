package com.google.android.systemui.smartspace.uitemplate;

import android.app.smartspace.SmartspaceTarget;
import android.app.smartspace.SmartspaceUtils;
import android.app.smartspace.uitemplatedata.BaseTemplateData;
import android.app.smartspace.uitemplatedata.Icon;
import android.app.smartspace.uitemplatedata.TapAction;
import android.app.smartspace.uitemplatedata.Text;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.launcher3.icons.GraphicsUtils;
import com.android.systemui.R;
import com.android.systemui.animation.Interpolators;
import com.google.android.systemui.smartspace.BcSmartSpaceUtil;
import com.google.android.systemui.smartspace.BcSmartspaceCardSecondary;
import com.google.android.systemui.smartspace.BcSmartspaceTemplateDataUtils;
import com.google.android.systemui.smartspace.CardPagerAdapter$$ExternalSyntheticLambda1;
import com.google.android.systemui.smartspace.DoubleShadowIconDrawable;
import com.google.android.systemui.smartspace.DoubleShadowTextView;
import com.google.android.systemui.smartspace.IcuDateTextView;
import com.google.android.systemui.smartspace.logging.BcSmartspaceCardLoggerUtil;
import com.google.android.systemui.smartspace.logging.BcSmartspaceCardLoggingInfo;
import com.google.android.systemui.smartspace.logging.BcSmartspaceCardMetadataLoggingInfo;
import com.google.android.systemui.smartspace.logging.BcSmartspaceSubcardLoggingInfo;
import java.util.List;
/* loaded from: classes2.dex */
public class BaseTemplateCard extends ConstraintLayout {
    public IcuDateTextView mDateView;
    public ImageView mDndImageView;
    public float mDozeAmount;
    public ViewGroup mExtrasGroup;
    public int mFeatureType;
    public int mIconTintColor;
    public boolean mIsDreaming;
    public BcSmartspaceCardLoggingInfo mLoggingInfo;
    public ImageView mNextAlarmImageView;
    public DoubleShadowTextView mNextAlarmTextView;
    public String mPrevSmartspaceTargetId;
    public BcSmartspaceCardSecondary mSecondaryCard;
    public ViewGroup mSecondaryCardPane;
    public boolean mShouldShowPageIndicator;
    public DoubleShadowTextView mSubtitleSupplementalView;
    public DoubleShadowTextView mSubtitleTextView;
    public DoubleShadowTextView mSupplementalLineTextView;
    public SmartspaceTarget mTarget;
    public BaseTemplateData mTemplateData;
    public ViewGroup mTextGroup;
    public DoubleShadowTextView mTitleTextView;
    public boolean mValidSecondaryCard;

    public BaseTemplateCard(Context context) {
        this(context, null);
    }

    public BaseTemplateCard(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mSecondaryCard = null;
        this.mFeatureType = 0;
        this.mLoggingInfo = null;
        this.mIconTintColor = GraphicsUtils.getAttrColor(16842806, getContext());
        this.mPrevSmartspaceTargetId = "";
        this.mTextGroup = null;
        this.mSecondaryCardPane = null;
        this.mDateView = null;
        this.mTitleTextView = null;
        this.mSubtitleTextView = null;
        this.mSubtitleSupplementalView = null;
        this.mExtrasGroup = null;
        this.mDndImageView = null;
        this.mNextAlarmImageView = null;
        this.mNextAlarmTextView = null;
        this.mSupplementalLineTextView = null;
        setImportantForAccessibility(2);
    }

    public static boolean shouldTint(BaseTemplateData.SubItemInfo subItemInfo) {
        if (subItemInfo != null && subItemInfo.getIcon() != null) {
            return subItemInfo.getIcon().shouldTint();
        }
        return false;
    }

    public final void resetTextView(DoubleShadowTextView doubleShadowTextView) {
        if (doubleShadowTextView == null) {
            return;
        }
        doubleShadowTextView.setCompoundDrawablesRelative(null, null, null, null);
        doubleShadowTextView.setOnClickListener(null);
        doubleShadowTextView.setContentDescription(null);
        doubleShadowTextView.setText((CharSequence) null);
        BcSmartspaceTemplateDataUtils.offsetTextViewForIcon(doubleShadowTextView, null, isRtl());
    }

    public final void setDozeAmount(float f) {
        boolean z;
        this.mDozeAmount = f;
        ImageView imageView = this.mDndImageView;
        if (imageView != null) {
            imageView.setAlpha(f);
        }
        SmartspaceTarget smartspaceTarget = this.mTarget;
        if (smartspaceTarget != null && smartspaceTarget.getBaseAction() != null && this.mTarget.getBaseAction().getExtras() != null) {
            Bundle extras = this.mTarget.getBaseAction().getExtras();
            if (this.mTitleTextView != null && extras.getBoolean("hide_title_on_aod")) {
                this.mTitleTextView.setAlpha(1.0f - f);
            }
            if (this.mSubtitleTextView != null && extras.getBoolean("hide_subtitle_on_aod")) {
                this.mSubtitleTextView.setAlpha(1.0f - f);
            }
        }
        if (this.mTextGroup != null) {
            ViewGroup viewGroup = this.mSecondaryCardPane;
            int i = 0;
            int i2 = 1;
            if (this.mDozeAmount != 1.0f && this.mValidSecondaryCard) {
                z = false;
            } else {
                z = true;
            }
            if (z) {
                i = 8;
            }
            BcSmartspaceTemplateDataUtils.updateVisibility(viewGroup, i);
            ViewGroup viewGroup2 = this.mSecondaryCardPane;
            if (viewGroup2 != null && viewGroup2.getVisibility() != 8) {
                ViewGroup viewGroup3 = this.mTextGroup;
                if (!isRtl()) {
                    i2 = -1;
                }
                viewGroup3.setTranslationX(Interpolators.EMPHASIZED.getInterpolation(this.mDozeAmount) * this.mSecondaryCardPane.getWidth() * i2);
                this.mSecondaryCardPane.setAlpha(Math.max(0.0f, Math.min(1.0f, ((1.0f - this.mDozeAmount) * 9.0f) - 6.0f)));
                return;
            }
            this.mTextGroup.setTranslationX(0.0f);
        }
    }

    public final void setPrimaryTextColor(int i) {
        this.mIconTintColor = i;
        DoubleShadowTextView doubleShadowTextView = this.mTitleTextView;
        if (doubleShadowTextView != null) {
            doubleShadowTextView.setTextColor(i);
            BaseTemplateData baseTemplateData = this.mTemplateData;
            if (baseTemplateData != null) {
                updateTextViewIconTint(this.mTitleTextView, shouldTint(baseTemplateData.getPrimaryItem()));
            }
        }
        IcuDateTextView icuDateTextView = this.mDateView;
        if (icuDateTextView != null) {
            icuDateTextView.setTextColor(i);
        }
        DoubleShadowTextView doubleShadowTextView2 = this.mSubtitleTextView;
        if (doubleShadowTextView2 != null) {
            doubleShadowTextView2.setTextColor(i);
            BaseTemplateData baseTemplateData2 = this.mTemplateData;
            if (baseTemplateData2 != null) {
                updateTextViewIconTint(this.mSubtitleTextView, shouldTint(baseTemplateData2.getSubtitleItem()));
            }
        }
        DoubleShadowTextView doubleShadowTextView3 = this.mSubtitleSupplementalView;
        if (doubleShadowTextView3 != null) {
            doubleShadowTextView3.setTextColor(i);
            BaseTemplateData baseTemplateData3 = this.mTemplateData;
            if (baseTemplateData3 != null) {
                updateTextViewIconTint(this.mSubtitleSupplementalView, shouldTint(baseTemplateData3.getSubtitleSupplementalItem()));
            }
        }
        updateZenColors();
    }

    public final void setUpTextView(DoubleShadowTextView doubleShadowTextView, BaseTemplateData.SubItemInfo subItemInfo, CardPagerAdapter$$ExternalSyntheticLambda1 cardPagerAdapter$$ExternalSyntheticLambda1) {
        BcSmartspaceSubcardLoggingInfo bcSmartspaceSubcardLoggingInfo;
        List<BcSmartspaceCardMetadataLoggingInfo> list;
        CharSequence text;
        CharSequence charSequence;
        if (doubleShadowTextView == null) {
            Log.d("SsBaseTemplateCard", "No text view can be set up");
            return;
        }
        resetTextView(doubleShadowTextView);
        if (subItemInfo == null) {
            Log.d("SsBaseTemplateCard", "Passed-in item info is null");
            BcSmartspaceTemplateDataUtils.updateVisibility(doubleShadowTextView, 8);
            return;
        }
        Text text2 = subItemInfo.getText();
        BcSmartspaceTemplateDataUtils.setText(doubleShadowTextView, subItemInfo.getText());
        if (!SmartspaceUtils.isEmpty(text2)) {
            doubleShadowTextView.setTextColor(this.mIconTintColor);
        }
        Icon icon = subItemInfo.getIcon();
        int i = 0;
        if (icon != null) {
            DoubleShadowIconDrawable doubleShadowIconDrawable = new DoubleShadowIconDrawable(getContext());
            doubleShadowIconDrawable.setIcon(BcSmartSpaceUtil.getIconDrawable(getContext(), icon.getIcon()));
            doubleShadowTextView.setCompoundDrawablesRelative(doubleShadowIconDrawable, null, null, null);
            if (SmartspaceUtils.isEmpty(text2)) {
                text = "";
            } else {
                text = text2.getText();
            }
            CharSequence contentDescription = icon.getContentDescription();
            if (TextUtils.isEmpty(text)) {
                charSequence = contentDescription;
            } else {
                charSequence = text;
                if (!TextUtils.isEmpty(contentDescription)) {
                    charSequence = ((ViewGroup) this).mContext.getString(R.string.generic_smartspace_concatenated_desc, contentDescription, text);
                }
            }
            doubleShadowTextView.setContentDescription(charSequence);
            updateTextViewIconTint(doubleShadowTextView, icon.shouldTint());
            BcSmartspaceTemplateDataUtils.offsetTextViewForIcon(doubleShadowTextView, doubleShadowIconDrawable, isRtl());
        }
        BcSmartspaceTemplateDataUtils.updateVisibility(doubleShadowTextView, 0);
        SmartspaceTarget smartspaceTarget = this.mTarget;
        TapAction tapAction = subItemInfo.getTapAction();
        BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo = this.mLoggingInfo;
        if (bcSmartspaceCardLoggingInfo != null && (bcSmartspaceSubcardLoggingInfo = bcSmartspaceCardLoggingInfo.mSubcardInfo) != null && (list = bcSmartspaceSubcardLoggingInfo.mSubcards) != null && !list.isEmpty() && subItemInfo.getLoggingInfo() != null) {
            int featureType = subItemInfo.getLoggingInfo().getFeatureType();
            BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo2 = this.mLoggingInfo;
            if (featureType != bcSmartspaceCardLoggingInfo2.mFeatureType) {
                List<BcSmartspaceCardMetadataLoggingInfo> list2 = bcSmartspaceCardLoggingInfo2.mSubcardInfo.mSubcards;
                BaseTemplateData.SubItemLoggingInfo loggingInfo = subItemInfo.getLoggingInfo();
                int i2 = 0;
                while (true) {
                    if (i2 >= list2.size()) {
                        break;
                    }
                    BcSmartspaceCardMetadataLoggingInfo bcSmartspaceCardMetadataLoggingInfo = list2.get(i2);
                    if (bcSmartspaceCardMetadataLoggingInfo.mInstanceId == loggingInfo.getInstanceId() && bcSmartspaceCardMetadataLoggingInfo.mCardTypeId == loggingInfo.getFeatureType()) {
                        i = i2 + 1;
                        break;
                    }
                    i2++;
                }
            }
        }
        BcSmartSpaceUtil.setOnClickListener$1(doubleShadowTextView, smartspaceTarget, tapAction, cardPagerAdapter$$ExternalSyntheticLambda1, "SsBaseTemplateCard", bcSmartspaceCardLoggingInfo, i);
    }

    public final void updateZenColors() {
        ImageView imageView = this.mNextAlarmImageView;
        if (imageView != null && imageView.getDrawable() != null) {
            imageView.getDrawable().setTint(this.mIconTintColor);
            imageView.invalidate();
        }
        DoubleShadowTextView doubleShadowTextView = this.mNextAlarmTextView;
        if (doubleShadowTextView != null) {
            doubleShadowTextView.setTextColor(this.mIconTintColor);
        }
        ImageView imageView2 = this.mDndImageView;
        if (imageView2 != null && imageView2.getDrawable() != null) {
            imageView2.getDrawable().setTint(this.mIconTintColor);
            imageView2.invalidate();
        }
        DoubleShadowTextView doubleShadowTextView2 = this.mSupplementalLineTextView;
        if (doubleShadowTextView2 != null) {
            doubleShadowTextView2.setTextColor(this.mIconTintColor);
            if (BcSmartspaceCardLoggerUtil.containsValidTemplateType(this.mTemplateData)) {
                updateTextViewIconTint(this.mSupplementalLineTextView, shouldTint(this.mTemplateData.getSupplementalLineItem()));
            }
        }
    }

    public final void updateZenVisibility() {
        boolean z;
        boolean z2;
        boolean z3;
        if (this.mExtrasGroup == null) {
            return;
        }
        ImageView imageView = this.mDndImageView;
        boolean z4 = true;
        if (imageView != null && imageView.getVisibility() == 0) {
            z = true;
        } else {
            z = false;
        }
        ImageView imageView2 = this.mNextAlarmImageView;
        if (imageView2 != null && imageView2.getVisibility() == 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        DoubleShadowTextView doubleShadowTextView = this.mSupplementalLineTextView;
        if (doubleShadowTextView != null && doubleShadowTextView.getVisibility() == 0) {
            z3 = true;
        } else {
            z3 = false;
        }
        if ((!z && !z2 && !z3) || (this.mShouldShowPageIndicator && this.mDateView == null)) {
            z4 = false;
        }
        if (!z4) {
            BcSmartspaceTemplateDataUtils.updateVisibility(this.mExtrasGroup, 8);
            return;
        }
        BcSmartspaceTemplateDataUtils.updateVisibility(this.mExtrasGroup, 0);
        updateZenColors();
    }

    @Override // android.view.View
    public final AccessibilityNodeInfo createAccessibilityNodeInfo() {
        AccessibilityNodeInfo createAccessibilityNodeInfo = super.createAccessibilityNodeInfo();
        createAccessibilityNodeInfo.getExtras().putCharSequence("AccessibilityNodeInfo.roleDescription", " ");
        return createAccessibilityNodeInfo;
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mTextGroup = (ViewGroup) findViewById(R.id.text_group);
        this.mSecondaryCardPane = (ViewGroup) findViewById(R.id.secondary_card_group);
        this.mDateView = (IcuDateTextView) findViewById(R.id.date);
        this.mTitleTextView = (DoubleShadowTextView) findViewById(R.id.title_text);
        this.mSubtitleTextView = (DoubleShadowTextView) findViewById(R.id.subtitle_text);
        this.mSubtitleSupplementalView = (DoubleShadowTextView) findViewById(R.id.base_action_icon_subtitle);
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.smartspace_extras_group);
        this.mExtrasGroup = viewGroup;
        if (viewGroup != null) {
            this.mDndImageView = (ImageView) viewGroup.findViewById(R.id.dnd_icon);
            this.mNextAlarmImageView = (ImageView) this.mExtrasGroup.findViewById(R.id.alarm_icon);
            this.mNextAlarmTextView = (DoubleShadowTextView) this.mExtrasGroup.findViewById(R.id.alarm_text);
            this.mSupplementalLineTextView = (DoubleShadowTextView) this.mExtrasGroup.findViewById(R.id.supplemental_line_text);
        }
    }

    public final void updateTextViewIconTint(DoubleShadowTextView doubleShadowTextView, boolean z) {
        Drawable[] compoundDrawablesRelative;
        for (Drawable drawable : doubleShadowTextView.getCompoundDrawablesRelative()) {
            if (drawable != null) {
                if (z) {
                    drawable.setTint(this.mIconTintColor);
                } else {
                    drawable.setTintList(null);
                }
            }
        }
    }
}
