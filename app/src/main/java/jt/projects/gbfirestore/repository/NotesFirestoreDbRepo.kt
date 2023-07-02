package jt.projects.gbfirestore.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore

import com.google.firebase.ktx.Firebase

import jt.projects.gbfirestore.model.Note
import jt.projects.gbfirestore.repository.mappers.toFirestoreEntity
import jt.projects.gbfirestore.repository.mappers.toNote
import jt.projects.gbfirestore.utils.FIRESTORE_DB_NAME
import jt.projects.gbfirestore.utils.LOG_TAG
import jt.projects.gbfirestore.utils.NO_ID
import kotlinx.coroutines.flow.Flow

class NotesFirestoreDbRepo : INotesRepo {
    private val data = mutableListOf<Note>()
    private val liveData: MutableLiveData<List<Note>> = MutableLiveData(data)

    private val db = Firebase.firestore.collection(FIRESTORE_DB_NAME)

    init {
        //получаем данные
        db.get()
            .addOnFailureListener { e ->
                Log.d(LOG_TAG, e.message.toString())
            }
        // подписываемся на изменения
        observeDbChanges()
    }

    private fun observeDbChanges() {
        db.addSnapshotListener { value, error ->
            value?.let {
                it.documentChanges.forEach { documentChange ->
                    val currDocData = documentChange.document.data
                    val currDocId = documentChange.document.id

                    when (documentChange.type) {

                        DocumentChange.Type.ADDED -> {
                            data.add(currDocData.toNote(currDocId))
                        }

                        DocumentChange.Type.REMOVED -> {
                            val dataToRemove = data.findLast { note ->
                                note.id == currDocId
                            }
                            data.remove(dataToRemove)
                        }

                        DocumentChange.Type.MODIFIED -> {
                            val dataToRemove = data.findLast { note ->
                                note.id == currDocId
                            }
                            data.remove(dataToRemove)
                            data.add(currDocData.toNote(currDocId))
                        }

                        else -> {
                            TODO("IMPOSSIBLE?")
                        }
                    }
                } // forEachIndexed
            }
            liveData.postValue(data)
        }
    }

    override fun getAllNotes(): Flow<List<Note>> = liveData.asFlow()

    override fun saveNote(note: Note) {
        if (note.id == NO_ID) {
            // Добавление нового документа
            db.add(note.toFirestoreEntity())
        } else {
            // Редактирование документа
            db.document(note.id)
                .set(note.toFirestoreEntity())
        }
            .addOnFailureListener { e ->
                Log.d(LOG_TAG, e.message.toString())
            }
    }

    override fun deleteNote(note: Note) {
        db.document(note.id).delete()
    }
}