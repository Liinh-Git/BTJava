package org.jobportal.dal.interfaces;

import org.jobportal.model.Candidate;

public interface ICandidateDAO {
    // Chức năng: Tìm candidate theo userId
    // Đầu vào: userId (String)
    // Đầu ra: Candidate
    Candidate findByUserId(String userId);

    // Chức năng: Tìm candidate theo id
    // Đầu vào: candidateId (String)
    // Đầu ra: Candidate
    Candidate findById(String candidateId);

    // Chức năng: Thêm candidate
    // Đầu vào: candidate (Candidate)
    // Đầu ra: boolean
    boolean insert(Candidate candidate);

    // Chức năng: Xóa candidate
    // Đầu vào: candidateId (String)
    // Đầu ra: boolean
    boolean delete(String candidateId);
}
