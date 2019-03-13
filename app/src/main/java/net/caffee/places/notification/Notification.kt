package net.caffee.places.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import net.caffee.places.R
import net.caffee.places.activity.notification.NotificationActivity

class Notification {

    companion object {
        const val NOTIFY_ID = "NOTIFY_ID"
        const val BILL_ID = "BILL_ID"
        const val bookingNotifyID = 1
        const val billNotifyID = 2
    }

    var billId: Long? = null

    fun showBookingNotification(context: Context, title: String, text: String) {
        val mNotifyBuilder = getBuilder(context, title, text, bookingNotifyID)
        val mNotificationManager = getNotificationManager(context)
        mNotificationManager.notify(bookingNotifyID, mNotifyBuilder?.build())
    }

    fun showBillNotification(context: Context, title: String, text: String, billId: Long) {
        this@Notification.billId = billId
        val mNotifyBuilder = getBuilder(context, title, text, billNotifyID)
        val mNotificationManager = getNotificationManager(context)
        mNotificationManager.notify(billNotifyID, mNotifyBuilder?.build())
    }

    private fun getNotificationManager(context: Context): NotificationManager {
        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        return mNotificationManager
    }

    private fun getBuilder(context: Context, title: String, text: String, notifyId: Int): NotificationCompat.Builder? {
        val mNotifyBuilder = NotificationCompat.Builder(context, "123")
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(getPendingIntent(context, notifyId))
        return mNotifyBuilder
    }

    private fun getPendingIntent(context: Context, notifyId: Int): PendingIntent {
        val notifyIntent = Intent(context, NotificationActivity::class.java)
        notifyIntent.putExtra(NOTIFY_ID, notifyId)
        billId?.let { notifyIntent.putExtra(BILL_ID, billId!!) }
        return PendingIntent.getActivity(
                context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}