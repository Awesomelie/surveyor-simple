package com.lunsol.surveyiorsimple

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.util.Log
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    val REQUEST_CODE = 6542

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.chooseImageButton) //変更不可はval

        checkPermission()
        button.setOnClickListener {
            val intent: Intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            startActivityForResult(intent, REQUEST_CODE)
            Toast.makeText(this, "Open Garally", Toast.LENGTH_LONG).show()

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkPermission() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }
            requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                return
            }
            val imageData = data?.data ?: return
            Log.d("imageData: ",imageData.toString())
            val docID = DocumentsContract.getDocumentId(imageData)
            Log.d("docID: ",docID.toString())
            val strSplittedDocId = docID.split(":")
            Log.d("strSplittedDocId: ",strSplittedDocId.toString())
            val strId = strSplittedDocId[strSplittedDocId.size - 1]
            Log.d("strId: ",strId.toString())
            val cursor = contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    arrayOf(MediaStore.MediaColumns.DATA),
                    "_id=?",
                    arrayOf<String>(strId),
                    null
            )
            cursor.moveToFirst()
            val imageURL: String = cursor.getString(0)
            Log.d("imageURL",imageURL.toString())

            val intent: Intent = Intent(this, DrawViewActivity::class.java)
            intent.putExtra("path", imageURL)
            startActivity(intent)

        }
    }
}
