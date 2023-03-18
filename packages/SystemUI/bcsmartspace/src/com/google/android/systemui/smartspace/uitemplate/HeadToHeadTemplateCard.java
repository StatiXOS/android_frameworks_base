package com.google.android.systemui.smartspace.uitemplate;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.R;
import com.google.android.systemui.smartspace.BcSmartspaceCardSecondary;
import com.google.android.systemui.smartspace.BcSmartspaceTemplateDataUtils;
/* loaded from: classes2.dex */
public class HeadToHeadTemplateCard extends BcSmartspaceCardSecondary {
    public ImageView mFirstCompetitorIcon;
    public TextView mFirstCompetitorText;
    public TextView mHeadToHeadTitle;
    public ImageView mSecondCompetitorIcon;
    public TextView mSecondCompetitorText;

    public HeadToHeadTemplateCard(Context context) {
        super(context);
    }

    public HeadToHeadTemplateCard(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.google.android.systemui.smartspace.BcSmartspaceCardSecondary
    public final void resetUi() {
        BcSmartspaceTemplateDataUtils.updateVisibility(this.mHeadToHeadTitle, 8);
        BcSmartspaceTemplateDataUtils.updateVisibility(this.mFirstCompetitorText, 8);
        BcSmartspaceTemplateDataUtils.updateVisibility(this.mSecondCompetitorText, 8);
        BcSmartspaceTemplateDataUtils.updateVisibility(this.mFirstCompetitorIcon, 8);
        BcSmartspaceTemplateDataUtils.updateVisibility(this.mSecondCompetitorIcon, 8);
    }

    @Override // com.google.android.systemui.smartspace.BcSmartspaceCardSecondary
    public final void setTextColor(int i) {
        this.mFirstCompetitorText.setTextColor(i);
        this.mSecondCompetitorText.setTextColor(i);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mHeadToHeadTitle = (TextView) findViewById(R.id.head_to_head_title);
        this.mFirstCompetitorText = (TextView) findViewById(R.id.first_competitor_text);
        this.mSecondCompetitorText = (TextView) findViewById(R.id.second_competitor_text);
        this.mFirstCompetitorIcon = (ImageView) findViewById(R.id.first_competitor_icon);
        this.mSecondCompetitorIcon = (ImageView) findViewById(R.id.second_competitor_icon);
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0066  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x008c  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00b2  */
    @Override // com.google.android.systemui.smartspace.BcSmartspaceCardSecondary
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean setSmartspaceActions(android.app.smartspace.SmartspaceTarget r10, com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceEventNotifier r11, com.google.android.systemui.smartspace.logging.BcSmartspaceCardLoggingInfo r12) {
        /*
            Method dump skipped, instructions count: 230
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.smartspace.uitemplate.HeadToHeadTemplateCard.setSmartspaceActions(android.app.smartspace.SmartspaceTarget, com.android.systemui.plugins.BcSmartspaceDataPlugin$SmartspaceEventNotifier, com.google.android.systemui.smartspace.logging.BcSmartspaceCardLoggingInfo):boolean");
    }
}
