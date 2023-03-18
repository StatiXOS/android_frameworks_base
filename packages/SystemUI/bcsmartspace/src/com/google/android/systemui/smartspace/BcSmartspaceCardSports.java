package com.google.android.systemui.smartspace;

import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceTarget;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.R;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.google.android.systemui.smartspace.logging.BcSmartspaceCardLoggingInfo;
/* loaded from: classes2.dex */
public class BcSmartspaceCardSports extends BcSmartspaceCardSecondary {
    public ImageView mFirstCompetitorLogo;
    public TextView mFirstCompetitorScore;
    public ImageView mSecondCompetitorLogo;
    public TextView mSecondCompetitorScore;
    public TextView mSummaryView;

    public BcSmartspaceCardSports(Context context) {
        super(context);
    }

    public BcSmartspaceCardSports(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.google.android.systemui.smartspace.BcSmartspaceCardSecondary
    public final void resetUi() {
        BcSmartspaceTemplateDataUtils.updateVisibility(this.mSummaryView, 4);
        BcSmartspaceTemplateDataUtils.updateVisibility(this.mFirstCompetitorScore, 4);
        BcSmartspaceTemplateDataUtils.updateVisibility(this.mSecondCompetitorScore, 4);
        BcSmartspaceTemplateDataUtils.updateVisibility(this.mFirstCompetitorLogo, 4);
        BcSmartspaceTemplateDataUtils.updateVisibility(this.mSecondCompetitorLogo, 4);
    }

    @Override // com.google.android.systemui.smartspace.BcSmartspaceCardSecondary
    public final void setTextColor(int i) {
        this.mSummaryView.setTextColor(i);
        this.mFirstCompetitorScore.setTextColor(i);
        this.mSecondCompetitorScore.setTextColor(i);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mSummaryView = (TextView) findViewById(R.id.match_time_summary);
        this.mFirstCompetitorScore = (TextView) findViewById(R.id.first_competitor_score);
        this.mSecondCompetitorScore = (TextView) findViewById(R.id.second_competitor_score);
        this.mFirstCompetitorLogo = (ImageView) findViewById(R.id.first_competitor_logo);
        this.mSecondCompetitorLogo = (ImageView) findViewById(R.id.second_competitor_logo);
    }

    @Override // com.google.android.systemui.smartspace.BcSmartspaceCardSecondary
    public final boolean setSmartspaceActions(SmartspaceTarget smartspaceTarget, BcSmartspaceDataPlugin.SmartspaceEventNotifier smartspaceEventNotifier, BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo) {
        Bundle extras;
        boolean z;
        SmartspaceAction baseAction = smartspaceTarget.getBaseAction();
        if (baseAction == null) {
            extras = null;
        } else {
            extras = baseAction.getExtras();
        }
        if (extras == null) {
            return false;
        }
        if (extras.containsKey("matchTimeSummary")) {
            String string = extras.getString("matchTimeSummary");
            TextView textView = this.mSummaryView;
            if (textView == null) {
                Log.w("BcSmartspaceCardSports", "No match time summary view to update");
            } else {
                BcSmartspaceTemplateDataUtils.updateVisibility(textView, 0);
                this.mSummaryView.setText(string);
            }
            z = true;
        } else {
            z = false;
        }
        if (extras.containsKey("firstCompetitorScore")) {
            String string2 = extras.getString("firstCompetitorScore");
            TextView textView2 = this.mFirstCompetitorScore;
            if (textView2 == null) {
                Log.w("BcSmartspaceCardSports", "No first competitor logo view to update");
            } else {
                BcSmartspaceTemplateDataUtils.updateVisibility(textView2, 0);
                this.mFirstCompetitorScore.setText(string2);
            }
            z = true;
        }
        if (extras.containsKey("secondCompetitorScore")) {
            String string3 = extras.getString("secondCompetitorScore");
            TextView textView3 = this.mSecondCompetitorScore;
            if (textView3 == null) {
                Log.w("BcSmartspaceCardSports", "No second competitor logo view to update");
            } else {
                BcSmartspaceTemplateDataUtils.updateVisibility(textView3, 0);
                this.mSecondCompetitorScore.setText(string3);
            }
            z = true;
        }
        if (extras.containsKey("firstCompetitorLogo")) {
            Bitmap bitmap = (Bitmap) extras.get("firstCompetitorLogo");
            ImageView imageView = this.mFirstCompetitorLogo;
            if (imageView == null) {
                Log.w("BcSmartspaceCardSports", "No first competitor logo view to update");
            } else {
                BcSmartspaceTemplateDataUtils.updateVisibility(imageView, 0);
                this.mFirstCompetitorLogo.setImageBitmap(bitmap);
            }
            z = true;
        }
        if (extras.containsKey("secondCompetitorLogo")) {
            Bitmap bitmap2 = (Bitmap) extras.get("secondCompetitorLogo");
            ImageView imageView2 = this.mSecondCompetitorLogo;
            if (imageView2 == null) {
                Log.w("BcSmartspaceCardSports", "No second competitor logo view to update");
                return true;
            }
            BcSmartspaceTemplateDataUtils.updateVisibility(imageView2, 0);
            this.mSecondCompetitorLogo.setImageBitmap(bitmap2);
            return true;
        }
        return z;
    }
}
