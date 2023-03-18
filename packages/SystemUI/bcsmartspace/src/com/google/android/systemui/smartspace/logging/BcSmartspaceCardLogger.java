package com.google.android.systemui.smartspace.logging;

import android.os.Debug;
import android.util.Log;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.android.systemui.smartspace.SmartspaceProtoLite$SmartSpaceCardMetadata;
import com.android.systemui.smartspace.SmartspaceProtoLite$SmartSpaceSubcards;
import com.android.systemui.smartspace.nano.SmartspaceProto$SmartspaceCardDimensionalInfo;
import com.google.android.systemui.smartspace.BcSmartspaceEvent;
import com.google.android.systemui.smartspace.EventEnum;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.nano.MessageNano;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
/* loaded from: classes2.dex */
public final class BcSmartspaceCardLogger {
    public static final boolean IS_VERBOSE = Log.isLoggable("StatsLog", 2);

    public static void log(EventEnum eventEnum, BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo) {
        byte[] bArr;
        List<BcSmartspaceCardMetadataLoggingInfo> list;
        BcSmartspaceSubcardLoggingInfo bcSmartspaceSubcardLoggingInfo = bcSmartspaceCardLoggingInfo.mSubcardInfo;
        byte[] bArr2 = null;
        if (bcSmartspaceSubcardLoggingInfo != null && (list = bcSmartspaceSubcardLoggingInfo.mSubcards) != null && !list.isEmpty()) {
            ArrayList arrayList = new ArrayList();
            List<BcSmartspaceCardMetadataLoggingInfo> list2 = bcSmartspaceSubcardLoggingInfo.mSubcards;
            for (int i = 0; i < list2.size(); i++) {
                BcSmartspaceCardMetadataLoggingInfo bcSmartspaceCardMetadataLoggingInfo = list2.get(i);
                SmartspaceProtoLite$SmartSpaceCardMetadata.Builder newBuilder = SmartspaceProtoLite$SmartSpaceCardMetadata.newBuilder();
                int i2 = bcSmartspaceCardMetadataLoggingInfo.mInstanceId;
                newBuilder.copyOnWrite();
                SmartspaceProtoLite$SmartSpaceCardMetadata.m60$$Nest$msetInstanceId((SmartspaceProtoLite$SmartSpaceCardMetadata) newBuilder.instance, i2);
                int i3 = bcSmartspaceCardMetadataLoggingInfo.mCardTypeId;
                newBuilder.copyOnWrite();
                SmartspaceProtoLite$SmartSpaceCardMetadata.m59$$Nest$msetCardTypeId((SmartspaceProtoLite$SmartSpaceCardMetadata) newBuilder.instance, i3);
                arrayList.add(newBuilder.build());
            }
            SmartspaceProtoLite$SmartSpaceSubcards.Builder newBuilder2 = SmartspaceProtoLite$SmartSpaceSubcards.newBuilder();
            int i4 = bcSmartspaceSubcardLoggingInfo.mClickedSubcardIndex;
            newBuilder2.copyOnWrite();
            SmartspaceProtoLite$SmartSpaceSubcards.m63$$Nest$msetClickedSubcardIndex((SmartspaceProtoLite$SmartSpaceSubcards) newBuilder2.instance, i4);
            newBuilder2.copyOnWrite();
            SmartspaceProtoLite$SmartSpaceSubcards.m62$$Nest$maddAllSubcards((SmartspaceProtoLite$SmartSpaceSubcards) newBuilder2.instance, arrayList);
            SmartspaceProtoLite$SmartSpaceSubcards build = newBuilder2.build();
            try {
                int serializedSize = build.getSerializedSize();
                byte[] bArr3 = new byte[serializedSize];
                Logger logger = CodedOutputStream.logger;
                CodedOutputStream.ArrayEncoder arrayEncoder = new CodedOutputStream.ArrayEncoder(bArr3, serializedSize);
                build.writeTo(arrayEncoder);
                if (arrayEncoder.spaceLeft() == 0) {
                    bArr = bArr3;
                } else {
                    throw new IllegalStateException("Did not write as much data as expected.");
                }
            } catch (IOException e) {
                throw new RuntimeException(build.getSerializingExceptionMessage("byte array"), e);
            }
        } else {
            bArr = null;
        }
        SmartspaceProto$SmartspaceCardDimensionalInfo smartspaceProto$SmartspaceCardDimensionalInfo = bcSmartspaceCardLoggingInfo.mDimensionalInfo;
        if (smartspaceProto$SmartspaceCardDimensionalInfo != null) {
            bArr2 = MessageNano.toByteArray(smartspaceProto$SmartspaceCardDimensionalInfo);
        }
        SysUiStatsLog.write(((BcSmartspaceEvent) eventEnum).getId(), bcSmartspaceCardLoggingInfo.mInstanceId, bcSmartspaceCardLoggingInfo.mDisplaySurface, bcSmartspaceCardLoggingInfo.mRank, bcSmartspaceCardLoggingInfo.mCardinality, bcSmartspaceCardLoggingInfo.mFeatureType, bcSmartspaceCardLoggingInfo.mUid, 0, 0, bcSmartspaceCardLoggingInfo.mReceivedLatency, bArr, bArr2);
        if (IS_VERBOSE) {
            Log.d("StatsLog", String.format("\nLogged Smartspace event(%s), info(%s), callers=%s", eventEnum, bcSmartspaceCardLoggingInfo.toString(), Debug.getCallers(5)));
        }
    }
}
