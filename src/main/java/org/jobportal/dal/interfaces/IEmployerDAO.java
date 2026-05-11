package org.jobportal.dal.interfaces;

import org.jobportal.model.Employer;

public interface IEmployerDAO {
    // Chức năng: Tìm employer theo userId
    // Đầu vào: userId (String)
    // Đầu ra: Employer
    Employer findByUserId(String userId);

    // Chức năng: Tìm employer theo id
    // Đầu vào: employerId (String)
    // Đầu ra: Employer
    Employer findById(String employerId);

    // Chức năng: Thêm employer
    // Đầu vào: employer (Employer)
    // Đầu ra: boolean
    boolean insert(Employer employer);

    // Chức năng: Cập nhật employer
    // Đầu vào: employer (Employer)
    // Đầu ra: boolean
    boolean update(Employer employer);

    // Chức năng: Xóa employer
    // Đầu vào: employerId (String)
    // Đầu ra: boolean
    boolean delete(String employerId);
}
