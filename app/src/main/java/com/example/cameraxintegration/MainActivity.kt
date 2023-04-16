    package com.example.cameraxintegration

    import android.Manifest
    import android.content.pm.PackageManager
    import android.os.Bundle
    import android.widget.Toast
    import androidx.appcompat.app.AppCompatActivity
    import androidx.camera.core.CameraSelector
    import androidx.camera.core.ImageAnalysis
    import androidx.camera.core.ImageCapture
    import androidx.camera.core.ImageCaptureException
    import androidx.camera.core.Preview
    import androidx.camera.lifecycle.ProcessCameraProvider
    import androidx.core.app.ActivityCompat
    import androidx.core.content.ContextCompat
    import androidx.lifecycle.LifecycleOwner
    import com.google.mlkit.vision.common.InputImage
    import androidx.camera.view.PreviewView
    import com.google.mlkit.vision.face.FaceDetection
    import com.google.mlkit.vision.face.FaceDetector
    import com.google.mlkit.vision.face.FaceDetectorOptions
    import java.io.File
    import java.util.concurrent.ExecutorService
    import java.util.concurrent.Executors



    @androidx.camera.core.ExperimentalGetImage
    class MainActivity : AppCompatActivity() {

        private var preview: Preview? = null
        private var imageCapture: ImageCapture? = null
        private var imageAnalyzer: ImageAnalysis? = null
        private var camera: androidx.camera.core.Camera? = null
        private lateinit var cameraExecutor: ExecutorService
        private lateinit var detector: FaceDetector
        private lateinit var viewFinder: PreviewView
        private var isBlinking = false

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            viewFinder = findViewById(R.id.viewFinder)


            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    PERMISSIONS_REQUEST_CAMERA
                )
            } else {
                startCamera()
            }


            val options = FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .setMinFaceSize(0.15f)
                .enableTracking()
                .build()
            detector = FaceDetection.getClient(options)

            cameraExecutor = Executors.newSingleThreadExecutor()
        }

        private fun startCamera() {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                preview = Preview.Builder()
                    .build()

                imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build()

                imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor) { image ->
                            if (isBlinking) {
                                image.close()
                                return@setAnalyzer
                            }

                            val inputImage = InputImage.fromMediaImage(
                                image.image!!, image.imageInfo.rotationDegrees
                            )

                            detector.process(inputImage)
                                .addOnSuccessListener { faces ->
                                    for (face in faces) {
                                        val leftEyeOpenProbability = face.leftEyeOpenProbability
                                        val rightEyeOpenProbability = face.rightEyeOpenProbability
                                        if ((leftEyeOpenProbability != null) && (rightEyeOpenProbability != null) &&
                                            (leftEyeOpenProbability < 0.2f) && (rightEyeOpenProbability < 0.2f)
                                        ) {
                                            captureImage()
                                            isBlinking = true
                                            break
                                        }
                                    }
                                    image.close()
                                }
                                .addOnFailureListener { exception ->
                                    image.close()
                                    Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
                                    isBlinking = false
                                    startCamera()
                                    return@addOnFailureListener
                                }
                        }
                    }

                val cameraSelector =
                    CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build()

                try {
                    cameraProvider.unbindAll()

                    camera = cameraProvider.bindToLifecycle(
                        this as LifecycleOwner, cameraSelector, preview, imageCapture, imageAnalyzer
                    )

                    preview?.setSurfaceProvider(viewFinder.surfaceProvider)
                } catch (e: Exception) {
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }, ContextCompat.getMainExecutor(this))
        }

        private fun captureImage() {
            val imageCapture = imageCapture ?: return

            val photoFile = File(
                externalMediaDirs.firstOrNull(),
                "${System.currentTimeMillis()}.jpg"
            )

            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

            imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(this),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        Toast.makeText(
                            this@MainActivity,
                            "Error capturing image: ${exc.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        isBlinking = false
                        startCamera()
                    }

                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        Toast.makeText(
                            this@MainActivity,
                            "Image captured successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        isBlinking = false
                        startCamera()
                    }
                })
        }

        override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>, grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == PERMISSIONS_REQUEST_CAMERA) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCamera()
                } else {
                    Toast.makeText(
                        this,
                        "Camera permission is required to use the camera",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        override fun onDestroy() {
            super.onDestroy()
            cameraExecutor.shutdown()
            detector.close()
        }

        companion object {
            private const val PERMISSIONS_REQUEST_CAMERA = 100
        }
    }