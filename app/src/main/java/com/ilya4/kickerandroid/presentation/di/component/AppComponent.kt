package com.ilya4.kickerandroid.presentation.di.component


import com.ilya4.kickerandroid.presentation.app.KickerApp
import com.ilya4.kickerandroid.presentation.di.module.*

import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    NetworkModule::class,
    AndroidSupportInjectionModule::class,
    AndroidModule::class,
    BuildersModule::class,
    ManagersModule::class,
    ProviderDataModule::class,
    StorageModule::class]
)
interface AppComponent {
    fun inject(app: KickerApp)

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun app(app: KickerApp) : Builder

        @BindsInstance
        fun moduleNetwork(module: NetworkModule) : Builder

        @BindsInstance
        fun moduleManager(module: ManagersModule) : Builder

        @BindsInstance
        fun moduleProvide(module: ProviderDataModule) : Builder

        @BindsInstance
        fun moduleStorage(module: StorageModule) : Builder

        fun build() : AppComponent
    }
}