package com.example.shopit.ui.search

import android.Manifest
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.location.Address
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.shopit.R
import com.example.shopit.data.store.ShopDataClass
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import org.w3c.dom.Text
import java.time.LocalDate
import java.util.*

class SearchListItemViewHolder(businessItemView: View) : RecyclerView.ViewHolder(businessItemView) {
    private val shopImage: ShapeableImageView = itemView.findViewById(R.id.search_shop_item_image)
    private val shopName: TextView = itemView.findViewById(R.id.search_shop_item_name)
    private val isOpen: TextView = itemView.findViewById(R.id.search_shop_item_is_open)
    private val storeAddress: TextView = itemView.findViewById(R.id.search_shop_item_address_text)

    val phoneButton: ImageView = itemView.findViewById(R.id.search_shop_item_phone_icon)
    val mapButton: ImageView = itemView.findViewById(R.id.search_shop_item_map_icon)

    val picasso: Picasso = Picasso.get()

    var shop: ShopDataClass? = null

    set(value) {
        field = value

        this.shop?.apply {
            this@SearchListItemViewHolder.shopName.text = this.shopName
            this@SearchListItemViewHolder.storeAddress.text = "${this.shopAddress.addressLineOne}, ${this.shopAddress.addressSuburb}"


            val todaysDay = LocalDate.now().getDayOfWeek().name
            Log.d(TAG, "Todays day is ${todaysDay}")

            for (item in this.shopHours){
                Log.d(TAG, "Day of week is ${item.dayOfWeek}")
                if (item.dayOfWeek.toString() == todaysDay.uppercase()){
                    if (item.isOpen){
                        this@SearchListItemViewHolder.isOpen.setTextColor(Color.parseColor("#09bd00"))
                        this@SearchListItemViewHolder.isOpen.text = "Open"
                    }else{
                        this@SearchListItemViewHolder.isOpen.setTextColor(Color.parseColor("#b50f00"))
                        this@SearchListItemViewHolder.isOpen.text = "Closed"
                    }
                }
            }

        }

        if (this.shop!!.shopImage?.isBlank() == true) {
            shopImage.setImageResource(R.drawable.ic_home_black_24dp)
            shopImage.setColorFilter(
                ContextCompat.getColor(
                    shopImage.context,
                    android.R.color.darker_gray
                )
            )
            shopImage.strokeWidth = 0.0F
        } else {
            picasso.load(this.shop!!.shopImage)
                .transform(CropCircleTransformation())
                .into(shopImage)
        }
    }
    companion object {
        private const val TAG = "ShopIt-SearchListItemViewHolder"
    }
}