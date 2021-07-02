package com.example.viewpager

import android.app.Activity
import java.util.ArrayList
import com.example.viewpager.ContactModel
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.View
import android.view.LayoutInflater
import com.example.viewpager.R
import android.widget.TextView

class MainAdapter(//initialize variable
    var activity: Activity, var arrayList: ArrayList<ContactModel>
) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //initialize view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        //return view
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //initialize contact model
        val model = arrayList[position]

        //set name, number
        holder.tvName.text = model.name
        holder.tvNumber.text = model.number
    }

    override fun getItemCount(): Int {
        //return array list size
        return arrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //initialize variable
        var tvName: TextView
        var tvNumber: TextView

        init {
            //assign variable
            tvName = itemView.findViewById(R.id.tv_name)
            tvNumber = itemView.findViewById(R.id.tv_number)
        }
    }

    //create constructor
    init {
        notifyDataSetChanged()
    }
}