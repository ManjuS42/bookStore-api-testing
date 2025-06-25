# 📘 Bookstore API Test Automation

## 📖 Overview

This project delivers an automated testing framework for validating the Bookstore API using industry-standard tools and practices. Built with Java, Cucumber, and RestAssured, it enables comprehensive validation of API operations such as user authentication and book management. The framework supports CI/CD integration with Jenkins and generates detailed Cucumber HTML reports.

---

## 🚀 Key Features

- 🔐 **Authentication & Book CRUD validation**  
  Covers core operations including user signup, login, and CRUD actions on books.

- 📝 **Readable BDD Scenarios**  
  Gherkin-based feature files promote clarity and collaboration.

- 🌐 **Environment Switching**  
  Easily toggle between environments using config property files.

- 📊 **Cucumber HTML Report**  
  Automatically generates reports post-test, saved in `reports/cucumber`.

- ⚙️ **CI/CD Friendly**  
  Jenkins pipeline (`Jenkinsfile`) supports checkout, build, test, and publish stages.

- 🔁 **Data Reusability**  
  Centralized test data management ensures scalability and maintainability.

---

## 🧰 Tech Stack

| Tool               | Purpose                                |
|--------------------|----------------------------------------|
| **Java 21**         | Core language                          |
| **RestAssured**     | HTTP request testing                   |
| **Cucumber v7.21**  | BDD test specification framework       |
| **JUnit 4**         | Test execution engine                  |
| **Maven**           | Build and dependency management        |
| **Lombok**          | Reduces boilerplate code               |
| **Jenkins**         | CI/CD pipeline automation              |

---

## 📂 Project Structure


               
          


---

## ▶️ How to Run

### 🔧 Prerequisites

- Java 21
- Maven 3.6+
- Git

### 🧪 Run Tests Locally

# Clone the repository
git clone https://github.com/ManjuSrinivasan04/bookStore-api-testing.git

# Navigate to the project directory
cd bookStore-api-testing

# Run tests and generate the report
mvn clean verify

---

## 📑 View Report

After running the tests, open the following HTML report in your browser:


reports/cucumber/cucumber-report.html


---

---
