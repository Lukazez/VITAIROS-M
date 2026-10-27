package cl.duoc.vitairosm.worker

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

object ReminderScheduler {
    private const val NAME = "recordatorio-diario"
    fun enabled(context: Context) = context.getSharedPreferences("recordatorios", Context.MODE_PRIVATE).getBoolean("activo", false)
    fun dailyRequest(): PeriodicWorkRequest = PeriodicWorkRequestBuilder<RecordatorioWorker>(24, TimeUnit.HOURS)
        .setInitialDelay(24, TimeUnit.HOURS).build()
    fun setEnabled(context: Context, enabled: Boolean) {
        context.getSharedPreferences("recordatorios", Context.MODE_PRIVATE).edit().putBoolean("activo", enabled).apply()
        val manager = WorkManager.getInstance(context)
        if (enabled) manager.enqueueUniquePeriodicWork(NAME, ExistingPeriodicWorkPolicy.KEEP, dailyRequest())
        else manager.cancelUniqueWork(NAME)
    }
}
