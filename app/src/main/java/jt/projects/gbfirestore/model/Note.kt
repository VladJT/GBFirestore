package jt.projects.gbfirestore.model

import android.os.Parcelable
import jt.projects.gbfirestore.utils.NO_ID
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class Note(
    val id: String = NO_ID,
    val dateTime: LocalDateTime,
    val pressure1: Int = 0,
    val pressure2: Int = 0,
    val pulse: Int = 0,
    val isHeader: Boolean = false
) : Parcelable