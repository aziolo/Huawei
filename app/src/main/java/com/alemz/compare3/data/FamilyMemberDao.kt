package com.alemz.compare3.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FamilyMemberDao {
    @Query("SELECT * FROM FamilyMember")
    fun getAll(): LiveData<List<FamilyMember>>

    @Query("SELECT * FROM FamilyMember")
    fun getAllNoLive(): List<FamilyMember>

    @Query("SELECT * FROM FamilyMember WHERE sex LIKE:s")
    fun getAll(s: String): LiveData<List<FamilyMember>>

    @Query("SELECT * FROM FamilyMember")
    fun getList(): List<FamilyMember>

    @Query("SELECT * FROM FamilyMember WHERE  uid LIKE :id")
    fun getBeloved(id: Long): FamilyMember

    @Insert
    fun insertAll(vararg member: FamilyMember)

    @Delete
    fun delete(member: FamilyMember)
}