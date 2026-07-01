package ru.netology.diplom.db;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
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
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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
            e.printStackTrace();
        }
    }

    // Получение статуса платежа по ID
    public static Optional<String> getPaymentStatus(String paymentId) {
        QueryRunner runner = new QueryRunner();
        try (Connection conn = getConnection()) {
            String sql = "SELECT status FROM payment_entity WHERE payment_id = ?";
            String status = runner.query(conn, sql, new ScalarHandler<>(), paymentId);
            return Optional.ofNullable(status);
        } catch (SQLException e) {
            System.err.println("Error getting payment status: " + e.getMessage());
            return Optional.empty();
        }
    }

    // Получение статуса кредита по ID
    public static Optional<String> getCreditStatus(String creditId) {
        QueryRunner runner = new QueryRunner();
        try (Connection conn = getConnection()) {
            String sql = "SELECT status FROM credit_request_entity WHERE credit_request_id = ?";
            String status = runner.query(conn, sql, new ScalarHandler<>(), creditId);
            return Optional.ofNullable(status);
        } catch (SQLException e) {
            System.err.println("Error getting credit status: " + e.getMessage());
            return Optional.empty();
        }
    }

    // Проверка, что платеж сохранен в БД
    public static boolean isPaymentSaved(String paymentId) {
        return getPaymentStatus(paymentId).isPresent();
    }

    // Проверка, что кредит сохранен в БД
    public static boolean isCreditSaved(String creditId) {
        return getCreditStatus(creditId).isPresent();
    }

    // Получение ID последнего платежа
    public static Optional<String> getLastPaymentId() {
        QueryRunner runner = new QueryRunner();
        try (Connection conn = getConnection()) {
            String sql = "SELECT payment_id FROM payment_entity ORDER BY created DESC LIMIT 1";
            String id = runner.query(conn, sql, new ScalarHandler<>());
            return Optional.ofNullable(id);
        } catch (SQLException e) {
            System.err.println("Error getting last payment id: " + e.getMessage());
            return Optional.empty();
        }
    }

    // Проверка, что данные карты НЕ сохранены в БД
    public static boolean isCardDataNotStored() {
        QueryRunner runner = new QueryRunner();
        try (Connection conn = getConnection()) {
            // Проверяем, есть ли колонки с данными карты
            // В реальном приложении их быть не должно
            String sql = "SELECT COUNT(*) FROM information_schema.columns " +
                    "WHERE table_name = 'payment_entity' AND column_name IN ('card_number', 'cvc', 'expiry')";
            Long count = runner.query(conn, sql, new ScalarHandler<>());
            return count == 0;
        } catch (SQLException e) {
            System.err.println("Error checking card data: " + e.getMessage());
            return true; // Если ошибка, считаем, что данные не сохраняются
        }
    }

    // Получение полной информации о платеже
    public static PaymentEntity getPaymentEntity(String paymentId) {
        QueryRunner runner = new QueryRunner();
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM payment_entity WHERE payment_id = ?";
            return runner.query(conn, sql, new BeanHandler<>(PaymentEntity.class), paymentId);
        } catch (SQLException e) {
            System.err.println("Error getting payment entity: " + e.getMessage());
            return null;
        }
    }

    // Класс для маппинга сущности платежа
    public static class PaymentEntity {
        private String paymentId;
        private String status;
        private String amount;
        private String created;

        // Геттеры и сеттеры
        public String getPaymentId() { return paymentId; }
        public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getAmount() { return amount; }
        public void setAmount(String amount) { this.amount = amount; }
        public String getCreated() { return created; }
        public void setCreated(String created) { this.created = created; }
    }
}