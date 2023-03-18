package com.google.android.systemui.smartspace;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Handler;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.management.ControlsProviderSelectorActivity;
import com.android.systemui.controls.ui.ControlsUiController;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.NextAlarmController;
import com.android.systemui.statusbar.policy.ZenModeController;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class KeyguardZenAlarmViewController_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider alarmManagerProvider;
    public final Provider contextProvider;
    public final Provider handlerProvider;
    public final Provider nextAlarmControllerProvider;
    public final Provider pluginProvider;
    public final Provider zenModeControllerProvider;

    public /* synthetic */ KeyguardZenAlarmViewController_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.pluginProvider = provider2;
        this.zenModeControllerProvider = provider3;
        this.alarmManagerProvider = provider4;
        this.nextAlarmControllerProvider = provider5;
        this.handlerProvider = provider6;
    }

    public static KeyguardZenAlarmViewController_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6) {
        return new KeyguardZenAlarmViewController_Factory(provider, provider2, provider3, provider4, provider5, provider6, 1);
    }

    @Override // javax.inject.Provider
    public final Object get() {
        switch (this.$r8$classId) {
            case 0:
                return new KeyguardZenAlarmViewController((Context) this.contextProvider.get(), (BcSmartspaceDataPlugin) this.pluginProvider.get(), (ZenModeController) this.zenModeControllerProvider.get(), (AlarmManager) this.alarmManagerProvider.get(), (NextAlarmController) this.nextAlarmControllerProvider.get(), (Handler) this.handlerProvider.get());
            default:
                return new ControlsProviderSelectorActivity((Executor) this.contextProvider.get(), (Executor) this.pluginProvider.get(), (ControlsListingController) this.zenModeControllerProvider.get(), (ControlsController) this.alarmManagerProvider.get(), (UserTracker) this.nextAlarmControllerProvider.get(), (ControlsUiController) this.handlerProvider.get());
        }
    }
}
