package com.notbytes.barcodereader

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.android.synthetic.main.activity_create.*
import java.util.*


class CreateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        qrText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.toString().isNotEmpty()) {
                    qrImage.setImageBitmap(encodeAsBitmap(s.toString()))
                } else {
                    qrImage.setImageBitmap(null)
                }
            }
        })
    }

    private fun encodeAsBitmap(contents: String?) =
            try {
                val writer = QRCodeWriter()
                val hints = Hashtable<EncodeHintType, Any>()
                hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
                val bitMatrix = writer.encode(contents, BarcodeFormat.QR_CODE, 512, 512, hints)
                val width = bitMatrix.width
                val height = bitMatrix.height
                val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                for (x in 0 until width) {
                    for (y in 0 until height) {
                        bmp.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
                    }
                }
                bmp
            } catch (e: WriterException) {
                e.printStackTrace()
                null
            }
}