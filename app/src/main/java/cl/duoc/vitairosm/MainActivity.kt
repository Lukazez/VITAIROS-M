package cl.duoc.vitairosm

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
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
import cl.duoc.vitairosm.ui.screens.ChatScreen
import cl.duoc.vitairosm.ui.screens.HomeScreen
import cl.duoc.vitairosm.ui.screens.ProfesionalesScreen
import cl.duoc.vitairosm.ui.screens.RecursosScreen
import cl.duoc.vitairosm.ui.theme.VITAIROSMTheme
import cl.duoc.vitairosm.viewmodel.AuthViewModel
import cl.duoc.vitairosm.viewmodel.CheckInViewModel
import cl.duoc.vitairosm.viewmodel.HomeViewModel

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

// se conecta todas las pantallas y definimos los caminos por donde navega el usuario
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
    }

    // permiso para las notificaciones si el telefono es moderno
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionCheck = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            )
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // mapa de navegacion con animaciones suaves de entrada y salida
    NavHost(
        navController = navController,
        startDestination = "auth",
        enterTransition = { fadeIn(animationSpec = tween(400)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) },
        popEnterTransition = { fadeIn(animationSpec = tween(400)) },
        popExitTransition = { fadeOut(animationSpec = tween(300)) }
    ) {

        // inicio de sesion
        composable("auth") {
            val authViewModel: AuthViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        val db = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "vitairos_db"
                        )
                            .fallbackToDestructiveMigration()
                            .build()

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

        // pantalla principal o panel de control
        composable("home") {
            val homeViewModel: HomeViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        val db = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "vitairos_db"
                        )
                            .fallbackToDestructiveMigration()
                            .build()

                        return HomeViewModel(db.registroDao()) as T
                    }
                }
            )

            LaunchedEffect(Unit) {
                homeViewModel.programarRecordatorioDiario(context)
            }

            val historial by homeViewModel.historial.collectAsState()

            HomeScreen(
                onVolver = {
                    navController.navigate("auth") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onRegistrar = { navController.navigate("checkin") },
                onIrAlChat = { navController.navigate("chat") },
                onIrARecursos = { navController.navigate("recursos") },
                onIrAProfesionales = { navController.navigate("profesionales") },
                historialRegistros = historial,
                onBorrarRegistro = { id -> homeViewModel.borrarRegistro(id) }
            )
        }

        // registrar el estado de animo
        composable("checkin") {
            val checkInViewModel: CheckInViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        val db = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "vitairos_db"
                        )
                            .fallbackToDestructiveMigration()
                            .build()

                        return CheckInViewModel(db.registroDao()) as T
                    }
                }
            )

            CheckInScreen(
                viewModel = checkInViewModel,
                onVolver = { navController.popBackStack() }
            )
        }

        // bloque del chat de orientacion
        composable("chat") {
            ChatScreen(
                onVolver = { navController.popBackStack() }
            )
        }

        // biblioteca de recursos y consejos
        composable("recursos") {
            RecursosScreen(
                onVolver = { navController.popBackStack() }
            )
        }

        // catalogo de profesionales
        composable("profesionales") {
            ProfesionalesScreen(
                onVolver = { navController.popBackStack() }
            )
        }
    }
}