package org.jobportal.dal;

import org.jobportal.dal.impl.CategoryDAO;
import org.jobportal.dal.interfaces.ICategoryDAO;
import org.jobportal.model.Category;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategoryDAOTest extends DalIntegrationTestBase {

    private final ICategoryDAO categoryDAO = new CategoryDAO();

    @Test
    void testFindMethodsWithSelfCreatedData() {
        String categoryId = newCategoryId();
        String categoryName = "TEST CAT " + categoryId.substring(4);
        Category category = new Category(categoryId, categoryName);

        assertTrue(categoryDAO.insert(category));
        registerCleanupId("categories", categoryId);

        assertNotNull(categoryDAO.findById(categoryId));
        assertTrue(categoryDAO.findAll().stream().anyMatch(c -> categoryId.equals(c.getCategoryId())));
        assertTrue(categoryDAO.existsByName(categoryName));
    }

    @Test
    void testInsertUpdateDelete() {
        String categoryId = newCategoryId();
        String categoryName = "TEST CAT " + categoryId.substring(4);
        Category category = new Category(categoryId, categoryName);

        assertTrue(categoryDAO.insert(category));
        registerCleanupId("categories", categoryId);

        category.setCategoryName("TEST CAT UPDATED " + categoryId.substring(4));
        assertTrue(categoryDAO.update(category));
        assertEquals(category.getCategoryName(), categoryDAO.findById(categoryId).getCategoryName());

        assertTrue(categoryDAO.delete(categoryId));
        assertNull(categoryDAO.findById(categoryId));
    }
}
