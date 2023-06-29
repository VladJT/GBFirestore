package jt.projects.gbfirestore.interactors


import jt.projects.gbfirestore.model.Note
import jt.projects.gbfirestore.repository.INotesRepo

class NotesInteractor(private val repo: INotesRepo) {

    fun getAllNotes() = repo.getAllNotes()

    fun saveNote(note: Note) = repo.saveNote(note)
}