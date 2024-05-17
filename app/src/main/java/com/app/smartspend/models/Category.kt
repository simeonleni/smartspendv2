package com.app.smartspend.models

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.smartspend.viewmodels.ExpensesViewModel
import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Category() : RealmObject {
  @PrimaryKey
  var _id: ObjectId = ObjectId.create()

  private var _colorValue: String = "0,0,0"
  var name: String = ""
  val color: Color
    get() {
      val colorComponents = _colorValue.split(",")
      val (red, green, blue) = colorComponents
      return Color(red.toFloat(), green.toFloat(), blue.toFloat())
    }

  constructor(
    name: String,
    color: Color
  ) : this() {
    this.name = name
    this._colorValue = "${color.red},${color.green},${color.blue}"
  }
}
@Composable
fun CategoryItem(
  category: Category,
  expensesViewModel: ExpensesViewModel = viewModel()
) {
  Text(
    text = category.name,
    modifier = Modifier
      .padding(8.dp)
      .clickable {
        expensesViewModel.filterExpensesByCategory(category.name)
      }
  )
}

@Preview
@Composable
fun CategoryItemPreview() {
  CategoryItem(category = Category(name = "Groceries", color = Color.Unspecified))
}
