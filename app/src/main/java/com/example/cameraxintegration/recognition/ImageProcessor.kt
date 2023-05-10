package com.example.cameraxintegration.recognition
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.graphics.Rect
import android.util.Base64
import com.example.cameraxintegration.repo.local.AppDb
import com.google.android.gms.vision.face.Landmark
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt


object ImageProcessor {

    /* It sets the classification mode to classify all possible facial features,
    landmark mode to detect all possible facial landmarks, and the minimum face size to be detected as 15% of the screen width.
    Once initialized, the face detector can be used to detect faces in images or live camera feeds, and returns a list of detected faces.*/
    private val faceDetector = FaceDetection.getClient(FaceDetectorOptions.Builder()
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setMinFaceSize(0.15f)
        .build())


    suspend fun compareImages(db: AppDb, clickedImage: Bitmap): List<Pair<Int, Float>> {
        val images = db.userImageDao().getAllImages()
        val results = mutableListOf<Pair<Int, Float>>()

        // Loop through the list of images in the database and compare them to the clicked image//will take time need an alternative
        for (image in images) {
            // Convert the database image from base64 to Bitmap
            //data is the Base64-encoded data as a string
            val decodedBytes = Base64.decode(image.data, Base64.DEFAULT)
            val dbImageBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

            // Detect the faces in both images
            val dbFaces = faceDetector.process(dbImageBitmap)
            val clickedFaces = faceDetector.process(clickedImage)

            // Extract the facial features from the faces
            //if i store them at the initial time of capture i dont need this , processing will be faster
            val dbFeatures = dbFaces.mapNotNull { face:Face ->
                face.boundingBox?.let { box ->
                    val landmarks = face.allLandmarks.mapNotNull { landmark ->
                        when (landmark.landmarkType) {
                            Landmark.NOSE_BASE -> landmark.position
                            Landmark.LEFT_MOUTH -> landmark.position
                            Landmark.RIGHT_MOUTH -> landmark.position
                            Landmark.LEFT_EYE -> landmark.position
                            Landmark.RIGHT_EYE -> landmark.position
                            Landmark.LEFT_EAR -> landmark.position
                            Landmark.RIGHT_EAR -> landmark.position
                            else -> null
                        }
                    }
                    FacialFeatures(box, landmarks)
                }
            }
//clicked image feature calculation
            val clickedFeatures = clickedFaces.mapNotNull { face:Face ->
                face.boundingBox?.let { box ->
                    val landmarks = face.allLandmarks.mapNotNull { landmark ->
                        when (landmark.landmarkType) {
                            Landmark.NOSE_BASE -> landmark.position
                            Landmark.LEFT_MOUTH -> landmark.position
                            Landmark.RIGHT_MOUTH-> landmark.position
                            Landmark.LEFT_EYE -> landmark.position
                            Landmark.RIGHT_EYE -> landmark.position
                            Landmark.LEFT_EAR -> landmark.position
                            Landmark.RIGHT_EAR -> landmark.position
                            else -> null
                        }
                    }
                    FacialFeatures(box, landmarks)
                }
            }

            // Compare the facial features using a similarity score
            val similarityScore = compareFacialFeatures(dbFeatures, clickedFeatures)

            // Add the result to the list of results
            results.add(Pair(image.id, similarityScore))
        }

        return results
    }

//function to compare facial features
    private fun compareFacialFeatures(features1: List<FacialFeatures>, features2: List<FacialFeatures>): Float {
        // Ensure that both feature lists have the same length
        require(features1.size == features2.size) { "Feature lists must have the same length" }

        // Calculate the dot product of the two feature vectors
        var dotProduct = 0.0
        for (i in features1.indices) {
            dotProduct += features1[i].value * features2[i].value
        }

        // Calculate the magnitudes of each feature vector
        var mag1 = 0.0
        var mag2 = 0.0
        for (feature in features1) {
            mag1 += feature.value * feature.value
        }
        for (feature in features2) {
            mag2 += feature.value * feature.value
        }
        mag1 = sqrt(mag1)
        mag2 = sqrt(mag2)

        // Calculate the cosine similarity between the two feature vectors
        val similarity = dotProduct / (mag1 * mag2)

        // Normalize the similarity score to a value between 0 and 1
        return max(0.0f, min(similarity.toFloat(), 1.0f))
    }


    data class FacialFeatures(val box: Rect, val landmarks: List<PointF>)
}
