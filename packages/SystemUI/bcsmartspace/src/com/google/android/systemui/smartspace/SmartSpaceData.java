package com.google.android.systemui.smartspace;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.util.Log;
/* loaded from: classes2.dex */
public final class SmartSpaceData {
    public SmartSpaceCard mCurrentCard;
    public SmartSpaceCard mWeatherCard;

    public final boolean handleExpire() {
        boolean z;
        SmartSpaceCard smartSpaceCard = this.mWeatherCard;
        boolean z2 = false;
        if (smartSpaceCard != null) {
            z = true;
        } else {
            z = false;
        }
        if (z && smartSpaceCard.isExpired()) {
            if (SmartSpaceController.DEBUG) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline0.m("weather expired ");
                m.append(this.mWeatherCard.getExpiration());
                Log.d("SmartspaceData", m.toString());
            }
            this.mWeatherCard = null;
            z2 = true;
        }
        if (hasCurrent() && this.mCurrentCard.isExpired()) {
            if (SmartSpaceController.DEBUG) {
                StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline0.m("current expired ");
                m2.append(this.mCurrentCard.getExpiration());
                Log.d("SmartspaceData", m2.toString());
            }
            this.mCurrentCard = null;
            return true;
        }
        return z2;
    }

    public final boolean hasCurrent() {
        if (this.mCurrentCard != null) {
            return true;
        }
        return false;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline0.m("{");
        m.append(this.mCurrentCard);
        m.append(",");
        m.append(this.mWeatherCard);
        m.append("}");
        return m.toString();
    }
}
