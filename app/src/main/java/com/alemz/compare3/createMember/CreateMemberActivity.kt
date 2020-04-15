package com.alemz.compare3.createMember

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.alemz.compare3.main.MainActivity
import com.alemz.compare3.R
import com.alemz.compare3.data.FamilyMember
import kotlinx.android.synthetic.main.activity_create_member.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor
import kotlin.math.sqrt

@Suppress("DEPRECATION")
class CreateMemberActivity : AppCompatActivity() {

    private var bitmap: Bitmap? = null
    private lateinit var photo: ByteArray
    private lateinit var selectedTime: String
    private lateinit var list: List<String>
    private lateinit var allList:List<FamilyMember>
    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private var father: Long? = null
    private var mother: Long? = null
    private var beloved: Long? = null

    private val viewModel: CMViewModel by lazy {
        ViewModelProviders.of(this).get(
            CMViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_member)
        female.isChecked = true
        allList = viewModel.getAllNoLive()
        list = viewModel.getList("a")

        setSpinners()

    }

    fun onClickButton(view: View) {
        val requestCode: Int
        when (view.id) {
            R.id.import_from_gallery_new -> {
                Log.d(TAG, "open gallery")
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                requestCode =
                    PHOTO_REQUEST_GALLERY
                startActivityForResult(intent, requestCode)
            }
            R.id.choose_birth_date -> {
                chooseDate()
            }
            R.id.button_save_member -> {
                saveMember()
            }
            else -> {
            }
        }
    }

    private fun getBitmap(imageUri: Uri) {
        val pathColumn = arrayOf(
            MediaStore.Images.Media.DATA
        )
        val cursor =
            contentResolver.query(imageUri, pathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor!!.getColumnIndex(pathColumn[0])
        val picturePath = cursor.getString(columnIndex)
        cursor.close()
        bitmap = BitmapFactory.decodeFile(picturePath)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return
            }
            val selectedImage = data.data
            Log.d(
                TAG,
                "select uri:" + selectedImage.toString()
            )
            getBitmap(selectedImage!!)
            new_photo!!.setImageBitmap(bitmap)
        }
    }
    private fun chooseDate() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val chooserDate = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, y, m, d ->
            selectedTime = formatter.format(Date(y-1900, m, d))
            input_date_of_birth.text = Editable.Factory.getInstance().newEditable(selectedTime)
        }, year, month, day)
        chooserDate.show()
    }

    private fun saveMember(){
        var sex = "F"
        if (female.isChecked) sex = "F"
        if (man.isChecked) sex = "M"

        if (bitmap !== null) {
            val scaledBitmap = scaleBitmap(bitmap!!)
            if (scaledBitmap != bitmap) {
                bitmap!!.recycle()
            }
            val stream = ByteArrayOutputStream()
            scaledBitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
            photo = stream.toByteArray()
            if (!(scaledBitmap == null || scaledBitmap.isRecycled)) {
                scaledBitmap.recycle()
            }
        } else{
            Toast.makeText(this,"Please select the photo. ", Toast.LENGTH_SHORT).show()
        }
        val id: Long = UUID.randomUUID().mostSignificantBits
        val firstName = input_name.text.toString()
        val lastName = input_surname.text.toString()
        val birth = input_date_of_birth.text.toString()

        if (firstName.isNotEmpty() && birth.isNotEmpty() && photo.isNotEmpty() ) {
            val newMember = FamilyMember(id, firstName, lastName, birth, sex, father, mother, beloved, photo)
            Log.e("insert", newMember.toString())
            viewModel.insertMember(newMember)
            startActivity(Intent(this, MainActivity::class.java ))
        }
        else Toast.makeText(this,"complete the fields! ", Toast.LENGTH_SHORT).show()
    }

    private fun scaleBitmap(input: Bitmap): Bitmap? {
        val currentWidth = input.width
        val currentHeight = input.height
        val currentPixels = currentWidth * currentHeight
        val maxPixels = 256 * 256 / 4
        if (currentPixels <= maxPixels) {
            return input
        }
        val scaleFactor =
            sqrt(maxPixels / currentPixels.toDouble())
        val newWidthPx = floor(currentWidth * scaleFactor).toInt()
        val newHeightPx = floor(currentHeight * scaleFactor).toInt()
        return Bitmap.createScaledBitmap(input, newWidthPx, newHeightPx, true)
    }
    private fun setSpinners(){
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
        input_father.adapter = adapter
        input_mother.adapter = adapter
        input_married.adapter = adapter

        input_father.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                if (input_father.selectedItem.toString() == "none") father = null
                else father = allList[position].uid
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        input_mother.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
               if (input_mother.selectedItem.toString() == "none") mother = null
               else mother = allList[position].uid
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        input_married.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (input_married.selectedItem.toString() == "none") beloved = null
                else beloved = allList[position].uid
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

    }

    companion object{
        private const val PHOTO_REQUEST_GALLERY = 1
        private const val TAG = "Create Member Activity"
    }
}
