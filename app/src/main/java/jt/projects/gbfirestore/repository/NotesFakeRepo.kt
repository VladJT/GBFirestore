package jt.projects.gbfirestore.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import jt.projects.gbfirestore.model.Note
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class NotesFakeRepo : INotesRepo {
    val data = mutableListOf<Note>(
        Note(
            dateTime = LocalDateTime.of(LocalDate.of(2023, 3, 2), LocalTime.of(23, 54)),
            pressure1 = 132, pressure2 = 71, pulse = 59
        ),
        Note(
            dateTime = LocalDateTime.of(LocalDate.of(2023, 3, 2), LocalTime.of(8, 1)),
            pressure1 = 126, pressure2 = 67, pulse = 49
        ),
        Note(
            dateTime = LocalDateTime.of(LocalDate.of(2023, 3, 3), LocalTime.of(22, 7)),
            pressure1 = 141, pressure2 = 64, pulse = 63
        ),
        Note(
            dateTime = LocalDateTime.of(LocalDate.of(2023, 3, 3), LocalTime.of(6, 39)),
            pressure1 = 127, pressure2 = 73, pulse = 58
        ),
        Note(
            dateTime = LocalDateTime.of(LocalDate.of(2023, 3, 5), LocalTime.of(22, 6)),
            pressure1 = 155, pressure2 = 55, pulse = 71
        )
    )

    val liveData : MutableLiveData<List<Note>> = MutableLiveData(data)

    override fun getAllNotes(): Flow<List<Note>> {
        return liveData.asFlow()
    }

    override fun saveNote(note: Note) {
        data.add(note)
        liveData.postValue(data)
    }

    override fun deleteNoteById(docId: String) {
        // TODO
    }
}