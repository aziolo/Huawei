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
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import com.alemz.compare3.R
import com.alemz.compare3.R.id.*
import com.alemz.compare3.data.FamilyMember
import com.alemz.compare3.data.Similarity
import com.huawei.hiai.vision.common.ConnectionCallback
import com.huawei.hiai.vision.common.VisionBase
import com.huawei.hiai.vision.face.FaceComparator
import com.huawei.hiai.vision.visionkit.common.Frame
import com.huawei.hiai.vision.visionkit.face.FaceCompareResult
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

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
    private lateinit var mImageViewPerson1: ImageView
    private lateinit var mImageViewPerson2: ImageView
    private var mTxtViewResult: TextView? = null
    private lateinit var mCardViewCheck: CardView
    private lateinit var mCardViewResult: CardView
    private lateinit var mCardViewPerson1: CardView
    private lateinit var mCardViewPerson2: CardView
    private var isPerson1 = false
    private val mWaitResult = Object()
    private var mFaceComparator: FaceComparator? = null
    private var indicator1: Boolean = false
    private var indicator2: Boolean = false
    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private var finalResult: Float = 0.0f
    private lateinit var spin1 : Spinner
    private lateinit var spin2 : Spinner
    private lateinit var list: List<String>
    private lateinit var allList:List<FamilyMember>
    private var per1: Long? = null
    private var per2: Long? = null
    private val viewModel: NCViewModel by lazy {
        ViewModelProviders.of(this).get(
            NCViewModel::class.java
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_compare)
        mImageViewPerson1 = findViewById(R.id.imgViewPerson1)
        mImageViewPerson2 = findViewById(R.id.imgViewPerson2)
        mTxtViewResult = findViewById(R.id.result)
        mCardViewCheck = findViewById<CardView>(R.id.check_card)
        mCardViewResult = findViewById<CardView>(R.id.result_card)
        mCardViewPerson1 = findViewById<CardView>(R.id.person_1_card)
        mCardViewPerson2 = findViewById<CardView>(R.id.person_2_card)
        spin1 = findViewById(R.id.spinner1)
        spin2 = findViewById(R.id.spinner2)
        allList = viewModel.getAllNoLive()
        list = viewModel.getList("b")
        setSpinners()

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
                Log.d(TAG, "btnPerson1_Gallery")
                isPerson1 = true
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                requestCode =
                    REQUEST_PHOTO_GALLERY
                startActivityForResult(intent, requestCode)
            }
            btnPerson2_Gallery -> {
                Log.d(TAG, "btnPerson2_Gallery")
                isPerson1 = false
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                requestCode =
                    REQUEST_PHOTO_GALLERY
                startActivityForResult(intent, requestCode)
            }
            btnPerson1_Camera -> {
                isPerson1 = true
                dispatchTakePictureIntent()
            }
            btnPerson2_Camera -> {
                isPerson1 = false
                dispatchTakePictureIntent()
            }
            btnstarCompare -> {
                if(indicator1 && indicator2) {
                    startCompare()
                    mCardViewCheck.isVisible = false
                    mCardViewResult.isVisible = true
                    mCardViewPerson1.isVisible = false
                    mCardViewPerson2.isVisible = false
                }
                else
                    toastx("Choose photos to start compare")
            }
            btn_Repeat -> {
                mCardViewCheck.isVisible = true
                mCardViewResult.isVisible = false
                mCardViewPerson1.isVisible = true
                mCardViewPerson2.isVisible = true
                mImageViewPerson1.setImageResource(R.drawable.user)
                mImageViewPerson2.setImageResource(R.drawable.user)
                indicator1 = false
                indicator2 = false
            }
            btn_Accept -> {
                insertSimilarityToDB()
                finish()
            }
            else -> {
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI = FileProvider.getUriForFile(
                        this,
                        "com.alemz.compare3",
                        it
                    )
                    //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI)
                    startActivityForResult(takePictureIntent,
                        REQUEST_PHOTO_CAMERA
                    )
                }
            }
        }
    }
    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }
    }
    lateinit var currentPhotoPath: String

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "IMG_${timeStamp}_", /* prefix */
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
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return
            }
            if (isPerson1)
                indicator1 = true
            else if (!isPerson1)
                indicator2 = true

            if (requestCode == REQUEST_PHOTO_CAMERA){
                galleryAddPic()
                if (isPerson1) {
                    mBitmapPerson1 = data.extras!!.get("data") as Bitmap
                    mImageViewPerson1.setImageBitmap(mBitmapPerson1)
                    Log.e("Bitmap person 1", mBitmapPerson1.toString())
                    mHandler.sendEmptyMessage(TYPE_CHOOSE_PHOTO_CODE4PERSON1)
                } else {
                    mBitmapPerson2 = data.extras!!.get("data") as Bitmap
                    mImageViewPerson2.setImageBitmap(mBitmapPerson2)
                    mHandler.sendEmptyMessage(TYPE_CHOOSE_PHOTO_CODE4PERSON2)
                }
            }
            if (requestCode == REQUEST_PHOTO_GALLERY){
                val selectedImage = data.data
                val type = data.getIntExtra("type",
                    TYPE_CHOOSE_PHOTO_CODE4PERSON1
                )
                Log.e(
                    TAG,
                    "select uri:" + selectedImage.toString() + "type: " + type
                )
                getBitmap(type, selectedImage!!)
            }

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
                    mImageViewPerson1.setImageBitmap(mBitmapPerson1)
                }
                TYPE_CHOOSE_PHOTO_CODE4PERSON2 -> {
                    if (mBitmapPerson2 == null) {
                        Log.e(TAG, "bitmap person2 is null !!!! ")
                        return
                    }
                    mImageViewPerson2.setImageBitmap(mBitmapPerson2)
                }
                TYPE_SHOW_RESULT -> {
                    val result = msg.obj as FaceCompareResult

                    val resultPercent = result.socre * 100
                    finalResult = (resultPercent * 100).roundToInt().toFloat()/100
                    mTxtViewResult!!.text = " $finalResult %"

                    if (result.isSamePerson) {
                        toastx("This is the same person. Change photos !")
                        mCardViewCheck.isVisible = true
                        mCardViewResult.isVisible = false
                        mCardViewPerson1.isVisible = true
                        mCardViewPerson2.isVisible = true
                    }
                }
                else -> {
                }
            }
        }
    }

    private fun startCompare() {
        mTxtViewResult!!.text = " "
        synchronized(mWaitResult) {
            mWaitResult.notifyAll()
        }
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

    private fun scaleBitmap(input: Bitmap): Bitmap {
        val currentWidth = input.width
        val currentHeight = input.height
        val currentPixels = currentWidth * currentHeight
        val maxPixels = 256 * 256 / 4
        if (currentPixels <= maxPixels) {
            return input
        }
        val scaleFactor =
            kotlin.math.sqrt(maxPixels / currentPixels.toDouble())
        val newWidthPx = kotlin.math.floor(currentWidth * scaleFactor).toInt()
        val newHeightPx = kotlin.math.floor(currentHeight * scaleFactor).toInt()
        return Bitmap.createScaledBitmap(input, newWidthPx, newHeightPx, true)
    }
    private fun bitmapToByteArray(bitmap: Bitmap) : ByteArray{
        val scaledBitmap = scaleBitmap(bitmap)
        if (scaledBitmap != bitmap) {
            bitmap.recycle()
        }
        val stream = ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val photo = stream.toByteArray()
        if (!(scaledBitmap == null || scaledBitmap.isRecycled)) {
            scaledBitmap.recycle()
        }
        return photo
    }

    //change id
    private fun insertSimilarityToDB(){
        val id = UUID.randomUUID().mostSignificantBits
        val id1 = per1!!
        val id2 = per2!!
        val date = formatter.format(Calendar.getInstance().time)
        val photo1 = bitmapToByteArray(mBitmapPerson1!!)
        val photo2 = bitmapToByteArray(mBitmapPerson2!!)
        val end = Similarity(id, finalResult, id1, id2, date,photo1,photo2)
        viewModel.insertMember(end)
    }

    private fun setSpinners(){
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
        spin1.adapter = adapter
        spin2.adapter = adapter

        spin1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (allList.isNotEmpty()) per1 = allList[position].uid
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        spin2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (allList.isNotEmpty()) per2 = allList[position].uid
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

}
