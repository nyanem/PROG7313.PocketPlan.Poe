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
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddReceiptFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var takePhotoButton: Button
    private lateinit var uploadButton: Button
    private lateinit var backButton: ImageButton

    private var photoUri: Uri? = null
    private var photoFile: File? = null
    private lateinit var uploadProgress: View


    private val storage = FirebaseStorage.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

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
        uploadProgress = view.findViewById(R.id.uploadProgress)


        takePhotoButton.setOnClickListener {
            takePhoto()
        }

        uploadButton.setOnClickListener {
            if (photoFile != null && photoFile!!.exists()) {
                uploadPhotoToFirebase(photoFile!!)
            } else {
                showToast("No image to upload")
            }
        }

        return view
    }

//    private fun takePhoto() {
//        photoFile = createImageFile()
//        photoFile?.let {
//            photoUri = FileProvider.getUriForFile(
//                requireContext(),
//                "${requireContext().packageName}.provider",
//                it
//            )
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
//                putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
//            }
//            takePhotoLauncher.launch(intent)
//        } ?: showToast("Could not create file")
//    }
private fun takePhoto() {
    val context = requireContext()
    val photoFile = createImageFile() ?: run {
        showToast("Failed to create image file")
        return
    }

    this.photoFile = photoFile
    photoUri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        photoFile
    )

    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
        putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    if (cameraIntent.resolveActivity(context.packageManager) != null) {
        takePhotoLauncher.launch(cameraIntent)
    } else {
        showToast("Camera app not available")
    }
}


//    private fun createImageFile(): File? {
//        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
//        val fileName = "Receipt_$timeStamp.jpg"
//        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        return try {
//            File(storageDir, fileName)
//        } catch (e: IOException) {
//            e.printStackTrace()
//            null
//        }
//    }

    private fun createImageFile(): File? {
        return try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            File.createTempFile("Receipt_$timeStamp", ".jpg", storageDir)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }


    private fun uploadPhotoToFirebase(file: File) {
        uploadProgress.visibility = View.VISIBLE

        val fileUri = Uri.fromFile(file)
        val storageRef = storage.reference.child("receipts/${file.name}")

        storageRef.putFile(fileUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    uploadProgress.visibility = View.GONE
                    saveTransactionWithReceiptUrl(uri.toString())
                }
            }
            .addOnFailureListener {
                uploadProgress.visibility = View.GONE
                showToast("Upload failed: ${it.message}")
            }
    }


    private fun saveTransactionWithReceiptUrl(receiptUrl: String) {
        val transactionData = hashMapOf(
            "amount" to 0, // Replace with actual amount if available
            "category" to "Uncategorized",
            "date" to SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
            "receiptUrl" to receiptUrl
        )

        firestore.collection("transactionsWithReceipt")
            .add(transactionData)
            .addOnSuccessListener {
                showToast("Transaction saved with receipt!")
            }
            .addOnFailureListener {
                showToast("Failed to save transaction: ${it.message}")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


}




//package com.example.prog7313pocketplanpoe
//
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.os.Environment
//import android.provider.MediaStore
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.ImageView
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.core.content.FileProvider
//import androidx.fragment.app.Fragment
//import java.io.File
//import java.io.IOException
//import java.text.SimpleDateFormat
//import java.util.*
//
//class AddReceiptFragment : Fragment() {
//
//    private lateinit var imageView: ImageView
//    private lateinit var takePhotoButton: Button
//    private lateinit var uploadButton: Button
//
//    private var photoUri: Uri? = null
//    private var photoFile: File? = null
//
//    private val takePhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == android.app.Activity.RESULT_OK) {
//            photoUri?.let {
//                imageView.setImageURI(it)
//                showToast("Photo captured and saved to: ${photoFile?.absolutePath}")
//            }
//        } else {
//            showToast("Photo capture cancelled")
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_add_receipt, container, false)
//
//        imageView = view.findViewById(R.id.imageViewReceipt)
//        takePhotoButton = view.findViewById(R.id.btnOpenCamera)
//        uploadButton = view.findViewById(R.id.uploadButton)
//
//        takePhotoButton.setOnClickListener {
//            takePhoto()
//        }
//
//        uploadButton.setOnClickListener {
//            if (photoFile != null && photoFile!!.exists()) {
//                showToast("Image saved at: ${photoFile!!.absolutePath}")
//                // You can send this path to ViewModel or another Fragment as needed
//            } else {
//                showToast("No image to save")
//            }
//        }
//
//        return view
//    }
//
//    private fun takePhoto() {
//        photoFile = createImageFile()
//        photoFile?.let {
//            photoUri = FileProvider.getUriForFile(
//                requireContext(),
//                "${requireContext().packageName}.provider",
//                it
//            )
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
//                putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
//            }
//            takePhotoLauncher.launch(intent)
//        } ?: showToast("Could not create file")
//    }
//
//    private fun createImageFile(): File? {
//        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
//        val fileName = "Receipt_$timeStamp.jpg"
//        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        return try {
//            File(storageDir, fileName)
//        } catch (e: IOException) {
//            e.printStackTrace()
//            null
//        }
//    }
//
//    private fun showToast(message: String) {
//        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
//    }
//}


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
