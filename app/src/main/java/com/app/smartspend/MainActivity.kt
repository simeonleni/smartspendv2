@file:OptIn(ExperimentalMaterial3Api::class)

package com.app.smartspend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.app.smartspend.pages.Add
import com.app.smartspend.pages.Categories
import com.app.smartspend.pages.Home
import com.app.smartspend.pages.Reports
import com.app.smartspend.pages.Settings
import com.app.smartspend.ui.theme.SmartSpend
import com.app.smartspend.ui.theme.TopAppBarBackground
import com.app.smartspend.viewmodels.CategoriesViewModel
import io.sentry.compose.withSentryObservableEffect


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartSpend {
                var showBottomBar by rememberSaveable { mutableStateOf(true) }
                val navController = rememberNavController().withSentryObservableEffect()
                val backStackEntry by navController.currentBackStackEntryAsState()

                showBottomBar = when (backStackEntry?.destination?.route) {
                    "settings/categories" -> false
                    else -> true
                }

                Scaffold(bottomBar = {
                    if (showBottomBar) {
                        NavigationBar(containerColor = TopAppBarBackground) {

                            NavigationBarItem(selected = backStackEntry?.destination?.route == "home",
                                onClick = { navController.navigate("home") },
                                label = {
                                    Text("home")
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Home,
                                        contentDescription = "home"
                                    )
                                })

                            NavigationBarItem(selected = backStackEntry?.destination?.route == "reports",
                                onClick = { navController.navigate("reports") },
                                label = {
                                    Text("Reports")
                                },
                                icon = {
                                    //Use appropriate icon for the chart, extend the material icons
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Reports"
                                    )
                                })

                            NavigationBarItem(selected = backStackEntry?.destination?.route == "add",
                                onClick = { navController.navigate("add") },
                                label = {
                                    Text("Add")
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add"
                                    )
                                })
                            NavigationBarItem(selected = backStackEntry?.destination?.route?.startsWith(
                                "settings"
                            ) ?: false,
                                onClick = { navController.navigate("settings") },
                                label = {
                                    Text("Settings")
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = "Settings"
                                    )
                                })
                        }
                    }
                }) { innerPadding ->
                    NavHost(
                        navController = navController, startDestination = "home"
                    ) {
                        composable("home") {
                            Surface(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                            ) {
                                Home(CategoriesViewModel())
                            }
                        }
                        composable("reports") {
                            Surface(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                            ) {
                                Reports()
                            }
                        }
                        composable("add") {
                            Surface(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                            ) {
                                Add(navController)
                            }
                        }
                        composable("settings") {
                            Surface(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                            ) {
                                Settings(navController)
                            }
                        }
                        composable("settings/categories") {
                            Surface(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                            ) {
                                Categories(navController)
                            }
                        }
                    }
                }
            }
        }
    }
}
