# Evidencias de VITAIROS-M — DSY1105

Revisión de cierre: 27 de octubre de 2026. Se recibieron los siete encabezados de la rúbrica y sus pesos, sin los descriptores de los niveles de logro. Esta tabla identifica implementación y evidencia; no asigna una nota ni garantiza un nivel de evaluación.

## Tabla de criterios

Las rutas Kotlin se encuentran dentro de `app/src/main/java/cl/duoc/vitairosm/`.

| Criterio y peso | Implementación | Evidencia para mostrar |
|---|---|---|
| IE 2.1.1 · Interfaz, jerarquía y navegación · 15 % | Bienvenida, acceso, inicio, registro, recursos, chat y catálogo; tema coherente, desplazamiento e insets | Recorrido entre pantallas, Atrás y campos con teclado. `MainActivity`, `ui/screens`, `ui/theme` |
| IE 2.1.2 · Formularios, validaciones visuales, feedback e íconos · 15 % | Errores por campo, íconos de nombre/correo/contraseña/nota, mostrar clave, indicadores de proceso y guardado | Registro vacío, correo inválido, clave de un carácter, nota de 201 caracteres y formulario válido. `AuthScreen`, `CheckInScreen`, `FormFeedback` |
| IE 2.2.1 · Validación centralizada y estados · 10 % | `AuthValidation` y `CheckInValidation`; ViewModels validan, mantienen datos ante fallos y bloquean envíos duplicados | Pruebas de límites, fallo de guardado, reintento y doble envío; observar errores y botones durante el proceso |
| IE 2.2.2 · Animaciones funcionales · 10 % | Transiciones fade, aparición de mensajes y ajuste animado del contenido; progreso durante operaciones | Navegar, alternar crear cuenta/iniciar sesión, provocar/corregir errores y guardar |
| IE 2.3.1 · Modularidad y persistencia local · 15 % | UI/ViewModels/repositorios/DAO separados; Room v4; cuentas, registros y fotos privados por usuario | Cuentas A/B, reinicio, consulta/borrado aislados, migraciones 2→4 y 3→4, hash y cámara |
| IE 2.3.2 · Colaboración y control de versiones · 20 % | Historia conservada con aportes de ambos integrantes y PR #1 integrado | Historial Git, commits de David y Lukazez, descripción de reparto en README y PR de cierre cuando se publique |
| IE 2.4.1 · Dos recursos del dispositivo · 15 % | Cámara para foto de perfil y notificaciones optativas | Foto aceptada/cancelada y por cuenta; aviso inmediato con permiso, rechazo sin bloqueo, activar/desactivar tarea periódica |

## Pruebas de cierre

- ✅ Compilación de aplicación y APK de pruebas correcta.
- ✅ Seis pruebas unitarias aprobadas: validación de cuentas, límites del formulario emocional, hash/verificación y prueba base.
- ✅ Diecisiete pruebas instrumentadas aprobadas en emulador API 37: siete de fotos en repositorio, dos de su ViewModel, una de aislamiento de registros, dos de migraciones, tres de flujo de registro/autenticación/programación y dos de recordatorios.
- ✅ Migraciones desde versiones 2 y 3 hasta versión 4: se conservan cuentas y registros, funciona la clave antigua corta, no se reasignan registros sin propietario y el hash no se vuelve a transformar al reabrir la base.
- ✅ Un fallo de guardado conserva lo escrito; reintentar funciona; dos pulsaciones no insertan dos registros.
- ✅ Recordatorio único al activarlo varias veces, cancelación al desactivar, ausencia de permiso sin excepción y notificación con permiso. Se inició esta comprobación con el permiso revocado en la app aislada.
- ✅ Revisión visual: bienvenida, errores por campo, contraseña corta rechazada, registro e inicio de sesión, formulario con teclado y guardado en historial. Se comprobó también una pantalla de 360 × 640 dp.
- ✅ Recorrido visual por biblioteca, chat y catálogo; regreso a Inicio, diálogo explícito de reserva simulada y botón de prueba de notificación.
- ✅ Lint sin errores; quedan advertencias de recursos sin uso, dependencias/configuración heredadas y recomendaciones menores.

