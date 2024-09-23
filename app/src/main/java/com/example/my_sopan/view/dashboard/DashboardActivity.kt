package com.example.my_sopan.view.dashboard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.my_sopan.databinding.ActivityDashboardBinding // Pastikan kelas binding ini diimpor
import com.example.my_sopan.utils.rotateFile
import com.example.my_sopan.utils.uriToFile
import com.example.my_sopan.view.camera.CameraActivity
import java.io.File

class DashboardActivity : AppCompatActivity() {

    private lateinit var grp_InsertImage : LinearLayout
    private lateinit var grp_Process : LinearLayout
    private lateinit var grp_Hasil : LinearLayout
    private lateinit var btn_mdlDenseNet : Button
    private lateinit var btn_mdlEfficientNet : Button
    private lateinit var btn_mdlMobileNet : Button
    private lateinit var btn_Batal : Button
    private lateinit var btn_Kembali : Button

    private var _binding: ActivityDashboardBinding? = null
    private val binding get() = _binding!!
    private var getFile: File? = null

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                this.let {
                    Toast.makeText(
                        it,
                        "Tidak mendapatkan permission.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                this.finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Menginisialisasi View Binding
        _binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        grp_Process = binding.groupProses
        grp_Hasil = binding.groupHasil
        grp_InsertImage = binding.groupButton
        btn_Batal = binding.btnBatal
        btn_Kembali = binding.btnKembali
        btn_mdlDenseNet = binding.btnModelDenseNet
        btn_mdlEfficientNet = binding.btnModelEfficientNet
        btn_mdlMobileNet = binding.btnModelMobileNet

        grp_Process.visibility = View.GONE
        grp_Hasil.visibility = View.GONE

        this.let {
            if (!allPermissionsGranted()) {
                ActivityCompat.requestPermissions(
                    it,
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            }
        }

        btn_Kembali.setOnClickListener {
            grp_Hasil.visibility = View.GONE
            grp_InsertImage.visibility = View.VISIBLE
        }

        btn_Batal.setOnClickListener{
            grp_Process.visibility = View.GONE
            grp_Hasil.visibility = View.GONE
            grp_InsertImage.visibility = View.VISIBLE

        }

        btn_mdlDenseNet.setOnClickListener{
            grp_Process.visibility = View.GONE
            grp_Hasil.visibility = View.VISIBLE

        }

        btn_mdlMobileNet.setOnClickListener{
            grp_Process.visibility = View.GONE
            grp_Hasil.visibility = View.VISIBLE

        }

        btn_mdlEfficientNet.setOnClickListener{
            grp_Process.visibility = View.GONE
            grp_Hasil.visibility = View.VISIBLE
        }

        binding.btnCamera.setOnClickListener {
            startCameraX()
        }

        binding.btnGallery.setOnClickListener {
            startGallery()
        }
//        binding.btnUpload.setOnClickListener { uploadImage() }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri

            selectedImg.let { uri ->
                this.let {
                    val myFile = uriToFile(uri, it)
                    getFile = myFile
                    binding.previewImageView.setImageURI(uri)
                    grp_InsertImage.visibility = View.GONE
                    grp_Process.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun startCameraX() {
        this.let {
            val intent = Intent(it, CameraActivity::class.java)
            launcherIntentCameraX.launch(intent)
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File

            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            myFile?.let { file ->
                rotateFile(file, isBackCamera)
                getFile = file
                binding.previewImageView.setImageBitmap(BitmapFactory.decodeFile(file.path))
                grp_InsertImage.visibility = View.GONE
                grp_Process.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null // Penting untuk menghindari memory leaks
    }
}
