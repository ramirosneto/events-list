package br.com.eventslist.application

import android.app.Application
import br.com.eventslist.di.EventModule
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(EventModule.instance)
        }
    }
}
