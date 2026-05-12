package org.jobportal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class QueryUsers {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/job_portal", "merlin", "Vlong@1926");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT u.username, u.role, u.full_name, e.company_name FROM users u LEFT JOIN employers e ON u.user_id = e.user_id");
            while(rs.next()) {
                System.out.println("User: " + rs.getString("username") + " | Role: " + rs.getString("role") + " | FullName: " + rs.getString("full_name") + " | Company: " + rs.getString("company_name"));
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
