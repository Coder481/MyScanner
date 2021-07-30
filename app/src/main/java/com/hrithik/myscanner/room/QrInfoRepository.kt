package com.hrithik.myscanner.room

import androidx.annotation.WorkerThread
import com.hrithik.myscanner.model.QrHistoryInfo
import kotlinx.coroutines.flow.Flow


// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class QrInfoRepository(private val qrInfoDao: QrInfoDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allQrInfo : Flow<MutableList<QrHistoryInfo>> = qrInfoDao.getAllQrs()

    /**
     * The suspend modifier tells the compiler that this needs
     * to be called from a coroutine or another suspending function.
     */
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(qrInfo: QrHistoryInfo) {
        qrInfoDao.insert(qrInfo)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll(){
        qrInfoDao.deleteAll()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteQrInfo(qrHistoryInfo: QrHistoryInfo){
        qrInfoDao.deleteQrInfo(qrHistoryInfo)
    }

}