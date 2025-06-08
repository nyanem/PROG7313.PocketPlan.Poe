package com.example.prog7313pocketplanpoe

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddReceiptFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var takePhotoButton: Button
    private lateinit var uploadButton: Button

    private var photoUri: Uri? = null
    private var photoFile: File? = null

    private val takePhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            photoUri?.let {
                imageView.setImageURI(it)
                showToast("Photo captured and saved to: ${photoFile?.absolutePath}")
            }
        } else {
            showToast("Photo capture cancelled")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_receipt, container, false)

        imageView = view.findViewById(R.id.imageViewReceipt)
        takePhotoButton = view.findViewById(R.id.btnOpenCamera)
        uploadButton = view.findViewById(R.id.uploadButton)

        takePhotoButton.setOnClickListener {
            takePhoto()
        }

        uploadButton.setOnClickListener {
            if (photoFile != null && photoFile!!.exists()) {
                showToast("Image saved at: ${photoFile!!.absolutePath}")
                // You can send this path to ViewModel or another Fragment as needed
            } else {
                showToast("No image to save")
            }
        }

        return view
    }

    private fun takePhoto() {
        photoFile = createImageFile()
        photoFile?.let {
            photoUri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.provider",
                it
            )
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            }
            takePhotoLauncher.launch(intent)
        } ?: showToast("Could not create file")
    }

    private fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "Receipt_$timeStamp.jpg"
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return try {
            File(storageDir, fileName)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
