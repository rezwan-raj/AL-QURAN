package com.quantum.alquran.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.quantum.alquran.databinding.AQuranBinding
import com.quantum.minar.database.User_D
import com.quantum.alquran.theme.Appli_Them
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class QuranActi : AppCompatActivity() {
    private var binding: AQuranBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Appli_Them(this)
        binding = AQuranBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.quranVector?.clipToOutline = true

        binding?.quranStart?.setOnClickListener {
            startActivity(
                Intent(
                    this, QuranMainActi::class.java
                )
            )
            try {
                copyDataBaseTwo()
                copyDataBase()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            User_D(this).quranLaunched = true
            finish()
        }
    }

    @Throws(IOException::class)
    private fun copyDataBase() {
        val DB_NAME = "ALQURAN.db"
        val DB_PATH = getDatabasePath(DB_NAME).path
        val myInput = assets.open(DB_NAME)
        val myOutput: OutputStream = FileOutputStream(DB_PATH)
        val buffer = ByteArray(1024)
        var length: Int
        while (myInput.read(buffer).also { length = it } > 0) {
            myOutput.write(buffer, 0, length)
        }
        myOutput.flush()
        myOutput.close()
        myInput.close()
    }

    @Throws(IOException::class)
    private fun copyDataBaseTwo() {
        val DB_NAME = "Su_r_ahList_Surah.db"
        val DB_PATH = getDatabasePath(DB_NAME).path
        val myInput = assets.open(DB_NAME)
        val myOutput: OutputStream = FileOutputStream(DB_PATH)
        val buffer = ByteArray(1024)
        var length: Int
        while (myInput.read(buffer).also { length = it } > 0) {
            myOutput.write(buffer, 0, length)
        }
        myOutput.flush()
        myOutput.close()
        myInput.close()
    }
}