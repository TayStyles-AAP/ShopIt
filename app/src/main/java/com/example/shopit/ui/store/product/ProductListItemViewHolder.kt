package com.example.shopit.ui.store.product

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

class ProductListItemViewHolder(storeItemView: View) : RecyclerView.ViewHolder(storeItemView) {

    private val productImage: ShapeableImageView = itemView.findViewById(R.id.product_item_image)
    private val productName: TextView = itemView.findViewById(R.id.product_item_name)
    private val productBarcode: TextView = itemView.findViewById(R.id.product_item_barcode)
    //private val productQuantity: TextView = itemView.findViewById(R.id.cart_item_quantity)

    private val picasso: Picasso = Picasso.get()

    var product: StoreProductDataClass? = null
        set(value) {
            field = value

            this.productName.text = this.product!!.productName
            this.productBarcode.text = this.product!!.cartProductBarcode

            Log.d(TAG, "product image URL is:(${this.product!!.productImage})")

            if (this.product!!.productImage.isBlank()) {
                productImage.setImageResource(R.drawable.ic_product)
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
        private const val TAG = "ShopIt-ProductListAdapter"
    }
}
