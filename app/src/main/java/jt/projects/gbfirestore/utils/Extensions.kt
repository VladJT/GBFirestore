package jt.projects.gbfirestore.utils

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


/**
 * ACTIVITY EXTENSIONS
 */
fun Activity.showSnackbar(text: String) {
    Snackbar.make(
        this.findViewById(android.R.id.content),
        text,
        Snackbar.LENGTH_SHORT
    ).show()
}

fun Activity.showToast(text: String) {
    Toast.makeText(
        this,
        text,
        Toast.LENGTH_SHORT
    ).show()
}


/**
 * FRAGMENT EXTENSIONS
 */
fun Fragment.showSnackbar(text: String) {
    Snackbar.make(
        requireActivity().findViewById(android.R.id.content),
        text,
        Snackbar.LENGTH_SHORT
    ).show()
}

fun Fragment.showSnackbarWithAction(
    text: String,
    actionText: String,
    action: View.OnClickListener
) {
    Snackbar.make(
        requireActivity().findViewById(android.R.id.content),
        text,
        Snackbar.LENGTH_SHORT
    ).setAction(actionText, action).show()
}

/**
 * DATETIME EXTENSIONS
 */
fun LocalDateTime.toHourMinString(): String {
    return "${this.hour.toString().padStart(2, '0')}:${this.minute.toString().padStart(2, '0')}"
}

fun LocalDate.toStdFormatString(): String {
    return this.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
}

fun String.toStdLocalDate(): LocalDate {
    return LocalDate.parse(this, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
}

fun String.toStdLocalTime(): LocalTime {
    val time = this.split(":")
    return LocalTime.of(time[0].toInt(), time[1].toInt())
}

/**
 * COROUTINES EXTENSIONS
 */
fun <T> createMutableSingleEventFlow(): MutableSharedFlow<T> =
    MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)

fun ViewModel.launchOrError(
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    action: suspend () -> Unit,
    error: (Exception) -> Unit
) {
    viewModelScope.launch(dispatcher) {
        try {
            action.invoke()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("${this@launchOrError::class.java}", "$e")
            error.invoke(e)
        }
    }
}