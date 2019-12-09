package com.example.duyustory.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore

class AddPictureUtil {
    fun getImageInGallery(): Intent {
        val galleryIntent = Intent()
        galleryIntent.type = "image/*"
        galleryIntent.action = Intent.ACTION_GET_CONTENT

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

    fun getRealPathFromURI(context: Context, contentUri: Uri): String? {
        if (contentUri.path.startsWith("/storage")) {
            return contentUri.path
        }
        val id = DocumentsContract.getDocumentId(contentUri).split(":")[1]
        val columns = arrayOf(MediaStore.Files.FileColumns.DATA)
        val selection = "${MediaStore.Files.FileColumns._ID} = $id"
        val cursor = context.contentResolver.query(
            MediaStore.Files.getContentUri("external"),
            columns,
            selection,
            null,
            null
        )

        try {
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex(columns[0]) )
            }
        } finally {
            cursor.close()
        }
        return null
    }

}