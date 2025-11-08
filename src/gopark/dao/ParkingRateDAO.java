package gopark.dao;

import gopark.model.DBConnection;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ParkingRateDAO {

    public static Map<String, Double> getParkingRates() {
        Map<String, Double> rates = new HashMap<>();
        String sql = "SELECT vehicle_type, first_3_hours, succeeding_hour, overnight_rate FROM parking_rates";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String vehicleType = rs.getString("vehicle_type");
                rates.put(vehicleType + "_first_3_hours", rs.getDouble("first_3_hours"));
                rates.put(vehicleType + "_succeeding_hour", rs.getDouble("succeeding_hour"));
                rates.put(vehicleType + "_overnight_rate", rs.getDouble("overnight_rate"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            setDefaultRates();
            return getParkingRates();
        }
        return rates;
    }

    public static boolean updateParkingRates(String vehicleType, double first3Hours, double succeedingHour, double overnightRate) {
        String sql = "INSERT INTO parking_rates (vehicle_type, first_3_hours, succeeding_hour, overnight_rate) " +
                "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE " +
                "first_3_hours = VALUES(first_3_hours), " +
                "succeeding_hour = VALUES(succeeding_hour), " +
                "overnight_rate = VALUES(overnight_rate)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vehicleType);
            stmt.setDouble(2, first3Hours);
            stmt.setDouble(3, succeedingHour);
            stmt.setDouble(4, overnightRate);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void setDefaultRates() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS parking_rates (
                vehicle_type VARCHAR(20) PRIMARY KEY,
                first_3_hours DECIMAL(10,2) NOT NULL,
                succeeding_hour DECIMAL(10,2) NOT NULL,
                overnight_rate DECIMAL(10,2) NOT NULL
            )
        """;

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(createTableSQL);

            // Insert default rates
            updateParkingRates("Motorcycle", 30.0, 10.0, 350.0);
            updateParkingRates("Car", 40.0, 15.0, 500.0);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}