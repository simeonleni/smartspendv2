package com.app.smartspend.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.app.smartspend.models.DayExpenses
import com.app.smartspend.models.Expense
import com.app.smartspend.ui.theme.LabelSecondary
import com.app.smartspend.ui.theme.Typography
import com.app.smartspend.utils.formatDay
import java.text.DecimalFormat
import java.time.LocalDate

@Composable
fun ExpensesDayGroup(
  date: LocalDate,
  dayExpenses: DayExpenses,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier) {
    Text(
      text = date.formatDay(),
      style = Typography.headlineLarge,
      color = LabelSecondary,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis
    )
    Divider(modifier = Modifier.padding(vertical = 4.dp))
    dayExpenses.expenses.forEach { expense ->
      if (expense is Expense) { // Check if the element is an Expense
        ExpenseRow(
          expense = expense,
          modifier = Modifier.padding(top = 8.dp)
        )
      }
    }

    Divider(modifier = Modifier.padding(vertical = 4.dp))
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text("Total:", style = Typography.bodyMedium, color = LabelSecondary)
      Text(
        text = DecimalFormat("USD 0.#").format(dayExpenses.total),
        style = Typography.headlineLarge,
        color = LabelSecondary
      )
    }
  }
}
