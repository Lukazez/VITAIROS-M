package cl.duoc.vitairosm

import android.Manifest
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.WorkInfo
import androidx.work.WorkManager
import cl.duoc.vitairosm.worker.RecordatorioWorker
import cl.duoc.vitairosm.worker.ReminderScheduler
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.io.FileInputStream
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class ReminderTest {
    @Test fun activarDosVecesNoDuplicaYDesactivarCancela() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val manager = WorkManager.getInstance(context)
        try {
            ReminderScheduler.setEnabled(context, true)
            ReminderScheduler.setEnabled(context, true)
            val work = manager.getWorkInfosForUniqueWork("recordatorio-diario").get(10, TimeUnit.SECONDS)
            assertTrue(ReminderScheduler.enabled(context))
            assertEquals(1, work.count { !it.state.isFinished })
            ReminderScheduler.setEnabled(context, false)
            assertFalse(ReminderScheduler.enabled(context))
            assertTrue(manager.getWorkInfosForUniqueWork("recordatorio-diario").get(10, TimeUnit.SECONDS)
                .all { it.state == WorkInfo.State.CANCELLED })
        } finally { ReminderScheduler.setEnabled(context, false) }
    }

    @Test fun respetaPermisoActualYConPermisoNotifica() {
        if (Build.VERSION.SDK_INT < 33) return
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val context = instrumentation.targetContext
        val manager = context.getSystemService(NotificationManager::class.java)
        fun permission(action: String) {
            instrumentation.uiAutomation.executeShellCommand(
                "pm $action ${context.packageName} ${Manifest.permission.POST_NOTIFICATIONS}"
            ).use { descriptor -> FileInputStream(descriptor.fileDescriptor).bufferedReader().use { it.readText() } }
        }
        try {
            val allowed = context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            assertEquals(allowed, RecordatorioWorker.showNotification(context))
            permission("grant")
            assertTrue(RecordatorioWorker.showNotification(context))
            val deadline = System.currentTimeMillis() + 5_000
            while (manager.activeNotifications.isEmpty() && System.currentTimeMillis() < deadline) Thread.sleep(50)
            assertEquals(1, manager.activeNotifications.size)
            assertNotNull(manager.activeNotifications.single().notification.contentIntent)
        } finally { manager.cancel(1) }
    }
}
