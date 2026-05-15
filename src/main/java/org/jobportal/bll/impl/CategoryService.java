package org.jobportal.bll.impl;

import org.jobportal.dal.impl.CategoryDAO;
import org.jobportal.dal.impl.RecruitmentDAO;
import org.jobportal.dal.interfaces.ICategoryDAO;
import org.jobportal.dal.interfaces.IRecruitmentDAO;
import org.jobportal.model.Category;

import java.util.Collections;
import java.util.List;

/**
 * CategoryService - Xu ly nghiep vu quan ly danh muc nganh nghe.
 * Khong viet SQL, khong goi Swing.
 */
public class CategoryService {

    private final ICategoryDAO    categoryDAO    = new CategoryDAO();
    private final IRecruitmentDAO recruitmentDAO = new RecruitmentDAO();

    // ------------------------------------------------------------------
    // ID generation
    // ------------------------------------------------------------------

    /** Sinh categoryId format "CAT-" + 6 so => 10 ky tu */
    private String generateCategoryId() {
        long ts = System.currentTimeMillis() % 1_000_000L;
        return String.format("CAT-%06d", ts);
    }

    // ------------------------------------------------------------------
    // getAllCategories
    // ------------------------------------------------------------------

    /** Lay tat ca danh muc tu DB */
    public List<Category> getAllCategories() {
        List<Category> list = categoryDAO.findAll();
        return (list != null) ? list : Collections.emptyList();
    }

    // ------------------------------------------------------------------
    // addCategory
    // ------------------------------------------------------------------

    /**
     * Them danh muc moi.
     * Validate ten khong rong, khong trung.
     */
    public boolean addCategory(String categoryName) {
        if (categoryName == null || categoryName.isBlank()) {
            System.err.println("[CategoryService] addCategory: ten danh muc khong duoc de trong.");
            return false;
        }
        String trimmed = categoryName.trim();
        if (categoryDAO.existsByName(trimmed)) {
            System.err.println("[CategoryService] addCategory: ten danh muc da ton tai.");
            return false;
        }

        String categoryId = generateCategoryId();
        // Tranh trung ID
        while (categoryDAO.findById(categoryId) != null) {
            try { Thread.sleep(1); } catch (InterruptedException ignored) {}
            categoryId = generateCategoryId();
        }

        Category category = new Category(categoryId, trimmed);
        return categoryDAO.insert(category);
    }

    // ------------------------------------------------------------------
    // updateCategory
    // ------------------------------------------------------------------

    /**
     * Cap nhat ten danh muc.
     * Validate ten moi khong rong, khong trung voi danh muc khac.
     */
    public boolean updateCategory(String categoryId, String newName) {
        if (categoryId == null || categoryId.isBlank()) return false;
        if (newName == null || newName.isBlank()) {
            System.err.println("[CategoryService] updateCategory: ten moi khong duoc de trong.");
            return false;
        }
        String trimmed = newName.trim();

        // Lay danh muc hien tai de kiem tra
        Category existing = categoryDAO.findById(categoryId);
        if (existing == null) {
            System.err.println("[CategoryService] updateCategory: khong tim thay danh muc id=" + categoryId);
            return false;
        }

        // Neu ten moi khac ten cu thi kiem tra trung
        if (!trimmed.equalsIgnoreCase(existing.getCategoryName()) && categoryDAO.existsByName(trimmed)) {
            System.err.println("[CategoryService] updateCategory: ten da ton tai.");
            return false;
        }

        existing.setCategoryName(trimmed);
        return categoryDAO.update(existing);
    }

    // ------------------------------------------------------------------
    // deleteCategory
    // ------------------------------------------------------------------

    /**
     * Xoa danh muc.
     * Rang buoc: khong xoa neu con tin tuyen dung dang su dung danh muc nay.
     */
    public boolean deleteCategory(String categoryId) {
        if (categoryId == null || categoryId.isBlank()) return false;

        int recruitmentCount = recruitmentDAO.countByCategory(categoryId);
        if (recruitmentCount > 0) {
            System.err.println("[CategoryService] deleteCategory: khong the xoa - co " + recruitmentCount
                    + " tin tuyen dung dang dung danh muc nay.");
            return false;
        }

        return categoryDAO.delete(categoryId);
    }
}

