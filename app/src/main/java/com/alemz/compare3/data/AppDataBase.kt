package com.alemz.compare3.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(
    entities = [FamilyMember::class, Similarity::class],
    version = 1
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun similarityDao(): SimilarityDao
    abstract fun familyMemberDao(): FamilyMemberDao
    companion object {

        //volatile guarantee that the value that is being read comes from the main memory not the cpu-cache, read the newest value from memory
        //operator ? = safe cals
        //return AppDataBase if not-null otherwise return null
        @Volatile
        private var instance: AppDataBase? = null

        //Any() returns true if has at least one element
        private val LOCK = Any()


        //synchronized with lock guarantee blocking the room. When we have one thread no one else(other thread) can use the room. reduce the mistakes
        //any thread that reaches the point lock the instance and does the work defined in the code-block:
        //Elvis operator ?:
        //if instance is not-null return instance otherwise return synchronized(LOCK)
        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK) {

                //if database does not exist build it
                instance
                    ?: buildDataBase(
                        context
                    ).also { instance = it }
            }

        //prawdziwa baza
        //private fun buildDataBase(context: Context) = Room.databaseBuilder(context, AppDataBase::class.java, "diabetic_daily.db").build()
        //baza do fazy testów, będzie działać tylko temporary
        //private fun buildDataBase(context: Context) =
          //  Room.databaseBuilder(context, AppDataBase::class.java, "local.db").build()
        private fun buildDataBase(context: Context) = Room.inMemoryDatabaseBuilder(context, AppDataBase::class.java).build()
    }

    private val roomCallback = object : RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
        }
    }

}