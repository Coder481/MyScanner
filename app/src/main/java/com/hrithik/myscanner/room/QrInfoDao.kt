package com.hrithik.myscanner.room

import androidx.room.*
import com.hrithik.myscanner.model.QrHistoryInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface QrInfoDao {

    /**
     * To observe data changes you will use Flow<> from kotlinx-coroutines.
     * Use a return value of type Flow in your method description,
     * and Room generates all necessary code to update the Flow when the database is updated
     *
     * When Room queries return Flow, the queries are automatically run asynchronously on a background thread.
     */

    @Query("SELECT * FROM QrHistoryInfo ORDER BY dateFormat DESC")
    fun getAllQrs(): Flow<MutableList<QrHistoryInfo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(qrHistoryInfo: QrHistoryInfo)

    @Query("DELETE FROM QrHistoryInfo")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteQrInfo(qrHistoryInfo: QrHistoryInfo)
}