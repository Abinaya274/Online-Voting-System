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

        if (cleanedUsername.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        if (users.containsKey(cleanedUsername)) {
            throw new IllegalArgumentException("That username is already registered.");
        }

        users.put(cleanedUsername, new User(cleanedUsername, password));
        hasVoted.put(cleanedUsername, false);
    }

    public boolean login(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        User user = users.get(username.trim());
        return user != null && user.hasPassword(password);
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
        int highestVotes = -1;
        List<Candidate> winners = new ArrayList<>();

        for (Candidate candidate : candidates.values()) {
            int votes = candidateVotes.get(candidate.getId());
            if (votes > highestVotes) {
                highestVotes = votes;
                winners.clear();
                winners.add(candidate);
            } else if (votes == highestVotes) {
                winners.add(candidate);
            }
        }
        return winners;
    }

    public List<Candidate> getRunnersUp() {
        int highestVotes = -1;
        int runnerUpVotes = -1;

        for (Candidate candidate : candidates.values()) {
            int votes = candidateVotes.get(candidate.getId());
            if (votes > highestVotes) {
                runnerUpVotes = highestVotes;
                highestVotes = votes;
            } else if (votes < highestVotes && votes > runnerUpVotes) {
                runnerUpVotes = votes;
            }
        }

        List<Candidate> runnersUp = new ArrayList<>();
        if (runnerUpVotes == -1) {
            return runnersUp;
        }

        for (Candidate candidate : candidates.values()) {
            if (candidateVotes.get(candidate.getId()) == runnerUpVotes) {
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
