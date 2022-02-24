package com.example.mygallery

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {


    private var readPermissionGranted = false
    private lateinit var permissionsLauncher : ActivityResultLauncher<Array<String>>

    private lateinit var mAdapter :GalleryAdapter
    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        permissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){permissions->
            readPermissionGranted = permissions[android.Manifest.permission.READ_EXTERNAL_STORAGE]?: readPermissionGranted


        }
        updateOrRequestPermission()

        val photos = loadPhotosFromExternalStorage()
        recyclerView.layoutManager = GridLayoutManager(this,2)
        mAdapter = GalleryAdapter(photos)
        mAdapter.notifyDataSetChanged()
        recyclerView.adapter = mAdapter

    }



    private fun updateOrRequestPermission() {
        val hasReadPermission = ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

        readPermissionGranted = hasReadPermission

        val permissionsToRequest = mutableListOf<String>()
        if(!readPermissionGranted){
            permissionsToRequest.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if(permissionsToRequest.isNotEmpty()){
            permissionsLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

 @RequiresApi(Build.VERSION_CODES.Q)
    fun loadPhotosFromExternalStorage(): List<ImageData>{

         val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
         val projection = arrayOf(MediaStore.Images.Media._ID)

         val photos = mutableListOf<ImageData>()
         contentResolver.query(
             collection,
             projection,
             null,
             null,
             "${MediaStore.Images.Media._ID} ASC"
         )?.use { cursor ->
             val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

             while(cursor.moveToNext()){
                 val id = cursor.getLong(idColumn)
                 val contentUri = ContentUris.withAppendedId(
                     MediaStore.Images.Media.EXTERNAL_CONTENT_URI,id
                 )
                 photos.add(ImageData(id,contentUri))
             }
             photos.toList()

         }?: listOf()
     return photos
 }




}