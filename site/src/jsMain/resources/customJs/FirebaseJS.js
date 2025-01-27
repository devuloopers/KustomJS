import {initializeApp} from "firebase/app";
import {getFirestore, collection, addDoc} from "firebase/firestore";
import {
    getAuth,
    PhoneAuthProvider,
    RecaptchaVerifier,
    signInWithPhoneNumber,
    signInWithCredential
} from "firebase/auth";

// Firebase configuration object
const firebaseConfig = {
    apiKey: "",
    authDomain: "",
    projectId: "",
    storageBucket: "",
    messagingSenderId: "",
    appId: "",
    measurementId: ""
};

// Initialize Firebase
export const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);

// Initialize Firestore
export const db = getFirestore(app);

let recaptchaVerifierInstance = null;

export function testFirebaseInitialization() {
    console.log("Firebase App:", app);
    console.log("Firestore Instance:", db);
    console.log("Its working");
}

// Function to add a test value to Firestore
export async function addDataToFirestore(collectionName, data) {
    try {
        const docRef =
            await addDoc(collection(db, collectionName), data);
        console.log("Document written with ID: ", docRef.id);
        return docRef.id;
    } catch (e) {
        console.error("Error adding document: ", e);
        throw e;
    }
}

export function printToConsole(message) {
    console.log("Message from FirebaseJS.js:", message);
}


export function setupRecaptcha(containerId) {
    if (!recaptchaVerifierInstance) {
        recaptchaVerifierInstance = new RecaptchaVerifier(auth, containerId, {});
    }
    return recaptchaVerifierInstance;
}


// Send OTP to a phone number
export async function sendOTP(phoneNumber, recaptchaVerifier) {
    try {
        const confirmationResult =
            await signInWithPhoneNumber(auth, phoneNumber, recaptchaVerifier);
        console.log("OTP sent successfully. Verification ID:", confirmationResult.verificationId);
        return confirmationResult.verificationId;
    } catch (error) {
        console.error("Error sending OTP:", error);
        throw error;
    }
}

// Verify OTP
export async function verifyOTP(verificationId, otp) {
    try {
        const credential = PhoneAuthProvider.credential(verificationId, otp);
        const userCredential = await signInWithCredential(auth, credential);
        console.log("OTP verified successfully. User:", userCredential.user);
        return JSON.stringify(userCredential.user)
    } catch (error) {
        console.error("Error verifying OTP:", error);
        throw error;
    }
}