package com.app.smartspend.models

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

class Expense() : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.create()
    var amount: Double = 0.0

    private var _recurrenceName: String = "None"
    val recurrence: Recurrence
        get() {
            return _recurrenceName.toRecurrence()
        }

    private var _dateValue: String = LocalDateTime.now().toString()
    val date: LocalDateTime
        get() {
            return LocalDateTime.parse(_dateValue)
        }

    var note: String = ""
    var category: Category? = null

    constructor(
        amount: Double,
        recurrence: Recurrence,
        date: LocalDateTime,
        note: String,
        category: Category,
    ) : this() {
        this.amount = amount
        this._recurrenceName = recurrence.name
        this._dateValue = date.toString()
        this.note = note
        this.category = category
    }
}

data class DayExpenses(
    val expenses: List<Any>,
    var total: Double,
)

fun List<Expense>.groupedByDay(): Map<LocalDate, DayExpenses> {
    return this.groupBy({ it.date.toLocalDate() }, { it })
        .mapValues { (_, expenses) ->
            DayExpenses(
                expenses = expenses.sortedBy { it.date }.toMutableList(),
                total = expenses.sumOf { it.amount }
            )
        }
        .toSortedMap(compareByDescending { it })
}

fun List<Expense>.groupedByDayOfWeek(): Map<String, DayExpenses> {
    return this.groupBy({ it.date.toLocalDate().dayOfWeek.name }, { it })
        .mapValues { (_, expenses) ->
            DayExpenses(
                expenses = expenses.toMutableList(),
                total = expenses.sumOf { it.amount }
            )
        }
}

fun List<Expense>.groupedByDayOfMonth(): Map<Int, DayExpenses> {
    return this.groupBy({ it.date.toLocalDate().dayOfMonth }, { it })
        .mapValues { (_, expenses) ->
            DayExpenses(
                expenses = expenses.toMutableList(),
                total = expenses.sumOf { it.amount }
            )
        }
}

fun List<Expense>.groupedByMonth(): Map<String, DayExpenses> {
    return this.groupBy({ it.date.toLocalDate().month.name }, { it })
        .mapValues { (_, expenses) ->
            DayExpenses(
                expenses = expenses.toMutableList(),
                total = expenses.sumOf { it.amount }
            )
        }
}

fun List<Expense>.groupedByCategory(): Map<String, DayExpenses> {
    return this.groupBy({ it.category?.name ?: "Uncategorized" }, { it })
        .mapValues { (_, expenses) ->
            DayExpenses(
                expenses = expenses.toMutableList(),
                total = expenses.sumOf { it.amount }
            )
        }
}
