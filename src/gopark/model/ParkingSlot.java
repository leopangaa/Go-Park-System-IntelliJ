package gopark.model;

public class ParkingSlot {
    private String id;
    private boolean occupied;
    private String type;

    public ParkingSlot(String id, boolean occupied, String type) {
        this.id = id;
        this.occupied = occupied;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public String getType() {
        return type;
    }
}
