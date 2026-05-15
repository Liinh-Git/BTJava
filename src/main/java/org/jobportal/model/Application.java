package org.jobportal.model;

import java.time.LocalDateTime;
import org.jobportal.enums.ApplicationStatus;

public class Application {
    private String applicationId;
    private String candidateId;
    private String recruitmentId;
    private ApplicationStatus status;
    private LocalDateTime appliedDate;

    public Application() {
    }

    public Application(String applicationId, String candidateId, String recruitmentId,
                       ApplicationStatus status, LocalDateTime appliedDate) {
        this.applicationId = applicationId;
        this.candidateId = candidateId;
        this.recruitmentId = recruitmentId;
        this.status = status;
        this.appliedDate = appliedDate;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public String getRecruitmentId() {
        return recruitmentId;
    }

    public void setRecruitmentId(String recruitmentId) {
        this.recruitmentId = recruitmentId;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public LocalDateTime getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDateTime appliedDate) {
        this.appliedDate = appliedDate;
    }
}
