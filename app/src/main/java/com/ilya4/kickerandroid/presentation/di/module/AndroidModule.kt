package com.ilya4.kickerandroid.presentation.di.module


import android.content.ClipboardManager
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.preference.PreferenceManager
import com.ilya4.kickerandroid.domain.executor.JobExecutor
import com.ilya4.kickerandroid.domain.executor.PostExecutionThread
import com.ilya4.kickerandroid.domain.executor.ThreadExecutor
import com.ilya4.kickerandroid.presentation.app.KickerApp
import com.ilya4.kickerandroid.presentation.view.UIThread
import dagger.Module
import dagger.Provides
import io.reactivex.processors.BehaviorProcessor
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

@Module
class AndroidModule {

    @Provides
    @Singleton
    fun provideContext(app: KickerApp) : Context = app.applicationContext

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context) : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    @Singleton
    fun provideClipboardManager(context: Context) : ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    @Provides
    @Singleton
    fun providePackageManager(context: Context) : PackageManager = context.packageManager

    @Provides
    @Singleton
    fun provideBehaviourProcessor() : BehaviorProcessor<Boolean> = BehaviorProcessor.createDefault(false)

    @Provides
    @Singleton
    fun provideThreadExecutor(jobExecutor: JobExecutor) : ThreadExecutor = jobExecutor

    @Provides
    @Singleton
    fun providePostExecutionThread(uiThread: UIThread) : PostExecutionThread = uiThread

    @Provides
    @Singleton
    @Named("DeviceUUID")
    fun provideDeviceUUD(context: Context) : String {
        var uniqueID : String?
        val sharedPreferences = context.getSharedPreferences("UUID_DEVICE", Context.MODE_PRIVATE)
        uniqueID = sharedPreferences.getString("UUID_DEVICE", null)
        if (uniqueID == null) {
            uniqueID = UUID.randomUUID().toString()
            val editor = sharedPreferences.edit()
            editor.putString("UUID_DEVICE", uniqueID)
            editor.apply()
        }
        return uniqueID
    }
}