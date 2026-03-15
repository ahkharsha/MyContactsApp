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
* **Factory-Based Object Instantiation:** Re-architected contact creation workflows to utilize a `ContactFactory`, which dynamically determines and instantiates the proper concrete subtypes (`Person` or `Organization`) based on runtime inputs, reducing tight coupling in the service layer.
* **Builder-Driven Construction:** Introduced a `ContactBuilder` to collect, validate, and manage optional and multi-value contact properties (e.g., lists of phone numbers and emails) step-by-step before finalizing the objects natively inside the factory method.


### UC5: View Contact Details
* **Dynamic Content Formatting:** Integrated the `ContactDisplay` interface and abstract `ContactDecorator` to allow users to dynamically apply non-destructive formatting (like ALL UPPERCASE) to the console output of contact details at runtime.
* **Privacy Controls:** Implemented a concrete `MaskedEmailDecorator` that leverages RegEx parsing to proactively obscure the username portion of sensitive email fields from the console UI before printing, and demonstrated the ability to chain multiple formatters safely.

### UC6: Edit Contact
* **Snapshot State Preservation:** Implemented the `ContactMemento` class and associated copy constructors within the `Contact` hierarchy to capture deep, immutable snapshots of a contact's state (including complex multi-value fields) before edits are made.
* **Undo/Redo Flow Control:** Designed a `ModifyContactCommand` and integrated a generic `ContactEditInvoker` into the CLI event loop to encapsulate mutation logic, enabling users to seamlessly undo and redo multiple continuous data modifications without losing state.

### UC7: Delete Contact
* **Event-Driven Architecture:** Implemented the `ContactDeletionObserver` and `ContactDeletionSubject` interfaces to establish an Observer Pattern within the service layer, decoupling core deletion logic from secondary actions like system auditing (`AuditLogObserver`).
* **Safe Deletion Strategies:** Supported both Soft Delete (which simply marks a contact as `isActive=false`, hiding it from the UI while preserving the data) and Hard Delete (which permanently strips the object from the underlying collection and invokes a file write), tied to an interactive CLI confirmation flow to prevent accidental data loss.

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