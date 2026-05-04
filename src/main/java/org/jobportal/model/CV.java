package org.jobportal.model;

import java.time.LocalDateTime;

public class CV {
    private String cvId;
    private String candidateId;
    private String objective;
    private String skills;
    private String desiredPosition;
    private Double desiredSalary;
    private LocalDateTime lastUpdated;

    public CV() {
    }

    public CV(String cvId, String candidateId, String objective, String skills,
              String desiredPosition, Double desiredSalary, LocalDateTime lastUpdated) {
        this.cvId = cvId;
        this.candidateId = candidateId;
        this.objective = objective;
        this.skills = skills;
        this.desiredPosition = desiredPosition;
        this.desiredSalary = desiredSalary;
        this.lastUpdated = lastUpdated;
    }

    public String getCvId() {
        return cvId;
    }

    public void setCvId(String cvId) {
        this.cvId = cvId;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getDesiredPosition() {
        return desiredPosition;
    }

    public void setDesiredPosition(String desiredPosition) {
        this.desiredPosition = desiredPosition;
    }

    public Double getDesiredSalary() {
        return desiredSalary;
    }

    public void setDesiredSalary(Double desiredSalary) {
        this.desiredSalary = desiredSalary;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
