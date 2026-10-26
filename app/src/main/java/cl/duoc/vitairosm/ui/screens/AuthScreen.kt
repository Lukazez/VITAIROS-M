package cl.duoc.vitairosm.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cl.duoc.vitairosm.viewmodel.AuthViewModel

@Composable
fun AuthScreen(viewModel: AuthViewModel, onLoginExitoso: () -> Unit) {
    var esRegistro by remember { mutableStateOf(false) }

    if (viewModel.usuarioLogueado != null) {
        onLoginExitoso()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (esRegistro) "Crear Cuenta" else "Iniciar Sesion",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (esRegistro) {
            OutlinedTextField(
                value = viewModel.nombre,
                onValueChange = { viewModel.actualizarNombre(it) },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        OutlinedTextField(
            value = viewModel.correo,
            onValueChange = { viewModel.actualizarCorreo(it) },
            label = { Text("Correo electronico") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = viewModel.contrasena,
            onValueChange = { viewModel.actualizarContrasena(it) },
            label = { Text("Contrasena") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (viewModel.mensajeError.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = viewModel.mensajeError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (esRegistro) {
                    viewModel.registrarUsuario()
                } else {
                    viewModel.iniciarSesion()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (esRegistro) "Registrarse" else "Ingresar")
        }

        TextButton(onClick = { esRegistro = !esRegistro }) {
            Text(if (esRegistro) "Ya tengo cuenta. Iniciar sesion" else "No tengo cuenta. Registrarse")
        }
    }
}