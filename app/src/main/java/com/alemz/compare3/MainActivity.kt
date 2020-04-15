package com.alemz.compare3

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.alemz.compare3.createMember.CMViewModel
import com.alemz.compare3.data.AppDataBase
import com.alemz.compare3.data.FamilyMember
import com.alemz.compare3.familyTree.FTViewModel
import com.alemz.compare3.familyTree.FamilyTreeFragment
import com.alemz.compare3.similarity.SimilarityFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.lang.Math.floor
import java.lang.Math.sqrt
import java.util.*


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), FamilyTreeFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener, RelationshipFragment.OnFragmentInteractionListener, SimilarityFragment.OnFragmentInteractionListener {

    private lateinit var navController: NavController
    private val viewModel: CMViewModel by lazy {
        ViewModelProviders.of(this).get(
            CMViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = AppDataBase(this)

        navController = findNavController(R.id.nav_host_fragment)
        findViewById<BottomNavigationView>(R.id.bottom_nav)
            .setupWithNavController(navController)
        requestPermissionsStorage()


       // addtoDB()


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

        var list = arrayListOf<String>(
            "storage/emulated/0/DCIM/Camera/IMG_20200415_093643.jpg",
            "storage/emulated/0/DCIM/Camera/IMG_20200331_135802.jpg",
            "storage/emulated/0/DCIM/Camera/IMG_20200301_223334.jpg",
            "storage/emulated/0/DCIM/Camera/IMG_20200301_223325.jpg",
            "storage/emulated/0/DCIM/Camera/IMG_20200301_223331.jpg",
            "storage/emulated/0/DCIM/Camera/IMG_20200308_005532.jpg",
            "storage/emulated/0/DCIM/Camera/IMG_20200415_092843.jpg"
        )
        val idlist = arrayListOf<Long>()
        for (i in 0.. 6) idlist.add(UUID.randomUUID().mostSignificantBits)
        val bitlist = arrayListOf<Bitmap>()
        val arr = arrayListOf<ByteArray>()
        for (i in list.indices) {
            val bitmap = BitmapFactory.decodeFile(list[i])
            bitlist.add(scaleBitmap(bitmap))
            arr.add(checkRecycle(bitlist[i]))
        }
        for (i in 0 ..6){
            if (i == 0){
                Toast.makeText(this, idlist[i].toString(), Toast.LENGTH_LONG).show()
                val newMember = FamilyMember(idlist[i], "Ola", "Zioło", "1999-11-02",null , null,null , arr[i])
                viewModel.insertMember(newMember)
            }
            if (i == 1){
                val newMember = FamilyMember(idlist[i], "łukasz", "mrozowski", "1999-11-02",null , null,idlist[0], arr[i])
                viewModel.insertMember(newMember)
            }
            if (i == 2){
                val newMember = FamilyMember(idlist[i], "Ola ", "szcz", "1999-11-02",idlist[1] , idlist[0],null , arr[i])
                viewModel.insertMember(newMember)
            }
            if (i == 3){
                val newMember = FamilyMember(idlist[i], "bartek", "bbb", "1999-11-02",null , null,idlist[2] , arr[i])
                viewModel.insertMember(newMember)
            }
            if (i == 4){
                val newMember = FamilyMember(idlist[i], "Ola", "sz 2", "1999-11-02",idlist[1] , idlist[0],null , arr[i])
                viewModel.insertMember(newMember)
            }
            if (i == 5){
                val newMember = FamilyMember(idlist[i], "nata", "kozak", "1999-11-02",idlist[1] , idlist[0],null , arr[i])
                viewModel.insertMember(newMember)
            }
            if (i == 6){
                val newMember = FamilyMember(idlist[i], "Ola", "Z 2", "1999-11-02", idlist[3] , idlist[2],null , arr[i])
                viewModel.insertMember(newMember)
            }
        }
    }

    private fun scaleBitmap(input: Bitmap): Bitmap {
        val currentWidth = input.width
        val currentHeight = input.height
        val currentPixels = currentWidth * currentHeight
        val maxPixels = 1024 * 1024 / 4
        if (currentPixels <= maxPixels) {
            return input
        }
        val scaleFactor =
            kotlin.math.sqrt(maxPixels / currentPixels.toDouble())
        val newWidthPx = kotlin.math.floor(currentWidth * scaleFactor).toInt()
        val newHeightPx = kotlin.math.floor(currentHeight * scaleFactor).toInt()
        return Bitmap.createScaledBitmap(input, newWidthPx, newHeightPx, true)
    }
    private fun checkRecycle(bitmap: Bitmap) : ByteArray{
        val scaledBitmap = scaleBitmap(bitmap)
        if (scaledBitmap != bitmap) {
            bitmap.recycle()
        }
        val stream = ByteArrayOutputStream()
        scaledBitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val photo = stream.toByteArray()
        if (!(scaledBitmap == null || scaledBitmap.isRecycled)) {
            scaledBitmap.recycle()
        }
        return photo
    }

}
