package com.alemz.compare3

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.alemz.compare3.data.AppDataBase
import com.alemz.compare3.familyTree.FamilyTreeFragment
import com.alemz.compare3.similarity.SimilarityFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

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


//    private fun getBitmap(type: Int, imageUri: Uri) {
//        val pathColumn = arrayOf(
//            MediaStore.Images.Media.DATA
//        )
//        //Query the photo corresponding to the specified Uri from the system table
//        val cursor =
//            contentResolver.query(imageUri, pathColumn, null, null, null)
//        cursor?.moveToFirst()
//        val columnIndex = cursor!!.getColumnIndex(pathColumn[0])
//        val picturePath = cursor.getString(columnIndex) //Get photo path
//        cursor.close()
//        if (isPerson1) {
//            mBitmapPerson1 = BitmapFactory.decodeFile(picturePath)
//            Log.e("Bitmap person 1", mBitmapPerson1.toString())
//            mHandler.sendEmptyMessage(TYPE_CHOOSE_PHOTO_CODE4PERSON1)
//        } else {
//            mBitmapPerson2 = BitmapFactory.decodeFile(picturePath)
//            mHandler.sendEmptyMessage(TYPE_CHOOSE_PHOTO_CODE4PERSON2)
//        }
//    }



}
