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

### UC5: View Contact Details
* **Polymorphic Formatting:** Bypassed complex Decorator patterns by utilizing overridden methods within the `Contact` hierarchy to dynamically generate specific console outputs for `Person` and `Organization` types.
* **Null Safety:** Integrated `java.util.Optional` to cleanly handle missing multi-value fields (like emails or phones) ensuring a safe, immutable display view without throwing exceptions.

### UC6: Edit Contact
* **Defensive State Modification:** Implemented safe setter wrappers in the service layer to validate input (e.g., null checks) before allowing mutations to the `Contact` objects, satisfying state protection requirements without relying on complex Memento patterns.
* **Real-time Synchronization:** Tied all mutation logic directly into the `FileHandler` utility, ensuring that any edits to an object's state in memory are immediately and permanently reflected in the `contacts.txt` storage file.

### UC7: Delete Contact
* **Lifecycle Management:** Implemented a Hard Delete strategy utilizing standard Java Collection `remove()` logic, avoiding the overhead of the Observer Pattern while maintaining strict synchronization with the File I/O system.
* **Safe Deletion Flow:** Built a dedicated console flow that incorporates explicit user confirmation dialogs and `NumberFormatException` handling to prevent accidental data loss.

### UC8: Bulk Operations
* **Collection Iteration:** Leveraged standard Java `for` loops and `List` structures to process bulk data operations, dynamically parsing comma-separated string inputs from the user to queue multiple objects for deletion in a single pass.
* **Data Exporting:** Implemented basic File I/O handling utilizing `FileWriter` and `PrintWriter` to iterate over user contacts and export them cleanly into an external, formatted CSV file.

### UC9: Search Contacts
* **Search Interface:** Implemented a clean `SearchFilterInterface` allowing the application to decouple search logic from iteration loops, adhering to pure OOP encapsulation rather than complicated Specification Patterns.
* **String Comparison Implementations:** Built individual static classes mapping to Name, Phone, and Email search types, securely utilizing core Java String methods (`equals()`, `toLowerCase()`, `contains()`) combined with nested iteration and conditional filtering logic.

## Tech Stack
* Java

## How to Run
1. Open the project in your IDE.
2. Run the `MyContactsApp.java` main class.