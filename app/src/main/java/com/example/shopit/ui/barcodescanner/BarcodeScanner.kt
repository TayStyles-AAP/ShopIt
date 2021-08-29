package com.example.shopit.ui.barcodescanner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.shopit.R
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import kotlinx.android.synthetic.main.code_scanner.*

class BarcodeScanner : Fragment() {

    lateinit var codeScanner: CodeScanner
    lateinit var barcodeOutput: TextView

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            codeScanner()
        } else {
            Snackbar.make(requireView(), "Scanner disabled. Enable permission", Snackbar.LENGTH_LONG)
                .setAction("Settings") {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.parse("package:" + requireContext().applicationInfo.packageName)
                    startActivity(intent)
                }.show()

            activity?.onBackPressed()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.code_scanner, container, false)
        Log.d(TAG, "=== onCreateView ===")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        codeScanner = CodeScanner(requireContext(), scannerView)
        barcodeOutput = view.findViewById(R.id.code_scanner_barcode_output)

        if (checkPermission()) {
            codeScanner()
        } else {
            requestPermission()
        }
    }


    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) -> {
                codeScanner()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun codeScanner() {

        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = listOf(BarcodeFormat.EAN_8, BarcodeFormat.EAN_13)

        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.CONTINUOUS
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        codeScanner.decodeCallback = DecodeCallback {
            activity?.runOnUiThread {
                // What happens when successful scan
                barcodeOutput.text = it.text
            }
        }

        codeScanner.errorCallback = ErrorCallback {
            activity?.runOnUiThread {
                Log.e("Main", "Camera initialization error: ${it.message}")
            }
        }

    codeScanner.startPreview()

    }

    override fun onResume() {
        super.onResume()
//        codeScanner.startPreview()
    }

    override fun onPause() {
        super.onPause()
        codeScanner.releaseResources()
    }

    companion object{
        private const val CAMERA_REQUEST_CODE = 101
        private const val TAG = "ShopIt-Barcode Scanner"
    }
}




