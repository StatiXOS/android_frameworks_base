package com.google.android.systemui.smartspace.logging;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes2.dex */
public final class BcSmartspaceSubcardLoggingInfo {
    public int mClickedSubcardIndex;
    public List<BcSmartspaceCardMetadataLoggingInfo> mSubcards;

    /* loaded from: classes2.dex */
    public static class Builder {
        public int mClickedSubcardIndex;
        public List<BcSmartspaceCardMetadataLoggingInfo> mSubcards;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BcSmartspaceSubcardLoggingInfo)) {
            return false;
        }
        BcSmartspaceSubcardLoggingInfo bcSmartspaceSubcardLoggingInfo = (BcSmartspaceSubcardLoggingInfo) obj;
        return this.mClickedSubcardIndex == bcSmartspaceSubcardLoggingInfo.mClickedSubcardIndex && Objects.equals(this.mSubcards, bcSmartspaceSubcardLoggingInfo.mSubcards);
    }

    public final int hashCode() {
        return Objects.hash(this.mSubcards, Integer.valueOf(this.mClickedSubcardIndex));
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline0.m("BcSmartspaceSubcardLoggingInfo{mSubcards=");
        m.append(this.mSubcards);
        m.append(", mClickedSubcardIndex=");
        m.append(this.mClickedSubcardIndex);
        m.append('}');
        return m.toString();
    }

    public BcSmartspaceSubcardLoggingInfo(Builder builder) {
        List<BcSmartspaceCardMetadataLoggingInfo> list = builder.mSubcards;
        if (list != null) {
            this.mSubcards = list;
        } else {
            this.mSubcards = new ArrayList();
        }
        this.mClickedSubcardIndex = builder.mClickedSubcardIndex;
    }
}
