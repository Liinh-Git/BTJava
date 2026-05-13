package org.jobportal.dto;

import java.time.LocalDateTime;
import java.util.List;
import org.jobportal.model.Education;

public class CVDTO {
    private String cvId;
    private String candidateId;
    private String objective;
    private String skills;
    private String desiredPosition;
    private String location;
    private Double desiredSalary;
    private LocalDateTime lastUpdated;
    private List<Education> educations;

    public CVDTO() {
    }

    public CVDTO(String cvId, String candidateId, String objective, String skills,
                 String desiredPosition, Double desiredSalary, LocalDateTime lastUpdated,
                 List<Education> educations) {
        this.cvId = cvId;
        this.candidateId = candidateId;
        this.objective = objective;
        this.skills = skills;
        this.desiredPosition = desiredPosition;
        this.desiredSalary = desiredSalary;
        this.lastUpdated = lastUpdated;
        this.educations = educations;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public List<Education> getEducations() {
        return educations;
    }

    public void setEducations(List<Education> educations) {
        this.educations = educations;
    }
}
