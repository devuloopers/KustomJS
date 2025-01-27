package com.devuloopers.kobwebjslibrary

import addDataToFirestore
import addTestValue
import androidx.compose.runtime.Composable
import app
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.core.App
import com.varabyte.kobweb.silk.SilkApp
import com.varabyte.kobweb.silk.components.layout.Surface
import com.varabyte.kobweb.silk.style.common.SmoothColorStyle
import com.varabyte.kobweb.silk.style.toModifier
import db
import kotlinx.browser.window
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import org.jetbrains.compose.web.css.vh
import printToConsole
import testFirebaseInitialization
import kotlin.js.json

@OptIn(DelicateCoroutinesApi::class)
@App
@Composable
fun AppEntry(content: @Composable () -> Unit) {
    SilkApp {
        Surface(SmoothColorStyle.toModifier().minHeight(100.vh)) {
            content()
            printToConsole(message = "Anant here")
            //testFirebaseInitialization()
            GlobalScope.promise {
                try {
                    val docId = addDataToFirestore(
                        "testCollection2", // Firestore collection name
                        json(
                            "name" to "John Doe",
                            "age" to 30,
                            "createdAt" to "20/01/2025"
                        ) // Data to be added
                    )
                    println("Document added with ID: $docId")
                } catch (e: Throwable) {
                    println("Error adding document: ${e.message}")
                }
            }
        }
    }
}
