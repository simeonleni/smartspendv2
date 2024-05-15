@file:OptIn(ExperimentalMaterial3Api::class)

package com.app.smartspend.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nikolovlazar.smartspend.models.Category
import com.nikolovlazar.smartspend.ui.theme.TopAppBarBackground
import com.nikolovlazar.smartspend.viewmodels.CategoriesViewModel
import com.nikolovlazar.smartspend.viewmodels.ExpensesViewModel

@ExperimentalMaterial3Api
@Composable
fun Home(
    categoriesViewModel: CategoriesViewModel = viewModel(),
    expensesViewModel: ExpensesViewModel = viewModel()
) {
    val categoriesState = categoriesViewModel.uiState.collectAsState()
    val expensesState = expensesViewModel.uiState.collectAsState()
    Scaffold(topBar = {
        MediumTopAppBar(
            title = { Text("Expenses") }, colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = TopAppBarBackground
            )
        )
    },

        content = {
            Column(
                modifier = Modifier
                    .padding(top = it.calculateTopPadding())
                    .fillMaxSize()
            ) {
                Text(text = "Categories", modifier = Modifier.padding(bottom = 8.dp))

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categoriesState.value.categories) { category ->
                        CategoryItem(category = category)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { /* TODO: Implement budget creation */ }) {
                    Text(text = "Create Budget")
                }

                Spacer(modifier = Modifier.height(16.dp))

                val totalExpenses = expensesState.value.sumTotal
                val budgetValue = 0 // Replace with actual budget value

                Text(
                    text = "Difference: ${budgetValue - totalExpenses}",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    )
}

@Composable
fun CategoryItem(category: Category) {
    Text(text = category.name,
        modifier = Modifier
            .padding(8.dp)
            .clickable { /* TODO: Filter expenses based on the selected category */ })
}

@Preview
@Composable
fun HomePreview(){
    Home(categoriesViewModel = CategoriesViewModel(), expensesViewModel = ExpensesViewModel())
}