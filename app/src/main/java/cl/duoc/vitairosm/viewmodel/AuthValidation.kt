package cl.duoc.vitairosm.viewmodel

object AuthValidation {
    fun nombre(valor: String): String? =
        if (valor.isBlank()) "Ingresa tu nombre" else null

    fun correo(valor: String): String? = when {
        valor.isBlank() -> "Ingresa tu correo"
        !Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$").matches(valor.trim()) -> "Ingresa un correo válido"
        else -> null
    }

    fun contrasena(valor: String, esRegistro: Boolean): String? = when {
        valor.isBlank() -> "Ingresa tu contraseña"
        esRegistro && valor.trim().length < 8 -> "Usa al menos 8 caracteres"
        else -> null
    }
}
