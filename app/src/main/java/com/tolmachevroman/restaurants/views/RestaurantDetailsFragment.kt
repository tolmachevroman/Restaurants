package com.tolmachevroman.restaurants.views

import android.annotation.SuppressLint
import android.app.Dialog
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.tolmachevroman.restaurants.R
import com.tolmachevroman.restaurants.models.restaurants.Restaurant

/**
 * Created by romantolmachev on 23/11/2017.
 */
class RestaurantDetailsFragment : BottomSheetDialogFragment() {

    companion object {
        val RESTAURANT = "RESTAURANT"
    }

    private val bottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss()
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val contentView = View.inflate(context, R.layout.fragment_restaurant_details, null)
        dialog.setContentView(contentView)

        if (arguments != null && arguments.containsKey(RESTAURANT)) {
            val restaurant = arguments.getSerializable(RESTAURANT) as Restaurant

            val name = contentView.findViewById<TextView>(R.id.name)
            name.text = restaurant.name

            val price = contentView.findViewById<TextView>(R.id.price)
            price.text = getString(R.string.price, restaurant.price)

            val description = contentView.findViewById<TextView>(R.id.description)
            description.text = restaurant.description

            val image = contentView.findViewById<ImageView>(R.id.image)
            val progressbar = contentView.findViewById<ProgressBar>(R.id.progressbar)

            val viewTreeObserver = contentView.viewTreeObserver
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    contentView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val width = contentView.measuredWidth
                    val height = contentView.measuredHeight

                    val params = (contentView.parent as View).layoutParams as CoordinatorLayout.LayoutParams
                    val behavior = params.behavior

                    if (behavior != null && behavior is BottomSheetBehavior<*>) {
                        behavior.setBottomSheetCallback(bottomSheetBehaviorCallback)
                        behavior.peekHeight = height
                    }

                    //load image asynchronously
                    if (restaurant.image.isNotBlank()) {
                        progressbar.visibility = View.VISIBLE
                        image.postDelayed({
                            Picasso.with(context).load(restaurant.image).resize(width,
                                    resources.getDimensionPixelSize(R.dimen.image_height)).centerCrop().into(image, object : Callback {
                                override fun onSuccess() {
                                    progressbar.visibility = View.GONE
                                }

                                override fun onError() {
                                }
                            })
                        }, 500)
                    }
                }
            })
        }

    }
}