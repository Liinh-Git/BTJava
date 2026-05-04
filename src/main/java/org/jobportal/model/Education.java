package org.jobportal.model;

public class Education {
    private String educationId;
    private String cvId;
    private String school;
    private String degree;
    private String major;
    private Integer startYear;
    private Integer endYear;
    private String description;

    public Education() {
    }

    public Education(String educationId, String cvId, String school, String degree, String major,
                     Integer startYear, Integer endYear, String description) {
        this.educationId = educationId;
        this.cvId = cvId;
        this.school = school;
        this.degree = degree;
        this.major = major;
        this.startYear = startYear;
        this.endYear = endYear;
        this.description = description;
    }

    public String getEducationId() {
        return educationId;
    }

    public void setEducationId(String educationId) {
        this.educationId = educationId;
    }

    public String getCvId() {
        return cvId;
    }

    public void setCvId(String cvId) {
        this.cvId = cvId;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
