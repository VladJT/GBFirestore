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

    private val db = Firebase.firestore

    init {
        // подписываемся на изменения
        db.collection(FIRESTORE_DB_NAME)
            .addSnapshotListener { value, error ->
                value?.let {
                    it.documentChanges.forEachIndexed { index, documentChange ->
                        val docId = it.documents[index].id
                        when (documentChange.type) {
                            DocumentChange.Type.ADDED -> {
                                it.documents[index].data?.let { firestoreEntity ->
                                    data.add(firestoreEntity.toNote(docId))
                                }
                            }

                            else -> {

                            }
                        }
                    }

                }
                liveData.postValue(data)
            }
    }

    override fun getAllNotes(): Flow<List<Note>> {
        db.collection(FIRESTORE_DB_NAME)
            .get()
            .addOnFailureListener { e ->
                Log.d(LOG_TAG, e.message.toString())
            }
        return liveData.asFlow()
    }

    override fun saveNote(note: Note) {
        if (note.id == NO_ID) {
            // Добавление нового документа
            db.collection(FIRESTORE_DB_NAME)
                .add(note.toFirestoreEntity())
        }
        else {
            // Редактирование документа
            db.collection(FIRESTORE_DB_NAME)
                .document(note.id)
                .set(note.toFirestoreEntity())
        }
            .addOnFailureListener { e ->
                Log.d(LOG_TAG, e.message.toString())
            }
    }
}