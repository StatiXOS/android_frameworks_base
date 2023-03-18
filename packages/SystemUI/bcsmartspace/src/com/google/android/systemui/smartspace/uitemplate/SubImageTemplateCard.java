package com.google.android.systemui.smartspace.uitemplate;

import android.app.smartspace.SmartspaceTarget;
import android.app.smartspace.uitemplatedata.SubImageTemplateData;
import android.app.smartspace.uitemplatedata.TapAction;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.graphics.ImageDecoder;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.systemui.R;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.wm.shell.splitscreen.StageCoordinator$$ExternalSyntheticLambda7;
import com.google.android.systemui.smartspace.BcSmartSpaceUtil;
import com.google.android.systemui.smartspace.BcSmartspaceCardSecondary;
import com.google.android.systemui.smartspace.logging.BcSmartspaceCardLoggerUtil;
import com.google.android.systemui.smartspace.logging.BcSmartspaceCardLoggingInfo;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
/* loaded from: classes2.dex */
public class SubImageTemplateCard extends BcSmartspaceCardSecondary {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final Handler mHandler;
    public final HashMap mIconDrawableCache;
    public final int mImageHeight;
    public ImageView mImageView;

    /* loaded from: classes2.dex */
    public static class LoadUriTask extends AsyncTask<DrawableWrapper, Void, DrawableWrapper> {
        @Override // android.os.AsyncTask
        public final DrawableWrapper doInBackground(DrawableWrapper[] drawableWrapperArr) {
            DrawableWrapper[] drawableWrapperArr2 = drawableWrapperArr;
            Drawable drawable = null;
            if (drawableWrapperArr2.length <= 0) {
                return null;
            }
            DrawableWrapper drawableWrapper = drawableWrapperArr2[0];
            try {
                InputStream openInputStream = drawableWrapper.mContentResolver.openInputStream(drawableWrapper.mUri);
                final int i = drawableWrapper.mHeightInPx;
                int i2 = SubImageTemplateCard.$r8$clinit;
                try {
                    drawable = ImageDecoder.decodeDrawable(ImageDecoder.createSource((Resources) null, openInputStream), new ImageDecoder.OnHeaderDecodedListener() { // from class: com.google.android.systemui.smartspace.uitemplate.SubImageTemplateCard$$ExternalSyntheticLambda1
                        @Override // android.graphics.ImageDecoder.OnHeaderDecodedListener
                        public final void onHeaderDecoded(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source) {
                            float f;
                            int i3 = i;
                            int i4 = SubImageTemplateCard.$r8$clinit;
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
                    Log.e("SubImageTemplateCard", "Unable to decode stream: " + e);
                }
                drawableWrapper.mDrawable = drawable;
            } catch (Exception e2) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline0.m("open uri:");
                m.append(drawableWrapper.mUri);
                m.append(" got exception:");
                m.append(e2);
                Log.w("SubImageTemplateCard", m.toString());
            }
            return drawableWrapper;
        }

        @Override // android.os.AsyncTask
        public final void onPostExecute(DrawableWrapper drawableWrapper) {
            DrawableWrapper drawableWrapper2 = drawableWrapper;
            drawableWrapper2.mListener.onDrawableLoaded(drawableWrapper2.mDrawable);
        }
    }

    public SubImageTemplateCard(Context context) {
        this(context, null);
    }

    @Override // com.google.android.systemui.smartspace.BcSmartspaceCardSecondary
    public final void setTextColor(int i) {
    }

    /* loaded from: classes2.dex */
    public static class DrawableWrapper {
        public final ContentResolver mContentResolver;
        public Drawable mDrawable;
        public final int mHeightInPx;
        public final Icon.OnDrawableLoadedListener mListener;
        public final Uri mUri;

        public DrawableWrapper(Uri uri, ContentResolver contentResolver, int i, SubImageTemplateCard$$ExternalSyntheticLambda0 subImageTemplateCard$$ExternalSyntheticLambda0) {
            this.mUri = uri;
            this.mHeightInPx = i;
            this.mContentResolver = contentResolver;
            this.mListener = subImageTemplateCard$$ExternalSyntheticLambda0;
        }
    }

    public SubImageTemplateCard(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mIconDrawableCache = new HashMap();
        this.mHandler = new Handler();
        this.mImageHeight = getResources().getDimensionPixelOffset(R.dimen.enhanced_smartspace_card_height);
    }

    @Override // com.google.android.systemui.smartspace.BcSmartspaceCardSecondary
    public final void resetUi() {
        HashMap hashMap = this.mIconDrawableCache;
        if (hashMap != null) {
            hashMap.clear();
        }
        ImageView imageView = this.mImageView;
        if (imageView != null) {
            imageView.getLayoutParams().width = -2;
            this.mImageView.setImageDrawable(null);
            this.mImageView.setBackgroundTintList(null);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v1, types: [com.google.android.systemui.smartspace.uitemplate.SubImageTemplateCard$$ExternalSyntheticLambda0, android.graphics.drawable.Icon$OnDrawableLoadedListener] */
    @Override // com.google.android.systemui.smartspace.BcSmartspaceCardSecondary
    public final boolean setSmartspaceActions(SmartspaceTarget smartspaceTarget, BcSmartspaceDataPlugin.SmartspaceEventNotifier smartspaceEventNotifier, BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo) {
        int i;
        String str;
        WeakReference weakReference;
        TreeMap treeMap;
        String sb;
        SubImageTemplateData templateData = smartspaceTarget.getTemplateData();
        char c = 0;
        if (BcSmartspaceCardLoggerUtil.containsValidTemplateType(templateData) && templateData.getSubImages() != null && !templateData.getSubImages().isEmpty()) {
            final List subImages = templateData.getSubImages();
            TapAction subImageAction = templateData.getSubImageAction();
            if (this.mImageView == null) {
                Log.w("SubImageTemplateCard", "No image view can be updated. Skipping background update...");
            } else if (subImageAction != null && subImageAction.getExtras() != null) {
                Bundle extras = subImageAction.getExtras();
                String string = extras.getString("imageDimensionRatio", "");
                if (!TextUtils.isEmpty(string)) {
                    this.mImageView.getLayoutParams().width = 0;
                    ((ConstraintLayout.LayoutParams) this.mImageView.getLayoutParams()).dimensionRatio = string;
                }
                if (extras.getBoolean("shouldShowBackground", false)) {
                    this.mImageView.setBackgroundTintList(ColorStateList.valueOf(getContext().getColor(R.color.smartspace_button_background)));
                }
            }
            int i2 = 200;
            if (subImageAction != null && subImageAction.getExtras() != null) {
                i2 = subImageAction.getExtras().getInt("GifFrameDurationMillis", 200);
            }
            final int i3 = i2;
            ContentResolver contentResolver = getContext().getApplicationContext().getContentResolver();
            TreeMap treeMap2 = new TreeMap();
            final WeakReference weakReference2 = new WeakReference(this.mImageView);
            String str2 = this.mPrevSmartspaceTargetId;
            int i4 = 0;
            while (i4 < subImages.size()) {
                android.app.smartspace.uitemplatedata.Icon icon = (android.app.smartspace.uitemplatedata.Icon) subImages.get(i4);
                if (icon != null && icon.getIcon() != null) {
                    Icon icon2 = icon.getIcon();
                    StringBuilder sb2 = new StringBuilder(icon2.getType());
                    switch (icon2.getType()) {
                        case 1:
                        case 5:
                            sb2.append(icon2.getBitmap().hashCode());
                            sb = sb2.toString();
                            break;
                        case 2:
                            sb2.append(icon2.getResPackage());
                            Object[] objArr = new Object[1];
                            objArr[c] = Integer.valueOf(icon2.getResId());
                            sb2.append(String.format("0x%08x", objArr));
                            sb = sb2.toString();
                            break;
                        case 3:
                            sb2.append(Arrays.hashCode(icon2.getDataBytes()));
                            sb = sb2.toString();
                            break;
                        case 4:
                        case 6:
                            sb2.append(icon2.getUriString());
                            sb = sb2.toString();
                            break;
                        default:
                            sb = sb2.toString();
                            break;
                    }
                    final String str3 = sb;
                    final String str4 = str2;
                    final TreeMap treeMap3 = treeMap2;
                    treeMap = treeMap2;
                    final int i5 = i4;
                    i = i4;
                    str = str2;
                    weakReference = weakReference2;
                    ?? r3 = new Icon.OnDrawableLoadedListener() { // from class: com.google.android.systemui.smartspace.uitemplate.SubImageTemplateCard$$ExternalSyntheticLambda0
                        @Override // android.graphics.drawable.Icon.OnDrawableLoadedListener
                        public final void onDrawableLoaded(Drawable drawable) {
                            SubImageTemplateCard subImageTemplateCard = SubImageTemplateCard.this;
                            String str5 = str4;
                            String str6 = str3;
                            Map map = treeMap3;
                            int i6 = i5;
                            List list = subImages;
                            int i7 = i3;
                            WeakReference weakReference3 = weakReference2;
                            int i8 = SubImageTemplateCard.$r8$clinit;
                            if (!str5.equals(subImageTemplateCard.mPrevSmartspaceTargetId)) {
                                Log.d("SubImageTemplateCard", "SmartspaceTarget has changed. Skip the loaded result...");
                                return;
                            }
                            subImageTemplateCard.mIconDrawableCache.put(str6, drawable);
                            map.put(Integer.valueOf(i6), drawable);
                            if (map.size() == list.size()) {
                                AnimationDrawable animationDrawable = new AnimationDrawable();
                                List list2 = (List) map.values().stream().filter(new Predicate() { // from class: com.android.systemui.util.leak.LeakDetector$$ExternalSyntheticLambda0
                                    @Override // java.util.function.Predicate
                                    public final boolean test(Object obj) {
                                        return Objects.nonNull((Drawable) obj);
                                    }
                                }).collect(Collectors.toList());
                                if (list2.isEmpty()) {
                                    Log.w("SubImageTemplateCard", "All images are failed to load. Reset imageView");
                                    ImageView imageView = subImageTemplateCard.mImageView;
                                    if (imageView != null) {
                                        imageView.getLayoutParams().width = -2;
                                        subImageTemplateCard.mImageView.setImageDrawable(null);
                                        subImageTemplateCard.mImageView.setBackgroundTintList(null);
                                        return;
                                    }
                                    return;
                                }
                                list2.forEach(new StageCoordinator$$ExternalSyntheticLambda7(i7, 1, animationDrawable));
                                ImageView imageView2 = (ImageView) weakReference3.get();
                                imageView2.setImageDrawable(animationDrawable);
                                int intrinsicWidth = animationDrawable.getIntrinsicWidth();
                                if (imageView2.getLayoutParams().width != intrinsicWidth) {
                                    Log.d("SubImageTemplateCard", "imageView requestLayout");
                                    imageView2.getLayoutParams().width = intrinsicWidth;
                                    imageView2.requestLayout();
                                }
                                animationDrawable.start();
                            }
                        }
                    };
                    if (this.mIconDrawableCache.containsKey(str3) && this.mIconDrawableCache.get(str3) != null) {
                        r3.onDrawableLoaded((Drawable) this.mIconDrawableCache.get(str3));
                    } else if (icon2.getType() == 4) {
                        new LoadUriTask().execute(new DrawableWrapper(icon2.getUri(), contentResolver, this.mImageHeight, r3));
                    } else {
                        icon2.loadDrawableAsync(getContext(), r3, this.mHandler);
                    }
                } else {
                    i = i4;
                    str = str2;
                    weakReference = weakReference2;
                    treeMap = treeMap2;
                }
                i4 = i + 1;
                treeMap2 = treeMap;
                str2 = str;
                weakReference2 = weakReference;
                c = 0;
            }
            if (subImageAction != null) {
                BcSmartSpaceUtil.setOnClickListener(this, smartspaceTarget, subImageAction, smartspaceEventNotifier, "SubImageTemplateCard", bcSmartspaceCardLoggingInfo);
            }
            return true;
        }
        Log.w("SubImageTemplateCard", "SubImageTemplateData is null or has no SubImage or invalid template type");
        return false;
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mImageView = (ImageView) findViewById(R.id.image_view);
    }
}
