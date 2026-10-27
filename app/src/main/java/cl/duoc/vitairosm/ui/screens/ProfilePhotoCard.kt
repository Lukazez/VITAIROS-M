package cl.duoc.vitairosm.ui.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import cl.duoc.vitairosm.viewmodel.ProfilePhotoViewModel

@Composable
fun ProfilePhotoCard(viewModel: ProfilePhotoViewModel) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        viewModel.completarCaptura(it)
    }
    Card(Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Foto de perfil", style = MaterialTheme.typography.titleMedium)
            val foto = viewModel.foto
            if (foto != null) {
                Image(
                    bitmap = foto.asImageBitmap(),
                    contentDescription = "Tu foto de perfil",
                    modifier = Modifier.size(96.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text("Aún no tienes una foto de perfil")
            }
            Button(
                enabled = !viewModel.ocupado,
                onClick = {
                    viewModel.tomarFoto { uri ->
                        val manager = context.packageManager
                        if (!manager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) ||
                            Intent(MediaStore.ACTION_IMAGE_CAPTURE).resolveActivity(manager) == null
                        ) throw ActivityNotFoundException()
                        launcher.launch(uri)
                    }
                }
            ) { Text(if (foto == null) "Tomar foto" else "Cambiar foto") }
            if (viewModel.ocupado) CircularProgressIndicator(Modifier.size(24.dp))
            viewModel.mensaje?.let {
                Text(it, modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite })
            }
        }
    }
}
