package org.jobportal.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.jobportal.config.DatabaseConfig;
import org.jobportal.enums.Gender;
import org.jobportal.enums.Role;
import org.jobportal.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

public abstract class DalIntegrationTestBase {

    private static final AtomicInteger SEQ = new AtomicInteger((int) (System.currentTimeMillis() % 900000) + 100000);

    private static final Map<String, String> ID_COLUMNS = Map.of(
            "applications", "application_id",
            "cvs", "cv_id",
            "recruitments", "recruitment_id",
            "candidates", "candidate_id",
            "employers", "employer_id",
            "categories", "category_id",
            "users", "user_id");

    private static final List<String> CLEANUP_ORDER = List.of(
            "applications",
            "cvs",
            "recruitments",
            "candidates",
            "employers",
            "categories",
            "users");

    private final Map<String, List<String>> cleanupIdsByTable = new LinkedHashMap<>();

    @BeforeEach
    void verifyTestDatabase(TestInfo info) throws SQLException {
        String url = DatabaseConfig.getDbUrl();
        if (!url.contains("job_portal_test")) {
            throw new IllegalStateException("Refuse to run integration test on non-test DB. Current db.url=" + url
                    + ". Please run with -Ddb.url=jdbc:mysql://127.0.0.1:3306/job_portal_test");
        }

        try (Connection conn = DatabaseConfig.getConnection()) {
            if (conn == null || conn.isClosed()) {
                throw new IllegalStateException("Cannot open DB connection for test: " + info.getDisplayName());
            }
        }
    }

    @AfterEach
    void cleanup() throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            for (String table : CLEANUP_ORDER) {
                List<String> ids = cleanupIdsByTable.get(table);
                if (ids == null || ids.isEmpty()) {
                    continue;
                }
                String idColumn = ID_COLUMNS.get(table);
                String sql = "DELETE FROM " + table + " WHERE " + idColumn + " = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    for (String id : ids) {
                        ps.setString(1, id);
                        ps.executeUpdate();
                    }
                }
            }
        } finally {
            cleanupIdsByTable.clear();
        }
    }

    protected void registerCleanupId(String table, String id) {
        cleanupIdsByTable.computeIfAbsent(table, k -> new ArrayList<>()).add(0, id);
    }

    private String nextSuffix() {
        int n = SEQ.incrementAndGet();
        return String.format("%06d", n % 1000000);
    }

    protected String newUserId() { return "U-" + nextSuffix(); }
    protected String newCandidateId() { return "CAN-" + nextSuffix(); }
    protected String newEmployerId() { return "EMP-" + nextSuffix(); }
    protected String newCategoryId() { return "CAT-" + nextSuffix(); }
    protected String newRecruitmentId() { return "REC-" + nextSuffix(); }
    protected String newApplicationId() { return "APP-" + nextSuffix(); }
    protected String newCvId() { return "CV-" + nextSuffix(); }

    protected User newUser(String userId, String username, String email, Role role) {
        User user = new User();
        user.setUserId(userId);
        user.setUsername(username);
        user.setPasswordHash("58abc71c7e988e7921ae0a65df481791b5b2c6f4e5efe90a10b03c91fd252486");
        user.setFullName("TEST " + username);
        user.setPhoneNumber("0900000000");
        user.setDateOfBirth(LocalDate.of(2000, 1, 1));
        user.setGender(Gender.OTHER);
        user.setEmail(email);
        user.setRole(role);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }
}
