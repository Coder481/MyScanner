package com.hrithik.myscanner.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.hrithik.myscanner.Constants
import com.hrithik.myscanner.activity.MainActivity
import com.hrithik.myscanner.databinding.FragmentCreateQrBinding
import com.hrithik.myscanner.model.QrHistoryInfo
import java.text.SimpleDateFormat
import java.util.*


class CreateQrFragment: Fragment() {

    private lateinit var b: FragmentCreateQrBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        b = FragmentCreateQrBinding.inflate(inflater)
//        return super.onCreateView(inflater, container, savedInstanceState)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLayout()
    }

    private fun setLayout() {
        b.createQrBtn.setOnClickListener {
            generateQrCode()
        }

        b.shareQrImageBtn.setOnClickListener {
            b.scannedImageView.invalidate()
            val drawable = b.scannedImageView.drawable as BitmapDrawable
            val bitmap = drawable.bitmap

            if (bitmap != null){
                shareImage(bitmap)
            }
        }
    }

    private fun shareImage(image: Bitmap) {

        val bitmapPath: String = MediaStore.Images.Media.insertImage(
            context?.contentResolver,
            image,
            "palette",
            "share palette"
        )
        val bitmapUri = Uri.parse(bitmapPath)

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/png"
        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
        startActivity(Intent.createChooser(intent, "Share"))

    }


    private fun generateQrCode() {

        val qrTxt = b.addDataTIE.text.toString()
        if (qrTxt.isEmpty()) {
            b.addDataTIE.error = "Please enter some data"
            return
        }


        val writer = QRCodeWriter()

        try {
            val bitMatrix = writer.encode(qrTxt,BarcodeFormat.QR_CODE,564,564)
            val width = bitMatrix.width
            val height = bitMatrix.height

            val bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565)

            for (x in 0 until width){
                for (y in 0 until height){
                    // if(condition is true) task1 else task2
                    bitmap.setPixel(x, y, if(bitMatrix.get(x,y)) Color.BLACK else Color.WHITE )
                }
            }

            b.scannedImageView.setImageBitmap(bitmap)
            b.shareQrImageBtn.visibility = View.VISIBLE

            saveQrToRoom(qrTxt)

        }catch (e : WriterException){
            Toast.makeText(context,e.localizedMessage,Toast.LENGTH_SHORT).show()
        }catch (e : Exception){
            Toast.makeText(context,e.localizedMessage,Toast.LENGTH_SHORT).show()
        }

    }

    private fun saveQrToRoom(qrTxt: String) {

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(Constants.SAVED_DATE_FORMAT).format(calendar.time)

        val qrHistoryInfo = QrHistoryInfo(dateFormat = dateFormat,
                                          qrData = qrTxt,
                                          isScanned = false,
                                          codeType = "QR_CODE",
                                          isTextData = true)

        (activity as MainActivity).saveQrInfoInRoom(qrHistoryInfo)
    }
}