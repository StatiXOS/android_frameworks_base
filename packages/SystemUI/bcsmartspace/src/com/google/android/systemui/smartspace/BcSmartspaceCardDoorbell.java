package com.google.android.systemui.smartspace;

import android.app.smartspace.SmartspaceTarget;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.graphics.ImageDecoder;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.android.launcher3.icons.RoundDrawableWrapper;
import com.android.systemui.R;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
/* loaded from: classes2.dex */
public class BcSmartspaceCardDoorbell extends BcSmartspaceCardGenericImage {
    public static final /* synthetic */ int $r8$clinit = 0;
    public int mGifFrameDurationInMs;
    public ImageView mLoadingIcon;
    public ViewGroup mLoadingScreenView;
    public String mPreviousTargetId;
    public ProgressBar mProgressBar;
    public final HashMap mUriToDrawable;

    /* loaded from: classes2.dex */
    public static class DrawableWithUri extends RoundDrawableWrapper {
        public ContentResolver mContentResolver;
        public Drawable mDrawable;
        public int mHeightInPx;
        public WeakReference<ImageView> mImageViewWeakReference;
        public WeakReference<View> mLoadingScreenWeakReference;
        public Uri mUri;

        public DrawableWithUri(float f, int i, ContentResolver contentResolver, Uri uri, WeakReference weakReference, WeakReference weakReference2) {
            super(new ColorDrawable(0), f);
            this.mUri = uri;
            this.mHeightInPx = i;
            this.mContentResolver = contentResolver;
            this.mImageViewWeakReference = weakReference;
            this.mLoadingScreenWeakReference = weakReference2;
        }
    }

    /* loaded from: classes2.dex */
    public static class LoadUriTask extends AsyncTask<DrawableWithUri, Void, DrawableWithUri> {
        @Override // android.os.AsyncTask
        public final DrawableWithUri doInBackground(DrawableWithUri[] drawableWithUriArr) {
            DrawableWithUri[] drawableWithUriArr2 = drawableWithUriArr;
            Drawable drawable = null;
            if (drawableWithUriArr2.length <= 0) {
                return null;
            }
            DrawableWithUri drawableWithUri = drawableWithUriArr2[0];
            try {
                InputStream openInputStream = drawableWithUri.mContentResolver.openInputStream(drawableWithUri.mUri);
                final int i = drawableWithUri.mHeightInPx;
                int i2 = BcSmartspaceCardDoorbell.$r8$clinit;
                try {
                    drawable = ImageDecoder.decodeDrawable(ImageDecoder.createSource((Resources) null, openInputStream), new ImageDecoder.OnHeaderDecodedListener() { // from class: com.google.android.systemui.smartspace.BcSmartspaceCardDoorbell$$ExternalSyntheticLambda2
                        @Override // android.graphics.ImageDecoder.OnHeaderDecodedListener
                        public final void onHeaderDecoded(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source) {
                            float f;
                            int i3 = i;
                            int i4 = BcSmartspaceCardDoorbell.$r8$clinit;
                            imageDecoder.setAllocator(3);
                            Size size = imageInfo.getSize();
                            if (size.getHeight() != 0) {
                                f = size.getWidth() / size.getHeight();
                            } else {
                                f = 0.0f;
                            }
                            imageDecoder.setTargetSize((int) (i3 * f), i3);
                        }
                    });
                } catch (IOException e) {
                    Log.e("BcSmartspaceCardBell", "Unable to decode stream: " + e);
                }
                drawableWithUri.mDrawable = drawable;
            } catch (Exception e2) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline0.m("open uri:");
                m.append(drawableWithUri.mUri);
                m.append(" got exception:");
                m.append(e2);
                Log.w("BcSmartspaceCardBell", m.toString());
            }
            return drawableWithUri;
        }

        @Override // android.os.AsyncTask
        public final void onPostExecute(DrawableWithUri drawableWithUri) {
            DrawableWithUri drawableWithUri2 = drawableWithUri;
            if (drawableWithUri2 != null) {
                Drawable drawable = drawableWithUri2.mDrawable;
                if (drawable != null) {
                    drawableWithUri2.setDrawable(drawable);
                    ImageView imageView = drawableWithUri2.mImageViewWeakReference.get();
                    int intrinsicWidth = drawableWithUri2.mDrawable.getIntrinsicWidth();
                    if (imageView.getLayoutParams().width != intrinsicWidth) {
                        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline0.m("imageView requestLayout ");
                        m.append(drawableWithUri2.mUri);
                        Log.d("BcSmartspaceCardBell", m.toString());
                        imageView.getLayoutParams().width = intrinsicWidth;
                        imageView.requestLayout();
                    }
                } else {
                    BcSmartspaceTemplateDataUtils.updateVisibility(drawableWithUri2.mImageViewWeakReference.get(), 8);
                }
                BcSmartspaceTemplateDataUtils.updateVisibility(drawableWithUri2.mLoadingScreenWeakReference.get(), 8);
            }
        }
    }

    public BcSmartspaceCardDoorbell(Context context) {
        super(context);
        this.mUriToDrawable = new HashMap();
        this.mGifFrameDurationInMs = 200;
    }

    /* JADX WARN: Removed duplicated region for block: B:69:0x0264  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0289  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x02a9  */
    @Override // com.google.android.systemui.smartspace.BcSmartspaceCardGenericImage, com.google.android.systemui.smartspace.BcSmartspaceCardSecondary
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean setSmartspaceActions(android.app.smartspace.SmartspaceTarget r14, com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceEventNotifier r15, com.google.android.systemui.smartspace.logging.BcSmartspaceCardLoggingInfo r16) {
        /*
            Method dump skipped, instructions count: 687
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.smartspace.BcSmartspaceCardDoorbell.setSmartspaceActions(android.app.smartspace.SmartspaceTarget, com.android.systemui.plugins.BcSmartspaceDataPlugin$SmartspaceEventNotifier, com.google.android.systemui.smartspace.logging.BcSmartspaceCardLoggingInfo):boolean");
    }

    public final void maybeResetImageView(SmartspaceTarget smartspaceTarget) {
        boolean z = !smartspaceTarget.getSmartspaceTargetId().equals(this.mPreviousTargetId);
        this.mPreviousTargetId = smartspaceTarget.getSmartspaceTargetId();
        if (z) {
            this.mImageView.getLayoutParams().width = -2;
            this.mImageView.setImageDrawable(null);
            this.mUriToDrawable.clear();
        }
    }

    @Override // com.google.android.systemui.smartspace.BcSmartspaceCardGenericImage, android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mLoadingScreenView = (ViewGroup) findViewById(R.id.loading_screen);
        this.mProgressBar = (ProgressBar) findViewById(R.id.indeterminateBar);
        this.mLoadingIcon = (ImageView) findViewById(R.id.loading_screen_icon);
    }

    @Override // com.google.android.systemui.smartspace.BcSmartspaceCardGenericImage, com.google.android.systemui.smartspace.BcSmartspaceCardSecondary
    public final void resetUi() {
        super.resetUi();
        BcSmartspaceTemplateDataUtils.updateVisibility(this.mImageView, 8);
        BcSmartspaceTemplateDataUtils.updateVisibility(this.mLoadingScreenView, 8);
        BcSmartspaceTemplateDataUtils.updateVisibility(this.mProgressBar, 8);
        BcSmartspaceTemplateDataUtils.updateVisibility(this.mLoadingIcon, 8);
    }

    public BcSmartspaceCardDoorbell(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mUriToDrawable = new HashMap();
        this.mGifFrameDurationInMs = 200;
    }
}
