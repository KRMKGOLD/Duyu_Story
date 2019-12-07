package com.example.duyustory.util

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore

class AddPictureUtil {
    fun getImageInGallery(): Intent {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")

        return galleryIntent
    }

    fun exifOrientationToDegrees(exifOrientation: Int): Int {
        return when (exifOrientation) {
            androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_90 -> 90
            androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_180 -> 180
            androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }

    fun rotate(bitmap: Bitmap?, degrees: Int): Bitmap? {
        var rotateBitmap = bitmap
        if (degrees != 0 && rotateBitmap != null) {
            val m = Matrix()
            m.setRotate(
                degrees.toFloat(),
                rotateBitmap.width.toFloat() / 2,
                rotateBitmap.height.toFloat() / 2
            )

            try {
                val converted = Bitmap.createBitmap(
                    rotateBitmap, 0, 0,
                    rotateBitmap.width, rotateBitmap.height, m, true
                )
                if (rotateBitmap != converted) {
                    rotateBitmap.recycle()
                    rotateBitmap = converted
                }
            } catch (ex: OutOfMemoryError) {

            }
        }
        return rotateBitmap
    }

    fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(
                contentUri,
                arrayOf(MediaStore.Images.Media.DATA),
                null,
                null,
                null
            )
            val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(columnIndex)
        } finally {
            cursor?.close()
        }
    }

}