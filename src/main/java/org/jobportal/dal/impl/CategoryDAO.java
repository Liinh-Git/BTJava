package org.jobportal.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.jobportal.config.DatabaseConfig;
import org.jobportal.dal.interfaces.ICategoryDAO;
import org.jobportal.model.Category;

public class CategoryDAO implements ICategoryDAO {

    // Ánh xạ một hàng ResultSet sang đối tượng Category.
    private Category mapRow(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setCategoryId(rs.getString("category_id"));
        category.setCategoryName(rs.getString("category_name"));
        return category;
    }

    // Chức năng: Lấy danh sách danh mục
    // Đầu vào: (void)
    // Đầu ra: List<Category> - danh sách danh mục
    // Tương tác: Được gọi từ CategoryService; sẽ dùng JDBC
    // Ghi chú: Trả về danh sách đầy đủ, sắp xếp theo tên
    @Override
    public List<Category> findAll() {
        // Bước 1 - Tạo kết nối và query SELECT
        String sql = "SELECT category_id, category_name FROM categories ORDER BY category_name";
        List<Category> result = new ArrayList<>();

        // Bước 2 - Map ResultSet sang list Category
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách category: " + e.getMessage(), e);
        }

        // Bước 3 - Đóng kết nối và trả về (try-with-resources tự đóng)
        return result;
    }

    // Chức năng: Tìm danh mục theo id
    // Đầu vào: categoryId (String) - mã danh mục
    // Đầu ra: Category - danh mục tìm thấy
    // Tương tác: Được gọi từ CategoryService; sẽ dùng JDBC
    // Ghi chú: Trả về null nếu không tìm thấy
    @Override
    public Category findById(String categoryId) {
        // Bước 1 - Query SELECT theo categoryId
        String sql = "SELECT category_id, category_name FROM categories WHERE category_id = ?";

        // Bước 2 - Map ResultSet sang Category
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, categoryId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm category theo id: " + e.getMessage(), e);
        }
        // Bước 3 - Trả về kết quả
        return null;
    }

    // Chức năng: Thêm danh mục
    // Đầu vào: category (Category) - danh mục cần thêm
    // Đầu ra: boolean - true nếu thêm thành công
    // Tương tác: Được gọi từ CategoryService; sẽ dùng JDBC
    // Ghi chú: Kiểm tra trùng tên trước khi insert (do BLL thực hiện)
    @Override
    public boolean insert(Category category) {
        // Bước 1 - Tạo câu lệnh INSERT
        String sql = "INSERT INTO categories (category_id, category_name) VALUES (?, ?)";

        // Bước 2 - Thực thi và lấy kết quả
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, category.getCategoryId());
            ps.setString(2, category.getCategoryName());

            // Bước 3 - Trả về boolean
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi thêm category: " + e.getMessage(), e);
        }
    }

    // Chức năng: Cập nhật danh mục
    // Đầu vào: category (Category) - danh mục cần cập nhật
    // Đầu ra: boolean - true nếu cập nhật thành công
    // Tương tác: Được gọi từ CategoryService; sẽ dùng JDBC
    // Ghi chú: Cập nhật theo categoryId
    @Override
    public boolean update(Category category) {
        // Bước 1 - Tạo câu lệnh UPDATE
        String sql = "UPDATE categories SET category_name = ? WHERE category_id = ?";

        // Bước 2 - Thực thi và lấy kết quả
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getCategoryId());

            // Bước 3 - Trả về boolean
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi cập nhật category: " + e.getMessage(), e);
        }
    }

    // Chức năng: Xóa danh mục
    // Đầu vào: categoryId (String) - mã danh mục
    // Đầu ra: boolean - true nếu xóa thành công
    // Tương tác: Được gọi từ CategoryService; sẽ dùng JDBC
    // Ghi chú: Cần đảm bảo không có recruitment liên kết (BLL kiểm tra countByCategory trước)
    @Override
    public boolean delete(String categoryId) {
        // Bước 1 - Tạo câu lệnh DELETE
        String sql = "DELETE FROM categories WHERE category_id = ?";

        // Bước 2 - Thực thi và lấy kết quả
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, categoryId);

            // Bước 3 - Trả về boolean
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi xóa category: " + e.getMessage(), e);
        }
    }

    // Chức năng: Kiểm tra tồn tại theo tên
    // Đầu vào: categoryName (String) - tên danh mục
    // Đầu ra: boolean - true nếu đã tồn tại
    // Tương tác: Được gọi từ CategoryService; sẽ dùng JDBC
    // Ghi chú: Dùng để kiểm tra trùng tên (không phân biệt hoa/thường)
    @Override
    public boolean existsByName(String categoryName) {
        // Bước 1 - Query COUNT theo categoryName (LOWER để không phân biệt hoa/thường)
        String sql = "SELECT COUNT(*) FROM categories WHERE LOWER(category_name) = LOWER(?)";

        // Bước 2 - Đọc kết quả
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, categoryName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi kiểm tra tên category: " + e.getMessage(), e);
        }
        // Bước 3 - Trả về boolean
        return false;
    }
}
