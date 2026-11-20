package cl.duoc.vitairosm.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F8FB))
            .safeDrawingPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE1ECF2))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.logo_vitairos),
                    contentDescription = "Logo VITAIROS-M",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(84.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Text(
            text = if (esRegistro) "Crear cuenta" else "Iniciar sesion",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF000E2E)
        )
        Text(
            text = "Tu espacio de orientacion y acompanamiento emocional.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF536477)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE1ECF2))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (esRegistro) {
                    OutlinedTextField(
                        value = viewModel.nombre,
                        onValueChange = viewModel::actualizarNombre,
                        label = { Text("Nombre completo") },
                        leadingIcon = { Icon(painterResource(R.drawable.ic_person), null) },
                        enabled = !viewModel.procesando,
                        singleLine = true,
                        isError = viewModel.errorNombre != null,
                        supportingText = { viewModel.errorNombre?.let { Text(it) } },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                OutlinedTextField(
                    value = viewModel.correo,
                    onValueChange = viewModel::actualizarCorreo,
                    label = { Text("Correo electronico") },
                    leadingIcon = { Icon(painterResource(R.drawable.ic_email), null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    enabled = !viewModel.procesando,
                    singleLine = true,
                    isError = viewModel.errorCorreo != null,
                    supportingText = { viewModel.errorCorreo?.let { Text(it) } },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = viewModel.contrasena,
                    onValueChange = viewModel::actualizarContrasena,
                    label = { Text("Contrasena") },
                    leadingIcon = { Icon(painterResource(R.drawable.ic_lock), null) },
                    visualTransformation = if (mostrarClave) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(onClick = { mostrarClave = !mostrarClave }) {
                            Text(if (mostrarClave) "Ocultar" else "Ver")
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    enabled = !viewModel.procesando,
                    singleLine = true,
                    isError = viewModel.errorContrasena != null,
                    supportingText = {
                        Text(viewModel.errorContrasena ?: if (esRegistro) "Minimo 8 caracteres" else "")
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                FormFeedback(viewModel.mensajeError)
                FormFeedback(viewModel.mensajeExito, error = false)

                Button(
                    enabled = !viewModel.procesando,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005B70)),
                    onClick = {
                        if (esRegistro) viewModel.registrarUsuario() else viewModel.iniciarSesion()
                    }
                ) {
                    if (viewModel.procesando) {
                        CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                        Spacer(Modifier.width(8.dp))
                    }
                    Text(
                        text = if (viewModel.procesando) "Procesando..." else if (esRegistro) "Registrarse" else "Ingresar",
                        fontWeight = FontWeight.Bold
                    )
                }

                TextButton(
                    enabled = !viewModel.procesando,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        esRegistro = !esRegistro
                        mostrarClave = false
                        viewModel.limpiarMensajes()
                    }
                ) {
                    Text(
                        if (esRegistro) "Ya tengo cuenta. Iniciar sesion" else "No tengo cuenta. Registrarse"
                    )
                }
            }
        }
    }
}