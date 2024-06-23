/*
 * Copyright (C) 2022 Paranoid Android
 * Copyright (C) 2023 StatiXOS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.util;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import com.android.internal.R;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PropImitationHooks {

    private static final String TAG = "PropImitationHooks";
    private static final boolean DEBUG = Build.IS_DEBUGGABLE;

    private static final String[] PIXEL_PROPS = Resources.getSystem().getStringArray(R.array.pixel_props);

    private static final String PACKAGE_SVT = "com.statix.svt";
    private static final String PACKAGE_FINSKY = "com.android.vending";
    private static final String PACKAGE_GMS = "com.google.android.gms";
    private static final String PROCESS_GMS_UNSTABLE = PACKAGE_GMS + ".unstable";

    private static final String PACKAGE_SUBSCRIPTION_RED = "com.google.android.apps.subscriptions.red";
    private static final String PACKAGE_TURBO = "com.google.android.apps.turbo";
    private static final String PACKAGE_VELVET = "com.google.android.googlequicksearchbox";
    private static final String PACKAGE_GBOARD = "com.google.android.inputmethod.latin";
    private static final String PACKAGE_SETUPWIZARD = "com.google.android.setupwizard";
    private static final Map<String, Object> sP8Props = new HashMap<>();
    static {
        sP8Props.put("BRAND", PIXEL_PROPS[0]);
        sP8Props.put("MANUFACTURER", PIXEL_PROPS[1]);
        sP8Props.put("DEVICE", PIXEL_PROPS[2]);
        sP8Props.put("PRODUCT", PIXEL_PROPS[3]);
        sP8Props.put("MODEL", PIXEL_PROPS[4]);
        sP8Props.put("FINGERPRINT", PIXEL_PROPS[5]);
    }

    private static volatile boolean sIsGms = false;
    private static volatile boolean sIsFinsky = false;

    public static void setProps(Context context) {
        final String packageName = context.getPackageName();
        final String processName = Application.getProcessName();

        if (packageName == null || processName == null) {
            return;
        }

        sIsGms = packageName.equals(PACKAGE_GMS) && processName.equals(PROCESS_GMS_UNSTABLE);
        sIsFinsky = packageName.equals(PACKAGE_FINSKY);

        if (sIsGms) {
            dlog("Setting Pixel XL fingerprint for: " + packageName);
            spoofBuildGms(context);
        } else if (!sCertifiedFp.isEmpty() && sIsFinsky) {
            dlog("Setting certified fingerprint for: " + packageName);
            setPropValue("FINGERPRINT", PIXEL_PROPS[5]);
        } else if (packageName.equals(PACKAGE_SUBSCRIPTION_RED) || packageName.equals(PACKAGE_TURBO)
                   || packageName.equals(PACKAGE_VELVET) || packageName.equals(PACKAGE_GBOARD)
                   || packageName.equals(PACKAGE_SETUPWIZARD) || packageName.equals(PACKAGE_GMS)) {
            dlog("Spoofing Pixel 8 Pro for: " + packageName);
            sP8Props.forEach((k, v) -> setPropValue(k, v));
        }
    }

    private static void setPropValue(String key, Object value){
        try {
            dlog("Setting prop " + key + " to " + value.toString());
            Field field = Build.class.getDeclaredField(key);
            field.setAccessible(true);
            field.set(null, value);
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Log.e(TAG, "Failed to set prop " + key, e);
        }
    }

    private static void spoofBuildGms(Context context) {
        PackageManager pm = context.getPackageManager();

        try {
            Resources resources = pm.getResourcesForApplication(PACKAGE_SVT);

            int resourceId = resources.getIdentifier("certifiedBuildProperties", "array", PACKAGE_SVT);
            String[] sCertifiedProps = resources.getStringArray(resourceId);

            if (sCertifiedProps.length == 6) {
                String[] array = {"MODEL", "DEVICE", "PRODUCT", "BRAND", "MANUFACTURER", "FINGERPRINT"};

                for (int i = 0; i < array.length; i++) {
                    if (!sCertifiedProps[i].isEmpty()) {
                        setBuildField(array[i], sCertifiedProps[i]);
                    }
                }
            } else {
                Log.e(TAG, "Insufficient array size for certified props: "
                    + sCertifiedProps.length + ", required 6");
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.i(TAG, "Error accessing resources for '" + PACKAGE_SVT + "': " + e.getMessage());
        }
    }

    private static boolean isCallerSafetyNet() {
        return sIsGms && Arrays.stream(Thread.currentThread().getStackTrace())
                .anyMatch(elem -> elem.getClassName().contains("DroidGuard"));
    }

    public static void onEngineGetCertificateChain() {
        // Check stack for SafetyNet or Play Integrity
        if (isCallerSafetyNet() || sIsFinsky) {
            dlog("Blocked key attestation sIsGms=" + sIsGms + " sIsFinsky=" + sIsFinsky);
            throw new UnsupportedOperationException();
        }
    }

    public static void dlog(String msg) {
      if (DEBUG) Log.d(TAG, msg);
    }
}
