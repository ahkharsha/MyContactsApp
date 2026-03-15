# MyContacts App

A Java-based console application for contact management, designed using clean, strictly modular Object-Oriented Programming principles.

## Features Implemented

### UC1: User Registration
* **Factory-Based User Creation:** Refactored registration to utilize a dedicated `UserFactory` that dynamically creates `FreeUser` and `PremiumUser` objects based on the selected account type, improving separation of concerns and adherence to the Factory design pattern.
* **Builder-Driven Object Construction:** Implemented a `UserBuilder` to assemble validated registration data step-by-step before object creation, aligning the workflow with the Builder design pattern while keeping user initialization clean and extensible.

### UC2: User Authentication
* **Strategy-Based Authentication:** Refactored login handling to utilize the `Authentication` strategy interface with dedicated `BasicAuth` and `OAuth` implementations, allowing the application to switch authentication mechanisms cleanly at runtime.
* **Singleton Session Control:** Implemented a centralized `SessionManager` Singleton to manage authenticated user state consistently across login, logout, and account-deletion flows without duplicating session logic in the UI layer.

### UC3: User Profile Management
* **Command Pattern Integration:** Re-architected user profile operations (Name Update, Email Update, Password Change) into distinct `UserCommand` execution objects managed by a centralized `ProfileUpdateInvoker`, effectively decoupling input logic from business state mutations.
* **Encapsulated Security Workflow:** Packaged security workflows, such as verifying the existing password before permitting a state transition to a new password, cleanly into an isolated command execution to ensure safety standards remain robust.


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

### UC11: Create and Manage Tags
* **Object Identity & Uniqueness:** Created a custom `Tag` class that strictly overrides `equals()` and `hashCode()` to ensure identical string inputs (e.g., "work" and "Work") are evaluated as the same object in memory.
* **Collection Modeling:** Integrated a `Set<Tag>` relationship inside the abstract `Contact` model to enforce tag uniqueness per contact, and built a dynamic extraction method in the service layer to pull a master list of unique tags across all user contacts.

### UC12: Apply Tags to Contacts
* **Object Relationships:** Actively utilized the `Set<Tag>` relationship within the `Contact` model, allowing users to safely assign and revoke multiple unique tags per contact via the main console loop.
* **Polymorphic Search Expansion:** Completed the `SearchFilterInterface` hierarchy by introducing a `TagSearch` implementation, securely leveraging the `contains()` method on the Java `Set` collection to locate specific tags efficiently.

## Tech Stack
* Java

## How to Run
1. Open the project in your IDE.
2. Run the `MyContactsApp.java` main class.