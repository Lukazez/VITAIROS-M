package cl.duoc.vitairosm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import cl.duoc.vitairosm.model.AppDatabase
import cl.duoc.vitairosm.ui.screens.AuthScreen
import cl.duoc.vitairosm.ui.screens.CheckInScreen
import cl.duoc.vitairosm.ui.screens.HomeScreen
import cl.duoc.vitairosm.ui.theme.VITAIROSMTheme
import cl.duoc.vitairosm.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VITAIROSMTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "auth") {

        composable("auth") {
            val context = LocalContext.current

            val authViewModel: AuthViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        val db = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "vitairos_db"
                        ).build()

                        // NOTA: Si en tu AppDatabase la funcion se llama usuarioDAO() en mayusculas,
                        // cambialo aqui abajo. Por convencion suele ser usuarioDao().
                        return AuthViewModel(db.usuarioDao()) as T
                    }
                }
            )

            AuthScreen(
                viewModel = authViewModel,
                onLoginExitoso = {
                    navController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(
                onVolver = {
                    navController.navigate("auth") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onRegistrar = { navController.navigate("checkin") },
                ultimoAnimo = "",
                ultimaCategoria = "",
                ultimaNota = ""
            )
        }

        composable("checkin") {
            CheckInScreen(
                onVolver = { navController.popBackStack() },
                onGuardar = { _, _, _ ->
                    navController.popBackStack()
                }
            )
        }
    }
}