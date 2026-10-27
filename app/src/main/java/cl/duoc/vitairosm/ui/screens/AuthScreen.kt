package cl.duoc.vitairosm.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import cl.duoc.vitairosm.R
import cl.duoc.vitairosm.viewmodel.AuthViewModel

@Composable
fun AuthScreen(viewModel: AuthViewModel, onLoginExitoso: () -> Unit) {
    var esRegistro by rememberSaveable { mutableStateOf(false) }
    var mostrarClave by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(viewModel.usuarioLogueado?.id) {
        if (viewModel.usuarioLogueado != null) onLoginExitoso()
    }
    Column(Modifier.fillMaxSize().safeDrawingPadding().imePadding()
        .verticalScroll(rememberScrollState()).padding(24.dp).animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("VITAIROS-M", style = MaterialTheme.typography.headlineLarge)
        Text(if (esRegistro) "Crear cuenta" else "Iniciar sesión", style = MaterialTheme.typography.headlineSmall)
        Text("Tu espacio de orientación y acompañamiento emocional.")
        if (esRegistro) OutlinedTextField(value = viewModel.nombre, onValueChange = viewModel::actualizarNombre,
            label = { Text("Nombre completo") }, leadingIcon = { Icon(painterResource(R.drawable.ic_person), null) },
            enabled = !viewModel.procesando, singleLine = true, isError = viewModel.errorNombre != null,
            supportingText = { viewModel.errorNombre?.let { Text(it) } }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = viewModel.correo, onValueChange = viewModel::actualizarCorreo,
            label = { Text("Correo electrónico") }, leadingIcon = { Icon(painterResource(R.drawable.ic_email), null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            enabled = !viewModel.procesando, singleLine = true, isError = viewModel.errorCorreo != null,
            supportingText = { viewModel.errorCorreo?.let { Text(it) } }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = viewModel.contrasena, onValueChange = viewModel::actualizarContrasena,
            label = { Text("Contraseña") }, leadingIcon = { Icon(painterResource(R.drawable.ic_lock), null) },
            visualTransformation = if (mostrarClave) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = { TextButton(onClick = { mostrarClave = !mostrarClave }) { Text(if (mostrarClave) "Ocultar" else "Ver") } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            enabled = !viewModel.procesando, singleLine = true, isError = viewModel.errorContrasena != null,
            supportingText = { Text(viewModel.errorContrasena ?: if (esRegistro) "Mínimo 8 caracteres" else "") },
            modifier = Modifier.fillMaxWidth())
        FormFeedback(viewModel.mensajeError)
        FormFeedback(viewModel.mensajeExito, error = false)
        Button(enabled = !viewModel.procesando, modifier = Modifier.fillMaxWidth(),
            onClick = { if (esRegistro) viewModel.registrarUsuario() else viewModel.iniciarSesion() }) {
            if (viewModel.procesando) { CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp); Spacer(Modifier.width(8.dp)) }
            Text(if (viewModel.procesando) "Procesando…" else if (esRegistro) "Registrarse" else "Ingresar")
        }
        TextButton(enabled = !viewModel.procesando,
            onClick = { esRegistro = !esRegistro; mostrarClave = false; viewModel.limpiarMensajes() }) {
            Text(if (esRegistro) "Ya tengo cuenta. Iniciar sesión" else "No tengo cuenta. Registrarse")
        }
    }
}
