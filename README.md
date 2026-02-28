# MyContacts App

A Java-based console application for contact management, designed using clean, strictly modular Object-Oriented Programming principles.

## Features Implemented

### UC1: User Registration
* **Secure Registration:** Implemented user creation with strict encapsulation, Regex-based input validation, and SHA-256 password hashing.
* **Robust Architecture:** Utilized abstract domain models (`FreeUser`/`PremiumUser`) for varied limits and a custom `ContactAppException` for clean error handling.

### UC2: User Authentication
* **Polymorphic Architecture:** Implemented an `Authentication` interface utilizing standard OOP principles, providing separate, modular implementations for standard logins (`BasicAuth`) and simulated third-party SSO (`OAuth`). 
* **Safe State Management:** Utilized `java.util.Optional` to safely wrap login queries, protecting the application from `NullPointerExceptions` during invalid login attempts, and established a basic session management loop in the main controller.

## Tech Stack
* Java

## How to Run
1. Open the project in your IDE.
2. Run the `MyContactsApp.java` main class.