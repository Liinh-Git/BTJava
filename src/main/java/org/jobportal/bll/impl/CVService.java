package org.jobportal.bll.impl;

import org.jobportal.dto.CVDTO;

public class CVService {
    // Chuc nang: Lay CV theo candidateId
    // Dau vao: candidateId (String) - ma ung vien
    // Dau ra: CVDTO - thong tin CV
    // Tuong tac: Duoc goi tu CVEditorPanel; se goi CVDAO
    // Ghi chu: Neu chua co CV thi tra ve DTO rong
    public CVDTO getCV(String candidateId) {
        // TODO: Buoc 1 - Truy van CVDAO.findByCandidateId
        // TODO: Buoc 2 - Neu null thi tao DTO rong
        // TODO: Buoc 3 - Tra ve DTO
        return null;
    }

    // Chuc nang: Luu CV
    // Dau vao: cv (CVDTO) - du lieu CV can luu
    // Dau ra: boolean - true neu luu thanh cong
    // Tuong tac: Duoc goi tu CVEditorPanel; se goi CVDAO
    // Ghi chu: Co the ap dung delete-then-insert cho education
    public boolean saveCV(CVDTO cv) {
        // TODO: Buoc 1 - Lay candidateId tu session
        // TODO: Buoc 2 - Insert hoac update CV va education
        // TODO: Buoc 3 - Tra ve ket qua
        return false;
    }
}
