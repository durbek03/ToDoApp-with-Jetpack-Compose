package com.example.mvcexample.navigation

sealed class Destinations(val route: String) {
    object HomeScreen: Destinations("home")
    object ViewListScreen: Destinations("category")
    object AddTaskScreen: Destinations("addTask")
}