import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val PERMISSIONS = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.BLUETOOTH,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val REQUEST_PERMISSIONS = 1
    private var recorder: MediaRecorder? = null
    private var recordingFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissions()
    }

    private fun requestPermissions() {
        if (!hasPermissions()) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSIONS)
        } else {
            startRecording()
        }
    }

    private fun hasPermissions(): Boolean {
        for (permission in PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    private fun startRecording() {
        recordingFile = createRecordingFile()
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.DEFAULT)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(recordingFile!!.absolutePath)
            try {
                prepare()
            } catch (e: IOException) {
                Log.e(TAG, "prepare() failed")
            }
            start()
        }
    }

    private fun createRecordingFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        return File.createTempFile(
            "AUDIO_$timestamp",
            ".3gp",
            storageDir
        )
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        MediaScannerConnection.scanFile(
            this,
            arrayOf(recordingFile!!.absolutePath),
            arrayOf(MediaStore.Audio.Media.CONTENT_TYPE),
            null
        )
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
