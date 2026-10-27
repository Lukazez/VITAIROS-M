package cl.duoc.vitairosm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WelcomeScreen(onContinue: () -> Unit) {
    var acepto by rememberSaveable { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().safeDrawingPadding().verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Text("VITAIROS-M", style = MaterialTheme.typography.headlineLarge)
        Text("Tu bienestar importa", style = MaterialTheme.typography.headlineMedium)
        Text("Registra cómo te sientes, explora recursos y encuentra tu próximo paso.")
        Card { Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Antes de comenzar", style = MaterialTheme.typography.titleLarge)
            Text("Esta aplicación académica ofrece orientación general. No realiza diagnósticos ni reemplaza la atención profesional.")
            Text("Usa únicamente datos ficticios. Las cuentas, registros y fotos se guardan en este dispositivo; no se envían a un servidor.")
            Text("El chat utiliza respuestas predefinidas y la agenda simula una solicitud; no contacta a profesionales.")
        } }
        Row { Checkbox(checked = acepto, onCheckedChange = { acepto = it });
            Text("He leído y comprendo el alcance de esta demostración.", Modifier.weight(1f).padding(top = 12.dp)) }
        Button(onClick = onContinue, enabled = acepto, modifier = Modifier.fillMaxWidth()) { Text("Continuar") }
    }
}
