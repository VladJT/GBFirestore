package jt.projects.gbfirestore.repository

import jt.projects.gbfirestore.model.Note
import kotlinx.coroutines.flow.Flow


interface INotesRepo {
    fun getAllNotes(): Flow<List<Note>>
    fun saveNote(note: Note)
    fun deleteNoteById(docId: String)
}