package org.jobportal.dal.interfaces;

import java.util.List;
import org.jobportal.model.CV;
import org.jobportal.model.Education;

public interface ICVDAO {
    // Chức năng: Lấy CV theo candidateId
    // Đầu vào: candidateId (String)
    // Đầu ra: CV
    CV findByCandidateId(String candidateId);

    // Chức năng: Thêm CV
    // Đầu vào: cv (CV)
    // Đầu ra: boolean
    boolean insert(CV cv);

    // Chức năng: Cập nhật CV
    // Đầu vào: cv (CV)
    // Đầu ra: boolean
    boolean update(CV cv);
    List<Education> findEducationsByCvId(String cvId);

    boolean replaceEducations(String cvId, List<Education> educations);
}
