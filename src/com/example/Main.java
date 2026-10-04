package com.example;

import java.util.List;
import java.util.Scanner;

public class Main {
    private final Scanner scanner;
    private final VotingSystem votingSystem = new VotingSystem();
    private String loggedInUsername;

    private Main(Scanner scanner) {
        this.scanner = scanner;
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            new Main(scanner).run();
        }
    }

    private void run() {
        System.out.println("================================");
        System.out.println("     COLLEGE VOTING SYSTEM");
        System.out.println("================================");

        boolean running = true;
        while (running) {
            showMenu();
            String choice = readLine("Choose an option: ");

            try {
                switch (choice) {
                    case "1" -> register();
                    case "2" -> login();
                    case "3" -> showCandidates();
                    case "4" -> castVote();
                    case "5" -> showTotalVotes();
                    case "6" -> showResults();
                    case "7" -> logout();
                    case "0" -> {
                        running = false;
                        System.out.println("Thank you for using the voting system.");
                    }
                    default ->
                        System.out.println("Please choose a number from the menu.");
                }
            } catch (IllegalArgumentException | IllegalStateException exception) {
                System.out.println("Error: " + exception.getMessage());
            }
        }
    }

    private void showMenu() {
        System.out.println();
        System.out.println("Logged in as: " + (loggedInUsername == null ? "Nobody" : loggedInUsername));
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. View candidates");
        System.out.println("4. Cast vote");
        System.out.println("5. View total votes");
        System.out.println("6. Display election results");
        System.out.println("7. Logout");
        System.out.println("0. Exit");
    }

    private void register() {
        String username = readLine("Create a username: ");
        String password = readLine("Create a password: ");
        votingSystem.registerUser(username, password);
        System.out.println("Registration successful. You can now log in.");
    }

    private void login() {
        String username = readLine("Username: ");
        String password = readLine("Password: ");

        if (votingSystem.login(username, password)) {
            loggedInUsername = username.trim();
            System.out.println("Login successful. Welcome, " + loggedInUsername + ".");
        } else {
            System.out.println("Login failed. Check your username and password.");
        }
    }

    private void showCandidates() {
        System.out.println();
        System.out.println("Candidates:");
        for (Candidate candidate : votingSystem.getCandidates()) {
            System.out.println(candidate.getId() + " - " + candidate.getName());
        }
    }

    private void castVote() {
        if (loggedInUsername == null) {
            throw new IllegalStateException("Please log in before casting a vote.");
        }
        if (votingSystem.hasUserVoted(loggedInUsername)) {
            throw new IllegalStateException("This account has already voted.");
        }

        showCandidates();
        String candidateId = readLine("Enter the candidate ID you want to vote for: ");
        votingSystem.castVote(loggedInUsername, candidateId);
        System.out.println("Your vote has been recorded. Thank you!");
    }

    private void showTotalVotes() {
        System.out.println("Total votes cast: " + votingSystem.getTotalVotes());
    }

    private void showResults() {
        System.out.println();
        System.out.println("Final vote totals:");
        for (Candidate candidate : votingSystem.getCandidates()) {
            System.out.println(candidate.getName() + ": " + votingSystem.getVotesFor(candidate.getId()));
        }

        List<Candidate> winners = votingSystem.getWinners();
        System.out.println("Winner / Finalist:");
        printCandidates(winners);

        List<Candidate> runnersUp = votingSystem.getRunnersUp();
        System.out.println("Runner-up:");
        if (runnersUp.isEmpty()) {
            System.out.println("No runner-up yet; all candidates are tied.");
        } else {
            printCandidates(runnersUp);
        }
    }

    private void printCandidates(List<Candidate> candidates) {
        for (Candidate candidate : candidates) {
            System.out.println(candidate.getName() + " (" + votingSystem.getVotesFor(candidate.getId()) + " votes)");
        }
    }

    private void logout() {
        if (loggedInUsername == null) {
            System.out.println("Nobody is currently logged in.");
            return;
        }

        System.out.println("Logged out " + loggedInUsername + ".");
        loggedInUsername = null;
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
