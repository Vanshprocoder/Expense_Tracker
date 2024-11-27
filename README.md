
# Expense Tracker (SpendSmart)

A **Java-based Expense Tracker application** designed to manage daily expenses, visualize them through pie charts, and track borrowings. This project is built with **Java Swing** and uses **MySQL** for database connectivity.

---

## 📁 Project Structure
```
ExpenseTracker/
├── bin/                  # Contains compiled class files and images
├── src/                  # Contains all Java source files
├── README.md             # Project documentation
```

---

## 🛠️ Prerequisites

1. **Java Development Kit (JDK)** installed (version 8 or later).  
   [Download JDK](https://www.oracle.com/java/technologies/javase-downloads.html)

2. **Eclipse IDE for Java Developers** installed.  
   [Download Eclipse](https://www.eclipse.org/downloads/)

3. **MySQL Database** installed.  
   [Download MySQL](https://dev.mysql.com/downloads/installer/)

---

## 🚀 Steps to Set Up the Project in Eclipse

1. **Clone the Repository**  
   Download the project files from GitHub:
        download the ZIP file and extract it.

2. **Open Eclipse and Import the Project**  
   - Open Eclipse and go to **File > Open Projects from File System**.  
   - Click **Directory...**, select the extracted project folder, and click **Finish**.

3. **Add JAR Files**  
   Download the required JAR files:  
   - [MySQL Connector JAR (mysql-connector-j-9.0.0.jar)](https://dev.mysql.com/downloads/connector/j/)  
   - [JCalendar JAR (jcalendar-0.8.1.jar)](https://toedter.com/jcalendar/)

   Add these JAR files to your project:  
   - Right-click the project in Eclipse and select **Build Path > Configure Build Path**.  
   - Go to the **Libraries** tab and click **Add External JARs**.  
   - Browse to the downloaded JAR files and add them.  
   - Click **Apply and Close**.

4. **Set Up the Database**  
   - Open **Xampp** or your preferred MySQL client.  
   - Create a database named `ExpenseTracker`.  
   - Import the database schema (`ExpenseTracker.sql`) provided with the project.

5. **Update Database Connection**  
   - Open the file `DBconnection.java` from the `src/` folder.  
   - Update the database URL, username, and password to match your local MySQL setup:
     ```java
     String url = "jdbc:mysql://localhost:3306/ExpenseTracker";
     String username = "root"; // Your MySQL username
     String password = "password"; // Your MySQL password
     ```

6. **Run the Application**  
   - In Eclipse, right-click the `signup.java` file in the `src` folder and select **Run As > Java Application**.  
   - The application should launch, and you can start using the Expense Tracker.

---

## 🧰 Features
- User Signup/Login  
- Dashboard with visualized expenses  
- Add and track expenses  
- Borrowing and planned expenses management  
- MySQL database integration  

---

## 📎 External JAR Files
- [MySQL Connector JAR (mysql-connector-j-9.0.0.jar)](https://dev.mysql.com/downloads/connector/j/)  
- [JCalendar JAR (jcalendar-0.8.1.jar)](https://toedter.com/jcalendar/)

---

## 💡 Troubleshooting

- **Error: `java.sql.SQLException`**  
   Ensure your MySQL server is running, and the database credentials in `DBconnection.java` are correct.

- **Missing Class Error**  
   Ensure the external JAR files are added to the project’s build path.

---

Feel free to reach out for any issues or suggestions! 😊  
