package com.hidnapp.androidfinder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
//import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.util.Log
//import android.widget.Toast
//import com.hidnapp.androidfinder.FService

class SmsListener : BroadcastReceiver() {
    //val sms = SmsManager.getDefault()

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
        return (senderNum.endsWith("65787661"))
    }

    private fun checkPwd(message: String): Boolean {//not implemented
        return false
    }
}