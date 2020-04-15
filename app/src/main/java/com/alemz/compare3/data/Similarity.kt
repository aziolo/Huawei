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
    @ColumnInfo(name = "data") val data: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB) val photo1: ByteArray?,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB) val photo2: ByteArray?


) {
    constructor() : this(0, 0.0f,0, 0,"", null,null)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Similarity

        if (photo1 != null) {
            if (other.photo1 == null) return false
            if (!photo1.contentEquals(other.photo1)) return false
        } else if (other.photo1 != null) return false
        if (photo2 != null) {
            if (other.photo2 == null) return false
            if (!photo2.contentEquals(other.photo2)) return false
        } else if (other.photo2 != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = photo1?.contentHashCode() ?: 0
        result = 31 * result + (photo2?.contentHashCode() ?: 0)
        return result
    }
}