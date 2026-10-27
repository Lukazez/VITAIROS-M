package cl.duoc.vitairosm.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val VitairosColors = lightColorScheme(
    primary = Color(0xFF006D85), onPrimary = Color.White,
    secondary = Color(0xFF47666F), background = Color(0xFFF2F8FB),
    surface = Color(0xFFF2F8FB), onBackground = Color(0xFF142F3A),
    onSurface = Color(0xFF142F3A), error = Color(0xFFB3261E)
)

@Composable
fun VITAIROSMTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = VitairosColors, typography = Typography, content = content)
}
