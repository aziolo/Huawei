package com.alemz.compare3.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Similarity(
    @PrimaryKey val uid: Long,
    @ColumnInfo(name = "value") val value: Float,
    @ColumnInfo(name = "id_person_1") val idPerson1: Long,
    @ColumnInfo(name = "id_person_2") val idPerson2: Long,
    @ColumnInfo(name = "data") val data: String


) {
    constructor() : this(0, 0.0f,0, 0,"")
}