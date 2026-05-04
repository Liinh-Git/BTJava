package org.jobportal.bll.impl;

import java.util.Collections;
import java.util.List;
import org.jobportal.model.Category;

public class CategoryService {
    // Chuc nang: Lay tat ca danh muc
    // Dau vao: (void)
    // Dau ra: List<Category> - danh sach danh muc
    // Tuong tac: Duoc goi tu JobSearchPanel/RecruitmentFormPanel/CategoryManagementPanel; se goi CategoryDAO
    // Ghi chu: Tra ve danh sach day du
    public List<Category> getAllCategories() {
        // TODO: Buoc 1 - Goi CategoryDAO.findAll
        // TODO: Buoc 2 - Xu ly ket qua
        // TODO: Buoc 3 - Tra ve danh sach
        return Collections.emptyList();
    }

    // Chuc nang: Them danh muc moi
    // Dau vao: categoryName (String) - ten danh muc
    // Dau ra: boolean - true neu them thanh cong
    // Tuong tac: Duoc goi tu CategoryManagementPanel; se goi CategoryDAO
    // Ghi chu: Can validate khong trong va khong trung ten
    public boolean addCategory(String categoryName) {
        // TODO: Buoc 1 - Validate ten danh muc
        // TODO: Buoc 2 - Kiem tra trung ten va tao categoryId
        // TODO: Buoc 3 - Luu vao DB va tra ve ket qua
        return false;
    }

    // Chuc nang: Cap nhat danh muc
    // Dau vao: categoryId (String) - ma danh muc; newName (String) - ten moi
    // Dau ra: boolean - true neu cap nhat thanh cong
    // Tuong tac: Duoc goi tu CategoryManagementPanel; se goi CategoryDAO
    // Ghi chu: Can validate va kiem tra trung ten
    public boolean updateCategory(String categoryId, String newName) {
        // TODO: Buoc 1 - Validate du lieu
        // TODO: Buoc 2 - Goi CategoryDAO.update
        // TODO: Buoc 3 - Tra ve ket qua
        return false;
    }

    // Chuc nang: Xoa danh muc
    // Dau vao: categoryId (String) - ma danh muc
    // Dau ra: boolean - true neu xoa thanh cong
    // Tuong tac: Duoc goi tu CategoryManagementPanel; se goi CategoryDAO va RecruitmentDAO
    // Ghi chu: Khong duoc xoa neu dang co tin tuyen dung lien ket
    public boolean deleteCategory(String categoryId) {
        // TODO: Buoc 1 - Kiem tra khong co recruitment lien ket
        // TODO: Buoc 2 - Goi CategoryDAO.delete
        // TODO: Buoc 3 - Tra ve ket qua
        return false;
    }
}
