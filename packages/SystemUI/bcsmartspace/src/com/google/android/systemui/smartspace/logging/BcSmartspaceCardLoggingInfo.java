package com.google.android.systemui.smartspace.logging;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import com.android.systemui.smartspace.nano.SmartspaceProto$SmartspaceCardDimensionalInfo;
import java.util.Objects;
/* loaded from: classes2.dex */
public final class BcSmartspaceCardLoggingInfo {
    public final int mCardinality;
    public SmartspaceProto$SmartspaceCardDimensionalInfo mDimensionalInfo;
    public final int mDisplaySurface;
    public int mFeatureType;
    public int mInstanceId;
    public final int mRank;
    public final int mReceivedLatency;
    public BcSmartspaceSubcardLoggingInfo mSubcardInfo;
    public final int mUid;

    /* loaded from: classes2.dex */
    public static class Builder {
        public int mCardinality;
        public SmartspaceProto$SmartspaceCardDimensionalInfo mDimensionalInfo;
        public int mDisplaySurface = 1;
        public int mFeatureType;
        public int mInstanceId;
        public int mRank;
        public int mReceivedLatency;
        public BcSmartspaceSubcardLoggingInfo mSubcardInfo;
        public int mUid;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BcSmartspaceCardLoggingInfo)) {
            return false;
        }
        BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo = (BcSmartspaceCardLoggingInfo) obj;
        return this.mInstanceId == bcSmartspaceCardLoggingInfo.mInstanceId && this.mDisplaySurface == bcSmartspaceCardLoggingInfo.mDisplaySurface && this.mRank == bcSmartspaceCardLoggingInfo.mRank && this.mCardinality == bcSmartspaceCardLoggingInfo.mCardinality && this.mFeatureType == bcSmartspaceCardLoggingInfo.mFeatureType && this.mReceivedLatency == bcSmartspaceCardLoggingInfo.mReceivedLatency && this.mUid == bcSmartspaceCardLoggingInfo.mUid && Objects.equals(this.mSubcardInfo, bcSmartspaceCardLoggingInfo.mSubcardInfo) && Objects.equals(this.mDimensionalInfo, bcSmartspaceCardLoggingInfo.mDimensionalInfo);
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(this.mInstanceId), Integer.valueOf(this.mDisplaySurface), Integer.valueOf(this.mRank), Integer.valueOf(this.mCardinality), Integer.valueOf(this.mFeatureType), Integer.valueOf(this.mReceivedLatency), Integer.valueOf(this.mUid), this.mSubcardInfo);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline0.m("instance_id = ");
        m.append(this.mInstanceId);
        m.append(", feature type = ");
        m.append(this.mFeatureType);
        m.append(", display surface = ");
        m.append(this.mDisplaySurface);
        m.append(", rank = ");
        m.append(this.mRank);
        m.append(", cardinality = ");
        m.append(this.mCardinality);
        m.append(", receivedLatencyMillis = ");
        m.append(this.mReceivedLatency);
        m.append(", uid = ");
        m.append(this.mUid);
        m.append(", subcardInfo = ");
        m.append(this.mSubcardInfo);
        m.append(", dimensionalInfo = ");
        m.append(this.mDimensionalInfo);
        return m.toString();
    }

    public BcSmartspaceCardLoggingInfo(Builder builder) {
        this.mInstanceId = builder.mInstanceId;
        this.mDisplaySurface = builder.mDisplaySurface;
        this.mRank = builder.mRank;
        this.mCardinality = builder.mCardinality;
        this.mFeatureType = builder.mFeatureType;
        this.mReceivedLatency = builder.mReceivedLatency;
        this.mUid = builder.mUid;
        this.mSubcardInfo = builder.mSubcardInfo;
        this.mDimensionalInfo = builder.mDimensionalInfo;
    }
}
