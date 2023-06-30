package jt.projects.gbfirestore.interactors


import jt.projects.gbfirestore.model.Note
import jt.projects.gbfirestore.repository.INotesRepo
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class NotesInteractor(private val repo: INotesRepo) {

    fun getAllNotes() = repo
        .getAllNotes()
        .map {
            val notes: MutableList<Note> = mutableListOf()
            notes.addAll(it)
            val uniqueDates: MutableSet<LocalDate> = mutableSetOf()

            notes.forEach { note ->
                uniqueDates.add(note.dateTime.toLocalDate())
            }

            uniqueDates.forEach { date ->
                notes.add(
                    Note(
                        dateTime = LocalDateTime.of(date, LocalTime.of(12, 0, 0)),
                        isHeader = true
                    )
                )
            }
            notes
        }.map { notes ->
//            notes.sortedBy {
//                it.dateTime
//            }
            notes.sortWith(
                compareBy(
                    { it.dateTime.toLocalDate() },
                    { !it.isHeader },
                    { it.dateTime })
            )
            notes
        }

    fun saveNote(note: Note) = repo.saveNote(note)

//    internal class CustomComparator : Comparator<Note> {
//        override fun compare(x: Note, y: Note): Int {
//            return when {
//                (x.dateTime == y.dateTime && x.isHeader==false && y.isHeader==false) -> 0
//                (x.dateTime == y.dateTime && x.isHeader==false && y.isHeader==false) -> -1
//                else -> 1
//            }
//        }
//    }
}