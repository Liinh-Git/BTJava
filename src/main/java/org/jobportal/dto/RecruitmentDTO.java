package org.jobportal.dto;

import java.time.LocalDateTime;
import org.jobportal.enums.AdminStatus;
import org.jobportal.enums.JobType;
import org.jobportal.enums.RecruitmentStatus;

public class RecruitmentDTO {
    private String recruitmentId;
    private String employerId;
    private String companyName;
    private String categoryId;
    private String categoryName;
    private String title;
    private String description;
    private JobType jobType;
    private RecruitmentStatus status;
    private AdminStatus adminStatus;
    private Double salary;
    private String location;
    private String experienceRequired;
    private LocalDateTime createdDate;
    private LocalDateTime dueDate;
    private int applicationCount;

    public RecruitmentDTO() {
    }

    public RecruitmentDTO(String recruitmentId, String employerId, String companyName,
                          String categoryId, String categoryName, String title, String description,
                          JobType jobType, RecruitmentStatus status, AdminStatus adminStatus,
                          Double salary, String location, String experienceRequired,
                          LocalDateTime createdDate, LocalDateTime dueDate, int applicationCount) {
        this.recruitmentId = recruitmentId;
        this.employerId = employerId;
        this.companyName = companyName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.title = title;
        this.description = description;
        this.jobType = jobType;
        this.status = status;
        this.adminStatus = adminStatus;
        this.salary = salary;
        this.location = location;
        this.experienceRequired = experienceRequired;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
        this.applicationCount = applicationCount;
    }

    public String getRecruitmentId() {
        return recruitmentId;
    }

    public void setRecruitmentId(String recruitmentId) {
        this.recruitmentId = recruitmentId;
    }

    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public RecruitmentStatus getStatus() {
        return status;
    }

    public void setStatus(RecruitmentStatus status) {
        this.status = status;
    }

    public AdminStatus getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(AdminStatus adminStatus) {
        this.adminStatus = adminStatus;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getExperienceRequired() {
        return experienceRequired;
    }

    public void setExperienceRequired(String experienceRequired) {
        this.experienceRequired = experienceRequired;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public int getApplicationCount() {
        return applicationCount;
    }

    public void setApplicationCount(int applicationCount) {
        this.applicationCount = applicationCount;
    }
}
