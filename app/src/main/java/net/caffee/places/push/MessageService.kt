package net.caffee.places.push

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import net.caffee.places.notification.Notification
import net.caffee.places.util.logWithTAG

class MessageService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if (message.data.isNotEmpty()) {
            message.data.forEach { (f, s) ->
                logWithTAG("f - $f  s - $s")
            }
        }

        message.notification?.let {
            val title = it.title ?: ""
            val body = it.body ?: ""
            Notification().showBillNotification(applicationContext, title, body, 6L) //todo change canal
        }
    }
}