package com.hrithik.myscanner.activity

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.hrithik.myscanner.MyApplication
import com.hrithik.myscanner.R
import com.hrithik.myscanner.adapter.QrHistoryAdapter
import com.hrithik.myscanner.databinding.ActivityQrInfoBinding
import com.hrithik.myscanner.databinding.QrHistoryLayoutBinding
import com.hrithik.myscanner.model.QrHistoryInfo
import com.hrithik.myscanner.room.QrInfoViewModel

class QrInfoActivity : AppCompatActivity() {
    private var observeFromRoom: Boolean = true
    private lateinit var adapter: QrHistoryAdapter
    private lateinit var b: ActivityQrInfoBinding
    private lateinit var qrInfoList: MutableList<QrHistoryInfo>

    private val qrInfoViewModel: QrInfoViewModel by viewModels {
        QrInfoViewModel.QrInfoViewModelFactory((application as MyApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityQrInfoBinding.inflate(layoutInflater)
        setContentView(b.root)

        if (supportActionBar != null){
            supportActionBar!!.title = "History"
        }

        qrInfoViewModel.allQrHistoryInfo.observe(this, { qrInfoList ->
            qrInfoList?.let {
                if (observeFromRoom) {
                    this.qrInfoList = it
                    setUpAdapter()
                    observeFromRoom = false
                }
            }
        })

    }

    private fun setUpAdapter() {
        b.qrHistoryRecyclerView.removeAllViews()

        if (qrInfoList.isEmpty()) {
            b.noHisTV.visibility = View.VISIBLE
            return
        }

        adapter = QrHistoryAdapter(this,qrInfoList)
        b.qrHistoryRecyclerView.adapter = adapter
        b.qrHistoryRecyclerView.layoutManager = LinearLayoutManager(this)

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallBack)
        itemTouchHelper.attachToRecyclerView(b.qrHistoryRecyclerView)

//        setLinearLay()

        /*val qrHistoryInfo = qrInfoList[0]
        val qrHisBinding = QrHistoryLayoutBinding.inflate(layoutInflater)

        // store values in variables
        val scannedData = if (qrHistoryInfo.isScanned) {
            "Scanned Data"
        }else{
            "Created Data"
        }


        // set layout
        qrHisBinding.codeTV.text = qrHistoryInfo.codeType
        qrHisBinding.textView55.text = scannedData
        qrHisBinding.qrDataTV.text = qrHistoryInfo.qrData
        qrHisBinding.dateTV.text = qrHistoryInfo.dateFormat


        AlertDialog.Builder(this)
            .setView(qrHisBinding.root).show()*/
    }


    private val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            val position = viewHolder.adapterPosition
            val deletedQrInfo = qrInfoList[position]

            qrInfoList.removeAt(position)
            adapter.notifyItemRemoved(position)
            qrInfoViewModel.deleteQrInfo(deletedQrInfo)

            Snackbar.make(findViewById(android.R.id.content),"Item Removed",Snackbar.LENGTH_LONG)
                .setBackgroundTint(ResourcesCompat.getColor(resources,R.color.error_red,null))
                .setActionTextColor(ResourcesCompat.getColor(resources,R.color.white,null))
                .setAction("Undo") {
                    qrInfoList.add(position, deletedQrInfo)
                    adapter.notifyItemInserted(position)
                    qrInfoViewModel.insert(deletedQrInfo)
                }.show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.qr_history_action_bar,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.clearHistory){
            if (qrInfoList.isNotEmpty())
                askForConfirmDelete()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun askForConfirmDelete() {
        AlertDialog.Builder(this).setTitle("Sure to clear history?")
            .setMessage("Data cannot be retrieved back again.")
            .setNegativeButton("Delete", DialogInterface.OnClickListener { dialog, which ->
                observeFromRoom = true
                qrInfoViewModel.deleteAll()
            })
            .setPositiveButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            }).show()
    }

    private fun setLinearLay() {

        for (qrHistoryInfo in qrInfoList){

            val qrHisBinding = QrHistoryLayoutBinding.inflate(layoutInflater)

            // store values in variables
            val scannedData = if (qrHistoryInfo.isScanned) {
                "Scanned Data"
            }else{
                "Created Data"
            }


            // set layout
            qrHisBinding.codeTV.text = qrHistoryInfo.codeType
            qrHisBinding.textView55.text = scannedData
            qrHisBinding.qrDataTV.text = qrHistoryInfo.qrData
            qrHisBinding.dateTV.text = qrHistoryInfo.dateFormat

            b.historyLL.addView(qrHisBinding.root)
        }
    }
}