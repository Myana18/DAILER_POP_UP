package com.demo.phonepopup.integration_layer.utility

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import com.demo.phonepopup.front_end_layer.controller.MainActivity
import android.view.Gravity
import com.demo.phonepopup.integration_layer.sharedprefernce.SharedPreferenceManager


class IncomingReceiver : BroadcastReceiver() {
    private val TAG = "::ICOMING MESSAGE:::"



    override fun onReceive(context: Context, intent: Intent) {



        if(intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
            showToast(context,"Call started...");
        }
        else if(intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE)){
            showToast(context,"Call ended...");

            // On call ended close the pop up
            val homeIntent = Intent(Intent.ACTION_MAIN)
            homeIntent.addCategory(Intent.CATEGORY_HOME)
            homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(homeIntent)
        }
        else if(intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)){
           // showToast(context,"Incoming call...");

           // Call at MainActivity

            // FETCH THE IN COMING NUMBER
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
            Log.e(TAG,""+incomingNumber)

            // Storing incoming mobile number in SharedPrefernce
            SharedPreferenceManager.instance!!.writeString(SharedPreferenceManager.KEY_MY_INCOMING!!,incomingNumber.toString())
            context.sendBroadcast(Intent("call"))





        }


    }


    // fun for toast to detect the actions on call receive

    fun showToast(context: Context?, message: String?) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        //toast.show()
    }

   


}