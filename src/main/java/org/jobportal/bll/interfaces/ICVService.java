package org.jobportal.bll.interfaces;

import org.jobportal.dto.CVDTO;

public interface ICVService {
    // Chuc nang: Lay CV theo candidateId
    // Dau vao: candidateId (String)
    // Dau ra: CVDTO
    CVDTO getCV(String candidateId);

    // Chuc nang: Luu CV
    // Dau vao: cv (CVDTO)
    // Dau ra: boolean
    boolean saveCV(CVDTO cv);
}
