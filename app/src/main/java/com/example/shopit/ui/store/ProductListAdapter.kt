package com.example.shopit.ui.store

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopit.R
import com.example.shopit.data.store.storeProduct.StoreProductDataClass

class ProductListAdapter : RecyclerView.Adapter<StoreListItemViewHolder>() {
    var data = mutableListOf<StoreProductDataClass>()
    var ctx: Context? = null

    var didClickProductAtPosition: ((Int) -> Int)? = null

        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreListItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        var view = layoutInflater.inflate(R.layout.product_list_item, parent, false)
        ctx = parent.context
        return StoreListItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoreListItemViewHolder, position: Int) {
        holder.product = this.data[position]

        holder.itemView.setOnClickListener {
            Log.d(TAG, "Clicked Product[$position]")
            this.didClickProductAtPosition?.let { f -> f(position) }
        }
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    fun update(modelList: MutableList<StoreProductDataClass>) {
        data = modelList
        this.notifyDataSetChanged()
    }

    companion object {
        private const val TAG = "ShopIt-ProductListAdapter"
    }
}
