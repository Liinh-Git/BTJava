package org.jobportal.dal.interfaces;

import org.jobportal.model.CV;

public interface ICVDAO {
    // Chuc nang: Lay CV theo candidateId
    // Dau vao: candidateId (String)
    // Dau ra: CV
    CV findByCandidateId(String candidateId);

    // Chuc nang: Them CV
    // Dau vao: cv (CV)
    // Dau ra: boolean
    boolean insert(CV cv);

    // Chuc nang: Cap nhat CV
    // Dau vao: cv (CV)
    // Dau ra: boolean
    boolean update(CV cv);
}
