package com.alemz.compare3.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FamilyMember(
    @PrimaryKey val uid: Long,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "birth_date") val birth: String,
    @ColumnInfo(name = "father") val father: String?,
    @ColumnInfo(name = "mother") val mother: String?,
    @ColumnInfo(name = "married_to") val marriedTo: String?,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB) val photo: ByteArray?
){
    constructor() : this(0, "", "", "", "", "", "", ByteArray(0))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FamilyMember

        if (photo != null) {
            if (other.photo == null) return false
            if (!photo.contentEquals(other.photo)) return false
        } else if (other.photo != null) return false

        return true
    }

    override fun hashCode(): Int {
        return photo?.contentHashCode() ?: 0
    }
}
