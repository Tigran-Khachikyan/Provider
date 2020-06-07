package com.calcforbuilder.provider

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var adapterRec: AdapterRec

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupPermissions()

        recycler_media.initialize()
        btn_images.initialize()
        btn_videos.initialize()


    }


    private fun RecyclerView.initialize() {
        setHasFixedSize(true)
        adapterRec = AdapterRec(this@MainActivity, null)
        adapter = adapterRec
    }

    private fun Button.initialize() {
        setOnClickListener {
            val paths = when (id) {
                R.id.btn_images -> getMediaImage(true)
                R.id.btn_videos -> getMediaImage(false)
                else -> null
            }
            val files = paths?.map { p -> File(p) }
            adapterRec.setFileList(files)
        }
    }

    private fun getMediaImage(image: Boolean): List<String> {
        val paths: ArrayList<String> = arrayListOf()
        var cursor: Cursor? = null
        val url =
            if (image) MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            else MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        try {
            cursor = contentResolver.query(
                url, null, null, null, null
            )
            cursor?.let {
                for (i in 0 until cursor.count) {
                    cursor.moveToPosition(i)
                    val columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                    val path = cursor.getString(columnIndex)
                    paths.add(path)
                }
            }
        } catch (ex: Exception) {

        } finally {
            cursor?.close()
        }

        return paths
    }


    private val TAG = "PermissionDemo"
    private val RECORD_REQUEST_CODE = 101

    fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied")
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            RECORD_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user")
                } else {
                    Log.i(TAG, "Permission has been granted by user")
                }
            }
        }
    }


}
