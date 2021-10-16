package com.example.shopit.ui.business

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.shopit.R
import com.example.shopit.data.store.storeProduct.StoreProductDataClass
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class BusinessListItemViewHolder(businessItemView: View) : RecyclerView.ViewHolder(businessItemView) {
    private val productImage: ShapeableImageView = itemView.findViewById(R.id.business_product_item_image)
    private val price: TextView = itemView.findViewById(R.id.business_product_item_price)
    private val productName: TextView = itemView.findViewById(R.id.business_product_item_name)

    val picasso: Picasso = Picasso.get()

    var product: StoreProductDataClass? = null
        set(value) {
            field = value

            this.productName.text = this.product!!.productName
            this.price.text = String.format("%.2f",this.product!!.productPrice)


            Log.d("SiteListItemViewHolder", "Product image URL is:(${this.product!!.productImage})")

            if (this.product!!.productImage.isBlank()) {
                productImage.setImageResource(R.drawable.ic_home_black_24dp)
                productImage.setColorFilter(
                    ContextCompat.getColor(
                        productImage.context,
                        android.R.color.darker_gray
                    )
                )
                productImage.strokeWidth = 0.0F
            } else {
                picasso.load(this.product!!.productImage)
                    .transform(CropCircleTransformation())
                    .into(productImage)
            }
        }

    companion object {
        private const val TAG = "ShopIt-BusinessListAdapter"
    }
}
