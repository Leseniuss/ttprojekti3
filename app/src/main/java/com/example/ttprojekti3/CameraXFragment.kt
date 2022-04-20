package com.example.ttprojekti3

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_camera_x.*
//import org.opencv.android.OpenCVLoader
//import org.opencv.android.Utils
//import org.opencv.core.Mat
import java.io.File
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// import android.content.ContentResolver

 typealias CornersListener = () -> Unit

typealias LumaListener = (luma: Double) -> Unit
typealias LumaListener2 = (luma2: Double) -> Unit


class CameraXFragment : Fragment() {

    // private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    // private var imageAnalyzer: ImageAnalysis? = null
    // private var imageAnalyzer2: ImageAnalysis? = null
    // private var camera: Camera? = null
    //private val lumaperkele = LumaListener2 = (luma2: Double) -> Unit




    private lateinit var safeContext: Context

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
   // private lateinit var cameraExecutor2: ExecutorService

    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
    }

    // commit change

    private fun getStatusBarHeight(): Int {
        val resourceId =
            safeContext.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            safeContext.resources.getDimensionPixelSize(resourceId)
        } else 0
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera_x, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
           // startCamera2()

        } else {
            startCamera()
           // startCamera2()

            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS

            )
        }



        // Setup the listener for take photo button
        image_capture_button.setOnClickListener { takePhoto() }

        // outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()
       // cameraExecutor2 = Executors.newSingleThreadExecutor()
//        cameraExecutor = Executors.newCachedThreadPool()
    }

   /* private fun startCamera2() {
       /* val imageAnalyzer2 = ImageAnalysis.Builder()
            .build()
            .also {
                it.setAnalyzer(cameraExecutor, LuminosityAnalyzer2 { luma2 ->
                    Log.d("Perkele22222", "Average luminosity: $luma2")
                    //  val lumaperkele = luma
                })
            } */

        imageAnalyzer2 = LuminosityAnalyzer2 { luma2 ->
                    Log.d("Perkele22222", "Average luminosity: $luma2")
                    //  val lumaperkele = luma
                })

    } */


    private fun startCamera() {
        /* // OpenCVLoader.initDebug()
          val cameraProviderFuture = ProcessCameraProvider.getInstance(safeContext)

          cameraProviderFuture.addListener({
              // Used to bind the lifecycle of cameras to the lifecycle owner
              val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()


              preview = Preview.Builder().build()

              imageCapture = ImageCapture.Builder().build()

              imageAnalyzer = ImageAnalysis.Builder().build().apply {
                  setAnalyzer(Executors.newSingleThreadExecutor(), CornerAnalyzer {
                      val bitmap = viewFinder.bitmap
                      val img = Mat()
                      Utils.bitmapToMat(bitmap, img)
                      bitmap?.recycle()

                     // val corner = processPicture(img)
                      // Do image analysis here if you need bitmap
                  })
              }
              // Select back camera
              val cameraSelector =
                  CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

              try {
                  // Unbind use cases before rebinding
                  cameraProvider.unbindAll()

                  // Bind use cases to camera
                  camera = cameraProvider.bindToLifecycle(
                      this,
                      cameraSelector,
                     // imageAnalyzer,
                      preview,
                      imageCapture
                  )

                  preview?.setSurfaceProvider(viewFinder.surfaceProvider)
              } catch (exc: Exception) {
                  Log.e(TAG, "Use case binding failed", exc)
              }

          }, ContextCompat.getMainExecutor(safeContext)) */

        val cameraProviderFuture = ProcessCameraProvider.getInstance(safeContext)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .build()

            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, LuminosityAnalyzer { luma ->
                       // val lumatag: Double = luma
                        Log.i(TAG, "Average luminosity: $luma")
                       // Log.d("PERKELE","Average luminosityyyyyyy: $lumaperkele")

                       // if (lumatag < 100) { takePhoto() }
                    })
                }/*.also {
                    it.setAnalyzer(cameraExecutor,LuminosityAnalyzer2 { luma ->
                        Log.d("PERKELE","Average luminosityyyyyyy: $luma")
                        Log.i(TAG, "Average luminosity: $luma")

                    })
                } */



           /* val imageAnalyzer2 = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, LuminosityAnalyzer2 { luma2 ->
                        Log.d("Perkele22222", "Average luminosity: $luma2")
                        //  val lumaperkele = luma
                    })
                } */



            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()


                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalyzer//, imageAnalyzer2
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(safeContext))


    }



    private fun takePhoto() {
        /*  // Get a stable reference of the modifiable image capture use case
          val imageCapture = imageCapture ?: return

          // Create timestamped output file to hold the image
          val photoFile = File(
              outputDirectory,
              SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg"
          )

          // Create output options object which contains file + metadata
          val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

          // Setup image capture listener which is triggered after photo has
          // been taken
          imageCapture.takePicture(
              outputOptions,
              ContextCompat.getMainExecutor(safeContext),
              object : ImageCapture.OnImageSavedCallback {
                  override fun onError(exc: ImageCaptureException) {
                      Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                  }

                  override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                      val savedUri = Uri.fromFile(photoFile)
                      val msg = "Photo capture succeeded: $savedUri"
                      Toast.makeText(safeContext, msg, Toast.LENGTH_SHORT).show()
                      Log.d(TAG, msg)
                  }
              }) */

        // Get a stable reference of the modifiable image capture use case

        val lumaperkele = LuminosityAnalyzer2 { luma ->
            Log.d("PERKELE","Average luminosityyyyyyy: $luma")
            Log.i(TAG, "Average luminosity: $luma")

        }

        val contentResolver: ContentResolver = requireActivity().contentResolver


        val imageCapture = imageCapture ?: return

        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/ttprojekti2-Image")
            }
        }

        // Create output options object which contains file + metadata


        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(safeContext),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun
                        onImageSaved(output: ImageCapture.OutputFileResults){
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    Toast.makeText(safeContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                }
            }
        )

    }

    override fun onPause() {
        super.onPause()
        isOffline = true
    }

    override fun onResume() {
        super.onResume()
        isOffline = false
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(safeContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    safeContext,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                //  finish()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun getOutputDirectory(): File {
        val mediaDir = activity?.externalMediaDirs?.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else activity?.filesDir!!
    }


    companion object {
        val TAG = "CameraXFragment"
        private const val FILENAME_FORMAT = "dd-MM-yyyy-mm-ss-SSS" // "yyyy-MM-dd-HH-mm-ss-SSS"
        internal const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        var isOffline = false // prevent app crash when goes offline
    }

    private class CornerAnalyzer(private val listener: CornersListener) : ImageAnalysis.Analyzer {

        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // Rewind the buffer to zero
            val data = ByteArray(remaining())
            get(data)   // Copy the buffer into a byte array
            return data // Return the byte array
        }

        @SuppressLint("UnsafeExperimentalUsageError")
        override fun analyze(imageProxy: ImageProxy) {
            if (!isOffline) {
                listener()
            }
            imageProxy.close() // important! if it is not closed it will only run once
        }

    }
}