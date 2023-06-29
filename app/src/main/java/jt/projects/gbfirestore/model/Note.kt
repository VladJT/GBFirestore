package jt.projects.gbfirestore.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class Note(
    val dateTime: LocalDateTime,
    val pressure1: Int = 0,
    val pressure2: Int = 0,
    val pulse: Int = 0,
    val isHeader: Boolean = false
) : Parcelable