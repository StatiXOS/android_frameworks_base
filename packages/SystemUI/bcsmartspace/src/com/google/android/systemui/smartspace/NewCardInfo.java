package com.google.android.systemui.smartspace;

import android.content.Intent;
import android.content.pm.PackageInfo;
import com.android.systemui.smartspace.nano.SmartspaceProto$SmartspaceUpdate;
/* loaded from: classes2.dex */
public final class NewCardInfo {
    public final SmartspaceProto$SmartspaceUpdate.SmartspaceCard mCard;
    public final Intent mIntent;
    public final boolean mIsPrimary;
    public final PackageInfo mPackageInfo;
    public final long mPublishTime;

    public NewCardInfo(SmartspaceProto$SmartspaceUpdate.SmartspaceCard smartspaceCard, Intent intent, boolean z, long j, PackageInfo packageInfo) {
        this.mCard = smartspaceCard;
        this.mIsPrimary = z;
        this.mIntent = intent;
        this.mPublishTime = j;
        this.mPackageInfo = packageInfo;
    }
}
