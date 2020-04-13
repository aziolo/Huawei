package com.alemz.compare3.repositories

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.alemz.compare3.data.AppDataBase
import com.alemz.compare3.data.FamilyMember
import com.alemz.compare3.data.FamilyMemberDao


const val TAG = "Family Member Repository"

class MemberRepository (application: Application) {
    private var dao: FamilyMemberDao
    private var allMembers: LiveData<List<FamilyMember>>
    private val con: Context

    init {
        val db: AppDataBase = AppDataBase.invoke(application.applicationContext)
        dao = db.FamilyMemberDao()
        con = application.applicationContext
        allMembers = dao.getAll()
    }

    fun getAll(): LiveData<List<FamilyMember>> {
        return GetAllAsyncTask(
            dao
        ).execute().get()
    }


    fun insert(member: FamilyMember) {
        InsertMemberAsyncTask(
            dao,
            con
        ).execute(member)
    }

}

private class InsertMemberAsyncTask(val dao: FamilyMemberDao, val con: Context) :
        AsyncTask<FamilyMember, Unit, Unit>() {
        override fun doInBackground(vararg params: FamilyMember) {
            try {
                dao.insertAll(params[0])
                Log.i(TAG, "operation insert successful.")
            } catch (e: RuntimeException) {
                Log.e(TAG, "operation insert failed.")
            }
        }
    }

private class GetAllAsyncTask(val dao: FamilyMemberDao) :
    AsyncTask<Unit, Unit, LiveData<List<FamilyMember>>>() {
    override fun doInBackground(vararg params: Unit?): LiveData<List<FamilyMember>> {
        return dao.getAll()
    }
}


