package com.google.android.systemui.smartspace;

import android.content.ComponentName;
import android.content.Context;
import android.view.View;
import com.android.systemui.CoreStartable;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.flags.UnreleasedFlag;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.android.systemui.util.InitializationChecker;
/* compiled from: KeyguardSmartspaceStartable.kt */
/* loaded from: classes2.dex */
public final class KeyguardSmartspaceStartable implements CoreStartable {
    public final Context context;
    public final FeatureFlags featureFlags;
    public final InitializationChecker initializationChecker;
    public final KeyguardMediaViewController mediaController;
    public final KeyguardZenAlarmViewController zenController;

    @Override // com.android.systemui.CoreStartable
    public final void start() {
        if (!this.initializationChecker.initializeComponents()) {
            return;
        }
        FeatureFlags featureFlags = this.featureFlags;
        UnreleasedFlag unreleasedFlag = Flags.TEAMFOOD;
        if (!featureFlags.isEnabled(Flags.SMARTSPACE)) {
            this.context.getPackageManager().setComponentEnabledSetting(new ComponentName(ThemeOverlayApplier.SYSUI_PACKAGE, "com.google.android.systemui.keyguard.KeyguardSliceProviderGoogle"), 1, 1);
            return;
        }
        final KeyguardZenAlarmViewController keyguardZenAlarmViewController = this.zenController;
        keyguardZenAlarmViewController.plugin.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.google.android.systemui.smartspace.KeyguardZenAlarmViewController$init$1
            @Override // android.view.View.OnAttachStateChangeListener
            public final void onViewAttachedToWindow(View view) {
                KeyguardZenAlarmViewController.this.smartspaceViews.add((BcSmartspaceDataPlugin.SmartspaceView) view);
                if (KeyguardZenAlarmViewController.this.smartspaceViews.size() == 1) {
                    KeyguardZenAlarmViewController keyguardZenAlarmViewController2 = KeyguardZenAlarmViewController.this;
                    keyguardZenAlarmViewController2.zenModeController.addCallback(keyguardZenAlarmViewController2.zenModeCallback);
                    KeyguardZenAlarmViewController keyguardZenAlarmViewController3 = KeyguardZenAlarmViewController.this;
                    keyguardZenAlarmViewController3.nextAlarmController.addCallback(keyguardZenAlarmViewController3.nextAlarmCallback);
                }
                KeyguardZenAlarmViewController keyguardZenAlarmViewController4 = KeyguardZenAlarmViewController.this;
                keyguardZenAlarmViewController4.updateDnd();
                keyguardZenAlarmViewController4.updateNextAlarm();
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public final void onViewDetachedFromWindow(View view) {
                KeyguardZenAlarmViewController.this.smartspaceViews.remove((BcSmartspaceDataPlugin.SmartspaceView) view);
                if (KeyguardZenAlarmViewController.this.smartspaceViews.isEmpty()) {
                    KeyguardZenAlarmViewController keyguardZenAlarmViewController2 = KeyguardZenAlarmViewController.this;
                    keyguardZenAlarmViewController2.zenModeController.removeCallback(keyguardZenAlarmViewController2.zenModeCallback);
                    KeyguardZenAlarmViewController keyguardZenAlarmViewController3 = KeyguardZenAlarmViewController.this;
                    keyguardZenAlarmViewController3.nextAlarmController.removeCallback(keyguardZenAlarmViewController3.nextAlarmCallback);
                }
            }
        });
        keyguardZenAlarmViewController.updateNextAlarm();
        final KeyguardMediaViewController keyguardMediaViewController = this.mediaController;
        keyguardMediaViewController.plugin.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.google.android.systemui.smartspace.KeyguardMediaViewController$init$1
            @Override // android.view.View.OnAttachStateChangeListener
            public final void onViewAttachedToWindow(View view) {
                KeyguardMediaViewController keyguardMediaViewController2 = KeyguardMediaViewController.this;
                keyguardMediaViewController2.smartspaceView = (BcSmartspaceDataPlugin.SmartspaceView) view;
                keyguardMediaViewController2.mediaManager.addCallback(keyguardMediaViewController2.mediaListener);
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public final void onViewDetachedFromWindow(View view) {
                KeyguardMediaViewController keyguardMediaViewController2 = KeyguardMediaViewController.this;
                keyguardMediaViewController2.smartspaceView = null;
                NotificationMediaManager notificationMediaManager = keyguardMediaViewController2.mediaManager;
                notificationMediaManager.mMediaListeners.remove(keyguardMediaViewController2.mediaListener);
            }
        });
    }

    public KeyguardSmartspaceStartable(Context context, FeatureFlags featureFlags, KeyguardZenAlarmViewController keyguardZenAlarmViewController, KeyguardMediaViewController keyguardMediaViewController, InitializationChecker initializationChecker) {
        this.context = context;
        this.featureFlags = featureFlags;
        this.zenController = keyguardZenAlarmViewController;
        this.mediaController = keyguardMediaViewController;
        this.initializationChecker = initializationChecker;
    }
}
