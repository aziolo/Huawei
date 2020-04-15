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

    fun getAllNoLive(): List<FamilyMember> {
        return GetAllNoLiveAsyncTask(
            dao
        ).execute().get()
    }

    fun getList(mode: String): List<String> {
        val list = mutableListOf<String>()
        val a =  GetListAsyncTask(
            dao
        ).execute().get()
        for (i in a.indices){
            list.add(i,a[i].firstName+" "+a[i].lastName)
        }
        if (mode == "a") list.plusAssign("none")
        return list
    }

    fun insert(member: FamilyMember) {
        InsertMemberAsyncTask(
            dao,
            con
        ).execute(member)
    }

    fun getBeloved(id: Long): FamilyMember {
        return GetBelovedAsyncTask(
            dao, id).execute().get()
    }

    fun getAllSex(sex:String): LiveData<List<FamilyMember>> {
        return GetSexAsyncTask(
            dao, sex
        ).execute().get()
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

class GetAllNoLiveAsyncTask(private val dao: FamilyMemberDao) :
    AsyncTask<Unit, Unit, List<FamilyMember>>() {
    override fun doInBackground(vararg params: Unit?): List<FamilyMember> {
        return dao.getAllNoLive()
    }
}

class GetListAsyncTask(private val dao: FamilyMemberDao) :
    AsyncTask<Unit, Unit, List<FamilyMember>>() {
    override fun doInBackground(vararg params: Unit?): List<FamilyMember> {
        return dao.getList()
    }
}

class GetBelovedAsyncTask(private val dao: FamilyMemberDao, private val id: Long) :
    AsyncTask<Unit, Unit, FamilyMember>() {
    override fun doInBackground(vararg params: Unit?): FamilyMember {
        return dao.getBeloved(id)
    }

}

class GetSexAsyncTask(private val dao: FamilyMemberDao, private val sex: String) :
    AsyncTask<Unit, Unit, LiveData<List<FamilyMember>>>() {
    override fun doInBackground(vararg params: Unit?): LiveData<List<FamilyMember>> {
        return dao.getAll(sex)
    }

}


