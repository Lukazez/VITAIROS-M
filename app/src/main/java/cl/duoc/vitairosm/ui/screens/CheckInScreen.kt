package cl.duoc.vitairosm.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cl.duoc.vitairosm.R
import cl.duoc.vitairosm.viewmodel.CheckInViewModel
import cl.duoc.vitairosm.viewmodel.CheckInValidation

@Composable
fun CheckInScreen(viewModel: CheckInViewModel, onVolver: () -> Unit) {
    Column(Modifier.fillMaxSize().safeDrawingPadding().imePadding()
        .verticalScroll(rememberScrollState()).padding(24.dp).animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        TextButton(onClick = onVolver, enabled = !viewModel.guardando) { Text("Volver") }
        Text("¿Cómo te sientes hoy?", style = MaterialTheme.typography.headlineMedium)
        Text("Selecciona tu estado de ánimo. La categoría y la nota son opcionales.")
        CheckInValidation.animos.forEach { estado ->
            FilterChip(selected = viewModel.animo == estado,
                onClick = { viewModel.actualizarAnimo(estado) }, enabled = !viewModel.guardando,
                label = { Text(estado) }, modifier = Modifier.fillMaxWidth())
        }
        FormFeedback(viewModel.errorAnimo)
        HorizontalDivider()
        Text("¿Con qué lo relacionas?", style = MaterialTheme.typography.titleMedium)
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CheckInValidation.categorias.forEach { opcion ->
                FilterChip(selected = viewModel.categoria == opcion,
                    enabled = !viewModel.guardando,
                    onClick = { viewModel.actualizarCategoria(if (viewModel.categoria == opcion) "" else opcion) },
                    label = { Text(opcion) })
            }
        }
        FormFeedback(viewModel.errorCategoria)
        OutlinedTextField(value = viewModel.nota, onValueChange = viewModel::actualizarNota,
            enabled = !viewModel.guardando, isError = viewModel.errorNota != null,
            label = { Text("Nota personal (opcional)") },
            leadingIcon = { Icon(painterResource(R.drawable.ic_note), null) },
            supportingText = { Text(viewModel.errorNota ?: "${viewModel.nota.length}/${CheckInValidation.MAX_NOTE}") },
            modifier = Modifier.fillMaxWidth(), minLines = 3, maxLines = 5)
        FormFeedback(viewModel.mensajeError)
        Text("Usa datos ficticios en esta demostración.", style = MaterialTheme.typography.bodySmall)
        Button(onClick = { viewModel.guardarRegistro(onVolver) }, enabled = !viewModel.guardando,
            modifier = Modifier.fillMaxWidth()) {
            if (viewModel.guardando) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
            else Icon(painterResource(R.drawable.ic_check), null)
            Spacer(Modifier.width(8.dp))
            Text(if (viewModel.guardando) "Guardando…" else "Guardar registro")
        }
    }
}
