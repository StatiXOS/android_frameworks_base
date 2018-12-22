/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.statusbar;

import android.content.om.IOverlayManager;
import android.content.om.OverlayInfo;
import android.os.RemoteException;

public class ThemeAccentUtils {

    public static final String TAG = "ThemeAccentUtils";

    // Stock dark theme package
    private static final String STOCK_DARK_THEME = "com.android.systemui.theme.dark";

    // Dark themes
    private static final String[] DARK_THEMES = {
        "com.android.system.theme.dark", // 0
        "com.android.settings.theme.dark", // 1
        "com.android.systemui.qstheme.dark", // 2
    };

    // Black themes
    private static final String[] BLACK_THEMES = {
        "com.android.system.theme.black", // 0
        "com.android.settings.theme.black", // 1
        "com.android.systemui.qstheme.black", // 2
    };

    // Accents
    private static final String[] ACCENTS = {
        "default_accent", // 0
        "com.accents.red", // 1
        "com.accents.pink", // 2
        "com.accents.purple", // 3
        "com.accents.deeppurple", // 4
        "com.accents.indigo", // 5
        "com.accents.blue", // 6
        "com.accents.lightblue", // 7
        "com.accents.cyan", // 8
        "com.accents.teal", // 9
        "com.accents.green", // 10
        "com.accents.lightgreen", // 11
        "com.accents.lime", // 12
        "com.accents.yellow", // 13
        "com.accents.amber", // 14
        "com.accents.orange", // 15
        "com.accents.deeporange", // 16
        "com.accents.userone", // 17
        "com.accents.usertwo", // 18
        "com.accents.userthree", // 19
        "com.accents.userfour", // 20
        "com.accents.userfive", // 21
        "com.accents.usersix", // 22
        "com.accents.userseven", // 23
    };

    // QS overlays
    private static final String[] QS_THEMES = {
        "default", // 0
        "com.statix.overlay.qs.framed", // 1
        "com.statix.overlay.qs.split", // 2
        "com.statix.overlay.qs.superbubble", // 3
        "com.statix.overlay.qs.teardrop", // 4
        "com.statix.overlay.qs.hexagon", // 5
    };

    // Unloads the stock dark theme
    public static void unloadStockDarkTheme(IOverlayManager om, int userId) {
        OverlayInfo themeInfo = null;
        try {
            themeInfo = om.getOverlayInfo(STOCK_DARK_THEME,
                    userId);
            if (themeInfo != null && themeInfo.isEnabled()) {
                om.setEnabled(STOCK_DARK_THEME,
                        false /*disable*/, userId);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // Check for the dark system theme
    public static boolean isUsingDarkTheme(IOverlayManager om, int userId) {
        OverlayInfo themeInfo = null;
        try {
            themeInfo = om.getOverlayInfo(DARK_THEMES[0],
                    userId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return themeInfo != null && themeInfo.isEnabled();
    }

     // Check for the black system theme
     public static boolean isUsingBlackTheme(IOverlayManager om, int userId) {
         OverlayInfo themeInfo = null;
         try {
             themeInfo = om.getOverlayInfo(BLACK_THEMES[0],
                     userId);
         } catch (RemoteException e) {
             e.printStackTrace();
         }
         return themeInfo != null && themeInfo.isEnabled();
     }

    // Set light / dark theme
    public static void setLightDarkTheme(IOverlayManager om, int userId, boolean useDarkTheme) {
        for (String theme : DARK_THEMES) {
            try {
                om.setEnabled(theme,
                        useDarkTheme, userId);
                if (useDarkTheme) {
                    unloadStockDarkTheme(om, userId);
                }
            } catch (RemoteException e) {
            }
        }
    }

     public static void setLightBlackTheme(IOverlayManager om, int userId, boolean useBlackTheme) {
         for (String theme : BLACK_THEMES) {
                 try {
                     om.setEnabled(theme,
                         useBlackTheme, userId);
                 } catch (RemoteException e) {
                 }
         }
    }

    // Check for any accent overlay
    public static boolean isUsingAccent(IOverlayManager om, int userId, int accent) {
        OverlayInfo themeInfo = null;
        try {
            themeInfo = om.getOverlayInfo(ACCENTS[accent],
                    userId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return themeInfo != null && themeInfo.isEnabled();
    }

    // Switches theme accent from one to another or back to stock
    public static void updateAccents(IOverlayManager om, int userId, int accentSetting) {
        if (accentSetting == 0) {
            unloadAccents(om, userId);
        } else  {
            try {
                om.setEnabled(ACCENTS[accentSetting],
                        true, userId);
            } catch (RemoteException e) {
            }
        }
    }

    // Unload all the theme accents
    public static void unloadAccents(IOverlayManager om, int userId) {
        // skip index 0
        for (int i = 1; i < ACCENTS.length; i++) {
            String accent = ACCENTS[i];
            try {
                om.setEnabled(accent,
                        false /*disable*/, userId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    // QS Switcher
    public static void updateQS(IOverlayManager om, int userId, int qsSetting) {
        if (qsSetting == 0) {
            unloadQS(om, userId);
        } else  {
            try {
                om.setEnabled(QS_THEMES[qsSetting],
                        true, userId);
            } catch (RemoteException e) {
            }
        }
    }

    // unload all the QS overlays
    public static void unloadQS(IOverlayManager om, int userId) {
        // skip index 0
        for (int i = 1; i < QS_THEMES.length; i++) {
            String theme = QS_THEMES[i];
            try {
                om.setEnabled(theme,
                        false /*disable*/, userId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    // Check for any QS tile styles overlay
    public static boolean isUsingQsTileStyles(IOverlayManager om, int userId, int qsstyle) {
        OverlayInfo themeInfo = null;
        try {
            themeInfo = om.getOverlayInfo(QS_THEMES[qsstyle],
                    userId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return themeInfo != null && themeInfo.isEnabled();
    }
}
