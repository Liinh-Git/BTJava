package org.jobportal.dal.impl;

import org.jobportal.model.CV;

public class CVDAO {
    // Chuc nang: Lay CV theo candidateId
    // Dau vao: candidateId (String) - ma ung vien
    // Dau ra: CV - thong tin CV
    // Tuong tac: Duoc goi tu CVService; se dung JDBC
    // Ghi chu: Tra ve null neu chua co CV
    public CV findByCandidateId(String candidateId) {
        // TODO: Buoc 1 - Query SELECT theo candidateId
        // TODO: Buoc 2 - Map ResultSet sang CV
        // TODO: Buoc 3 - Tra ve ket qua
        return null;
    }

    // Chuc nang: Them CV
    // Dau vao: cv (CV) - du lieu CV
    // Dau ra: boolean - true neu insert thanh cong
    // Tuong tac: Duoc goi tu CVService; se dung JDBC
    // Ghi chu: Can insert education neu co
    public boolean insert(CV cv) {
        // TODO: Buoc 1 - Tao cau lenh INSERT CV
        // TODO: Buoc 2 - Thuc thi va xu ly education
        // TODO: Buoc 3 - Tra ve boolean
        return false;
    }

    // Chuc nang: Cap nhat CV
    // Dau vao: cv (CV) - du lieu CV
    // Dau ra: boolean - true neu update thanh cong
    // Tuong tac: Duoc goi tu CVService; se dung JDBC
    // Ghi chu: Co the ap dung delete-then-insert cho education
    public boolean update(CV cv) {
        // TODO: Buoc 1 - Tao cau lenh UPDATE CV
        // TODO: Buoc 2 - Xu ly danh sach education
        // TODO: Buoc 3 - Tra ve boolean
        return false;
    }
}
