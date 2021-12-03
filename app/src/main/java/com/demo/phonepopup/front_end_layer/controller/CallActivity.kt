package com.demo.phonepopup.front_end_layer.controller

import android.os.Bundle
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.demo.phonepopup.integration_layer.sharedprefernce.SharedPreferenceManager
import com.demo.phonepopup.R

class CallActivity : AppCompatActivity() {

    var txtDialogNumber:TextView?=null
    var txtDialogname:TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_calling)
        initView()



    }

    fun initView(){

        var mynumber=""+ SharedPreferenceManager.KEY_MY_CONTACT_NUMBER?.let { it1 ->
            SharedPreferenceManager!!.instance!!.readString(
                it1,"")
        }


        var name=""+ SharedPreferenceManager.KEY_MY_CONTACT_NAME?.let { it1 ->
            SharedPreferenceManager!!.instance!!.readString(
                it1,"")
        }


        txtDialogname=findViewById(R.id.txtDialogName)
        txtDialogNumber=findViewById(R.id.txtDialogNumber)
        txtDialogNumber!!.setText(""+mynumber)
        txtDialogname!!.setText(""+name)
    }
}