package net.caffee.places.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony

class SmsReceiver : BroadcastReceiver() {

    private var mListener: Listener? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        val smsBody = StringBuilder()
        if (intent != null && intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                smsBody.append(smsMessage.messageBody)
            }
        }
        mListener?.onTextReceived(smsBody.toString().trim().substring(smsBody.length - 6, smsBody.length))
    }

    fun setListener(mListener: Listener) {
        this.mListener = mListener
    }

    interface Listener {
        fun onTextReceived(codeFromSms: String)
    }
}