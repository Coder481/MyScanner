package com.hrithik.myscanner.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "QrHistoryInfo")
data class QrHistoryInfo(@PrimaryKey @ColumnInfo(name = "dateFormat")val dateFormat: String
                        ,@ColumnInfo(name = "qrData")val qrData:String
                        ,@ColumnInfo(name = "isScanned")val isScanned:Boolean
                        ,@ColumnInfo(name = "codeType")val codeType:String
                        ,@ColumnInfo(name = "isTextData")val isTextData:Boolean)
