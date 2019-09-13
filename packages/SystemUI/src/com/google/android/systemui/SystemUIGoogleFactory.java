package com.google.android.systemui;

import com.android.systemui.SystemUIFactory;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;

public class SystemUIGoogleFactory extends SystemUIFactory {

    @Override
    public NotificationLockscreenUserManager provideNotificationLockscreenUserManager(Context context) {
        return new NotificationLockscreenUserManagerGoogle(context);
    }

}
