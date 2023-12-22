package com.dicoding.picodiploma.loginwithanimation.view.ui.upload

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.databinding.FragmentUploadBinding
import com.dicoding.picodiploma.loginwithanimation.tensor.FaceEmotionClassifier
import com.dicoding.picodiploma.loginwithanimation.utils.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.utils.getImageUri
import com.dicoding.picodiploma.loginwithanimation.utils.reduceFileImage
import com.dicoding.picodiploma.loginwithanimation.utils.uriToFile
import com.dicoding.picodiploma.loginwithanimation.view.MainActivity
import com.dicoding.picodiploma.loginwithanimation.view.customview.EditQuestion
import com.dicoding.picodiploma.loginwithanimation.view.ui.home.HomeFragment

class UploadFragment : Fragment() {
    private var currentImageUri: Uri? = null
    private lateinit var binding: FragmentUploadBinding
    private lateinit var faceEmotionClassifier : FaceEmotionClassifier

    private val viewModel by viewModels<UploadViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUploadBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }


        binding.btncamera.setOnClickListener { startCamera() }
        binding.btnupload.setOnClickListener {
//            Stressscore()
            analyze()
        }
        faceEmotionClassifier = FaceEmotionClassifier(requireContext())

        requireActivity().actionBar?.hide()
    }



    private fun analyze() {
        val progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, requireContext()).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val imgpath = imageFile.path
            val bitmap = BitmapFactory.decodeFile(imgpath)
            val emotion = faceEmotionClassifier.classifyEmotion(bitmap)

            val quest1 = binding.ansQuest1.text.toString()
            val quest2 = binding.ansQuest2.text.toString()
            val quest3 = binding.ansQuest3.text.toString()
            val quest4 = binding.ansQuest4.text.toString()
            val quest5 = binding.ansQuest5.text.toString()
            val quest6 = binding.ansQuest6.text.toString()
            val quest7 = binding.ansQuest7.text.toString()
            val quest8 = binding.ansQuest8.text.toString()
            val quest9 = binding.ansQuest9.text.toString()
            val quest10 = binding.ansQuest10.text.toString()

            if (quest1.isEmpty() || quest2.isEmpty() || quest3.isEmpty() || quest4.isEmpty() || quest5.isEmpty() ||
                quest6.isEmpty() || quest7.isEmpty() || quest8.isEmpty() || quest9.isEmpty() || quest10.isEmpty()
            ) {
                progressBar.visibility = View.GONE
                // Display a Toast message indicating that all questions must be filled
                Toast.makeText(requireContext(), "Semua pertanyaan harus diisi", Toast.LENGTH_SHORT).show()
                return
            }

            // Convert non-empty strings to integers
            val intQuest1 = quest1.toIntOrNull() ?: 0
            val intQuest2 = quest2.toIntOrNull() ?: 0
            val intQuest3 = quest3.toIntOrNull() ?: 0
            val intQuest4 = quest4.toIntOrNull() ?: 0
            val intQuest5 = quest5.toIntOrNull() ?: 0
            val intQuest6 = quest6.toIntOrNull() ?: 0
            val intQuest7 = quest7.toIntOrNull() ?: 0
            val intQuest8 = quest8.toIntOrNull() ?: 0
            val intQuest9 = quest9.toIntOrNull() ?: 0
            val intQuest10 = quest10.toIntOrNull() ?: 0

            val totalScore = intQuest1 + intQuest2 + intQuest3 + intQuest4 + intQuest5 +
                    intQuest6 + intQuest7 + intQuest8 + intQuest9 + intQuest10

            val stressLevel = when (totalScore) {
                in 0..13 -> "Normal"
                in 14..26 -> "Menengah"
                in 27..40 -> "Tinggi"
                else -> "Tidak dapat menentukan tingkat stres"
            }
            val message = "Facial Emotion : $emotion \nTingkat Stres: $stressLevel\nLanjutkan untuk menambahkan data ke history"
            val message2 = "Data berhasil ditambahkan ke history"

            progressBar.visibility = View.GONE

            AlertDialog.Builder(requireContext()).apply {
                setTitle("Analyze")
                setMessage(message)
                setPositiveButton("Lanjut") { _, _ ->
                    AlertDialog.Builder(requireContext()).apply {
                        setTitle("History")
                        setMessage(message2)
                        setPositiveButton("Lanjut") { _, _ ->
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            startActivity(intent)
                        }
                        create()
                        show()
                    }
                }
                create()
                show()
            }
        }




//            viewModel.getSession().observe(viewLifecycleOwner) {
//                viewModel.uploaddata(imageFile)
//                viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
//                    if (isLoading) {
//                        binding.progressBar.visibility = View.VISIBLE
//                    } else {
//                        binding.progressBar.visibility = View.GONE
//                    }
//                })
//                viewModel.uploadResult.observe(viewLifecycleOwner, Observer { success ->
//                    if (success) {
//                        val intent = Intent(requireContext(), HomeFragment::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                        startActivity(intent)
//                        requireActivity().finish()
//                        showToast("Berhasil mengupload")
//                    } else {
//                        showToast("Gagal mengupload")
//                    }
//                })
//            }
//        } ?: showToast(getString(R.string.empty_image_warning))

    }



    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewimg.setImageURI(it)
        }
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

}