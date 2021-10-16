package com.example.shopit.ui.business

import com.example.shopit.ui.home.HomeListItemViewHolder
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.shopit.data.store.storeProduct.StoreProductDataClass
import com.example.shopit.R

class BusinessListAdapter : RecyclerView.Adapter<BusinessListItemViewHolder>() {
    var data = mutableListOf<StoreProductDataClass>()
    var ctx: Context? = null

    var didClickProductAtPosition: ((Int) -> Unit)? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessListItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        var view = layoutInflater.inflate(R.layout.business_product_list_item, parent, false)
        ctx = parent.context
        return BusinessListItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    fun update(modelList: MutableList<StoreProductDataClass>) {
        data = modelList
        this.notifyDataSetChanged()
    }

    companion object {
        private const val TAG = "ShopIt-BusinessListAdapter"
    }

    override fun onBindViewHolder(holder: BusinessListItemViewHolder, position: Int) {
        holder.product = this.data[position]

        holder.itemView.setOnClickListener {
            Log.d(TAG, "Clicked shop[$position]")
            this.didClickProductAtPosition?.let { f -> f(position) }
        }
    }
}