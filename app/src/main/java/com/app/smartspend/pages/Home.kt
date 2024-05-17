@file:OptIn(ExperimentalMaterial3Api::class)

package com.app.smartspend.pages

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.smartspend.models.Category
import com.app.smartspend.models.DayExpenses
import com.app.smartspend.models.groupedByCategory
import com.app.smartspend.ui.theme.TopAppBarBackground
import com.app.smartspend.viewmodels.CategoriesViewModel
import com.app.smartspend.viewmodels.ExpensesViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    categoriesViewModel: CategoriesViewModel = viewModel(),
    expensesViewModel: ExpensesViewModel = viewModel()
) {
    val categoriesState by categoriesViewModel.uiState.collectAsState()
    val expensesState by expensesViewModel.uiState.collectAsState()
    var selectedCategory by rememberSaveable { mutableStateOf<Category?>(null) }

    Scaffold(topBar = {
        MediumTopAppBar(
            title = { Text("Home") }, colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = TopAppBarBackground
            )
        )
    }, content = { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            CategoryChips(categories = categoriesState.categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it })

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { /* TODO: Implement budget creation */ }) {
                Text(text = "Create Budget")
            }

            Spacer(modifier = Modifier.height(16.dp))

            val categoryName = selectedCategory?.name ?: "Select a category"
            val totalExpenses = selectedCategory?.let { category ->
                expensesState.expenses.groupedByCategory()
                    .getOrElse(category.name) { DayExpenses(emptyList(), 0.0) }.total
            } ?: 0.0

            Text(
                text = "Total Expenses for $categoryName: $totalExpenses",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )


// Log the selected category and total expenses
            selectedCategory?.let { category ->
                val totalExpenses = expensesState.expenses.groupedByCategory()
                    .getOrElse(category.name) { DayExpenses(emptyList(), 0.0) }.total
                Log.d("Home", "Category: ${category.name}, Total expenses: $totalExpenses")
            }

        }
    })
}


@Composable
private fun CategoryChips(
    categories: List<Category>, selectedCategory: Category?, onCategorySelected: (Category) -> Unit
) {
    Text(text = "Categories", modifier = Modifier.padding(bottom = 8.dp))

    LazyRow(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            InputChip(selected = selectedCategory == category,
                onClick = { onCategorySelected(category) },
                label = { Text(text = category.name) },
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

@Preview
@Composable
fun HomePreview() {
    Home(categoriesViewModel = CategoriesViewModel(), expensesViewModel = ExpensesViewModel())
}
