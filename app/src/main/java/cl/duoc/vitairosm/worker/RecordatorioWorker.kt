    package cl.duoc.vitairosm.worker

    import android.app.NotificationChannel
    import android.app.NotificationManager
    import android.content.Context
    import android.os.Build
    import androidx.core.app.NotificationCompat
    import androidx.work.Worker
    import androidx.work.WorkerParameters

    class RecordatorioWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
        override fun doWork(): Result {
            mostrarNotificacion()
            return Result.success()
        }

        private fun mostrarNotificacion() {
            val context = applicationContext
            val channelId = "canal_bienestar"
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    "Recordatorios de bienestar",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)
            }

            val notification = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("VITAIROS-M")
                .setContentText("No olvides registrar tu estado de animo hoy.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(1, notification)
        }
    }