package org.jobportal.dto;

import java.time.LocalDateTime;
import org.jobportal.enums.ApplicationStatus;
import org.jobportal.enums.JobType;

public class ApplicationDTO {
    private String applicationId;
    private String candidateId;
    private String candidateName;
    private String email;
    private String recruitmentId;
    private String jobTitle;
    private String companyName;
    private JobType jobType;
    private ApplicationStatus status;
    private LocalDateTime appliedDate;

    public ApplicationDTO() {
    }

    public ApplicationDTO(String applicationId, String candidateId, String candidateName, String email,
                          String recruitmentId, String jobTitle, String companyName, JobType jobType,
                          ApplicationStatus status, LocalDateTime appliedDate) {
        this.applicationId = applicationId;
        this.candidateId = candidateId;
        this.candidateName = candidateName;
        this.email = email;
        this.recruitmentId = recruitmentId;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.jobType = jobType;
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

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRecruitmentId() {
        return recruitmentId;
    }

    public void setRecruitmentId(String recruitmentId) {
        this.recruitmentId = recruitmentId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
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
