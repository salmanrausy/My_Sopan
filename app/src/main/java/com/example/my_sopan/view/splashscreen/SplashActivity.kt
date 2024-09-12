package com.example.my_sopan.view.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.my_sopan.R
import com.example.my_sopan.view.dashboard.DashboardActivity

//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {

    //INI CLASS UNTUK SPLASHSCREEN
    //Deklarasi variabel timer splash muncul
    private val SPLASH_TIME_OUT:Long = 3500
    private lateinit var logo_splashScreen: ImageView
    private lateinit var text_namaAplikasi:LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        logo_splashScreen = findViewById(R.id.logoSplash)
        text_namaAplikasi = findViewById(R.id.NamaAplikasi)

        supportActionBar?.hide()
        setAnimation()

        //Instruksi menjalankan main screen setelah timer splash screen selesai
        Handler().postDelayed({
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_TIME_OUT)


//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }

    private fun setAnimation(){
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        logo_splashScreen.animation = fadeIn
        text_namaAplikasi.animation = fadeIn

    }
}