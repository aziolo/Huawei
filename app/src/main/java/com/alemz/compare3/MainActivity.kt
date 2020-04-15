package com.alemz.compare3

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.alemz.compare3.data.AppDataBase
import com.alemz.compare3.familyTree.FamilyTreeFragment
import com.alemz.compare3.similarity.SimilarityFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.net.URI


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
    private fun addtoDB(){
        var dir: File = Environment.getDataDirectory()
        var list = arrayListOf<String>("DCIM/Camera/IMG_20200415_093643",
                                                        )





        var yourFile = File(dir, "DCIM/Camera/IMG_20200415_093643")
        Toast.makeText(this, yourFile.path, Toast.LENGTH_LONG).show()
    }


}
