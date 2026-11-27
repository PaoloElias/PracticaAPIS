package com.example.apispractica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.apispractica.ui.PersonajeScreen
import com.example.apispractica.ui.theme.APISpracticaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            APISpracticaTheme {
                PersonajeScreen()
            }
        }
    }
}
