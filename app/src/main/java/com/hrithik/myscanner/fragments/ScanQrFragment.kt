package com.hrithik.myscanner.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.hrithik.myscanner.Constants
import com.hrithik.myscanner.activity.MainActivity
import com.hrithik.myscanner.databinding.FragmentScanQrBinding
import com.hrithik.myscanner.model.QrHistoryInfo
import java.text.SimpleDateFormat
import java.util.*

class ScanQrFragment : Fragment() {

    private lateinit var b: FragmentScanQrBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        b = FragmentScanQrBinding.inflate(inflater)
        return b.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLayout()
    }

    private fun setLayout() {
        b.scanTV.text = "Hello Scanner"

        b.scanQrBtn.setOnClickListener {
            showQrScanner()
        }
    }

    private fun showQrScanner() {
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        integrator.setPrompt("Scan")
        integrator.setCameraId(0)
        integrator.setOrientationLocked(false)
        integrator.setBeepEnabled(true)
        integrator.setBarcodeImageEnabled(false)
        integrator.setBarcodeImageEnabled(true)
        integrator.initiateScan()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data)

        if (intentResult != null ){
            if (intentResult.contents != null){
                val res = intentResult.contents
                b.scanTV.text = res
                saveQrInfo(intentResult)
            }else{
                Toast.makeText(context,"Scan Cancelled",Toast.LENGTH_SHORT).show()
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun saveQrInfo(intentResult: IntentResult) {

        val res = intentResult.contents
        val codeType = intentResult.formatName

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(Constants.SAVED_DATE_FORMAT).format(calendar.time)

        val qrHistoryInfo = QrHistoryInfo(dateFormat = dateFormat,
                                          qrData = res,
                                          isScanned = true,
                                          codeType = codeType,
                                          isTextData = true)

        (activity as MainActivity).saveQrInfoInRoom(qrHistoryInfo)
    }
}