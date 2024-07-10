package com.tondz.facepartdetection

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Spinner
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : Activity(), SurfaceHolder.Callback {
    private val yolov8ncnn = FacePartDetection()
    private var facing = 0
    private val current_model = 0
    private val current_cpugpu = 0

    private var cameraView: SurfaceView? = null

    /** Called when the activity is first created.  */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        cameraView = findViewById<View>(R.id.cameraview) as SurfaceView

        cameraView!!.holder.setFormat(PixelFormat.RGBA_8888)
        cameraView!!.holder.addCallback(this)

        val buttonSwitchCamera = findViewById<View>(R.id.buttonSwitchCamera) as Button
        buttonSwitchCamera.setOnClickListener {
            val new_facing = 1 - facing
            yolov8ncnn.closeCamera()

            yolov8ncnn.openCamera(new_facing)
            facing = new_facing
        }

        reload()
    }

    private fun reload() {
        val ret_init = yolov8ncnn.loadModel(assets, current_model, current_cpugpu)
        if (!ret_init) {
            Log.e("MainActivity", "yolov8ncnn loadModel failed")
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        yolov8ncnn.setOutputWindow(holder.surface)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
    }

    public override fun onResume() {
        super.onResume()

        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA
            )
        }

        yolov8ncnn.openCamera(facing)
    }

    public override fun onPause() {
        super.onPause()

        yolov8ncnn.closeCamera()
    }

    companion object {
        const val REQUEST_CAMERA: Int = 100
    }
}
