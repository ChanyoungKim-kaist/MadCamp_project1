package com.example.viewpager

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.viewpager.gallery.Image
import com.example.viewpager.gallery.ImageAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Fragment2 : Fragment() {

    private var imageRecycler:RecyclerView?=null
    private var progressBar:ProgressBar?=null
    private var allPictures:ArrayList<Image>?=null
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var curPhotoPath : String // 사진 경로 값

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageRecycler= view.findViewById(R.id.image_recycler)
        progressBar=view.findViewById(R.id.recycler_progress)
        val btn = view.findViewById<FloatingActionButton>(R.id.fab)

        imageRecycler?.layoutManager= GridLayoutManager(activity, 3)
        imageRecycler?.setHasFixedSize(true)

        setPermission()

        btn.setOnClickListener {
            takeCapture()
        }

        refreshApp()

    }

    private fun setPermission() {
        val permission = object: PermissionListener {
            override fun onPermissionGranted() {
                Toast.makeText(requireContext(), "Permission allowed", Toast.LENGTH_SHORT).show()

                allPictures = ArrayList()

                if(allPictures !!.isEmpty())
                {
                    progressBar?.visibility= View.VISIBLE
                    allPictures = getAllImages()
                    imageRecycler?.adapter= ImageAdapter(requireContext(), allPictures!! )
                    progressBar?.visibility=View.GONE
                }
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        TedPermission.with(requireContext())
            .setPermissionListener(permission)
            .setRationaleMessage("카메라 앱을 사용하시려면 권한을 허용해주세요.")
            .setDeniedMessage("권한을 거부함")
            .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .check()
    }

    private fun takeCapture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also{ takePictureIntent ->
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                }catch (ex: IOException) { null}
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.viewpager.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    private fun createImageFile(): File? {
        val timestamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)
            .apply { curPhotoPath = absolutePath }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //val bird = findViewById<ImageView>(R.id.bird)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val bitmap: Bitmap
            val file = File(curPhotoPath)
            if (Build.VERSION.SDK_INT < 28) {
                bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, Uri.fromFile(file))
                //bird.setImageBitmap(bitmap)
            } else {
                val decode = ImageDecoder.createSource(
                    requireContext().contentResolver,
                    Uri.fromFile(file)
                )
                bitmap = ImageDecoder.decodeBitmap(decode)
                //bird.setImageBitmap(bitmap)
            }
            savePhoto(bitmap)
        }

    }

    private fun savePhoto(bitmap: Bitmap) {

        val mediaScan = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val file = File(curPhotoPath)
        val uri = Uri.fromFile(file)
        mediaScan.setData(uri)
        requireContext().sendBroadcast(mediaScan)

        Toast.makeText(requireContext(), "사진이 앨범에 저장되었습니다", Toast.LENGTH_SHORT).show()

    }

    private fun refreshApp() {
        val refresh = view?.findViewById<SwipeRefreshLayout>(R.id.refresh)
        refresh?.setOnRefreshListener{

            allPictures = ArrayList()

            if(allPictures !!.isEmpty())
            {
                progressBar?.visibility= View.VISIBLE
                allPictures = getAllImages()
                imageRecycler?.adapter= ImageAdapter(requireContext(), allPictures!! )
                progressBar?.visibility=View.GONE
            }

            refresh.isRefreshing = false
        }
    }


    private fun getAllImages(): ArrayList<Image>? {
        val images=ArrayList<Image>()
        val allImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME)
        var cursor = requireContext().contentResolver.query(allImageUri, projection, null, null, null)

        try {
            cursor!!.moveToFirst()
            do{
                val image= Image()
                image.imagePath=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                image.imageName=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                images.add(image)
            }while (cursor.moveToNext())
            cursor.close()
        }
        catch (e:Exception)
        {
            e.printStackTrace()
        }
        return images
    }



}