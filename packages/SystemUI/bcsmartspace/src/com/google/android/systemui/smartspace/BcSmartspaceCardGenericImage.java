package com.google.android.systemui.smartspace;

import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceTarget;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.constraintlayout.motion.widget.MotionLayout$$ExternalSyntheticOutline1;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.systemui.R;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.google.android.systemui.smartspace.logging.BcSmartspaceCardLoggingInfo;
/* loaded from: classes2.dex */
public class BcSmartspaceCardGenericImage extends BcSmartspaceCardSecondary {
    public ImageView mImageView;

    public BcSmartspaceCardGenericImage(Context context) {
        super(context);
    }

    @Override // com.google.android.systemui.smartspace.BcSmartspaceCardSecondary
    public void setTextColor(int i) {
    }

    public BcSmartspaceCardGenericImage(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.google.android.systemui.smartspace.BcSmartspaceCardSecondary
    public void resetUi() {
        this.mImageView.setImageBitmap(null);
    }

    public void setImageBitmap(Bitmap bitmap) {
        this.mImageView.setImageBitmap(bitmap);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mImageView = (ImageView) findViewById(R.id.image_view);
    }

    @Override // com.google.android.systemui.smartspace.BcSmartspaceCardSecondary
    public boolean setSmartspaceActions(SmartspaceTarget smartspaceTarget, BcSmartspaceDataPlugin.SmartspaceEventNotifier smartspaceEventNotifier, BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo) {
        Bundle extras;
        SmartspaceAction baseAction = smartspaceTarget.getBaseAction();
        if (baseAction == null) {
            extras = null;
        } else {
            extras = baseAction.getExtras();
        }
        if (extras != null && extras.containsKey("imageBitmap")) {
            if (extras.containsKey("imageScaleType")) {
                String string = extras.getString("imageScaleType");
                try {
                    this.mImageView.setScaleType(ImageView.ScaleType.valueOf(string));
                } catch (IllegalArgumentException unused) {
                    MotionLayout$$ExternalSyntheticOutline1.m("Invalid imageScaleType value: ", string, "SmartspaceGenericImg");
                }
            }
            String dimensionRatio = BcSmartSpaceUtil.getDimensionRatio(extras);
            if (dimensionRatio != null) {
                ((ConstraintLayout.LayoutParams) this.mImageView.getLayoutParams()).dimensionRatio = dimensionRatio;
            }
            if (extras.containsKey("imageLayoutWidth")) {
                ((ViewGroup.MarginLayoutParams) ((ConstraintLayout.LayoutParams) this.mImageView.getLayoutParams())).width = extras.getInt("imageLayoutWidth");
            }
            if (extras.containsKey("imageLayoutHeight")) {
                ((ViewGroup.MarginLayoutParams) ((ConstraintLayout.LayoutParams) this.mImageView.getLayoutParams())).height = extras.getInt("imageLayoutHeight");
            }
            setImageBitmap((Bitmap) extras.get("imageBitmap"));
            return true;
        }
        return false;
    }
}
