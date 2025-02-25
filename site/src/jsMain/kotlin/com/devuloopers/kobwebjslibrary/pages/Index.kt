package com.devuloopers.kobwebjslibrary.pages

import androidx.compose.runtime.*
import com.devuloopers.kobwebjslibrary.moment.MomentImpl
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.core.Page
import org.jetbrains.compose.web.dom.Text
import com.varabyte.kobweb.worker.rememberWorker
import com.devuloopers.kobwebjslibrary.worker.EchoWorker

@Page
@Composable
fun HomePage() {
    val worker = rememberWorker { EchoWorker { output -> console.log("Echoed: $output") } }
    LaunchedEffect(Unit) {
        worker.postInput("Hello, worker!")
    }

    // TODO: Replace the following with your own content
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("THIS PAGE INTENTIONALLY LEFT BLANK")
        MomentImpl()
    }
}
