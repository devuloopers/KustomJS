@file:JsModule("./customJs/FirebaseJS.js")
@file:JsNonModule

// Firebase app instance
external val app: dynamic
// Firestore database instance
external val db: dynamic
// Function to add a test value
external fun addTestValue(): dynamic
external fun printToConsole(message: String)
external fun testFirebaseInitialization()
external fun addDataToFirestore(collectionName: String, data: dynamic): dynamic

// OTP functions
external fun setupRecaptcha(containerId: String): dynamic
external fun sendOTP(phoneNumber: String, recaptchaVerifier: dynamic): dynamic
external fun verifyOTP(verificationId: String, otp: String): dynamic

