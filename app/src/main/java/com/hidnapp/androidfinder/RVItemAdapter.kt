package com.hidnapp.androidfinder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import java.util.ArrayList

class RVItemAdapter     // creating a constructor for our variables.
    (// creating a variable for array list and context.
    private val itemModalArrayList: ArrayList<RVItemModal>?, private val context: Context
) : RecyclerView.Adapter<RVItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // below line is to inflate our layout.
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // setting data to our views of recycler view.
        val modal = itemModalArrayList?.get(position)
        holder.itemNameTV.text = modal?.itemName
        holder.itemDescTV.text = modal?.itemDescription
    }

    override fun getItemCount(): Int {
        // returning the size of array list.
        return itemModalArrayList!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // creating variables for our views.
        val itemNameTV: TextView
        val itemDescTV: TextView

        init {

            // initializing our views with their ids.
            itemNameTV = itemView.findViewById(R.id.idTVItemName)
            itemDescTV = itemView.findViewById(R.id.idTVItemDescription)
        }
    }
}