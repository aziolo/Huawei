package com.alemz.compare3

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.browse.MediaBrowser
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.huawei.hiai.vision.common.ConnectionCallback
import com.huawei.hiai.vision.common.VisionBase
import com.huawei.hiai.vision.face.FaceComparator
import com.huawei.hiai.vision.visionkit.common.Frame
import com.huawei.hiai.vision.visionkit.face.FaceCompareResult

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), FamilyTreeFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener, RelationshipFragment.OnFragmentInteractionListener, SimilarityFragment.OnFragmentInteractionListener {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.nav_host_fragment)
        findViewById<BottomNavigationView>(R.id.bottom_nav)
            .setupWithNavController(navController)

    }
    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }




}
