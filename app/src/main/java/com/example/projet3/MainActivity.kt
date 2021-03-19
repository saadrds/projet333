package com.example.projet3

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


class MainActivity : AppCompatActivity() {
    val RESULT_LOAD_IMAGE = 22
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val imageView = findViewById<ImageView>(R.id.imageView2)
        imageView.visibility = View.VISIBLE
       val  icoGallery = findViewById<Button>(R.id.image)
        icoGallery.setOnClickListener{
                val galleryIntent = Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                galleryIntent.type = "image/*"
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE)
            }
        val save = findViewById<Button>(R.id.save)
        save.setOnClickListener{
            // Save the image in internal storage and get the uri
            imageNamePath = saveImageToInternalStorage(bitmap)

            Toast.makeText(this, "image Saved !", Toast.LENGTH_SHORT).show()
        }

        val afficher = findViewById<Button>(R.id.afficher)
        afficher.setOnClickListener{
            var uri3 = Uri.parse(imageNamePath)
            val image3 = findViewById<ImageView>(R.id.imageView3)
            image3.setImageURI(uri3)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            val uri: Uri = data.data!!
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            // Log.d(TAG, String.valueOf(bitmap));

            // Log.d(TAG, String.valueOf(bitmap));
            val imageView = findViewById<ImageView>(R.id.imageView2)
            imageView.setImageURI(uri)
            //imageView.setImageBitmap(bitmap)
            imageView.visibility = View.VISIBLE
        } else {
            Toast.makeText(this, "You have not selected and image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImageToInternalStorage(bitmap1 :Bitmap): String {
        // Get the image from drawable resource as drawable object

        // Get the bitmap from drawable object

        // Get the context wrapper instance
        val wrapper = ContextWrapper(applicationContext)

        // Initializing a new file
        // The bellow line return a directory in internal storage
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)


        // Create a file to save the image
        val fileName ="${UUID.randomUUID()}.jpg"
        file = File(file, fileName)

        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress bitmap
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the stream
            stream.flush()

            // Close stream
            stream.close()
        } catch (e: IOException){ // Catch the exception
            e.printStackTrace()
        }

        // Return the saved image uri
        return file.absolutePath
    }
    lateinit var bitmap:Bitmap
    lateinit var imageNamePath:String
}