package com.google.android.systemui.smartspace.logging;

import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceTarget;
import android.app.smartspace.uitemplatedata.BaseTemplateData;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.slice.view.R$plurals;
import com.android.systemui.smartspace.nano.SmartspaceProto$SmartspaceCardDimensionalInfo;
import com.android.systemui.smartspace.nano.SmartspaceProto$SmartspaceFeatureDimension;
import com.google.android.systemui.smartspace.logging.BcSmartspaceCardMetadataLoggingInfo;
import com.google.android.systemui.smartspace.logging.BcSmartspaceSubcardLoggingInfo;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public final class BcSmartspaceCardLoggerUtil {
    public static SmartspaceProto$SmartspaceCardDimensionalInfo createDimensionalLoggingInfo(BaseTemplateData baseTemplateData) {
        if (baseTemplateData == null || baseTemplateData.getPrimaryItem() == null || baseTemplateData.getPrimaryItem().getTapAction() == null) {
            return null;
        }
        Bundle extras = baseTemplateData.getPrimaryItem().getTapAction().getExtras();
        ArrayList arrayList = new ArrayList();
        if (extras != null && !extras.isEmpty()) {
            ArrayList<Integer> integerArrayList = extras.getIntegerArrayList("ss_card_dimension_ids");
            ArrayList<Integer> integerArrayList2 = extras.getIntegerArrayList("ss_card_dimension_values");
            if (integerArrayList != null && integerArrayList2 != null && integerArrayList.size() == integerArrayList2.size()) {
                for (int i = 0; i < integerArrayList.size(); i++) {
                    SmartspaceProto$SmartspaceFeatureDimension smartspaceProto$SmartspaceFeatureDimension = new SmartspaceProto$SmartspaceFeatureDimension();
                    smartspaceProto$SmartspaceFeatureDimension.featureDimensionId = integerArrayList.get(i).intValue();
                    smartspaceProto$SmartspaceFeatureDimension.featureDimensionValue = integerArrayList2.get(i).intValue();
                    arrayList.add(smartspaceProto$SmartspaceFeatureDimension);
                }
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        SmartspaceProto$SmartspaceCardDimensionalInfo smartspaceProto$SmartspaceCardDimensionalInfo = new SmartspaceProto$SmartspaceCardDimensionalInfo();
        smartspaceProto$SmartspaceCardDimensionalInfo.featureDimensions = (SmartspaceProto$SmartspaceFeatureDimension[]) arrayList.toArray(new SmartspaceProto$SmartspaceFeatureDimension[arrayList.size()]);
        return smartspaceProto$SmartspaceCardDimensionalInfo;
    }

    public static BcSmartspaceSubcardLoggingInfo createSubcardLoggingInfo(SmartspaceTarget smartspaceTarget) {
        if (smartspaceTarget == null || smartspaceTarget.getBaseAction() == null || smartspaceTarget.getBaseAction().getExtras() == null || smartspaceTarget.getBaseAction().getExtras().isEmpty() || smartspaceTarget.getBaseAction().getExtras().getInt("subcardType", -1) == -1) {
            return null;
        }
        SmartspaceAction baseAction = smartspaceTarget.getBaseAction();
        int create = R$plurals.create(baseAction.getExtras().getString("subcardId"));
        int i = baseAction.getExtras().getInt("subcardType");
        BcSmartspaceCardMetadataLoggingInfo.Builder builder = new BcSmartspaceCardMetadataLoggingInfo.Builder();
        builder.mInstanceId = create;
        builder.mCardTypeId = i;
        BcSmartspaceCardMetadataLoggingInfo bcSmartspaceCardMetadataLoggingInfo = new BcSmartspaceCardMetadataLoggingInfo(builder);
        ArrayList arrayList = new ArrayList();
        arrayList.add(bcSmartspaceCardMetadataLoggingInfo);
        BcSmartspaceSubcardLoggingInfo.Builder builder2 = new BcSmartspaceSubcardLoggingInfo.Builder();
        builder2.mSubcards = arrayList;
        builder2.mClickedSubcardIndex = 0;
        return new BcSmartspaceSubcardLoggingInfo(builder2);
    }

    public static int getUid(PackageManager packageManager, SmartspaceTarget smartspaceTarget) {
        if (packageManager == null || smartspaceTarget == null || smartspaceTarget.getComponentName() == null || TextUtils.isEmpty(smartspaceTarget.getComponentName().getPackageName()) || "package_name".equals(smartspaceTarget.getComponentName().getPackageName())) {
            return -1;
        }
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(smartspaceTarget.getComponentName().getPackageName(), PackageManager.ApplicationInfoFlags.of(0L));
            if (applicationInfo != null) {
                return applicationInfo.uid;
            }
            return -1;
        } catch (PackageManager.NameNotFoundException unused) {
            return -1;
        }
    }

    public static boolean containsValidTemplateType(BaseTemplateData baseTemplateData) {
        if (baseTemplateData != null && baseTemplateData.getTemplateType() != 0 && baseTemplateData.getTemplateType() != 8) {
            return true;
        }
        return false;
    }

    public static void createSubcardLoggingInfoHelper(ArrayList arrayList, BaseTemplateData.SubItemInfo subItemInfo) {
        if (subItemInfo != null && subItemInfo.getLoggingInfo() != null) {
            BaseTemplateData.SubItemLoggingInfo loggingInfo = subItemInfo.getLoggingInfo();
            BcSmartspaceCardMetadataLoggingInfo.Builder builder = new BcSmartspaceCardMetadataLoggingInfo.Builder();
            builder.mCardTypeId = loggingInfo.getFeatureType();
            builder.mInstanceId = loggingInfo.getInstanceId();
            arrayList.add(new BcSmartspaceCardMetadataLoggingInfo(builder));
        }
    }

    public static void tryForcePrimaryFeatureTypeAndInjectWeatherSubcard(BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo, SmartspaceTarget smartspaceTarget) {
        boolean z;
        if (bcSmartspaceCardLoggingInfo.mFeatureType != 1) {
            z = false;
        } else {
            bcSmartspaceCardLoggingInfo.mFeatureType = 39;
            bcSmartspaceCardLoggingInfo.mInstanceId = R$plurals.create("date_card_794317_92634");
            z = true;
        }
        if (z && smartspaceTarget != null && !"date_card_794317_92634".equals(smartspaceTarget.getSmartspaceTargetId())) {
            if (bcSmartspaceCardLoggingInfo.mSubcardInfo == null) {
                BcSmartspaceSubcardLoggingInfo.Builder builder = new BcSmartspaceSubcardLoggingInfo.Builder();
                builder.mClickedSubcardIndex = 0;
                builder.mSubcards = new ArrayList();
                bcSmartspaceCardLoggingInfo.mSubcardInfo = new BcSmartspaceSubcardLoggingInfo(builder);
            }
            BcSmartspaceSubcardLoggingInfo bcSmartspaceSubcardLoggingInfo = bcSmartspaceCardLoggingInfo.mSubcardInfo;
            if (bcSmartspaceSubcardLoggingInfo.mSubcards == null) {
                bcSmartspaceSubcardLoggingInfo.mSubcards = new ArrayList();
            }
            if (bcSmartspaceCardLoggingInfo.mSubcardInfo.mSubcards.size() == 0 || (bcSmartspaceCardLoggingInfo.mSubcardInfo.mSubcards.get(0) != null && bcSmartspaceCardLoggingInfo.mSubcardInfo.mSubcards.get(0).mCardTypeId != 1)) {
                List<BcSmartspaceCardMetadataLoggingInfo> list = bcSmartspaceCardLoggingInfo.mSubcardInfo.mSubcards;
                BcSmartspaceCardMetadataLoggingInfo.Builder builder2 = new BcSmartspaceCardMetadataLoggingInfo.Builder();
                builder2.mInstanceId = R$plurals.create(smartspaceTarget);
                builder2.mCardTypeId = 1;
                list.add(0, new BcSmartspaceCardMetadataLoggingInfo(builder2));
                BcSmartspaceSubcardLoggingInfo bcSmartspaceSubcardLoggingInfo2 = bcSmartspaceCardLoggingInfo.mSubcardInfo;
                int i = bcSmartspaceSubcardLoggingInfo2.mClickedSubcardIndex;
                if (i > 0) {
                    bcSmartspaceSubcardLoggingInfo2.mClickedSubcardIndex = i + 1;
                }
            }
        }
    }

    public static void tryForcePrimaryFeatureTypeOrUpdateLogInfoFromTemplateData(BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo, BaseTemplateData baseTemplateData) {
        boolean z = true;
        if (bcSmartspaceCardLoggingInfo.mFeatureType != 1) {
            z = false;
        } else {
            bcSmartspaceCardLoggingInfo.mFeatureType = 39;
            bcSmartspaceCardLoggingInfo.mInstanceId = R$plurals.create("date_card_794317_92634");
        }
        if (!z && baseTemplateData != null && baseTemplateData.getPrimaryItem() != null && baseTemplateData.getPrimaryItem().getLoggingInfo() != null) {
            int featureType = baseTemplateData.getPrimaryItem().getLoggingInfo().getFeatureType();
            if (featureType > 0) {
                bcSmartspaceCardLoggingInfo.mFeatureType = featureType;
            }
            int instanceId = baseTemplateData.getPrimaryItem().getLoggingInfo().getInstanceId();
            if (instanceId > 0) {
                bcSmartspaceCardLoggingInfo.mInstanceId = instanceId;
            }
        }
    }

    public static BcSmartspaceSubcardLoggingInfo createSubcardLoggingInfo(BaseTemplateData baseTemplateData) {
        if (baseTemplateData == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        createSubcardLoggingInfoHelper(arrayList, baseTemplateData.getSubtitleItem());
        createSubcardLoggingInfoHelper(arrayList, baseTemplateData.getSubtitleSupplementalItem());
        createSubcardLoggingInfoHelper(arrayList, baseTemplateData.getSupplementalLineItem());
        if (arrayList.isEmpty()) {
            return null;
        }
        BcSmartspaceSubcardLoggingInfo.Builder builder = new BcSmartspaceSubcardLoggingInfo.Builder();
        builder.mSubcards = arrayList;
        return new BcSmartspaceSubcardLoggingInfo(builder);
    }
}
