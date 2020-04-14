package com.alemz.compare3.repositories

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import com.alemz.compare3.data.*

class SimilarityRepository (application: Application) {
    private var dao: SimilarityDao
    private var allMembers: LiveData<List<Similarity>>
    private val con: Context

    init {
        val db: AppDataBase = AppDataBase.invoke(application.applicationContext)
        dao = db.similarityDao()
        con = application.applicationContext
        allMembers = dao.getAll()
    }

    fun getAll(): LiveData<List<Similarity>> {
        return GetSimilarityAllAsyncTask(
            dao
        ).execute().get()
    }


    fun insert(member: Similarity) {
        InsertSimilarityAsyncTask(
            dao,
            con
        ).execute(member)
    }

}
private class InsertSimilarityAsyncTask(val dao: SimilarityDao, val con: Context) :
    AsyncTask<Similarity, Unit, Unit>() {
    override fun doInBackground(vararg params: Similarity) {
        try {
            dao.insertAll(params[0])
            Log.i(TAG, "operation insert successful.")
        } catch (e: RuntimeException) {
            Log.e(TAG, "operation insert failed.")
        }
    }
}

class GetSimilarityAllAsyncTask(val dao: SimilarityDao) :
    AsyncTask<Unit, Unit, LiveData<List<Similarity>>>() {
    override fun doInBackground(vararg params: Unit?): LiveData<List<Similarity>> {
        return dao.getAll()
    }
}
