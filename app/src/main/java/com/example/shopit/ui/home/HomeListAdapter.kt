package com.example.shopit.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.shopit.data.store.ShopDataClass
import com.example.shopit.R

class HomeListAdapter : RecyclerView.Adapter<HomeListItemViewHolder>() {
    var data = mutableListOf<ShopDataClass>()
    var ctx: Context? = null

    var didClickShopAtPosition: ((String) -> Unit)? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        var view = layoutInflater.inflate(R.layout.shop_list_item, parent, false)
        ctx = parent.getContext()
        return HomeListItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeListItemViewHolder, position: Int) {
        holder.shop = this.data[position]

        holder.itemView.setOnClickListener {
            Log.d(TAG, "Clicked shop[$position]")
            this.didClickShopAtPosition?.let { f -> f("${holder.shop!!.shopSid}") }
        }
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    fun update(modelList: MutableList<ShopDataClass>) {
        data = modelList
        this.notifyDataSetChanged()
    }

    companion object {
        private const val TAG = "ShopIt-HomeListAdapter"
    }
}