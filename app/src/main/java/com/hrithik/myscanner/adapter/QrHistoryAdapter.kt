package com.hrithik.myscanner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hrithik.myscanner.databinding.QrHistoryLayoutBinding
import com.hrithik.myscanner.model.QrHistoryInfo

class QrHistoryAdapter(private val context: Context, private val qrInfoList: MutableList<QrHistoryInfo>):
    RecyclerView.Adapter<QrHistoryAdapter.QrHistoryVieHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QrHistoryVieHolder {
        val binding = QrHistoryLayoutBinding.inflate(LayoutInflater.from(context),parent,false)
        return QrHistoryVieHolder(binding)
    }

    override fun onBindViewHolder(holder: QrHistoryVieHolder, position: Int) {
        val qrHistoryInfo = qrInfoList[position]
        holder.bind(qrHistoryInfo)
    }

    override fun getItemCount(): Int {
        return qrInfoList.size
    }


    class QrHistoryVieHolder(qrHistoryLayoutBinding: QrHistoryLayoutBinding) :
        RecyclerView.ViewHolder(qrHistoryLayoutBinding.root){
        private val binding = qrHistoryLayoutBinding

        fun bind(qrHistoryInfo: QrHistoryInfo){
            // store values in variables
            val scannedData = if (qrHistoryInfo.isScanned) {
                "Scanned Data"
            }else{
                "Created Data"
            }

            // set layout
            binding.codeTV.text = qrHistoryInfo.codeType
            binding.textView55.text = scannedData
            binding.qrDataTV.text = qrHistoryInfo.qrData
            binding.dateTV.text = qrHistoryInfo.dateFormat

        }
    }

}