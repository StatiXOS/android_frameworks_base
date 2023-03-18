package com.google.android.systemui.smartspace;

import android.view.MotionEvent;
import com.android.systemui.settings.brightness.BrightnessSliderController;
import com.android.systemui.settings.brightness.BrightnessSliderView;
import com.google.android.systemui.smartspace.InterceptingViewPager;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes2.dex */
public final /* synthetic */ class InterceptingViewPager$$ExternalSyntheticLambda0 implements BrightnessSliderView.DispatchTouchEventListener, InterceptingViewPager.EventProxy {
    public final /* synthetic */ Object f$0;

    @Override // com.google.android.systemui.smartspace.InterceptingViewPager.EventProxy
    public final boolean delegateEvent(MotionEvent motionEvent) {
        return InterceptingViewPager.m110$r8$lambda$Kinb8UkpjhBhKntCOQxRMNdiw((InterceptingViewPager) this.f$0, motionEvent);
    }

    @Override // com.android.systemui.settings.brightness.BrightnessSliderView.DispatchTouchEventListener
    public final boolean onDispatchTouchEvent(MotionEvent motionEvent) {
        return ((BrightnessSliderController) this.f$0).mirrorTouchEvent(motionEvent);
    }

    public /* synthetic */ InterceptingViewPager$$ExternalSyntheticLambda0(Object obj) {
        this.f$0 = obj;
    }
}
