package com.app.smartspend.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.smartspend.components.TableRow
import com.app.smartspend.db
import com.app.smartspend.models.Category
import com.app.smartspend.models.Expense
import com.app.smartspend.ui.theme.BackgroundElevated
import com.app.smartspend.ui.theme.DividerColor
import com.app.smartspend.ui.theme.Shapes
import com.app.smartspend.ui.theme.TopAppBarBackground
import io.realm.kotlin.ext.query
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(navController: NavController) {
    val coroutineScope = rememberCoroutineScope()
    var deleteConfirmationShowing by remember {
        mutableStateOf(false)
    }

    val eraseAllData: () -> Unit = {
        coroutineScope.launch {
            db.write {
                val expenses = this.query<Expense>().find()
                val categories = this.query<Category>().find()

                delete(expenses)
                delete(categories)

                deleteConfirmationShowing = false
            }
        }
    }

    Scaffold(topBar = {
        MediumTopAppBar(
            title = { Text("Settings") }, colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = TopAppBarBackground
            )
        )
    }, content = { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(Shapes.large)
                    .background(BackgroundElevated)
                    .fillMaxWidth()
            ) {
                TableRow(label = "Categories", hasArrow = true, modifier = Modifier.clickable {
                    navController.navigate("settings/categories")
                })
                Divider(
                    modifier = Modifier.padding(start = 16.dp),
                    thickness = 1.dp,
                    color = DividerColor
                )
                TableRow(label = "Erase all data",
                    isDestructive = true,
                    modifier = Modifier.clickable {
                        // Show delete confirmation dialog
                        deleteConfirmationShowing = true
                    })

                // Display delete confirmation dialog if needed
                if (deleteConfirmationShowing) {
                    AlertDialog(onDismissRequest = { deleteConfirmationShowing = false },
                        title = { Text("Are you sure?") },
                        text = { Text("This action cannot be undone.") },
                        confirmButton = {
                            TextButton(onClick = eraseAllData) {
                                Text("Delete everything")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { deleteConfirmationShowing = false }) {
                                Text("Cancel")
                            }
                        })
                }
            }
        }
    })
}
