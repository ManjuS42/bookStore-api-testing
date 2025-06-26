# Bookstore API Test Automation

----
## Overview

---
This project delivers an automated testing framework for validating the Bookstore API using industry-standard tools and practices. Built with Java, Cucumber, and RestAssured, it enables comprehensive validation of API operations such as user authentication and book management. The framework supports CI/CD integration with Jenkins and generates detailed Cucumber HTML reports.

---
## Table of Contents

----

<!-- TOC -->
* [Bookstore API Test Automation](#bookstore-api-test-automation)
  * [Overview](#overview)
  * [Key Features](#key-features)
  * [Tech Stack](#tech-stack)
  * [Project Structure](#project-structure)
  * [How to Run](#how-to-run)
    * [Prerequisites](#prerequisites)
    * [Run Tests Locally](#run-tests-locally)
    * [View Report](#view-report)
  * [CI/CD Integration - Bookstore API Automation (Cucumber + Maven + Jenkins)](#cicd-integration---bookstore-api-automation-cucumber--maven--jenkins)
    * [Prerequisites](#prerequisites-1)
    * [Required Jenkins Plugins](#required-jenkins-plugins)
    * [Jenkins Project Setup](#jenkins-project-setup)
    * [Jenkins Configuration Steps](#jenkins-configuration-steps)
    * [Now Test It](#now-test-it)
    * [View Report](#view-report-1)
<!-- TOC -->

---

## Key Features

---
- **Authentication & Book CRUD validation**  
  Covers core operations including user signup, login, and CRUD actions on books.

- **Readable BDD Scenarios**  
  Gherkin-based feature files promote clarity and collaboration.

- **Environment Switching**  
  Easily toggle between environments using config property files.

- **Cucumber HTML Report**  
  Automatically generates reports post-test, saved in `reports/cucumber`.

- **CI/CD Friendly**  
  Jenkins pipeline (`Jenkinsfile`) supports checkout, build, test, and publish stages.

- **Data Reusability**  
  Centralized test data management ensures scalability and maintainability.

---
## Tech Stack

---

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

## Project Structure

![img.png](img.png)




---

## How to Run

---
### Prerequisites

- Java 21
- Maven 3.6+
- Git

---
### Run Tests Locally

1. Clone the repository
   git clone https://github.com/ManjuSrinivasan04/bookStore-api-testing.git
2. Navigate to the project directory
_cd bookStore-api-testing_
3. Run tests and generate the report
`mvn clean verify
`
---

### View Report

After running the tests, open the following HTML report in your browser:


`reports/cucumber/cucumber-report.html
`

---
##  CI/CD Integration - Bookstore API Automation (Cucumber + Maven + Jenkins)

---

### Prerequisites

---

Make sure the following tools are installed on your local machine:

* Jenkins (latest LTS recommended)
* JDK 21
* Maven 3.6.3+
* Ngrok (for exposing localhost temporarily)
* Git

---
### Required Jenkins Plugins

---

Install these from Manage Jenkins → Plugins → Available:

* Git Plugin
* Pipeline
* HTML Publisher
* GitHub Integration

---
### Jenkins Project Setup

---
1. DevRepo Pipeline Job – Simulated dev repo, triggering test
2. Bookstore-QA-Automation Pipeline Job – runs actual automation and reports
3. Jenkinsfile for DevRepo - This Jenkinsfile triggers the QA automation:
   `pipeline {
   agent any
   stages {
   stage('Build Dev') {
   steps {
   echo 'This is where dev code would be built/tested.'
   }
   }
   stage('Trigger QA Automation') {
   steps {
   build job: 'Bookstore-QA-Automation'
   }
   }
   }
   }
`
4. Jenkinsfile for Bookstore-QA-Automation
`   https://github.com/ManjuSrinivasan04/bookStore-api-testing/blob/ddfece5485cc227ed231db38a46a689387077d44/JenkinsFile
`


---

### Jenkins Configuration Steps

---
1. Start Jenkins Locally
   
   `java -jar jenkins.war`
Login at: http://localhost:8080

2. Create 2 Jenkins Pipeline Jobs for Dev Repo and Bookstore-QA-Automation repo 
3. Configure the repo in their respective jobs and also make the configuration as necessary 
4. In Dev repo Webhooks - Need to add payload url for triggering the Dev Job whenever dev commits the code 
5. Since github can't access your local , run ngrok command to let public access for your local server
`ngrok http http://localhost:8080
`
6. It will generate the url , use that as payload url along with repo name 
`(eg : https://gitUserName:gitPassword@ngrokServerProvided/job/DevRepo/build )//Replace with dev repo name
`
---
### Now Test It

---
1. Commit any code to DevRepo
2. GitHub will notify Jenkins via the webhook
3. DevRepo job runs, and then it automatically triggers Bookstore-QA-Automation job
4. Cucumber tests will run
5. Reports are generated and viewable in Jenkins UI

---
### View Report

---

* After the QA job runs, go to the Jenkins job → Last Successful Build → Cucumber Test Report
* Able to view HTML report from:

`target/cucumber-report/cucumber-html-report.html
`