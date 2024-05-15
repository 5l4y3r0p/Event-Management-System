
# Event Management System

## Overview

The Event Management System is a Java-based application designed to streamline the process of organizing and managing events. It features functionalities for user registration, event creation, and notification management.

## Features

- **User Registration**: Allows new users to sign up and existing users to log in.
- **Event Creation and Management**: Admins can create, update, and delete events.
- **User Dashboard**: Users can view and register for events.
- **Notifications**: Users receive notifications for upcoming events.
- **Database Integration**: Utilizes SQL for storing user and event data.

## Installation

### Prerequisites

- Java Development Kit (JDK) installed
- MySQL installed and running

### Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/5l4y3r0p/Event-Management-System.git
   ```

2. **Open the Project in Your IDE**
   Open the project in your preferred Java IDE (e.g., IntelliJ, Eclipse).

3. **Setup the Database**
   - Create a database using the `DataBase.sql` script provided in the repository.
   - Update the database connection settings in `DatabaseConnector.java` with your database credentials.

4. **Build the Project**
   - Ensure all dependencies are included in your IDE.
   - Build the project to resolve any dependencies and compile the source code.

5. **Run the Application**
   - Execute the `Main.java` file to start the application.

## Usage

### Admin

- **Login**: Admins can log in using their credentials.
- **Manage Events**: Admins can create, update, and delete events through the Admin Dashboard.
- **View Registrations**: Admins can view the list of users registered for each event.

### User

- **Registration**: Users can sign up for a new account.
- **Login**: Users can log in using their credentials.
- **View Events**: Users can view available events and register for them.
- **Notifications**: Users receive notifications for events they are registered for.

## File Structure

- **src**
  - **admin**
    - `AdminDashboard.java`: Manages the admin functionalities.
  - **user**
    - `UserDashboard.java`: Manages the user functionalities.
    - `LoginFrame.java`: Handles user login.
    - `SignUpFrame.java`: Handles user registration.
  - **event**
    - `Event.java`: Represents the event entity.
  - **database**
    - `DatabaseConnector.java`: Handles database connections.

## Contributing

Contributions are welcome! Please follow these steps to contribute:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature-name`).
3. Commit your changes (`git commit -m 'Add some feature'`).
4. Push to the branch (`git push origin feature/your-feature-name`).
5. Open a Pull Request.

## License

This project is licensed under the MIT License. See the `LICENSE` file for more details.

## Contact

For any queries or issues, please contact [your email or GitHub profile link].
