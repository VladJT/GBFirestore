package jt.projects.gbfirestore.model

import java.time.LocalDateTime

data class Note(
    val dateTime: LocalDateTime,
    val pressure1: Int = 0,
    val pressure2: Int = 0,
    val pulse: Int = 0,
    val isHeader: Boolean = false
)