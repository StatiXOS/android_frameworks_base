package com.google.android.systemui.smartspace.logging;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import java.util.Objects;
/* loaded from: classes2.dex */
public final class BcSmartspaceCardMetadataLoggingInfo {
    public final int mCardTypeId;
    public final int mInstanceId;

    /* loaded from: classes2.dex */
    public static class Builder {
        public int mCardTypeId;
        public int mInstanceId;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BcSmartspaceCardMetadataLoggingInfo)) {
            return false;
        }
        BcSmartspaceCardMetadataLoggingInfo bcSmartspaceCardMetadataLoggingInfo = (BcSmartspaceCardMetadataLoggingInfo) obj;
        return this.mInstanceId == bcSmartspaceCardMetadataLoggingInfo.mInstanceId && this.mCardTypeId == bcSmartspaceCardMetadataLoggingInfo.mCardTypeId;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(this.mInstanceId), Integer.valueOf(this.mCardTypeId));
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline0.m("BcSmartspaceCardMetadataLoggingInfo{mInstanceId=");
        m.append(this.mInstanceId);
        m.append(", mCardTypeId=");
        m.append(this.mCardTypeId);
        m.append('}');
        return m.toString();
    }

    public BcSmartspaceCardMetadataLoggingInfo(Builder builder) {
        this.mInstanceId = builder.mInstanceId;
        this.mCardTypeId = builder.mCardTypeId;
    }
}
