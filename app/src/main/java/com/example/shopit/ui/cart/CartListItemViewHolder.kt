package com.example.productit.ui.home

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.shopit.R
import com.example.shopit.data.cart.CartProductDataClass
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class CartListItemViewHolder(cartItemView: View) : RecyclerView.ViewHolder(cartItemView) {

    private val productImage : ShapeableImageView = itemView.findViewById(R.id.cart_item_image)
    private val productName: TextView = itemView.findViewById(R.id.cart_item_name)
    private val productPrice: TextView = itemView.findViewById(R.id.cart_item_price)
    //private val productQuantity: TextView = itemView.findViewById(R.id.cart_item_quantity)

    val picasso: Picasso = Picasso.get()

    var product: CartProductDataClass? = null
        set(value) {
            field = value

            this.productName.text = this.product!!.cartProductName
            this.productPrice.text = this.product!!.cartProductPrice
            //this.productQuantity.text = this.product!!.cartProductQuantity // to be defined


            Log.d("SiteListItemViewHolder", "product image URL is:(${this.product!!.cartProductImage})")

            if (this.product!!.cartProductImage.isBlank()) {
                productImage.setImageResource(R.drawable.ic_product)
                productImage.setColorFilter(
                    ContextCompat.getColor(
                        productImage.context,
                        android.R.color.darker_gray
                    )
                )
                productImage.strokeWidth = 0.0F
            } else {
                picasso.load(this.product!!.cartProductImage)
                    .transform(CropCircleTransformation())
                    .into(productImage)
            }
        }
}
