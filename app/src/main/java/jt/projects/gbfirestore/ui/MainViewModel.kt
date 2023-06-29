package jt.projects.gbfirestore.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jt.projects.gbfirestore.interactors.NotesInteractor
import jt.projects.gbfirestore.model.Note
import jt.projects.gbfirestore.utils.LOG_TAG
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

class MainViewModel @Inject constructor(private val interactor: NotesInteractor) : ViewModel() {

    private var job: Job? = null

    private val _resultRecycler = MutableStateFlow<List<Note>>(listOf())
    val resultRecycler get() = _resultRecycler.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading get() = _isLoading.asStateFlow()

    init {
        loadData()
    }

    // загрузка сразу всех данных из удаленного или локального источника
    private fun loadData() {
        job?.cancel()
        _isLoading.tryEmit(true)

        job = viewModelScope.launch {
            interactor.getAllNotes()
                .onEach {
                    _resultRecycler.tryEmit(it)
                    _isLoading.tryEmit(false)
                }.collect()
        }
    }

    fun addNote(date: LocalDate, time: LocalTime, pressure1: Int, pressure2: Int, pulse: Int) {
        launchOrError(
            action = {
                interactor.saveNote(
                    Note(
                        dateTime = LocalDateTime.of(date, time),
                        pressure1 = pressure1,
                        pressure2 = pressure2,
                        pulse = pulse
                    )
                )
                loadData()
            },
            error = {
                Log.d(LOG_TAG, it.toString())
            })
    }

    private fun launchOrError(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        action: suspend () -> Unit,
        error: (Exception) -> Unit
    ) {
        job?.cancel()
        job = viewModelScope.launch(dispatcher) {
            try {
                action.invoke()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(LOG_TAG, "$e")
                error.invoke(e)
            }
        }
    }

}