package jt.projects.gbfirestore.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jt.projects.gbfirestore.interactors.NotesInteractor
import jt.projects.gbfirestore.model.Note
import jt.projects.gbfirestore.utils.LOG_TAG
import jt.projects.gbfirestore.utils.createMutableSingleEventFlow
import jt.projects.gbfirestore.utils.launchOrError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(private val interactor: NotesInteractor) : ViewModel() {
    companion object {
        const val FAKE_DELAY = 0L
    }

    private var job: Job? = null

    private val _resultRecycler = MutableStateFlow<List<Note>>(listOf())
    val resultRecycler get() = _resultRecycler.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading get() = _isLoading.asStateFlow()

    private val _someError: MutableLiveData<String> = MutableLiveData()
    val someErrorLiveData: LiveData<String>
        get() = _someError

    private val _noteIdFlow = createMutableSingleEventFlow<Note>()
    val noteIdFlow get() = _noteIdFlow.asSharedFlow()

    private val liveData: MutableLiveData<List<Note>> = MutableLiveData()
    val liveDataForViewToObserve: LiveData<List<Note>>
        get() = liveData

    init {

    }

    // загрузка сразу всех данных из удаленного или локального источника
    fun loadData() {
        job?.cancel()
        job = viewModelScope.launch {
            interactor.getAllNotes()
                .onStart { _isLoading.tryEmit(true) }
                .onEach {
                    _isLoading.tryEmit(true)

                    delay(FAKE_DELAY)
                    liveData.postValue(it)
                    _resultRecycler.tryEmit(it)
                    _isLoading.tryEmit(false)
                }.collect()
        }
    }

    fun addNote(note: Note) {
        launchOrError(
            action = {
                interactor.saveNote(note)
            },
            error = {
                _someError.postValue("Ошибка при добавлении данных: ${it.message}")
            })
    }

    fun onEditNoteClicked(note: Note) {
        _noteIdFlow.tryEmit(note)
    }

    fun onDeleteNoteClicked(note: Note) {
        launchOrError(
            action = {
                interactor.deleteNote(note)
            },
            error = {
                _someError.postValue("Ошибка при удалении данных: ${it.message}")
            })
    }

}