package com.example.cpawellnessapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.hardware.camera2.*
import android.media.*
import android.media.MediaMetadataRetriever.OPTION_CLOSEST
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Range
import android.view.MotionEvent
import android.view.Surface
import android.view.SurfaceHolder
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.webkit.MimeTypeMap
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.example.android.camera.utils.AutoFitSurfaceView
import com.example.android.camera.utils.OrientationLiveData
import com.example.android.camera.utils.getPreviewOutputSize
import com.example.cpawellnessapp.Diary.Diary
import com.example.cpawellnessapp.Diary.DiaryDbAdapter
import com.example.cpawellnessapp.diary_questions.PopulateDBFromGSON
import com.example.cpawellnessapp.diary_questions.Questions
import com.example.cpawellnessapp.diary_questions.QuestionsDbAdapter
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.android.synthetic.main.activity_diary_record.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import wseemann.media.FFmpegMediaMetadataRetriever
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class DiaryRecord : AppCompatActivity() {

    /** Detects, characterizes, and connects to a CameraDevice (used for all camera operations) */
    private val cameraManager: CameraManager by lazy {
        val context = applicationContext
        context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    /** [CameraCharacteristics] corresponding to the provided Camera ID */
    private val characteristics: CameraCharacteristics by lazy {
        cameraManager.getCameraCharacteristics(cameraId)
    }
    private var cameraId: String = "2" //Front Camera (FULL)
    private var fps: Int = 30
    private var width: Int = 4032
    private var height: Int = 2268
    private var orientation: Int = 270 //Properly orients the screen upright
    private var incrementer: Long = (1000000/fps).toLong()

    private var frameAnalysisCount: Int = 20

    val context = this

    /** Milliseconds used for UI animations */
    val ANIMATION_FAST_MILLIS = 50L
    val ANIMATION_SLOW_MILLIS = 100L
    private val IMMERSIVE_FLAG_TIMEOUT = 500L

    /** File where the recording will be saved */
    private val outputFile: File by lazy { createFile(this, "mp4") }

    /**
     * Setup a persistent [Surface] for the recorder so we can use it as an output target for the
     * camera session without preparing the recorder
     */
    private val recorderSurface: Surface by lazy {

        // Get a persistent Surface from MediaCodec, don't forget to release when done
        val surface = MediaCodec.createPersistentInputSurface()

        // Prepare and release a dummy MediaRecorder with our new surface
        // Required to allocate an appropriately sized buffer before passing the Surface as the
        //  output target to the capture session
        createRecorder(surface).apply {
            prepare()
            release()
        }

        surface
    }

    /** Saves the video recording */
    private val recorder: MediaRecorder by lazy { createRecorder(recorderSurface) }

    /** [HandlerThread] where all camera operations run */
    private val cameraThread = HandlerThread("CameraThread").apply { start() }

    /** [Handler] corresponding to [cameraThread] */
    private val cameraHandler = Handler(cameraThread.looper)

    /** Performs recording animation of flashing screen */
    private val animationTask: Runnable by lazy {
        Runnable {
            // Flash white animation
            //overlay.foreground = Color.argb(150, 255, 255, 255).toDrawable()
            // Wait for ANIMATION_FAST_MILLIS
            //overlay.postDelayed({
            // Remove white flash animation
            //overlay.foreground = null
            // Restart animation recursively
            //overlay.postDelayed(animationTask, CameraActivity.ANIMATION_FAST_MILLIS)
            //}, CameraActivity.ANIMATION_FAST_MILLIS)
        }
    }

    /** Where the camera preview is displayed */
    private lateinit var viewFinder: AutoFitSurfaceView

    /** Overlay on top of the camera preview */
    private lateinit var overlay: View

    /** Captures frames from a [CameraDevice] for our video recording */
    private lateinit var session: CameraCaptureSession

    /** The [CameraDevice] that will be opened in this fragment */
    private lateinit var camera: CameraDevice

    /** Requests used for preview only in the [CameraCaptureSession] */
    private val previewRequest: CaptureRequest by lazy {
        // Capture request holds references to target surfaces
        session.device.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW).apply {
            // Add the preview surface target
            addTarget(viewFinder.holder.surface)
        }.build()
    }

    /** Requests used for preview and recording in the [CameraCaptureSession] */
    private val recordRequest: CaptureRequest by lazy {
        // Capture request holds references to target surfaces
        session.device.createCaptureRequest(CameraDevice.TEMPLATE_RECORD).apply {
            // Add the preview and recording surface targets
            addTarget(viewFinder.holder.surface)
            addTarget(recorderSurface)
            // Sets user requested FPS for all targets
            set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, Range(fps, fps))
        }.build()
    }

    private var recordingStartMillis: Long = 0L

    /** Live data listener for changes in the device orientation relative to the camera */
    private lateinit var relativeOrientation: OrientationLiveData

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_record)
        overlay = findViewById(R.id.overlay)
        viewFinder = findViewById(R.id.view_finder)


        viewFinder.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceDestroyed(holder: SurfaceHolder) = Unit
            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int) = Unit

            override fun surfaceCreated(holder: SurfaceHolder) {

                // Selects appropriate preview size and configures view finder
                val previewSize = getPreviewOutputSize(
                    viewFinder.display, characteristics, SurfaceHolder::class.java)
                Log.d(TAG, "View finder size: ${viewFinder.width} x ${viewFinder.height}")
                Log.d(TAG, "Selected preview size: $previewSize")
                viewFinder.setAspectRatio(previewSize.width, previewSize.height)

                // To ensure that size is set, initialize camera in the view's thread
                viewFinder.post { initializeCamera() }
            }



        })

        getRandomQuestion()

        Animate(R.animator.grow)
    }

    fun getRandomQuestion(){

        val db = QuestionsDbAdapter(context)
        db.open()
        var allEntries = ArrayList<Questions>();
        allEntries = db.getAllQuestionEntries()
        db.close()

        if(allEntries.size == 0){
            var populate = PopulateDBFromGSON()
            populate.ok(context)
        }

        var rnds = (0..allEntries.size-1).random()

        var question = allEntries[rnds]

        var questionString = question.question

        var edit = findViewById<TextView>(R.id.question_textview)
        edit.setText(questionString)
    }

    private fun Animate(animationResourceID: Int) {
        val text: TextView = findViewById<TextView>(R.id.question_textview) as TextView

        // Load the appropriate animation
        val an: Animation = AnimationUtils.loadAnimation(this, animationResourceID)
        // set up listener, so that you know states of the animation
        an.setAnimationListener(MyAnimationListener())
        // start the animation
        text.startAnimation(an)
    }

    inner class MyAnimationListener : Animation.AnimationListener {
        override fun onAnimationEnd(animation: Animation) {

        }

        override fun onAnimationRepeat(animation: Animation) {}
        override fun onAnimationStart(animation: Animation) {}
    }


    /** Creates a [MediaRecorder] instance using the provided [Surface] as input */
    private fun createRecorder(surface: Surface) = MediaRecorder().apply {
        setAudioSource(MediaRecorder.AudioSource.MIC)
        setVideoSource(MediaRecorder.VideoSource.SURFACE)
        setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        setOutputFile(outputFile.absolutePath)
        setVideoEncodingBitRate(RECORDER_VIDEO_BITRATE)
        if (fps > 0) setVideoFrameRate(fps)
        setVideoSize(width, height)
        setVideoEncoder(MediaRecorder.VideoEncoder.H264)
        setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        setInputSurface(surface)
    }

    /**
     * Begin all camera operations in a coroutine in the main thread. This function:
     * - Opens the camera
     * - Configures the camera session
     * - Starts the preview by dispatching a repeating request
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun initializeCamera() = lifecycleScope.launch(Dispatchers.Main) {

        // Open the selected camera
        camera = openCamera(cameraManager, cameraId, cameraHandler)

        // Creates list of Surfaces where the camera will output frames
        val targets = listOf(viewFinder.holder.surface, recorderSurface)

        // Start a capture session using our open camera and list of Surfaces where frames will go
        session = createCaptureSession(camera, targets, cameraHandler)

        // Sends the capture request as frequently as possible until the session is torn down or
        //  session.stopRepeating() is called
        session.setRepeatingRequest(previewRequest, null, cameraHandler)

        // React to user touching the capture button
        capture_button.setOnTouchListener { view, event ->
            when (event.action) {

                MotionEvent.ACTION_DOWN -> lifecycleScope.launch(Dispatchers.IO) {

                    // Prevents screen rotation during the video recording
                    requestedOrientation =
                        ActivityInfo.SCREEN_ORIENTATION_LOCKED

                    // Start recording repeating requests, which will stop the ongoing preview
                    //  repeating requests without having to explicitly call `session.stopRepeating`
                    session.setRepeatingRequest(recordRequest, null, cameraHandler)

                    // Finalizes recorder setup and starts recording
                    recorder.apply {
                        // Sets output orientation based on current sensor value at start time
                        setOrientationHint(orientation)
                        prepare()
                        start()
                    }
                    recordingStartMillis = System.currentTimeMillis()
                    Log.d(TAG, "Recording started")

                    // Starts recording animation
                    overlay.post(animationTask)
                }

                MotionEvent.ACTION_UP -> lifecycleScope.launch(Dispatchers.IO) {

                    // Unlocks screen rotation after recording finished
                    requestedOrientation =
                        ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

                    // Requires recording of at least MIN_REQUIRED_RECORDING_TIME_MILLIS
                    val elapsedTimeMillis = System.currentTimeMillis() - recordingStartMillis
                    if (elapsedTimeMillis < MIN_REQUIRED_RECORDING_TIME_MILLIS) {
                        delay(MIN_REQUIRED_RECORDING_TIME_MILLIS - elapsedTimeMillis)
                    }

                    Log.d(TAG, "Recording stopped. Output file: $outputFile")
                    recorder.stop()

                    // Removes recording animation
                    overlay.removeCallbacks(animationTask)

                    // Broadcasts the media file to the rest of the system
                    MediaScannerConnection.scanFile(
                        view.context, arrayOf(outputFile.absolutePath), null, null)
                    var authority = "${BuildConfig.APPLICATION_ID}.provider"
                    var bytes = let { getBytes(context, FileProvider.getUriForFile(view.context, authority, outputFile)) }


                    Log.d(TAG, "Recording stopped. Output file: $bytes")
                    authority = "${BuildConfig.APPLICATION_ID}.provider"
                    val retriever = FFmpegMediaMetadataRetriever()
                    val db = DiaryDbAdapter(context)
                    try {
                        var path = File(externalMediaDirs?.get(0)?.path.toString()+"/videoDiaries");
                        if(!path.exists()){
                            path.mkdirs();
                        }
                        val sdf = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS", Locale.US)
                        val fileName = "VID_${sdf.format(Date())}.mp4"
                        val outFile = File(path, fileName)
                        val outputStream = FileOutputStream(outFile)
                        outputStream.write(bytes);
                        outputStream.close();



                        retriever.setDataSource(outFile.path);
                        val options = FaceDetectorOptions.Builder()
                                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                                        .enableTracking()
                                        .build()
                        val detector: FaceDetector = FaceDetection.getClient(options)

                        var happinessScores: List<Float> = arrayListOf()
                        var counter: Long = 0
                        val duration = retriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION)?.toDouble()

                        var bitmap: Bitmap? = retriever.getFrameAtTime(counter, OPTION_CLOSEST)
                        while(counter < duration!!) {
                            if (bitmap != null) {
                                //From the current image, grab the face, and capture smile score
                                detector.process(InputImage.fromBitmap(bitmap, orientation))
                                    .addOnSuccessListener {
                                        val frameFace = it.get(0);
                                        val smileScore = frameFace.smilingProbability
                                        if (smileScore != null) {
                                            happinessScores += smileScore
                                        }
                                    }
                                //Load the next image in the video
                                counter += incrementer
                                bitmap = retriever.getFrameAtTime(counter, OPTION_CLOSEST)
                            }
                        }
                        var averageScore: Float = 0.0F;
                        for(score in happinessScores) {
                            averageScore += score
                        }
                        if(happinessScores.size != 0) {
                            averageScore /= happinessScores.size
                        }


                        db.open()
                        val diary = Diary()
                        diary.dateRecorded = SimpleDateFormat("yyyy/MM/dd").format(Date());
                        diary.entryNumber = db.getNextEntryNumber(diary.dateRecorded)
                        diary.fileURI = fileName
                        diary.smileScore = averageScore
                        db.insertDiaryEntry(diary)

                    }
                    catch (e: IOException) {
                        Log.e(TAG, "Saving received message failed with", e);
                    }
                    finally {
                        db.close()
                        retriever.release();
                    }





                    // Launch external activity via intent to play video recorded using our provider
                    startActivity(Intent().apply {
                        action = Intent.ACTION_VIEW
                        type = MimeTypeMap.getSingleton()
                            .getMimeTypeFromExtension(outputFile.extension)
                        val authority = "${BuildConfig.APPLICATION_ID}.provider"
                        data = FileProvider.getUriForFile(view.context, authority, outputFile)
                        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                                Intent.FLAG_ACTIVITY_CLEAR_TOP
                    })

                    // Finishes our current camera screen
                    delay(ANIMATION_SLOW_MILLIS)

                }
            }

            true
        }
    }

    /** Opens the camera and returns the opened device (as the result of the suspend coroutine) */
    @SuppressLint("MissingPermission")
    private suspend fun openCamera(
        manager: CameraManager,
        cameraId: String,
        handler: Handler? = null
    ): CameraDevice = suspendCancellableCoroutine { cont ->
        manager.openCamera(cameraId, object : CameraDevice.StateCallback() {
            override fun onOpened(device: CameraDevice) = cont.resume(device)

            override fun onDisconnected(device: CameraDevice) {
                Log.w(TAG, "Camera $cameraId has been disconnected")
                finish()
            }

            override fun onError(device: CameraDevice, error: Int) {
                val msg = when(error) {
                    ERROR_CAMERA_DEVICE -> "Fatal (device)"
                    ERROR_CAMERA_DISABLED -> "Device policy"
                    ERROR_CAMERA_IN_USE -> "Camera in use"
                    ERROR_CAMERA_SERVICE -> "Fatal (service)"
                    ERROR_MAX_CAMERAS_IN_USE -> "Maximum cameras in use"
                    else -> "Unknown"
                }
                val exc = RuntimeException("Camera $cameraId error: ($error) $msg")
                Log.e(TAG, exc.message, exc)
                if (cont.isActive) cont.resumeWithException(exc)
            }
        }, handler)
    }

    /**
     * Creates a [CameraCaptureSession] and returns the configured session (as the result of the
     * suspend coroutine)
     */
    private suspend fun createCaptureSession(
        device: CameraDevice,
        targets: List<Surface>,
        handler: Handler? = null
    ): CameraCaptureSession = suspendCoroutine { cont ->

        // Creates a capture session using the predefined targets, and defines a session state
        // callback which resumes the coroutine once the session is configured
        device.createCaptureSession(targets, object: CameraCaptureSession.StateCallback() {

            override fun onConfigured(session: CameraCaptureSession) = cont.resume(session)

            override fun onConfigureFailed(session: CameraCaptureSession) {
                val exc = RuntimeException("Camera ${device.id} session configuration failed")
                Log.e(TAG, exc.message, exc)
                cont.resumeWithException(exc)
            }
        }, handler)
    }

    override fun onStop() {
        super.onStop()
        try {
            camera.close()
        } catch (exc: Throwable) {
            Log.e(TAG, "Error closing camera", exc)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraThread.quitSafely()
        recorder.release()
        recorderSurface.release()
    }

    companion object {
        private val TAG = "CameraActivity"

        private const val RECORDER_VIDEO_BITRATE: Int = 10_000_000
        private const val MIN_REQUIRED_RECORDING_TIME_MILLIS: Long = 1000L

        /** Creates a [File] named with the current date and time */
        private fun createFile(context: Context, extension: String): File {
            val sdf = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS", Locale.US)
            return File(context.filesDir, "VID_${sdf.format(Date())}.$extension")
        }
    }

    @Throws(IOException::class)
    fun getBytes(context: Context, uri: Uri): ByteArray? {
        val iStream = context.contentResolver.openInputStream(uri)
        return try {
            getBytes(iStream)
        } finally {
            // close the stream
            try {
                iStream!!.close()
            } catch (ignored: IOException) { /* do nothing */
            }
        }
    }


    /**
     * get bytes from input stream.
     *
     * @param inputStream inputStream.
     * @return byte array read from the inputStream.
     * @throws IOException
     */
    @Throws(IOException::class)
    fun getBytes(inputStream: InputStream?): ByteArray? {
        var bytesResult: ByteArray? = null
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        try {
            var len: Int
            while (inputStream!!.read(buffer).also { len = it } != -1) {
                byteBuffer.write(buffer, 0, len)
            }
            bytesResult = byteBuffer.toByteArray()
        } finally {
            // close the stream
            try {
                byteBuffer.close()
            } catch (ignored: IOException) { /* do nothing */
            }
        }
        return bytesResult
    }
}