package com.alemz.compare3

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.alemz.compare3.data.AppDataBase
import com.alemz.compare3.familyTree.FamilyTreeFragment
import com.alemz.compare3.similarity.SimilarityFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), FamilyTreeFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener, RelationshipFragment.OnFragmentInteractionListener, SimilarityFragment.OnFragmentInteractionListener {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = AppDataBase(this)

        navController = findNavController(R.id.nav_host_fragment)
        findViewById<BottomNavigationView>(R.id.bottom_nav)
            .setupWithNavController(navController)
        requestPermissionsStorage()
        addtoDB()


    }
    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    private fun requestPermissionsStorage() {
        try {
            val permissionStorage = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ), 0x0010
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun addtoDB() {
        var dir: File = Environment.getDataDirectory()
        var list2 = arrayListOf<String>(
            "//media/external/images/media/1002"


        )
        var list = arrayListOf<String>(
            "internal/DCIM/Camera/IMG_20200415_093643",
            "DCIM/Camera/IMG_20200331_135802",
            "DCIM/Camera/IMG_20200301_223334",
            "DCIM/Camera/IMG_20200301_223325",
            "DCIM/Camera/IMG_20200301_223331",
            "DCIM/Camera/IMG_20200308_005532",
            "DCIM/Camera/IMG_20200415_092843"
        )
        for (i in list.indices) {
           // val bitmap = BitmapFactory.decodeFile(list2[i])

            val bitmap = BitmapFactory.decodeFile(list[i])
            imageViewq.setImageBitmap(bitmap)
           // mImageView.setImageBitmap(bitmap)
        }


    }
//        var yourFile = File(dir, "DCIM/Camera/IMG_20200415_093643")
//        Toast.makeText(this, yourFile.path, Toast.LENGTH_LONG).show()
//    }
//    private fun getBitmap(imageUri: Uri) {
//        val pathColumn = arrayOf(
//            MediaStore.Images.Media.DATA
//        )
//        val cursor =
//            contentResolver.query(imageUri, pathColumn, null, null, null)
//        cursor?.moveToFirst()
//        val columnIndex = cursor!!.getColumnIndex(pathColumn[0])
//        val picturePath = cursor.getString(columnIndex)
//        cursor.close()
//        bitmap = BitmapFactory.decodeFile(picturePath)
//    }
//
//    private fun saveMember(){
//        if (bitmap !== null) {
//            val scaledBitmap = scaleBitmap(bitmap!!)
//            if (scaledBitmap != bitmap) {
//                bitmap!!.recycle()
//            }
//            val stream = ByteArrayOutputStream()
//            scaledBitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
//            photo = stream.toByteArray()
//            if (!(scaledBitmap == null || scaledBitmap.isRecycled)) {
//                scaledBitmap.recycle()
//            }
//        } else{
//            Toast.makeText(this,"Please select the photo. ", Toast.LENGTH_SHORT).show()
//        }
//        val id: Long = UUID.randomUUID().mostSignificantBits
//        val firstName = input_name.text.toString()
//        val lastName = input_surname.text.toString()
//        val birth = input_date_of_birth.text.toString()
//        val father = input_father.toString()
//        val mother = input_mother.toString()
//        val marriedTo = input_married.toString()
//
//        if (firstName.isNotEmpty() && birth.isNotEmpty() && photo.isNotEmpty()) {
//            val newMember = FamilyMember(id, firstName, lastName, birth, father, mother, marriedTo, photo)
//            viewModel.insertMember(newMember)
//            finish()
//        }
//        else Toast.makeText(this,"complete the fields! ", Toast.LENGTH_SHORT).show()
//    }
//
//    private fun scaleBitmap(input: Bitmap): Bitmap? {
//        val currentWidth = input.width
//        val currentHeight = input.height
//        val currentPixels = currentWidth * currentHeight
//        val maxPixels = 1024 * 1024 / 4
//        if (currentPixels <= maxPixels) {
//            return input
//        }
//        val scaleFactor =
//            sqrt(maxPixels / currentPixels.toDouble())
//        val newWidthPx = floor(currentWidth * scaleFactor).toInt()
//        val newHeightPx = floor(currentHeight * scaleFactor).toInt()
//        return Bitmap.createScaledBitmap(input, newWidthPx, newHeightPx, true)
//    }

}
