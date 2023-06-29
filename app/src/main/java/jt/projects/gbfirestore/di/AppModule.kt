package jt.projects.gbfirestore.di

import dagger.Module
import dagger.Provides
import jt.projects.gbfirestore.App
import javax.inject.Singleton


@Module
class AppModule(app: App) {
    private val application: App

    init {
        this.application = app
    }

    @Provides
    @Singleton
    fun application(): App {
        return application
    }
}