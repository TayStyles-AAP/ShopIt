package com.example.shopit.ui.home

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.shopit.data.store.ShopDataClass
import com.example.shopit.R
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class HomeListItemViewHolder(shopItemView: View) : RecyclerView.ViewHolder(shopItemView) {
    private val shopImage: ShapeableImageView = itemView.findViewById(R.id.shop_item_image)
    private val shopName: TextView = itemView.findViewById(R.id.shop_item_name)
    private val shopContact: TextView = itemView.findViewById(R.id.shop_item_contact)
    private val shopAddress: TextView = itemView.findViewById(R.id.shop_item_address)

    val picasso: Picasso = Picasso.get()

    var shop: ShopDataClass? = null
    set(value) {
        field = value

        this.shopName.text = this.shop!!.shopName
        this.shopAddress.text = this.shop!!.shopAddress.addressLineOne

        if (this.shop!!.shopEmail != null) {
            this.shopContact.text = this.shop!!.shopEmail
        } else {
            this.shopContact.text = this.shop!!.shopPhoneNumber
        }

        Log.d("SiteListItemViewHolder", "Shop image URL is:(${this.shop!!.shopImage})")

        if (this.shop!!.shopImage.isNullOrBlank()) {
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
}
