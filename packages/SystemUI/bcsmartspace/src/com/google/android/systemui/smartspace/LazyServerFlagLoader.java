package com.google.android.systemui.smartspace;

import android.provider.DeviceConfig;
import com.android.systemui.screenshot.SaveImageInBackgroundTask$$ExternalSyntheticLambda2;
/* loaded from: classes2.dex */
public final class LazyServerFlagLoader {
    public final String mPropertyKey;
    public Boolean mValue = null;

    public final boolean get() {
        if (this.mValue == null) {
            this.mValue = Boolean.valueOf(DeviceConfig.getBoolean("launcher", this.mPropertyKey, true));
            DeviceConfig.addOnPropertiesChangedListener("launcher", new SaveImageInBackgroundTask$$ExternalSyntheticLambda2(), new DeviceConfig.OnPropertiesChangedListener() { // from class: com.google.android.systemui.smartspace.LazyServerFlagLoader$$ExternalSyntheticLambda0
                public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                    LazyServerFlagLoader lazyServerFlagLoader = LazyServerFlagLoader.this;
                    lazyServerFlagLoader.getClass();
                    if (properties.getKeyset().contains(lazyServerFlagLoader.mPropertyKey)) {
                        lazyServerFlagLoader.mValue = Boolean.valueOf(properties.getBoolean(lazyServerFlagLoader.mPropertyKey, true));
                    }
                }
            });
        }
        return this.mValue.booleanValue();
    }

    public LazyServerFlagLoader(String str) {
        this.mPropertyKey = str;
    }
}
