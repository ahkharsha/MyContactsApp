# MyContacts App

A Java-based console application for contact management, designed using clean, strictly modular Object-Oriented Programming principles.

## Features Implemented

### UC1: User Registration
* **Secure Registration:** Implemented user creation with strict encapsulation, Regex-based input validation, and SHA-256 password hashing.
* **Robust Architecture:** Utilized abstract domain models (`FreeUser`/`PremiumUser`) for varied limits and a custom `ContactAppException` for clean error handling.

### UC2: User Authentication
* **Polymorphic Architecture:** Implemented an `Authentication` interface utilizing standard OOP principles, providing separate, modular implementations for standard logins (`BasicAuth`) and simulated third-party SSO (`OAuth`). 
* **Safe State Management:** Utilized `java.util.Optional` to safely wrap login queries, protecting the application from `NullPointerExceptions` during invalid login attempts, and established a basic session management loop in the main controller.

### UC3: User Profile Management
* **State Modification:** Implemented strict setter logic to allow users to update their profile names while preventing invalid states (e.g., null or empty strings).
* **Security Best Practices:** Created a secure password update flow that actively requires hashing and verifying the current password before allowing state changes to the new password.

### UC4: Create Contact
* **Polymorphic Data Models:** Designed a hierarchical model containing an abstract `Contact` base class extended by `Person` and `Organization`, leveraging collections to store multi-value data like phone numbers and emails.
* **Data Persistence:** Implemented a robust `FileHandler` utility to securely read and write Application state (Users and Contacts) to text files, enabling long-term storage and eliminating the need for repeated manual logins during testing.

## Tech Stack
* Java

## How to Run
1. Open the project in your IDE.
2. Run the `MyContactsApp.java` main class.