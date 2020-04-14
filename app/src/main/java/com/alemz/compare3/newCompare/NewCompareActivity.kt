package com.alemz.compare3.newCompare

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.browse.MediaBrowser
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.alemz.compare3.R
import com.alemz.compare3.R.id.*
import com.huawei.hiai.vision.common.ConnectionCallback
import com.huawei.hiai.vision.common.VisionBase
import com.huawei.hiai.vision.face.FaceComparator
import com.huawei.hiai.vision.visionkit.common.Frame
import com.huawei.hiai.vision.visionkit.face.FaceCompareResult
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class NewCompareActivity : AppCompatActivity() {

    companion object{
        private const val TAG = "FaceCompare"
        private const val REQUEST_PHOTO_GALLERY = 2
        private const val TYPE_CHOOSE_PHOTO_CODE4PERSON1 = 10
        private const val TYPE_CHOOSE_PHOTO_CODE4PERSON2 = 11
        private const val TYPE_SHOW_RESULT = 12
        private const val REQUEST_PHOTO_CAMERA = 1
    }
    private var mBitmapPerson1: Bitmap? = null
    private var mBitmapPerson2: Bitmap? = null
    private var mImageViewPerson1: ImageView? = null
    private var mImageViewPerson2: ImageView? = null
    private var mTxtViewResult: TextView? = null
    private var isPerson1 = false
    private val mWaitResult = Object()
    private var mFaceComparator: FaceComparator? = null
    private var Indicator_1: Boolean = false
    private var Indicator_2: Boolean = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_compare)

        mImageViewPerson1 = findViewById(R.id.imgViewPerson1)
        mImageViewPerson2 = findViewById(R.id.imgViewPerson2)
        mTxtViewResult = findViewById(R.id.result)

        VisionBase.init(this, object : MediaBrowser.ConnectionCallback(), ConnectionCallback {
            override fun onServiceConnect() {
                Log.i(TAG, "onServiceConnect ")
            }

            override fun onServiceDisconnect() {
                Log.i(TAG, "onServiceDisconnect")
            }
        })
        requestPermissionsCamera()
        mThread.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mFaceComparator != null) {
            mFaceComparator!!.release()
        }
    }

    fun onClickButton(view: View) {
        val requestCode: Int
        when (view.id) {
            btnPerson1_Gallery -> {
                toastx("Gallery")
                Log.d(TAG, "btnPerson1_Gallery")
                isPerson1 = true
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                requestCode =
                    REQUEST_PHOTO_GALLERY
                startActivityForResult(intent, requestCode)
            }
            btnPerson2_Gallery -> {
                toastx("Gallery")
                Log.d(TAG, "btnPerson2_Gallery")
                isPerson1 = false
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                requestCode =
                    REQUEST_PHOTO_GALLERY
                startActivityForResult(intent, requestCode)
            }
            btnPerson1_Camera -> {
                toastx("Camera")
                isPerson1 = true
                dispatchTakePictureIntent()
            }
            btnPerson2_Camera -> {
                toastx("Camera")
                isPerson1 = false
                dispatchTakePictureIntent()
            }
            btnstarCompare -> {
                if(Indicator_1 && Indicator_2)
                    startCompare()
                else
                    toastx("Choose photos to start compare")
            }

            else -> {
            }
        }
    }
    private fun dispatchTakePictureIntent() {
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//            takePictureIntent.resolveActivity(packageManager)?.also {
//                startActivityForResult(takePictureIntent, REQUEST_PHOTO_CAMERA)
//
//            }
//        }
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI = FileProvider.getUriForFile(
                        this,
                        "com.alemz.compare3",
                        it
                    )
                    toastx(photoURI.toString())
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI)
                    startActivityForResult(takePictureIntent,
                        REQUEST_PHOTO_CAMERA
                    )
                }
            }
        }
    }
    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun toastx(text:String){
        val toast = Toast.makeText(applicationContext,text,Toast.LENGTH_SHORT)
        toast.show()

    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        toastx(REQUEST_PHOTO_CAMERA.toString())
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                toastx("No Data")
                return
            }


            if (isPerson1)
                Indicator_1 = true
            else if (!isPerson1)
                Indicator_2 = true
