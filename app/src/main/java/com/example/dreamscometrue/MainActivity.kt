package com.example.dreamscometrue

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.selects.select
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import java.io.File
import java.lang.Exception

private const val REQUEST_READ_EXTERNAL_STORAGE = 1000
private val REQUEST_IMAGE_CAPTURE = 2

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 권한이 부여되었는지 확인 ①
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED)   {

            // 권한이 허용되지 않음 ②
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))   {
                // 이전에 이미 권한이 거부되었을 때 설명 ③
                val builder = AlertDialog.Builder(this)
                builder.setTitle("권한이 필요한 이유")
                builder.setMessage("사진 정보를 얻기 위해서는 외부 저장소 권한이 필수로 필요합니다")
                builder.setPositiveButton("yes") { dialog: DialogInterface?, which: Int -> ActivityCompat.requestPermissions(
                    this@MainActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_READ_EXTERNAL_STORAGE)  }
                builder.setNegativeButton("no") { dialog: DialogInterface?, which: Int ->  }
                builder.show()
            } else  {
                // 권한 요청 ④
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_READ_EXTERNAL_STORAGE)
            }
        } else  {
            // 권한이 이미 허용됨 ⑤
            gallery_button.setOnClickListener { selectGallery() }
        }

    }

    private fun selectGallery() {
        val intent :Intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            2 -> {
                if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE){
                    output_image.setImageURI(data?.data) // handle chosen image
                }
            }
        }
    }
}