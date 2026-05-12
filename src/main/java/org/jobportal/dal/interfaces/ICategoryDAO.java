package org.jobportal.dal.interfaces;

import java.util.List;
import org.jobportal.model.Category;

public interface ICategoryDAO {
    // Chức năng: Lấy danh sách danh mục
    // Đầu vào: (void)
    // Đầu ra: List<Category>
    List<Category> findAll();

    // Chức năng: Tìm danh mục theo id
    // Đầu vào: categoryId (String)
    // Đầu ra: Category
    Category findById(String categoryId);

    // Chức năng: Thêm danh mục
    // Đầu vào: category (Category)
    // Đầu ra: boolean
    boolean insert(Category category);

    // Chức năng: Cập nhật danh mục
    // Đầu vào: category (Category)
    // Đầu ra: boolean
    boolean update(Category category);

    // Chức năng: Xóa danh mục
    // Đầu vào: categoryId (String)
    // Đầu ra: boolean
    boolean delete(String categoryId);

    // Chức năng: Kiểm tra tồn tại theo tên
    // Đầu vào: categoryName (String)
    // Đầu ra: boolean
    boolean existsByName(String categoryName);
}
