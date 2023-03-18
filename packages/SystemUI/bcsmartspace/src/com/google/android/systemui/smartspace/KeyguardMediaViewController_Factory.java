package com.google.android.systemui.smartspace;

import android.content.Context;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class KeyguardMediaViewController_Factory implements Factory<KeyguardMediaViewController> {
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Context> contextProvider;
    public final Provider<NotificationMediaManager> mediaManagerProvider;
    public final Provider<BcSmartspaceDataPlugin> pluginProvider;
    public final Provider<DelayableExecutor> uiExecutorProvider;
    public final Provider<UserTracker> userTrackerProvider;

    @Override // javax.inject.Provider
    public final Object get() {
        this.broadcastDispatcherProvider.get();
        return new KeyguardMediaViewController(this.contextProvider.get(), this.userTrackerProvider.get(), this.pluginProvider.get(), this.uiExecutorProvider.get(), this.mediaManagerProvider.get());
    }

    public KeyguardMediaViewController_Factory(Provider<Context> provider, Provider<UserTracker> provider2, Provider<BcSmartspaceDataPlugin> provider3, Provider<DelayableExecutor> provider4, Provider<NotificationMediaManager> provider5, Provider<BroadcastDispatcher> provider6) {
        this.contextProvider = provider;
        this.userTrackerProvider = provider2;
        this.pluginProvider = provider3;
        this.uiExecutorProvider = provider4;
        this.mediaManagerProvider = provider5;
        this.broadcastDispatcherProvider = provider6;
    }
}
