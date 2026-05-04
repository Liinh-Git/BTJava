package org.jobportal.dal.impl;

import java.util.Collections;
import java.util.List;
import org.jobportal.model.Category;

public class CategoryDAO {
    // Chuc nang: Lay danh sach danh muc
    // Dau vao: (void)
    // Dau ra: List<Category> - danh sach danh muc
    // Tuong tac: Duoc goi tu CategoryService; se dung JDBC
    // Ghi chu: Tra ve danh sach day du
    public List<Category> findAll() {
        // TODO: Buoc 1 - Tao ket noi va query SELECT
        // TODO: Buoc 2 - Map ResultSet sang list Category
        // TODO: Buoc 3 - Dong ket noi va tra ve
        return Collections.emptyList();
    }

    // Chuc nang: Tim danh muc theo id
    // Dau vao: categoryId (String) - ma danh muc
    // Dau ra: Category - danh muc tim thay
    // Tuong tac: Duoc goi tu CategoryService; se dung JDBC
    // Ghi chu: Tra ve null neu khong tim thay
    public Category findById(String categoryId) {
        // TODO: Buoc 1 - Query SELECT theo categoryId
        // TODO: Buoc 2 - Map ResultSet sang Category
        // TODO: Buoc 3 - Tra ve ket qua
        return null;
    }

    // Chuc nang: Them danh muc
    // Dau vao: category (Category) - danh muc can them
    // Dau ra: boolean - true neu them thanh cong
    // Tuong tac: Duoc goi tu CategoryService; se dung JDBC
    // Ghi chu: Kiem tra trung ten truoc khi insert
    public boolean insert(Category category) {
        // TODO: Buoc 1 - Tao cau lenh INSERT
        // TODO: Buoc 2 - Thuc thi va lay ket qua
        // TODO: Buoc 3 - Tra ve boolean
        return false;
    }

    // Chuc nang: Cap nhat danh muc
    // Dau vao: category (Category) - danh muc can cap nhat
    // Dau ra: boolean - true neu cap nhat thanh cong
    // Tuong tac: Duoc goi tu CategoryService; se dung JDBC
    // Ghi chu: Cap nhat theo categoryId
    public boolean update(Category category) {
        // TODO: Buoc 1 - Tao cau lenh UPDATE
        // TODO: Buoc 2 - Thuc thi va lay ket qua
        // TODO: Buoc 3 - Tra ve boolean
        return false;
    }

    // Chuc nang: Xoa danh muc
    // Dau vao: categoryId (String) - ma danh muc
    // Dau ra: boolean - true neu xoa thanh cong
    // Tuong tac: Duoc goi tu CategoryService; se dung JDBC
    // Ghi chu: Can dam bao khong co recruitment lien ket
    public boolean delete(String categoryId) {
        // TODO: Buoc 1 - Tao cau lenh DELETE
        // TODO: Buoc 2 - Thuc thi va lay ket qua
        // TODO: Buoc 3 - Tra ve boolean
        return false;
    }

    // Chuc nang: Kiem tra ton tai theo ten
    // Dau vao: categoryName (String) - ten danh muc
    // Dau ra: boolean - true neu da ton tai
    // Tuong tac: Duoc goi tu CategoryService; se dung JDBC
    // Ghi chu: Dung de kiem tra trung ten
    public boolean existsByName(String categoryName) {
        // TODO: Buoc 1 - Query COUNT theo categoryName
        // TODO: Buoc 2 - Doc ket qua
        // TODO: Buoc 3 - Tra ve boolean
        return false;
    }
}