//
//            if (requestCode == REQUEST_PHOTO_CAMERA){
//                val imageBitmap = data.extras?.get("data") as Bitmap
//                if (isPerson1)
//                    mImageViewPerson1?.setImageBitmap(imageBitmap)
//                else if (!isPerson1)
//                    mImageViewPerson2?.setImageBitmap(imageBitmap)
//
//            }

                    val selectedImage = data.data
                    val type = data.getIntExtra("type",
                        TYPE_CHOOSE_PHOTO_CODE4PERSON1
                    )
                    Log.d(
                        TAG,
                        "select uri:" + selectedImage.toString() + "type: " + type
                    )
                    getBitmap(type, selectedImage!!)
              //  }
            }
    }


    private fun getBitmap(type: Int, imageUri: Uri) {
        val pathColumn = arrayOf(
            MediaStore.Images.Media.DATA
        )
        //Query the photo corresponding to the specified Uri from the system table
        val cursor =
            contentResolver.query(imageUri, pathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor!!.getColumnIndex(pathColumn[0])
        val picturePath = cursor.getString(columnIndex) //Get photo path
        cursor.close()
        if (isPerson1) {
            mBitmapPerson1 = BitmapFactory.decodeFile(picturePath)
            Log.e("Bitmap person 1", mBitmapPerson1.toString())
            mHandler.sendEmptyMessage(TYPE_CHOOSE_PHOTO_CODE4PERSON1)
        } else {
            mBitmapPerson2 = BitmapFactory.decodeFile(picturePath)
            mHandler.sendEmptyMessage(TYPE_CHOOSE_PHOTO_CODE4PERSON2)
        }
    }

    private val mHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val status = msg.what
            Log.d(TAG, "handleMessage status = $status")
            when (status) {
                TYPE_CHOOSE_PHOTO_CODE4PERSON1 -> {
                    if (mBitmapPerson1 == null) {
                        Log.e(TAG, "bitmap person1 is null !!!! ")
                        return
                    }
                    mImageViewPerson1!!.setImageBitmap(mBitmapPerson1)
                }
                TYPE_CHOOSE_PHOTO_CODE4PERSON2 -> {
                    if (mBitmapPerson2 == null) {
                        Log.e(TAG, "bitmap person2 is null !!!! ")
                        return
                    }
                    mImageViewPerson2!!.setImageBitmap(mBitmapPerson2)
                }
                TYPE_SHOW_RESULT -> {
                    val result = msg.obj as FaceCompareResult
                    if (result.isSamePerson) {
                        mTxtViewResult!!.text = "The same Person !!  and score: " + result.socre
                    } else {
                        mTxtViewResult!!.text = "Not the same Person !! and score:" + result.socre
                    }
                }
                else -> {
                }
            }
        }
    }

    private fun startCompare() {
        mTxtViewResult!!.text = "Is the same Person ??? "
        synchronized(mWaitResult) {
            mWaitResult.notifyAll() }
    }

    private val mThread = Thread(Runnable {
        mFaceComparator = FaceComparator(applicationContext)
        val faceComparator = mFaceComparator!!
        while (true) {
            try {
                synchronized(mWaitResult) { mWaitResult.wait() }
            } catch (e: InterruptedException) {
                Log.e(TAG, e.message!!)
            }
            Log.d(TAG, "start Compare !!!! ")
            val frame1 =
                Frame()
            frame1.bitmap = mBitmapPerson1
            val frame2 =
                Frame()
            frame2.bitmap = mBitmapPerson2
            val jsonObject = faceComparator.faceCompare(frame1, frame2, null)
            if (jsonObject != null) Log.d(
                TAG,
                "Compare end !!!!  json: $jsonObject"
            )
            val result = faceComparator.convertResult(jsonObject)
            Log.d(TAG, "convertResult end !!!! ")
            val msg = Message()
            msg.what =
                TYPE_SHOW_RESULT
            msg.obj = result
            mHandler.sendMessage(msg)
        }
    })



    private fun requestPermissionsCamera() {
        try {
            val permissionCamera = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            )
            if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.CAMERA
                    ), 0x0010
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}
