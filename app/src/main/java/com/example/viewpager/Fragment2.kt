package com.example.viewpager

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.viewpager.gallery.Image
import com.example.viewpager.gallery.ImageAdapter

class Fragment2 : Fragment() {

    private var imageRecycler:RecyclerView?=null
    private var progressBar:ProgressBar?=null
    private var allPictures:ArrayList<Image>?=null

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

        imageRecycler?.layoutManager= GridLayoutManager(activity, 3)
        imageRecycler?.setHasFixedSize(true)

        if (ContextCompat.checkSelfPermission(
                requireContext() ,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(
                requireContext() as Activity,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 101
            )
        }

        allPictures = ArrayList()

        if(allPictures !!.isEmpty())
        {
            progressBar?.visibility= View.VISIBLE
            allPictures = getAllImages()
            imageRecycler?.adapter= ImageAdapter(requireContext(), allPictures!! )
            progressBar?.visibility=View.GONE
        }
    }

    private fun getAllImages(): ArrayList<Image>? {
        val images=ArrayList<Image>()
        val allImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME)
        var cursor = activity?.contentResolver?.query(allImageUri, projection, null, null, null)

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