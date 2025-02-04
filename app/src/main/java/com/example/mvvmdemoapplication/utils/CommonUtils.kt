package com.example.mvvmdemoapplication.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar

        fun View.gone() {
            this.visibility = View.GONE
        }

        fun View.visible() {
            this.visibility = View.VISIBLE
        }
        fun View.invisible() {
            this.visibility = View.INVISIBLE
        }


        fun Context.toast(message: CharSequence): Toast = Toast
            .makeText(this, message, Toast.LENGTH_SHORT)
            .apply {
                show()
            }

        fun View.snackbar(message: CharSequence) = Snackbar
            .make(this, message, Snackbar.LENGTH_SHORT)
            .apply { show() }


        fun checkStringValue(text: String?): Boolean {
            return !(text == null || text.trim { it <= ' ' } == "null" || text.trim { it <= ' ' }.isEmpty())
        }

        /**
         * for return string with suggested value
         */
        fun checkStringReturnValue(text: String?, stReturnString: String = ""): String {
            return if (text == null || text.trim { it <= ' ' } == "null" || text.trim { it <= ' ' }
                    .isEmpty()) {
                stReturnString
            } else {
                text
            }
        }

        fun checkIntReturnValue(text: Int?, stReturnString: Int = 0): Int {
            return if (text == null || "$text".trim { it <= ' ' } == "null" || "$text".trim { it <= ' ' }
                    .isEmpty()) {
                stReturnString
            } else {
                text
            }
        }

        /**
         * For show dialog
         *
         * @param title - title which shown in dialog (application name)
         * @param msg - message which shown in dialog
         * @param positiveText - positive button text
         * @param listener - positive button listener
         * @param negativeText - negative button text
         * @param negativeListener - negative button listener
         * @param icon - drawable icon which shown is dialog
         */
        fun Context.showDialogMain(
            msg: String,
            positiveText: String? = "OK",
            listener: DialogInterface.OnClickListener? = null,
            negativeText: String? = "Cancel",
            negativeListener: DialogInterface.OnClickListener? = null,
            title: String? = "Casali",
            icon: Int? = null
        ) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(title)
            builder.setMessage(msg)
            builder.setCancelable(false)
            builder.setPositiveButton(positiveText) { dialog, which ->
                listener?.onClick(dialog, which)
            }
            if (negativeListener != null) {
                builder.setNegativeButton(negativeText) { dialog, which ->
                    negativeListener.onClick(dialog, which)
                }
            }
            if (icon != null) {
                builder.setIcon(icon)
            }
            builder.create().show()
        }

        /**
         * For validate email-id
         *
         * @return email-id is valid or not
         */
        fun String.isValidEmail(): Boolean {
            return !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
        }

        /**
         * For validate phone
         *
         * @return phone is valid or not
         */
        fun String.isValidPhone(): Boolean {
            return !TextUtils.isEmpty(this) && Patterns.PHONE.matcher(this).matches()
        }

        /**
         * isNetworkAvailable - Check if there is a NetworkConnection
         * @return boolean
         */
        fun Context.isNetworkAvailable(): Boolean {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }

        /**
         * compare string
         * @return boolean
         */
        infix fun String.shouldBeSame(other: String) = this == other

        /**
         * For launch other activity
         */
        inline fun <reified T : Any> Activity.launchActivity(
            requestCode: Int = -1,
            options: Bundle? = null,
            noinline init: Intent.() -> Unit = {}
        ) {

            val intent = newIntent<T>(this)
            intent.init()
            startActivityForResult(intent, requestCode, options)
        }

        inline fun <reified T : Any> Context.launchActivity(
            options: Bundle? = null,
            noinline init: Intent.() -> Unit = {}
        ) {

            val intent = newIntent<T>(this)
            intent.init()
            startActivity(intent, options)
        }

        inline fun <reified T : Any> newIntent(context: Context): Intent =
            Intent(context, T::class.java)


        /**
         * load image
         */
        fun Context.loadImage(imagePath: String, imageView: AppCompatImageView) {
            Glide.with(this)
                .load(imagePath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
        }

        fun Context.loadCircleImage(imagePath: String, imageView: AppCompatImageView) {
            Glide.with(this)
                .load(imagePath)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
        }

        fun Context.loadRoundedCornerImage(imagePath: String, imageView: AppCompatImageView) {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)

            Glide.with(this).load(imagePath)
                .transform(CenterCrop(), RoundedCorners(10))
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
        }
