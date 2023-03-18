package com.google.android.systemui.smartspace;
/* loaded from: classes2.dex */
public interface SmartSpaceUpdateListener {
    default void onGsaChanged() {
    }

    default void onSensitiveModeChanged(boolean z, boolean z2) {
    }

    void onSmartSpaceUpdated(SmartSpaceData smartSpaceData);
}
