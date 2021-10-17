package com.example.shopit.ui.search

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopit.R
import com.example.shopit.data.store.ShopDataClass
import com.example.shopit.ui.profile.ProfileFragment.Companion.TAG

class SearchListAdapter : RecyclerView.Adapter<SearchListItemViewHolder>() {
    var data = mutableListOf<ShopDataClass>()
    var ctx: Context? = null

    var clickedShopAtIdx: ((String) -> Unit)? = null
    var clickedPhone: ((String) -> Unit)? = null
    var clickedMap: ((String) -> Unit)? = null

        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        var view = layoutInflater.inflate(R.layout.search_list_item, parent, false)

        return SearchListItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchListItemViewHolder, position: Int) {
        holder.shop = this.data[position]

        holder.itemView.setOnClickListener {
            Log.d(TAG, "Click shop[$position]")
            this.clickedShopAtIdx?.let { f -> f("${holder.shop!!.shopSid}") }
        }
        holder.phoneButton.setOnClickListener {
            this.clickedPhone?.let{ f -> f(holder.shop!!.shopPhoneNumber.toString())}
        }
        holder.mapButton.setOnClickListener {
            this.clickedMap?.let{ f -> f("${holder.shop!!.shopAddress.addressLineOne} ${holder.shop!!.shopAddress.addressSuburb} ${holder.shop!!.shopAddress.addressCity}")}
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
        private const val TAG = "ShopIt-SearchListAdapter"
    }
}