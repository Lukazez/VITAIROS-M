package cl.duoc.vitairosm.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import cl.duoc.vitairosm.worker.ReminderScheduler
import cl.duoc.vitairosm.worker.RecordatorioWorker

@Composable
fun ReminderCard() {
    val context = LocalContext.current
    var activo by remember { mutableStateOf(ReminderScheduler.enabled(context)) }
    var mensaje by rememberSaveable { mutableStateOf<String?>(null) }
    var error by rememberSaveable { mutableStateOf(false) }
    var accion by rememberSaveable { mutableStateOf("") }
    fun completar(action: String) {
        error = false
        if (!RecordatorioWorker.notificationsEnabled(context)) {
            error = true
            mensaje = "Las notificaciones están desactivadas. Puedes habilitarlas en Ajustes."
            return
        }
        if (action == "activar") {
            ReminderScheduler.setEnabled(context, true); activo = true
            mensaje = "Recordatorio diario activado. Puedes desactivarlo cuando quieras."
        } else {
            error = !RecordatorioWorker.showNotification(context)
            mensaje = if (!error) "Notificación de prueba enviada."
                else "No se pudo mostrar la notificación. Revisa el permiso en Ajustes."
        }
    }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { allowed ->
        if (allowed) completar(accion)
        else {
            error = true
            mensaje = "No se concedió el permiso. Puedes seguir usando la aplicación."
        }
        accion = ""
    }
    fun solicitar(action: String) {
        if (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(context,
                Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            accion = action; launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else completar(action)
    }
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Recordatorios", style = MaterialTheme.typography.titleMedium)
            Row {
                Text("Recordatorio diario en este dispositivo", Modifier.weight(1f))
                Switch(checked = activo, onCheckedChange = {
                    if (it) solicitar("activar") else {
                        ReminderScheduler.setEnabled(context, false); activo = false
                        error = false
                        mensaje = "Recordatorio desactivado."
                    }
                })
            }
            Text("Aproximadamente cada 24 horas. Android puede retrasarlo para ahorrar batería.", style = MaterialTheme.typography.bodySmall)
            OutlinedButton(onClick = { solicitar("probar") }) { Text("Probar notificación") }
            TextButton(onClick = { context.startActivity(Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)) }) { Text("Ajustes de notificaciones") }
            FormFeedback(mensaje, error = error)
        }
    }
}
