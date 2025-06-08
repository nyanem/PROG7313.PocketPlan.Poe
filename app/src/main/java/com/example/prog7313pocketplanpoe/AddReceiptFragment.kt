//package com.example.prog7313pocketplanpoe
//
//
//
//import android.app.Activity
//import android.content.Intent
//import android.graphics.Bitmap
//import android.graphics.drawable.BitmapDrawable
//import android.os.Bundle
//import android.provider.MediaStore
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.ImageView
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.fragment.app.Fragment
//import java.io.ByteArrayOutputStream
//
//class AddReceiptFragment : Fragment() {
//
//    private lateinit var imageView: ImageView
//    private lateinit var takePhotoButton: Button
//    private lateinit var uploadButton: Button
//
//    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//        uri?.let {
//            imageView.setImageURI(uri)
//        } ?: showToast("No image selected")
//    }
//
//    private val takePhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            val photo = result.data?.extras?.get("data") as? Bitmap
//            photo?.let {
//                imageView.setImageBitmap(it)
//            } ?: showToast("No photo captured")
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        return inflater.inflate(R.layout.fragment_add_receipt, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        imageView = view.findViewById(R.id.imageViewReceipt)
//        uploadButton = view.findViewById(R.id.uploadButton)
//        takePhotoButton = view.findViewById(R.id.btnOpenCamera)
//
//        takePhotoButton.setOnClickListener {
//            takePhoto()
//        }
//
//
//        uploadButton.setOnClickListener {
//            val bitmap = (imageView.drawable as? BitmapDrawable)?.bitmap
//            if (bitmap != null) {
//                val imageBytes = getBitmapAsByteArray(bitmap)
//                val dbHelper = PocketPlanDBHelper(requireContext())
//                val id = dbHelper.insertImage(imageBytes)
//                if (id != -1L) {
//                    showToast("Image saved with ID: $id")
//                } else {
//                    showToast("Failed to save image")
//                }
//            } else {
//                showToast("No image to upload")
//            }
//        }
//    }
//
//    private fun takePhoto() {
//        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        try {
//            takePhotoLauncher.launch(takePictureIntent)
//        } catch (e: Exception) {
//            showToast("Camera not available")
//        }
//    }
//
//    private fun showToast(message: String) {
//        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
//    }
//
//    private fun getBitmapAsByteArray(bitmap: Bitmap): ByteArray {
//        val outputStream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
//        return outputStream.toByteArray()
//    }
//}

//Default code - do not use
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//
//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
//
//
//class AddReceiptFragment : Fragment() {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_add_receipt, container, false)
//    }
//}