package com.example.financeapp.data.model
/**
 * ExpenseCategory - Enumeración de categorías de gastos
 * */
enum class ExpenseCategory(val displayName: String) {
    FOOD("Alimentación"),
    TRANSPORT("Transporte"),
    ENTERTAINMENT("Entretenimiento"),
    BILLS("Servicios"),
    SHOPPING("Compras"),
    HEALTH("Salud"),
    EDUCATION("Educación"),
    Travel("Viajes"),
    OTHER("Otros")
}