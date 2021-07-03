package com.example.viewpager

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.viewpager.adapters.PhotoAdapter
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import gun0912.tedbottompicker.TedBottomPicker
import android.support.v4.app.Fragment as Fragment4

class Fragment3 : Fragment4() {
    private var btnSelectImage: Button? = null
    private var rcvPhoto: RecyclerView? = null
    private var photoAdapter: PhotoAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_3, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSelectImage = view.findViewById(R.id.btn_select_image)
        rcvPhoto = view.findViewById(R.id.rcv_photo)
        photoAdapter = PhotoAdapter(requireContext())
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        rcvPhoto?.layoutManager = gridLayoutManager
        rcvPhoto?.adapter = PhotoAdapter(requireContext())
        btnSelectImage?.setOnClickListener(View.OnClickListener { requestPermission() })
    }

    private fun requestPermission() {
        val permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                selectImagesFromGallery()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(
                    requireContext(),
                    "Permission Denied\n$deniedPermissions",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        TedPermission.with(requireContext())
            .setPermissionListener(permissionlistener)
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .check()
    }

    private fun selectImagesFromGallery() {
        TedBottomPicker.with(requireActivity())
            .setPeekHeight(1600)
            .showTitle(false)
            .setCompleteButtonText("Done")
            .setEmptySelectionText("No Select")
            .showMultiImage { uriList ->
                if (uriList != null && !uriList.isEmpty()) {
                    photoAdapter!!.setData(uriList)
                }
            }
    }
}