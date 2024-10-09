package com.example.my_sopan.view.splashscreen

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.my_sopan.R
import com.example.my_sopan.view.dashboard.DashboardActivity

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 4000
    private lateinit var logo_splashScreen: ImageView
    private lateinit var tv_copyright: TextView
    private lateinit var text_namaAplikasi: LinearLayout

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        logo_splashScreen = findViewById(R.id.logoSplash)
        text_namaAplikasi = findViewById(R.id.NamaAplikasi)
        tv_copyright = findViewById(R.id.copyright)

        supportActionBar?.hide()
        setAnimation()

        playWelcomeAudio()

        // Instruksi menjalankan main screen setelah timer splash screen selesai
        Handler().postDelayed({
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_TIME_OUT)
    }

    private fun playWelcomeAudio() {
        // Inisialisasi MediaPlayer dengan file audio dari raw folder
        mediaPlayer = MediaPlayer.create(this, R.raw.startup_sound)
        mediaPlayer?.start() // Mulai memutar audio

        // Listener untuk melepaskan MediaPlayer setelah audio selesai
        mediaPlayer?.setOnCompletionListener {
            it.release()
            mediaPlayer = null
        }
    }

    private fun setAnimation() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        logo_splashScreen.animation = fadeIn
        text_namaAplikasi.animation = fadeIn
        tv_copyright.animation = fadeIn
    }

    override fun onDestroy() {
        super.onDestroy()
        // Jika audio masih berjalan saat Activity dihancurkan, hentikan dan lepaskan
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
            mediaPlayer = null
        }
    }
}
