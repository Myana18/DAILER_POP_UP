package com.demo.phonepopup.front_end_layer.controller

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.*
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.demo.phonepopup.R
import androidx.core.content.ContextCompat
import android.database.Cursor
import android.provider.ContactsContract
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.demo.phonepopup.front_end_layer.view.adapter.ContactListAdapter
import com.demo.phonepopup.integration_layer.model.PhoneCallModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.phonepopup.integration_layer.sharedprefernce.SharedPreferenceManager
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import android.widget.EditText
import android.text.Editable

import android.text.TextWatcher
import android.view.Gravity


class MainActivity : AppCompatActivity() {
    var ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 1
    var dialog: Dialog? = null
    var TAG = "::MY ACTIIVTY:::"
    var cursor: Cursor? = null
    var phoneCallModel: PhoneCallModel? = null
    var myContactList: ArrayList<PhoneCallModel> = arrayListOf()
    var contactListAdapter: ContactListAdapter? = null
    var listContact: RecyclerView? = null
    var mEditSerach: EditText? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listContact = findViewById(R.id.listContact)

        // check permision
        checkPermision()
        // InitView
        initView()
        // Get List of contacts
        getContacts()

        // Register the receiver from Brodcast
        registerReceiver(broadcastReceiver, IntentFilter("call"));
    }

    fun initView() {


        mEditSerach = findViewById(R.id.editSerachView)


    }


    var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // internet lost alert dialog method call from here...

            Log.e(TAG, "Recived call back")
            //  showDialog()
            // popUP(this@MainActivity)
            popUpDialog(this@MainActivity)

        }
    }


    // Fun to check the permissions


    fun checkPermision() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.READ_CONTACTS
                ),
                ASK_MULTIPLE_PERMISSION_REQUEST_CODE
            )
        }
    }

    // Function for showing pop up on screen

    fun popUpDialog(context: Context) {

        var mynumber = "" + SharedPreferenceManager.KEY_MY_CONTACT_NUMBER?.let { it1 ->
            SharedPreferenceManager!!.instance!!.readString(
                it1, ""
            )
        }

        var incoming = "" + SharedPreferenceManager.KEY_MY_INCOMING?.let { it1 ->
            SharedPreferenceManager!!.instance!!.readString(
                it1, ""
            )
        }
        // FETCH THE IN COMING NUMBER

        if (mynumber.contains("+91")) {
            mynumber = mynumber
        } else {
            mynumber = "+91" + mynumber
        }

        mynumber = mynumber.trim()
        incoming = incoming.trim()

        if (mynumber.equals(incoming)) {
            val intentPhoneCall = Intent(context, CallActivity::class.java)
            intentPhoneCall.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intentPhoneCall.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intentPhoneCall)
            finish()
        } else {
            val toast = Toast.makeText(
                context,
                "You did not select this number to show pop up on screen",
                Toast.LENGTH_LONG
            )
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }


    }


    // Function get all phone contacts in app
    fun getContacts() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            checkPermision()
        } else {

            // If already has the permission go here
            readataAction()

            Log.e(TAG, "MY LIST" + myContactList.size)
        }
    }

    // Show list on screen

    @SuppressLint("Range")
    fun readataAction() {
        val phones = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        while (phones!!.moveToNext()) {
            val name =
                phones!!.getString(phones!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber =
                phones!!.getString(phones!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            phoneCallModel = PhoneCallModel()
            phoneCallModel!!.number = phoneNumber
            phoneCallModel!!.name = name
            myContactList.add(phoneCallModel!!)
        }
        phones!!.close()


        // TO sort the duplicate values
        val hashSet = HashSet<PhoneCallModel>()
        hashSet.addAll(myContactList)
        myContactList.clear()
        myContactList.addAll(hashSet)


        // To have list  in Alphabetical order
        contactListAdapter = ContactListAdapter(this, myContactList)
        var mLayoutManager: LinearLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        listContact!!.layoutManager = mLayoutManager
        listContact!!.adapter = contactListAdapter

        // Add Text Change Listener to EditText

        mEditSerach!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Call back the Adapter with current character to Filter
                contactListAdapter!!.getFilter()!!.filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })
    }




    // Results for permission granted

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED
            )
                readataAction()
            Log.e(TAG, "PERMISSION GRATED")
        } else {
            Log.e(
                TAG, "NO " +
                        "PERMISSION GRATED"
            )
        }
    }


}