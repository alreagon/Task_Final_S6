package com.example.task_final_s6

import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

data class ImageModel (
    var name: String = "",
    val status: String = "",
    val image: String = "",
    var number: String = "",
    val uid: String = "",
    val online: String = "offline",
    val typing: String = "false"
    ) {

        companion object {

            @JvmStatic
            @BindingAdapter("imageUrl")
            fun loadImage(view: CircleImageView, imageUrl: String?) {
                imageUrl?.let {
                    Glide.with(view.context).load(imageUrl).into(view)
                }
            }
        }

    }

