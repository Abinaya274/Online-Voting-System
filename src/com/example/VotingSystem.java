package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VotingSystem {
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Boolean> hasVoted = new HashMap<>();
    private final Map<String, Candidate> candidates = new LinkedHashMap<>();
    private final Map<String, Integer> candidateVotes = new HashMap<>();

    public VotingSystem() {
        addCandidate(new Candidate("C1", "Ava Patel"));
        addCandidate(new Candidate("C2", "Noah Williams"));
        addCandidate(new Candidate("C3", "Mia Chen"));
    }

    public void registerUser(String username, String password) {
        String cleanedUsername = username == null ? "" : username.trim();
        String cleanedPassword = password == null ? "" : password.trim();

        if (cleanedUsername.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        if (cleanedPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        if (users.containsKey(cleanedUsername)) {
            throw new IllegalArgumentException("That username is already registered.");
        }

        users.put(cleanedUsername, new User(cleanedUsername, cleanedPassword));
        hasVoted.put(cleanedUsername, false);
    }

    public boolean login(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        String cleanedUsername = username.trim();
        String cleanedPassword = password.trim();
        User user = users.get(cleanedUsername);
        return user != null && user.hasPassword(cleanedPassword);
    }

    public List<Candidate> getCandidates() {
        return Collections.unmodifiableList(new ArrayList<>(candidates.values()));
    }

    public void castVote(String username, String candidateId) {
        String cleanedUsername = username == null ? "" : username.trim();
        String cleanedCandidateId = candidateId == null ? "" : candidateId.trim().toUpperCase();

        if (!users.containsKey(cleanedUsername)) {
            throw new IllegalArgumentException("Please log in with a registered account before voting.");
        }
        if (!candidates.containsKey(cleanedCandidateId)) {
            throw new IllegalArgumentException("Candidate ID not found. Please choose an ID from the list.");
        }
        if (Boolean.TRUE.equals(hasVoted.get(cleanedUsername))) {
            throw new IllegalStateException("This account has already voted. Each student may vote only once.");
        }

        int currentVotes = candidateVotes.get(cleanedCandidateId);
        candidateVotes.put(cleanedCandidateId, currentVotes + 1);
        hasVoted.put(cleanedUsername, true);
    }

    public int getVotesFor(String candidateId) {
        Integer votes = candidateVotes.get(candidateId);
        if (votes == null) {
            throw new IllegalArgumentException("Candidate ID not found.");
        }
        return votes;
    }

    public int getTotalVotes() {
        int total = 0;
        for (Integer votes : candidateVotes.values()) {
            total += votes;
        }
        return total;
    }

    public boolean hasUserVoted(String username) {
        return Boolean.TRUE.equals(hasVoted.get(username));
    }

    public List<Candidate> getWinners() {
        if (candidates.isEmpty()) {
            return new ArrayList<>();
        }

        int highestVotes = Integer.MIN_VALUE;
        for (Candidate candidate : candidates.values()) {
            highestVotes = Math.max(highestVotes, candidateVotes.getOrDefault(candidate.getId(), 0));
        }

        List<Candidate> winners = new ArrayList<>();
        for (Candidate candidate : candidates.values()) {
            if (candidateVotes.getOrDefault(candidate.getId(), 0) == highestVotes) {
                winners.add(candidate);
            }
        }
        return winners;
    }

    public List<Candidate> getRunnersUp() {
        List<Candidate> winners = getWinners();
        if (winners.size() == candidates.size()) {
            return new ArrayList<>();
        }

        int runnerUpVotes = Integer.MIN_VALUE;
        for (Candidate candidate : candidates.values()) {
            if (!winners.contains(candidate)) {
                runnerUpVotes = Math.max(runnerUpVotes, candidateVotes.getOrDefault(candidate.getId(), 0));
            }
        }

        List<Candidate> runnersUp = new ArrayList<>();
        if (runnerUpVotes == Integer.MIN_VALUE) {
            return runnersUp;
        }

        for (Candidate candidate : candidates.values()) {
            if (!winners.contains(candidate)
                    && candidateVotes.getOrDefault(candidate.getId(), 0) == runnerUpVotes) {
                runnersUp.add(candidate);
            }
        }
        return runnersUp;
    }

    private void addCandidate(Candidate candidate) {
        candidates.put(candidate.getId(), candidate);
        candidateVotes.put(candidate.getId(), 0);
    }
}
