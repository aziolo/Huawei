package com.alemz.compare3.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SimilarityDao {
    @Query("SELECT * FROM Similarity")
    fun getAll(): LiveData<List<Similarity>>

    @Insert
    fun insertAll(vararg member: Similarity)

    @Delete
    fun delete(member: Similarity)
}