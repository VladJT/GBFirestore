package jt.projects.gbfirestore.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import jt.projects.gbfirestore.model.Note
import jt.projects.gbfirestore.repository.mappers.toFirestoreEntity
import jt.projects.gbfirestore.repository.mappers.toNote
import jt.projects.gbfirestore.utils.FIRESTORE_DB_NAME
import jt.projects.gbfirestore.utils.LOG_TAG
import kotlinx.coroutines.flow.Flow

class NotesFirestoreDbRepo : INotesRepo {
    private val data = mutableListOf<Note>()
    private val liveData: MutableLiveData<List<Note>> = MutableLiveData(data)

    private val db = Firebase.firestore


    override fun getAllNotes(): Flow<List<Note>> {
        db.collection(FIRESTORE_DB_NAME)
            .get()
            .addOnSuccessListener { querySnapshot ->
                data.clear()
                querySnapshot.forEach { document ->
                    val note = document.data.toNote()
                    data.add(note)
                }
                liveData.postValue(data)
            }
            .addOnFailureListener { e ->
                Log.d(LOG_TAG, e.message.toString())
            }
        return liveData.asFlow()
    }

    override fun saveNote(note: Note) {
        db.collection(FIRESTORE_DB_NAME)
            .add(note.toFirestoreEntity())
            .addOnSuccessListener {
//                data.add(note)
//                liveData.postValue(data)
            }
            .addOnFailureListener { e ->
                Log.d(LOG_TAG, e.message.toString())
            }
    }
}