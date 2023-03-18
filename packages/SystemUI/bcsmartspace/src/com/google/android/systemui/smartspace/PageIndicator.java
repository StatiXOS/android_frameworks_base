package com.google.android.systemui.smartspace;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.content.res.AppCompatResources;
import com.android.systemui.R;
/* loaded from: classes2.dex */
public class PageIndicator extends LinearLayout {
    public int mCurrentPageIndex;
    public int mNumPages;
    public int mPrimaryColor;

    public PageIndicator(Context context) {
        this(context, null);
    }

    public final void setNumPages(int i, boolean z) {
        int i2;
        ImageView imageView;
        LinearLayout.LayoutParams layoutParams;
        float f;
        int i3;
        if (i <= 0) {
            Log.w("PageIndicator", "Total number of pages invalid: " + i + ". Assuming 1 page.");
            i = 1;
        }
        if (i < 2) {
            BcSmartspaceTemplateDataUtils.updateVisibility(this, 8);
            return;
        }
        BcSmartspaceTemplateDataUtils.updateVisibility(this, 0);
        if (i != this.mNumPages) {
            this.mNumPages = i;
            int i4 = this.mCurrentPageIndex;
            if (i4 < 0) {
                if (z) {
                    i3 = i - 1;
                } else {
                    i3 = 0;
                }
                this.mCurrentPageIndex = i3;
            } else if (i4 >= i) {
                if (z) {
                    i2 = 0;
                } else {
                    i2 = i - 1;
                }
                this.mCurrentPageIndex = i2;
            }
            int childCount = getChildCount() - this.mNumPages;
            for (int i5 = 0; i5 < childCount; i5++) {
                removeViewAt(0);
            }
            int dimensionPixelSize = getContext().getResources().getDimensionPixelSize(R.dimen.page_indicator_dot_margin);
            for (int i6 = 0; i6 < this.mNumPages; i6++) {
                if (i6 < getChildCount()) {
                    imageView = (ImageView) getChildAt(i6);
                } else {
                    imageView = new ImageView(getContext());
                }
                if (i6 < getChildCount()) {
                    layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                } else {
                    layoutParams = new LinearLayout.LayoutParams(-2, -2);
                }
                if (i6 == 0) {
                    layoutParams.setMarginStart(0);
                } else {
                    layoutParams.setMarginStart(dimensionPixelSize);
                }
                if (i6 == this.mNumPages - 1) {
                    layoutParams.setMarginEnd(0);
                } else {
                    layoutParams.setMarginEnd(dimensionPixelSize);
                }
                if (i6 < getChildCount()) {
                    imageView.setLayoutParams(layoutParams);
                } else {
                    Drawable drawable = AppCompatResources.getDrawable(R.drawable.page_indicator_dot, getContext());
                    drawable.setTint(this.mPrimaryColor);
                    imageView.setImageDrawable(drawable);
                    addView(imageView, layoutParams);
                }
                if (i6 == this.mCurrentPageIndex) {
                    f = 1.0f;
                } else {
                    f = 0.4f;
                }
                imageView.setAlpha(f);
            }
            setContentDescription(getContext().getString(R.string.accessibility_smartspace_page, 1, Integer.valueOf(this.mNumPages)));
        }
    }

    public final void setPageOffset(int i, float f) {
        int i2;
        int i3 = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
        if ((i3 != 0 || i != this.mCurrentPageIndex) && i >= 0 && i < getChildCount()) {
            ImageView imageView = (ImageView) getChildAt(i);
            int i4 = i + 1;
            ImageView imageView2 = (ImageView) getChildAt(i4);
            if (i3 == 0 || f >= 0.99f) {
                int i5 = this.mCurrentPageIndex;
                if (i5 >= 0 && i5 < getChildCount()) {
                    getChildAt(this.mCurrentPageIndex).setAlpha(0.4f);
                }
                if (i3 == 0) {
                    i2 = i;
                } else {
                    i2 = i4;
                }
                this.mCurrentPageIndex = i2;
            }
            imageView.setAlpha(((1.0f - f) * 0.6f) + 0.4f);
            if (imageView2 != null) {
                imageView2.setAlpha((0.6f * f) + 0.4f);
            }
            Context context = getContext();
            Object[] objArr = new Object[2];
            if (f >= 0.5d) {
                i4 = i + 2;
            }
            objArr[0] = Integer.valueOf(i4);
            objArr[1] = Integer.valueOf(this.mNumPages);
            setContentDescription(context.getString(R.string.accessibility_smartspace_page, objArr));
        }
    }

    public PageIndicator(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PageIndicator(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public PageIndicator(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(new int[]{16842806});
        int color = obtainStyledAttributes.getColor(0, 0);
        obtainStyledAttributes.recycle();
        this.mPrimaryColor = color;
        this.mCurrentPageIndex = -1;
        this.mNumPages = -1;
    }
}
