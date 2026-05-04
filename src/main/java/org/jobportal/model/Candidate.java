package org.jobportal.model;

public class Candidate {
    private String candidateId;
    private String userId;

    public Candidate() {
    }

    public Candidate(String candidateId, String userId) {
        this.candidateId = candidateId;
        this.userId = userId;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
