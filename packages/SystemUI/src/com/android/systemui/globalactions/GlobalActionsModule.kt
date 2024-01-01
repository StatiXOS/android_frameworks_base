package com.android.systemui.globalactions

import com.android.systemui.plugins.GlobalActions
import dagger.Binds
import dagger.Module

/**
 * A dagger [Module] that provides GlobalActions-related implementations to SystemUI (reboot menu, etc.)
 */
@Module
interface GlobalActionsModule {

    /** */
    @Binds
    abstract fun provideGlobalActions(controllerImpl: GlobalActionsImpl): GlobalActions

    /** */
    @Binds
    abstract fun provideGlobalActionsManager(controllerImpl: GlobalActionsComponent): GlobalActions.GlobalActionsManager

}
