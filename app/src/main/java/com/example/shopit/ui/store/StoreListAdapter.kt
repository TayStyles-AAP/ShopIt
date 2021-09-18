package com.example.shopit.ui.store

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.shopit.R
import com.example.shopit.data.preferences.Preferences
import com.example.shopit.data.store.storeProduct.StoreProductDataClass

class StoreListAdapter : RecyclerView.Adapter<StoreListItemViewHolder>() {
    var data = mutableListOf<StoreProductDataClass>()
    var ctx: Context? = null

    var addItemToCart: ((Int) -> Unit)? = null

    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreListItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.cart_list_item, parent, false)
        ctx = parent.context
        return StoreListItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoreListItemViewHolder, position: Int) {
        holder.product = this.data[position]

        holder.itemView.findViewById<Button>(R.id.cart_add_button).setOnClickListener {
            Log.d(TAG, "Clicked Item[$position]")
            this.addItemToCart?.let { f -> f(position) }
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
        private const val TAG = "ShopIt-StoreListAdapter"
    }
}