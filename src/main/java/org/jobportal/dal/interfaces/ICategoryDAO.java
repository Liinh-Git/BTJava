package org.jobportal.dal.interfaces;

import java.util.List;
import org.jobportal.model.Category;

public interface ICategoryDAO {
    // Chuc nang: Lay danh sach danh muc
    // Dau vao: (void)
    // Dau ra: List<Category>
    List<Category> findAll();

    // Chuc nang: Tim danh muc theo id
    // Dau vao: categoryId (String)
    // Dau ra: Category
    Category findById(String categoryId);

    // Chuc nang: Them danh muc
    // Dau vao: category (Category)
    // Dau ra: boolean
    boolean insert(Category category);

    // Chuc nang: Cap nhat danh muc
    // Dau vao: category (Category)
    // Dau ra: boolean
    boolean update(Category category);

    // Chuc nang: Xoa danh muc
    // Dau vao: categoryId (String)
    // Dau ra: boolean
    boolean delete(String categoryId);

    // Chuc nang: Kiem tra ton tai theo ten
    // Dau vao: categoryName (String)
    // Dau ra: boolean
    boolean existsByName(String categoryName);
}
