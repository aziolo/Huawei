package com.alemz.compare3.repositories

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import com.alemz.compare3.data.*


const val TAG = "Family Member Repository"

class MemberRepository (application: Application) {
    private var dao: FamilyMemberDao
    private var allMembers: LiveData<List<FamilyMember>>
    private val con: Context

    init {
        val db: AppDataBase = AppDataBase.invoke(application.applicationContext)
        dao = db.familyMemberDao()
        con = application.applicationContext
        allMembers = dao.getAll()
    }

    fun getAll(): LiveData<List<FamilyMember>> {
        return GetAllAsyncTask(
            dao
        ).execute().get()
    }

    fun getList(): List<String> {
        val list = mutableListOf<String>()
        val a =  GetListAsyncTask(
            dao
        ).execute().get()
        for (i in a.indices){
            list[i] = a[i].firstName+" "+a[i].lastName
        }
        list.plusAssign("none")
        return list
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

class GetAllAsyncTask(private val dao: FamilyMemberDao) :
    AsyncTask<Unit, Unit, LiveData<List<FamilyMember>>>() {
    override fun doInBackground(vararg params: Unit?): LiveData<List<FamilyMember>> {
        return dao.getAll()
    }
}

class GetListAsyncTask(private val dao: FamilyMemberDao) :
    AsyncTask<Unit, Unit, List<FamilyMember>>() {
    override fun doInBackground(vararg params: Unit?): List<FamilyMember> {
        return dao.getList()
    }
}


