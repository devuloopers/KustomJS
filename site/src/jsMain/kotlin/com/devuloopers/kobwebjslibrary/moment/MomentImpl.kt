package com.devuloopers.kobwebjslibrary.moment

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.text.SpanText
import moment
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.justifyContent
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.TextArea
import sendOTP
import setupRecaptcha
import verifyOTP

@Composable
fun MomentImpl() {
    var phoneNumber by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf("") }
    val currentTime = moment().format("YYYY-MM-DD HH:mm:ss")
    val captchaInstance = remember { mutableStateOf<dynamic>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SpanText(
            modifier = Modifier.fontSize(50.px).color(Colors.Black).fontWeight(FontWeight.ExtraBold),
            text = currentTime
        )

        textField1(modifier = Modifier, height = 100.px, width = 300.px, textHint = "Phone Number") {
            phoneNumber = it
        }
        textField1(modifier = Modifier, height = 100.px, width = 300.px, textHint = "OTP") {
            otp = it
        }
        Box(modifier = Modifier.id("recaptcha").height(100.px).width(15.vw))
        ButtonType3(
            modifier = Modifier.padding(leftRight = 5.vw, topBottom = 3.vh).onClick {
                console.log("Clicked sendOtp")
                console.log(captchaInstance)
                sendOtpImpl(phoneNumber = phoneNumber, recaptchaVerifier = captchaInstance.value) {
                    verificationId = it as String
                }
            },
            textModifier = Modifier,
            text = "Send otp"
        )
        ButtonType3(
            modifier = Modifier.padding(leftRight = 5.vw, topBottom = 3.vh).onClick {
                verifyOtpImpl(verificationId = verificationId, otp = otp)
            },
            textModifier = Modifier,
            text = "Verify otp"
        )
    }
    LaunchedEffect(Unit) {

            // Initialize reCAPTCHA only once
            captchaInstance.value = initializeRecaptcha("recaptcha")

    }
}

fun initializeRecaptcha(containerId: String): dynamic {
    return setupRecaptcha(containerId)
}

fun sendOtpImpl(
    phoneNumber: String,
    recaptchaVerifier: dynamic,
    returnVerificationId: (dynamic) -> Unit
) {
    //val recaptchaVerifier = setupRecaptcha(containerId)
    sendOTP(phoneNumber, recaptchaVerifier).then { verificationId ->
        println("OTP sent successfully. Verification ID (Kotlin): $verificationId")
        returnVerificationId(verificationId)
    }.catch { error ->
        console.error("Error sending OTP: $error")
    }
}

fun verifyOtpImpl(verificationId: String, otp: String) {
    verifyOTP(verificationId, otp).then { user ->
        console.log("Kotlin User Log - $user")
    }
}

@Composable
fun textField1(
    modifier: Modifier = Modifier,
    height: CSSNumeric,
    width: CSSNumeric,
    textHint: String,
    output: ((String) -> Unit)? = null
) {
    var text by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }

    val fontSize = (height * 0.4)
    val padding = (height * 0.2)

    // Container for positioning and styling
    Box(modifier = modifier.styleModifier {
        height(height)
        width(width)
        position(Position.Relative)
        alignItems(AlignItems.Center)
        justifyContent(org.jetbrains.compose.web.css.JustifyContent.Center)
    }, contentAlignment = Alignment.CenterStart) {
        TextArea(
            value = text,
            Modifier.fillMaxHeight().fillMaxWidth().styleModifier {
                padding(padding)
                border(1.px, LineStyle.Solid, Colors.LightGray)
                borderRadius(8.px)
                boxSizing(BoxSizing.BorderBox)
                resize(Resize.None)
                fontSize(fontSize)
                fontFamily("Inter")
                letterSpacing(0.5.px)
                lineHeight(1.5.em)
                color(Colors.Black)
                outline(style = LineStyle.None)
                overflow(Overflow.Hidden)
                whiteSpace(WhiteSpace.NoWrap)
                textOverflow(TextOverflow.Ellipsis)
            }.toAttrs {
                onInput { event ->
                    text = event.target.asDynamic()?.value as? String ?: ""
                }
                onFocus { _ ->
                    isFocused = true
                }
                onBlur { _ ->
                    isFocused = false
                }
                placeholder(textHint)
            }
        )
    }
    if (output != null) {
        output(text)
    }
}

@Composable
fun ButtonType3(
    modifier: Modifier,
    textModifier: Modifier,
    text: String
) {
    Button(
        attrs = modifier.cursor(Cursor.Pointer).boxShadow(
            offsetX = 0.px,            // No horizontal offset
            offsetY = 8.px,            // Vertical shadow below the button
            blurRadius = 10.px,        // Soft shadow blur
            spreadRadius = 0.px,       // Keeps shadow contained
            color = Color.rgba(0, 0, 0, 0.2f) // Light gray shadow with transparency
        ).toAttrs(),
    ) {
        SpanText(modifier = textModifier, text = text)
    }
}
