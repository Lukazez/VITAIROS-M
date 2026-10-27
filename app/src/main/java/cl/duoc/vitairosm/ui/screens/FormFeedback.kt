package cl.duoc.vitairosm.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import cl.duoc.vitairosm.R

@Composable
fun FormFeedback(message: String?, error: Boolean = true) {
    AnimatedVisibility(visible = !message.isNullOrBlank()) {
        Row(Modifier.fillMaxWidth().animateContentSize().padding(vertical = 8.dp)
            .semantics { liveRegion = LiveRegionMode.Polite }) {
            Icon(painterResource(if (error) R.drawable.ic_warning else R.drawable.ic_check), null,
                Modifier.size(20.dp), tint = if (error) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(8.dp))
            Text(message.orEmpty(), color = if (error) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
        }
    }
}
