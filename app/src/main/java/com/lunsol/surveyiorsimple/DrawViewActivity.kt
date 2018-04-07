package com.lunsol.surveyiorsimple

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import android.widget.FrameLayout


/**
 * Created by abcd7 on 2018/04/07.
 */
class DrawViewActivity : AppCompatActivity() {

    var mGraffitiView: GraffitiView? = null
    var checkView : FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.draw_view)

        val imagePath: String = intent.getStringExtra("path")
        val bitmap = BitmapFactory.decodeFile(imagePath)

//        val imageView = findViewById<ImageView>(R.id.img)
//        imageView.setImageBitmap(bitmap)

        mGraffitiView = GraffitiView(this)
        mGraffitiView!!.setId(R.id.graffitiview)     //setId，当页面被移除后恢复时GraffitiView调用保存状态
        mGraffitiView!!.setSaveEnabled(true)
        mGraffitiView!!.setImageBitmap(bitmap)
        checkView = findViewById<FrameLayout>(R.id.graffiti)
        checkView!!.addView(mGraffitiView)

    }

}