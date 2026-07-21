package ru.netology.diplom.db;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public class DatabaseHelper {
    private static final String DB_URL = System.getProperty("db.url", "jdbc:mysql://localhost:3306/app");
    private static final String DB_USER = System.getProperty("db.user", "app");
    private static final String DB_PASSWORD = System.getProperty("db.password", "pass");

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static void cleanDatabase() {
        QueryRunner runner = new QueryRunner();
        try (Connection conn = getConnection()) {
            runner.update(conn, "DELETE FROM order_entity");
            runner.update(conn, "DELETE FROM payment_entity");
            runner.update(conn, "DELETE FROM credit_request_entity");
            System.out.println("Database cleaned successfully");
        } catch (SQLException e) {
            System.err.println("Error cleaning database: " + e.getMessage());
        }
    }

    public static Optional<String> getPaymentStatus(String paymentId) {
        QueryRunner runner = new QueryRunner();
        try (Connection conn = getConnection()) {
            String sql = "SELECT status FROM payment_entity WHERE payment_id = ?";
            String status = runner.query(conn, sql, new ScalarHandler<>(), paymentId);
            return Optional.ofNullable(status);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public static Optional<String> getCreditStatus(String creditId) {
        QueryRunner runner = new QueryRunner();
        try (Connection conn = getConnection()) {
            String sql = "SELECT status FROM credit_request_entity WHERE credit_request_id = ?";
            String status = runner.query(conn, sql, new ScalarHandler<>(), creditId);
            return Optional.ofNullable(status);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public static Optional<String> getLastPaymentId() {
        QueryRunner runner = new QueryRunner();
        try (Connection conn = getConnection()) {
            String sql = "SELECT payment_id FROM payment_entity ORDER BY created DESC LIMIT 1";
            String id = runner.query(conn, sql, new ScalarHandler<>());
            return Optional.ofNullable(id);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public static Optional<String> getLastCreditId() {
        QueryRunner runner = new QueryRunner();
        try (Connection conn = getConnection()) {
            String sql = "SELECT credit_request_id FROM credit_request_entity ORDER BY created DESC LIMIT 1";
            String id = runner.query(conn, sql, new ScalarHandler<>());
            return Optional.ofNullable(id);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
}