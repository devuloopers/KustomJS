# KustomJS

Sample project demonstrating custom JavaScript usage in Kotlin/JS projects, with a focus on Firebase Mobile OTP authentication.

## Instructions for Using Custom JavaScript in Kotlin/JS

### 1. Create a Folder for JavaScript Scripts
- Choose a location within `site/src/jsMain/resources/` to store custom JavaScript files
- Place scripts inside your desired folder

### 2. Set Gradle Dependencies
Add the following to your Gradle configuration to adding all the JS files inside the build directory:
```kotlin
val customJsDir = rootProject.projectDir.resolve("site/src/jsMain/resources/customJs") //Example Directory
customJsDir.listFiles { file -> file.extension == "js" }?.forEach { jsFile ->
    implementation(npm(jsFile.name, "file:$customJsDir"))
}
implementation(npm("firebase", "11.2.0"))
```

### 3. Install Firebase
Install the latest Firebase package at the project root also:
```bash
npm install firebase@latest
```

## JavaScript Interoperability Example

### JavaScript File (FirebaseJS.js)
```javascript
import {initializeApp} from "firebase/app";
import {getFirestore, collection, addDoc} from "firebase/firestore";
import {
    getAuth,
    PhoneAuthProvider,
    RecaptchaVerifier,
    signInWithPhoneNumber,
    signInWithCredential
} from "firebase/auth";

// Firebase configuration
const firebaseConfig = {
    apiKey: "",
    authDomain: "",
    projectId: "",
    // ... other config values
};

// Initialize Firebase
export const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);
export const db = getFirestore(app);

// Example functions for OTP authentication
export async function sendOTP(phoneNumber, recaptchaVerifier) {
    try {
        const confirmationResult = 
            await signInWithPhoneNumber(auth, phoneNumber, recaptchaVerifier);
        return confirmationResult.verificationId;
    } catch (error) {
        console.error("Error sending OTP:", error);
        throw error;
    }
}

export async function verifyOTP(verificationId, otp) {
    try {
        const credential = PhoneAuthProvider.credential(verificationId, otp);
        const userCredential = await signInWithCredential(auth, credential);
        return JSON.stringify(userCredential.user);
    } catch (error) {
        console.error("Error verifying OTP:", error);
        throw error;
    }
}
```

### Kotlin Interop File
```kotlin
@file:JsModule("./customJs/FirebaseJS.js")
@file:JsNonModule

// External declarations
external val app: dynamic
external val db: dynamic
external fun sendOTP(phoneNumber: String, recaptchaVerifier: dynamic): dynamic
external fun verifyOTP(verificationId: String, otp: String): dynamic

// Kotlin implementation functions
fun sendOtpImpl(
    phoneNumber: String,
    recaptchaVerifier: dynamic,
    returnVerificationId: (dynamic) -> Unit
) {
    sendOTP(phoneNumber, recaptchaVerifier).then { verificationId ->
        println("OTP sent successfully. Verification ID: $verificationId")
        returnVerificationId(verificationId)
    }.catch { error ->
        console.error("Error sending OTP: $error")
    }
}

fun verifyOtpImpl(verificationId: String, otp: String) {
    verifyOTP(verificationId, otp).then { user ->
        console.log("User verified: $user")
    }
}
```

## Key Considerations
- Use `@JsModule` and `@JsNonModule` for JavaScript interoperability
- Declare external functions with matching signatures
- Handle asynchronous operations using `.then()` and `.catch()`
- Use `dynamic` type for flexible JavaScript interop