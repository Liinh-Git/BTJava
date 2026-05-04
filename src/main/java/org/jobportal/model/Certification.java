package org.jobportal.model;

import java.time.LocalDate;

public class Certification {
    private String certId;
    private String cvId;
    private String name;
    private String issuer;
    private LocalDate issueDate;

    public Certification() {
    }

    public Certification(String certId, String cvId, String name, String issuer, LocalDate issueDate) {
        this.certId = certId;
        this.cvId = cvId;
        this.name = name;
        this.issuer = issuer;
        this.issueDate = issueDate;
    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String getCvId() {
        return cvId;
    }

    public void setCvId(String cvId) {
        this.cvId = cvId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }
}
