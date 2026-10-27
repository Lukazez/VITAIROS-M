package cl.duoc.vitairosm.viewmodel

object CheckInValidation {
    const val MAX_NOTE = 200
    val animos = listOf("Muy desanimado", "Desanimado", "Neutral", "Bien", "Muy bien")
    val categorias = listOf("Estudios", "Trabajo", "Relaciones", "Familia", "Sueño", "Bienestar", "Otro")
    fun animo(value: String): String? = if (value in animos) null else "Selecciona un estado de ánimo."
    fun categoria(value: String): String? = if (value.isEmpty() || value in categorias) null else "Selecciona una categoría de la lista."
    fun nota(value: String): String? = if (value.length <= MAX_NOTE) null else "La nota puede tener hasta 200 caracteres."
}
