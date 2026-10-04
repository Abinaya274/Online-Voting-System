# Online Voting System (Core Java)

A beginner-friendly, menu-driven college election program organized with the `com.example` Java package.

## Project structure

```text
Online Voting System
|
+-- src
|   +-- com
|       +-- example
|           +-- Main.java
|           +-- User.java
|           +-- Candidate.java
|           +-- VotingSystem.java
|
+-- README.md
```

## Requirements

- JDK installed (Java 14 or later; the menu uses rule-style switch syntax)
- A terminal or command prompt opened in the project folder

## Compile and run

From the project folder, run:

```text
javac -d out src\com\example\Main.java src\com\example\User.java src\com\example\Candidate.java src\com\example\VotingSystem.java
java -cp out com.example.Main
```

The `-d out` option places compiled `.class` files in an `out` folder, matching the package structure.

## How to use it

1. Choose **1. Register** and create a username and password.
2. Choose **2. Login** with that account.
3. Choose **3. View candidates** to see the candidate IDs.
4. Choose **4. Cast vote** and enter one candidate ID.
5. Choose **5. View total votes** or **6. Display election results**.
6. Register another account to cast another vote. An account can vote only once.

The candidates are added in `src\com\example\VotingSystem.java`. Change the sample names there if needed.

## Beginner concept guide

- **OOP:** `User` stores a student's username and password. `Candidate` stores an ID and name. `VotingSystem` manages the election.
- **Collections:** `users` is a `HashMap<String, User>`. `hasVoted` is a `HashMap<String, Boolean>` that records if each user has voted. `candidateVotes` is a `HashMap<String, Integer>` that records each candidate's total. Candidate details are kept in a `LinkedHashMap` so they display in a predictable order.
- **Methods:** Registration, login, vote casting, counting, and result display are kept in separate methods so each part has one job.
- **Exception handling:** Invalid registration and voting actions raise clear exceptions. The menu catches them and displays an error without stopping the program.
- **Winner and runner-up:** The result is calculated from the vote totals. Candidates tied at first or second place are all displayed. If every candidate is tied, there is no runner-up yet.

## Important limitation

This learning project keeps accounts and votes in memory, so they reset when the program exits. Passwords are stored as plain text for simplicity; this design is for learning only and is not suitable for a real election.
