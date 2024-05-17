package com.app.smartspend.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.smartspend.db
import com.app.smartspend.models.Expense
import com.app.smartspend.models.Recurrence
import com.app.smartspend.utils.calculateDateRange
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ExpensesState(
  val recurrence: Recurrence = Recurrence.Daily,
  val sumTotal: Double = 1250.98,
  val expenses: List<Expense> = listOf()
)

class ExpensesViewModel: ViewModel() {
  private val _uiState = MutableStateFlow(ExpensesState())
  val uiState: StateFlow<ExpensesState> = _uiState.asStateFlow()

  init {
    _uiState.update { currentState ->
      currentState.copy(
        expenses = db.query<Expense>().find()
      )
    }
    viewModelScope.launch(Dispatchers.IO) {
      setRecurrence(Recurrence.Daily)
    }
  }



  fun filterExpensesByCategory(categoryName: String) {
    viewModelScope.launch(Dispatchers.IO) {
      val filteredExpenses = if (categoryName.isNotBlank()) {
        db.query<Expense>().find().filter { it.category?.name.equals(categoryName, ignoreCase = true) }
      } else {
        db.query<Expense>().find()
      }


      val sumTotal = filteredExpenses.sumOf { it.amount }

      println("Filtered Expenses for category $categoryName: $filteredExpenses")
      println("Total expenses for category $categoryName: $sumTotal")

      _uiState.value = _uiState.value.copy(
        expenses = filteredExpenses,
        sumTotal = sumTotal
      )
    }
  }



  fun setRecurrence(recurrence: Recurrence) {
    val (start, end) = calculateDateRange(recurrence, 0)

    val filteredExpenses = db.query<Expense>().find().filter { expense ->
      (expense.date.toLocalDate().isAfter(start) && expense.date.toLocalDate()
        .isBefore(end)) || expense.date.toLocalDate()
        .isEqual(start) || expense.date.toLocalDate().isEqual(end)
    }

    val sumTotal = filteredExpenses.sumOf { it.amount }

    _uiState.update { currentState ->
      currentState.copy(
        recurrence = recurrence,
        expenses = filteredExpenses,
        sumTotal = sumTotal
      )
    }
  }
}