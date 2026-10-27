package cl.duoc.vitairosm.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import cl.duoc.vitairosm.MainActivity

class RecordatorioWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        if (ReminderScheduler.enabled(applicationContext)) showNotification(applicationContext)
        return Result.success()
    }
    companion object {
        private const val CHANNEL_ID = "canal_bienestar"
        fun notificationsEnabled(context: Context): Boolean {
            if (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(context,
                    Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) return false
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(NotificationChannel(CHANNEL_ID, "Recordatorios de bienestar", NotificationManager.IMPORTANCE_DEFAULT))
            return NotificationManagerCompat.from(context).areNotificationsEnabled() &&
                manager.getNotificationChannel(CHANNEL_ID).importance != NotificationManager.IMPORTANCE_NONE
        }
        fun showNotification(context: Context): Boolean {
            if (!notificationsEnabled(context)) return false
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val intent = PendingIntent.getActivity(context, 0,
                Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info).setContentTitle("VITAIROS-M")
                .setContentText("Recuerda dedicar un momento a tu bienestar y registrar cómo te sientes.")
                .setContentIntent(intent).setAutoCancel(true).build()
            return try { manager.notify(1, notification); true } catch (_: SecurityException) { false }
        }
    }
}
