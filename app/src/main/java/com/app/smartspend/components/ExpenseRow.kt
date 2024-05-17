package com.app.smartspend.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.smartspend.models.Expense
import com.app.smartspend.ui.theme.LabelSecondary
import com.app.smartspend.ui.theme.Typography
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter

@Composable
fun ExpenseRow(expense: Expense, modifier: Modifier = Modifier) {
  Column(modifier = modifier) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = expense.note ?: expense.category?.name ?: "Unknown",
        style = Typography.headlineLarge
      )
      Text(
        text = "USD ${DecimalFormat("0.#").format(expense.amount)}",
        style = Typography.headlineLarge
      )
    }
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 6.dp),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      expense.category?.let { CategoryBadge(category = it) }
      Text(
        text = expense.date.format(DateTimeFormatter.ofPattern("HH:mm")),
        style = Typography.bodyMedium,
        color = LabelSecondary
      )
    }
  }
}