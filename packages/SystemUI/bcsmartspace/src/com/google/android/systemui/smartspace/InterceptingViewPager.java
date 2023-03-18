package com.google.android.systemui.smartspace;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import androidx.viewpager.widget.ViewPager;
import com.android.keyguard.KeyguardPatternView$$ExternalSyntheticLambda0;
import com.android.systemui.wmshell.WMShell$8$$ExternalSyntheticLambda0;
import com.android.wm.shell.pip.phone.PipTouchHandler$$ExternalSyntheticLambda1;
/* loaded from: classes2.dex */
public class InterceptingViewPager extends ViewPager {
    public boolean mHasPerformedLongPress;
    public boolean mHasPostedLongPress;
    public final Runnable mLongPressCallback;
    public final EventProxy mSuperOnIntercept;
    public final EventProxy mSuperOnTouch;

    /* loaded from: classes2.dex */
    public interface EventProxy {
        boolean delegateEvent(MotionEvent motionEvent);
    }

    public InterceptingViewPager(Context context) {
        super(context);
        this.mSuperOnTouch = new EventProxy() { // from class: com.google.android.systemui.smartspace.InterceptingViewPager$$ExternalSyntheticLambda1
            @Override // com.google.android.systemui.smartspace.InterceptingViewPager.EventProxy
            public final boolean delegateEvent(MotionEvent motionEvent) {
                boolean onTouchEvent;
                onTouchEvent = super/*androidx.viewpager.widget.ViewPager*/.onTouchEvent(motionEvent);
                return onTouchEvent;
            }
        };
        this.mSuperOnIntercept = new EventProxy() { // from class: com.google.android.systemui.smartspace.InterceptingViewPager$$ExternalSyntheticLambda2
            @Override // com.google.android.systemui.smartspace.InterceptingViewPager.EventProxy
            public final boolean delegateEvent(MotionEvent motionEvent) {
                boolean onInterceptTouchEvent;
                onInterceptTouchEvent = super/*androidx.viewpager.widget.ViewPager*/.onInterceptTouchEvent(motionEvent);
                return onInterceptTouchEvent;
            }
        };
        this.mLongPressCallback = new WMShell$8$$ExternalSyntheticLambda0(5, this);
    }

    public final void cancelScheduledLongPress() {
        if (this.mHasPostedLongPress) {
            this.mHasPostedLongPress = false;
            removeCallbacks(this.mLongPressCallback);
        }
    }

    @Override // androidx.viewpager.widget.ViewPager, android.view.ViewGroup
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return handleTouchOverride(motionEvent, this.mSuperOnIntercept);
    }

    @Override // androidx.viewpager.widget.ViewPager, android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        return handleTouchOverride(motionEvent, this.mSuperOnTouch);
    }

    public final boolean handleTouchOverride(MotionEvent motionEvent, EventProxy eventProxy) {
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action == 1 || action == 3) {
                cancelScheduledLongPress();
            }
        } else {
            this.mHasPerformedLongPress = false;
            if (isLongClickable()) {
                cancelScheduledLongPress();
                this.mHasPostedLongPress = true;
                postDelayed(this.mLongPressCallback, ViewConfiguration.getLongPressTimeout());
            }
        }
        if (this.mHasPerformedLongPress) {
            cancelScheduledLongPress();
            return true;
        } else if (!eventProxy.delegateEvent(motionEvent)) {
            return false;
        } else {
            cancelScheduledLongPress();
            return true;
        }
    }

    public InterceptingViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mSuperOnTouch = new InterceptingViewPager$$ExternalSyntheticLambda0(this);
        this.mSuperOnIntercept = new PipTouchHandler$$ExternalSyntheticLambda1(this);
        this.mLongPressCallback = new KeyguardPatternView$$ExternalSyntheticLambda0(6, this);
        setImportantForAccessibility(2);
    }
}
