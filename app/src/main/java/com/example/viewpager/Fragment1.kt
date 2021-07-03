package com.example.viewpager

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.viewpager.contact.ContactModel
import com.example.viewpager.contact.MainAdapter
import java.util.*

class Fragment1 : Fragment() {
    //Initialize variable
    var recyclerView: RecyclerView? = null
    var arrayList = ArrayList<ContactModel>()
    var adapter: MainAdapter? = null




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_1, container, false)

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        var number: String?=""

        //Assign variable
        recyclerView = view.findViewById(R.id.recycler_view)


        //button click handle
//        val callBtn = view.findViewById<ImageButton>(R.id.callBtn)
//        callBtn.setOnClickListener{
//            //get input from edit text
//            number =
//        }

        //check permission
        checkPermission()
    }

    private fun checkPermission() {
        //check condition
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_CONTACTS
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            //When permission is not granted
            //request permission
            ActivityCompat.requestPermissions(
                requireContext() as Activity, arrayOf(Manifest.permission.READ_CONTACTS), 100
            )
        } else {
            //when permission is granted
            //create method
            contactList
        }
    }//when phone cursor move to next

    //Initialize contact model
    //Set name
    //Set number
    //add model in array list
    //Close phone cursor
    //close cursor
    //set layout manager
    //initialize adapter
    //set adapter
//cursor move to next
    //get contact id
    //Get contact name
    //Initialize phone uri
    //initialize selection
    //initialize phone cursor
    //check condition
//when count is greater than 0
    //use while loop
    //initialize uri
    val contactList:
    //sort by ascending
    //initizlize cursor
    //check condition
            Unit
        get() {
            //initialize uri
            val uri = ContactsContract.Contacts.CONTENT_URI
            //sort by ascending
            val sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
            //initizlize cursor

            val cursor = activity?.contentResolver?.query(
                uri, null, null, null, sort
            )
            //check condition
            if (cursor!!.count > 0) {
                //when count is greater than 0
                //use while loop
                while (cursor.moveToNext()) {
                    //cursor move to next
                    //get contact id
                    val id = cursor.getString(
                        cursor.getColumnIndex(
                            ContactsContract.Contacts._ID
                        )
                    )
                    //Get contact name
                    val name = cursor.getString(
                        cursor.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME
                        )
                    )
                    //Initialize phone uri
                    val uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                    //initialize selection
                    val selection = (ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                            + " =?")
                    //initialize phone cursor
                    val phoneCursor = activity?.contentResolver?.query(
                        uriPhone, null, selection, arrayOf(id), null
                    )
                    //check condition
                    if (phoneCursor!!.moveToNext()) {
                        //when phone cursor move to next
                        val number = phoneCursor.getString(
                            phoneCursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                            )
                        )
                        var dialNumber: String?=""
                        val callBtn = view?.findViewById<ImageButton>(R.id.callBtn)
                        callBtn?.setOnClickListener{
                            //get input from contact address
                            dialNumber = number
                            //Dialer intent
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + Uri.encode(dialNumber)))
                            startActivity(intent)
                        }

                        //Initialize contact model
                        val model = ContactModel()
                        //Set name
                        model.name = name
                        //Set number
                        model.number = number
                        //add model in array list
                        arrayList.add(model)
                        //Close phone cursor
                        phoneCursor.close()
                    }
                }
                //close cursor
                cursor.close()
            }
            //set layout manager
            recyclerView!!.layoutManager = LinearLayoutManager(requireContext())
            //initialize adapter
            adapter = MainAdapter(requireContext() as Activity, arrayList)
            //set adapter
            recyclerView!!.adapter = adapter
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //check condition
        if (requestCode == 100 && grantResults.size > 0 && (grantResults[0]
                    == PackageManager.PERMISSION_GRANTED)
        ) {
            //when permission is granted
            //call method
            contactList
        } else {
            //when permission is denied
            //display toast
            Toast.makeText(
                requireContext(), "Permission denied.", Toast.LENGTH_SHORT
            ).show()
            //call check permission method
            checkPermission()
        }
    }
}