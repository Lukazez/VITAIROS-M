# VITAIROS-M

Aplicación Android de orientación y acompañamiento emocional para el proyecto académico **DSY1105**. Desarrollada en Kotlin con Jetpack Compose, Navigation, ViewModels, Room y WorkManager.

## Funciones

- Bienvenida con aceptación del alcance de la demostración.
- Registro e inicio de sesión local: errores por campo, correo válido, nombre requerido y mínimo de 8 caracteres para cuentas nuevas.
- Registro emocional con estado de ánimo, categoría opcional y nota de hasta 200 caracteres; historial, filtros, resumen y borrado con confirmación.
- Historial y foto de perfil separados por cuenta. Cerrar sesión elimina el estado de sesión; los datos locales se conservan.
- Foto mediante una aplicación de cámara y `FileProvider`: guardado privado, reducción de tamaño, orientación EXIF y reemplazo atómico. Cancelar conserva la foto anterior.
- Recordatorio diario opcional en este dispositivo, prueba inmediata y acceso a ajustes de notificaciones. La tarea se repite aproximadamente cada 24 horas; Android puede retrasarla. No es una alarma de hora exacta.
- Biblioteca de recursos, chat de respuestas predefinidas y catálogo con simulación de reserva.

**Alcance:** proyecto académico local, sin servidor, IA generativa, contacto con profesionales ni reservas reales. No realiza diagnósticos ni reemplaza la atención profesional. Usar datos ficticios para la demostración.

## Ejecutar en Android Studio

1. Abrir la carpeta raíz que contiene `settings.gradle.kts`.
2. Usar el JDK incluido con Android Studio y tener instalado Android SDK 37. La aplicación admite Android 8.0/API 26 o posterior.
3. Sincronizar Gradle. La primera sincronización requiere acceso a los repositorios de dependencias.
4. Elegir un emulador o dispositivo y ejecutar la configuración `app` con **Run**. Para actualizar una instalación anterior, conservar sus datos: no hace falta desinstalarla.
5. Aceptar la bienvenida, crear una cuenta de prueba e iniciar sesión.

Para mostrar la cámara en un emulador, habilitar una cámara en su configuración. El permiso de notificaciones se solicita al activar el recordatorio o probarlo, en Android 13 o superior. La app sigue funcionando si se rechaza.

## Organización

| Ubicación | Responsabilidad |
|---|---|
| `MainActivity.kt` | Sesión compartida, navegación y transiciones |
| `ui/screens/` | Pantallas Compose, componentes y retroalimentación |
| `ui/theme/` | Colores y tipografía |
| `viewmodel/` | Estado observable, validaciones y operaciones de cada flujo |
| `model/` | Entidades y DAO de Room, migraciones, repositorios y hash de contraseñas |
| `worker/` | Programación y entrega de recordatorios |
| `src/test/` y `src/androidTest/` | Pruebas unitarias y en dispositivo |

Las rutas son relativas a `app/src/main/java/cl/duoc/vitairosm/`, excepto las pruebas.

## Persistencia y recursos del dispositivo

Las fotos quedan en el almacenamiento privado de la app, asociadas al ID de usuario. Solo se comparte con la cámara el archivo temporal de captura. No se solicita permiso propio de cámara ni almacenamiento porque la captura se delega a otra aplicación. Se corrige orientación y se recodifica sin copiar metadatos de ubicación. Las copias de seguridad automáticas y la transferencia de estos datos se excluyen en la configuración de Android.

Las notificaciones muestran un texto genérico, sin datos del historial. La preferencia de recordatorios es del dispositivo, se conserva entre sesiones y se puede desactivar desde Inicio. WorkManager evita duplicar la tarea al activarla varias veces.

.

