/*
 * Copyright (C) 2021 The Android Open Source Project
 * Copyright (C) 2024 The LeafOS Project
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package com.android.internal.gmscompat;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;

/** @hide */
public final class AttestationHooks {
    private static final String TAG = AttestationHooks.class.getSimpleName();
    private static final boolean DEBUG = Log.isLoggable(TAG, Log.DEBUG);

    private static final String PACKAGE_GMS = "com.google.android.gms";
    private static final String PROCESS_UNSTABLE = "com.google.android.gms.unstable";
    private static final String SAMSUNG = "com.samsung.android.";
    private static final String DATA_FILE = "gms_certified_props.json";

    private static final boolean SPOOF_GMS =
            SystemProperties.getBoolean("persist.sys.spoof.gms", true);

    private static volatile boolean sIsGms = false;

    private AttestationHooks() {}

    private static void setBuildField(String key, String value) {
        try {
            // Unlock
            Class clazz = Build.class;
            if (key.startsWith("VERSION:")) {
                clazz = Build.VERSION.class;
                key = key.substring(8);
            }
            Field field = clazz.getDeclaredField(key);
            field.setAccessible(true);

            // Edit
            if (field.getType().equals(Long.TYPE)) {
                field.set(null, Long.parseLong(value));
            } else if (field.getType().equals(Integer.TYPE)) {
                field.set(null, Integer.parseInt(value));
            } else {
                field.set(null, value);
            }

            // Lock
            field.setAccessible(false);
        } catch (Exception e) {
            Log.e(TAG, "Failed to spoof Build." + key, e);
        }
    }

    public static void initApplicationBeforeOnCreate(Context context) {
        final String packageName = context.getPackageName();
        final String processName = Application.getProcessName();

        if (TextUtils.isEmpty(packageName) || TextUtils.isEmpty(processName)) {
            Log.e(TAG, "Null package or process name");
            return;
        }

        if (SPOOF_GMS && PACKAGE_GMS.equals(packageName)) {
            setBuildField("TIME", String.valueOf(System.currentTimeMillis()));
            if (PROCESS_UNSTABLE.equals(processName)) {
                sIsGms = true;
                setGmsCertifiedProps();
            }
        }

        // Samsung apps like SmartThings, Galaxy Wearable crashes
        // on samsung devices running AOSP
        if (packageName.startsWith(SAMSUNG)) {
            setBuildField("BRAND", "google");
            setBuildField("MANUFACTURER", "google");
        }
    }

    private static void setGmsCertifiedProps() {
        File dataFile = new File(Environment.getDataSystemDirectory(), DATA_FILE);
        String savedProps = readFromFile(dataFile);

        if (TextUtils.isEmpty(savedProps)) {
            Log.e(TAG, "No props found to spoof");
            return;
        }

        dlog("Found props");
        try {
            JSONObject parsedProps = new JSONObject(savedProps);
            Iterator<String> keys = parsedProps.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                String value = parsedProps.getString(key);
                dlog(key + ": " + value);

                setBuildField(key, value);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON data", e);
        }
    }

    private static boolean isCallerSafetyNet() {
        return Arrays.stream(Thread.currentThread().getStackTrace())
                .anyMatch(elem -> elem.getClassName().contains("DroidGuard"));
    }

    public static void onEngineGetCertificateChain() {
        // Check stack for SafetyNet
        if (sIsGms && isCallerSafetyNet()) {
            throw new UnsupportedOperationException();
        }
    }

    private static String readFromFile(File file) {
        StringBuilder content = new StringBuilder();

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
            } catch (IOException e) {
                Log.e(TAG, "Error reading from file", e);
            }
        }
        return content.toString();
    }

    private static void dlog(String message) {
        if (DEBUG) Log.d(TAG, message);
    }
}
