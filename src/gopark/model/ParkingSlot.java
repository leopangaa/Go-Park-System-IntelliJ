package gopark.model;

import java.awt.*;

public class ParkingSlot {
    private String id;
    private String status; // AVAILABLE, HOLD, OCCUPIED
    private String type;

    public ParkingSlot(String id, String status, String type) {
        this.id = id;
        this.status = status == null ? "AVAILABLE" : status;
        this.type = type;
    }

    public String getId() { return id; }
    public String getStatus() { return status; }
    public String getType() { return type; }

    public boolean isOccupied() {
        return "OCCUPIED".equalsIgnoreCase(status) || "HOLD".equalsIgnoreCase(status);
    }

    public Color getColor() {
        if ("OCCUPIED".equalsIgnoreCase(status)) {
            return new Color(220, 53, 69); // red
        } else if ("HOLD".equalsIgnoreCase(status)) {
            return new Color(255, 193, 7); // yellow
        } else {
            return new Color(40, 167, 69); // green
        }
    }
}
