package com.example.shopit.ui.search

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.shopit.R
import com.example.shopit.data.store.ShopDataClass
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class SearchListItemViewHolder(businessItemView: View) : RecyclerView.ViewHolder(businessItemView) {
    private val shopImage: ShapeableImageView =
        itemView.findViewById(R.id.search_shop_item_image)
    private val shopName: TextView = itemView.findViewById(R.id.search_shop_item_name)
    val picasso: Picasso = Picasso.get()

    var shop: ShopDataClass? = null
    set(value) {
        field = value

        this.shopName.text = this.shop!!.shopName

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
        private const val TAG = "ShopIt-BusinessListAdapter"
    }

}