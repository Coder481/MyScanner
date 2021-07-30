package com.hrithik.myscanner.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hrithik.myscanner.model.QrHistoryInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [QrHistoryInfo::class], version = 1, exportSchema = false)
public abstract class QrInfoDatabase : RoomDatabase() {

    abstract fun qrInfoDao() : QrInfoDao

    companion object{

        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE : QrInfoDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope) : QrInfoDatabase{
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database

            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QrInfoDatabase::class.java,
                    "qr_info_database"
                ).addCallback(QrInfoCallback(scope))
                    .build()

                INSTANCE = instance

                // return instance
                instance
            }
        }
    }

    private class QrInfoCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            /*INSTANCE?.let { qrInfoDatabase ->
                scope.launch {
                    // if you want to populate database
                    // when RoomDatabase is created
                    // populate here
                    qrInfoDatabase.qrInfoDao().deleteAll()

                }
            }*/
        }
    }
}