Las pruebas Android se ejecutaron con `adb` y `AndroidJUnitRunner`, seleccionando las seis clases funcionales. No se usó Espresso para automatizar Compose en API 37; la revisión de interfaz se realizó con el árbol de accesibilidad de Android y capturas. Los comandos y la salida del runner están conservados en la copia de trabajo de verificación.

### Capturas de evidencia

Datos ficticios de la instalación aislada:

- [Validación visual por campo](imagenes/formulario-errores.png).
- [Rechazo de contraseña corta](imagenes/clave-corta.png).
- [Registro emocional con teclado en pantalla pequeña](imagenes/registro-teclado.png).

Las pruebas en emulador usan un identificador aislado, `cl.duoc.vitairosm.codexfinaltest`, solo en la copia de verificación. La aplicación entregada conserva `cl.duoc.vitairosm`. Las pruebas de migración usan bases temporales y las de cámara usan archivos temporales de prueba.

## Guion de demostración

1. Abrir la app, leer y aceptar la bienvenida. Mostrar el alcance de la demostración.
2. Ir a crear cuenta. Probar campos vacíos, correo sin formato y contraseña corta; corregirlos y registrar una cuenta ficticia A.
3. Iniciar sesión, tomar una foto, aceptarla y después cancelar un cambio de foto. La foto anterior debe conservarse.
4. Entrar a registrar ánimo, intentar guardar sin selección y mostrar el error. Seleccionar un ánimo, categoría y nota; guardar y comprobar historial/resumen.
5. Crear un segundo registro, filtrar por categoría y borrar uno con confirmación. Comprobar el resultado.
6. Cerrar sesión, crear/entrar con cuenta B y comprobar que no ve historial ni foto de A. Volver a A y verificar que mantiene sus datos.
7. Activar el recordatorio y conceder permiso, pulsar **Probar notificación**, abrir el aviso y volver a la app. Desactivarlo. Repetir la prueba con las notificaciones bloqueadas desde Ajustes: el resto de la app debe seguir funcionando.
8. Abrir recursos, chat y catálogo, volver y realizar una reserva simulada. Explicar que son contenidos de demostración.
9. Cerrar y abrir la app; iniciar sesión y comprobar persistencia. Mostrar Git, commits de ambos integrantes y el PR.

La cámara ya tuvo confirmación manual de funcionamiento el 26 de octubre. Las pruebas automatizadas de repositorio/ViewModel cubren archivos, cancelación y fallos; no sustituyen mostrar el obturador de una app de cámara real en la presentación. La tarea diaria se verifica por su programación: una prueba inmediata no demuestra haber esperado 24 horas.

## Entrega y sincronización

Antes de integrar cambios, ejecutar `git status`, revisar `git diff` y publicar la rama de trabajo con una descripción y los resultados reales. Revisar el PR antes de integrarlo. Para sincronizar `main` después de la integración, partir de un árbol limpio, hacer `git fetch origin`, cambiar a `main` y usar `git pull --ff-only origin main`. Si Git informa cambios locales o divergencia, conservarlos y revisarlos antes de continuar.

Los dos documentos locales de contexto del asistente no forman parte de la entrega. No subir configuraciones locales, bases, fotos, contraseñas ni `local.properties`.

## Límites conocidos

- No hay servidor, sincronización entre equipos, recuperación de contraseña ni sesiones persistentes.
- Chat y reserva son simulaciones explícitas. El catálogo no realiza contacto profesional.
- Los registros heredados sin propietario se conservan, pero no se muestran a ninguna cuenta.
- Las contraseñas se almacenan como hash; la base completa no está cifrada.
- El recordatorio depende de permisos, canal y gestión de batería de Android. No promete una hora exacta.
- La configuración heredada de Gradle/Kotlin/KSP mantiene advertencias de actualización; registrar si afectan una compilación futura.
