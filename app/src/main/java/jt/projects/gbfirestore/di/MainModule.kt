package jt.projects.gbfirestore.di

import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import jt.projects.gbfirestore.interactors.NotesInteractor
import jt.projects.gbfirestore.repository.INotesRepo
import jt.projects.gbfirestore.repository.NotesFakeRepo
import jt.projects.gbfirestore.ui.MainActivity
import javax.inject.Singleton


@Module
class MainModule {

    @Provides
    @Singleton
    fun notesRepo(): INotesRepo = NotesFakeRepo()


    @Provides
    @Singleton
    fun notesInteractor(repo: INotesRepo): NotesInteractor = NotesInteractor(repo)

}

/**
 * Модуль для Activity. Так как мы используем дополнительную библиотеку поддержки для Android, то все
становится гораздо проще при помощи ContributesAndroidInjector. Он позволяет внедрять
зависимости в Activity (нашу ViewModel) благодаря простому AndroidInjection.inject(this) в методе onCreate
 */
@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

}