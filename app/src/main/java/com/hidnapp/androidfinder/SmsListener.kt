package com.hidnapp.androidfinder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
//import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.ArrayList

//import android.widget.Toast
//import com.hidnapp.androidfinder.FService

class SmsListener : BroadcastReceiver() {
    private var numberModalArrayList: ArrayList<CourseModal>? = null
    //val sms = SmsManager.getDefault()

    private fun loadData(ctx: Context) {
        val sharedPreferences = ctx.getSharedPreferences("shared preferences",
            AppCompatActivity.MODE_PRIVATE
        )

        val gson = Gson()

        val json = sharedPreferences.getString("tn", null)

        val type: Type = object : TypeToken<ArrayList<CourseModal?>?>() {}.getType()

        numberModalArrayList = gson.fromJson(json, type)

        if (numberModalArrayList == null) {
            numberModalArrayList = ArrayList()
        }
    }

    override fun onReceive(ctx: Context, intent: Intent) {

        // Retrieves a map of extended data from the intent.
        val bundle = intent.extras
        Log.i("SmsReceiver", "here")
        try {
            if (bundle != null) {
                val pdusObj = bundle["pdus"] as Array<Any>?
                for (i in pdusObj!!.indices) {
                    val currentMessage = SmsMessage.createFromPdu(pdusObj[i] as ByteArray)
                    val phoneNumber = currentMessage.displayOriginatingAddress
                    val message = currentMessage.displayMessageBody
                    if (!message.startsWith("/")) continue  //checking message patter (should start with /)
                    if (!(checkPwd(message) || checkNumber(phoneNumber))) continue  //message should be sent from accepted num or have valid password, otherwise skip

                    Log.i("SmsReceiver", "senderNum: $phoneNumber; message: $message")
                    textMessage(ctx,
                        "senderNum: $phoneNumber, message: $message", true)

                    val i: Intent = Intent(ctx, FService::class.java)
                    ctx.startService(i)



                }
            }
        } catch (e: Exception) {
            Log.e("SmsReceiver", "Exception smsReceiver$e")
        }
    }

    private fun checkNumber(senderNum: String): Boolean {
        if(numberModalArrayList == null){
            return false
        }

        for(item in numberModalArrayList!!){
            if(senderNum.endsWith(item.courseName)) {
                return true
            }
        }
        return false
    }

    private fun checkPwd(message: String): Boolean {//not implemented
        return false
    }
}