package gopark.controller;

import gopark.model.ParkingSlot;
import gopark.view.ParkingSlotView;
import java.util.ArrayList;
import java.util.List;

public class ParkingSlotController {
    private List<ParkingSlot> slots;
    private ParkingSlotView view;

    public ParkingSlotController() {
        slots = new ArrayList<>();

        // Sample slots
        for (int i = 1; i <= 18; i++) {
            slots.add(new ParkingSlot("C" + i, i % 4 == 0, "Car"));
        }
        for (int i = 1; i <= 17; i++) {
            slots.add(new ParkingSlot("M" + i, i % 3 == 0, "Motorcycle"));
        }

        view = new ParkingSlotView(slots);
        updateStats();
        view.setVisible(true);
    }

    private void updateStats() {
        long occupied = slots.stream().filter(ParkingSlot::isOccupied).count();
        long available = slots.size() - occupied;

        view.getAvailableLabel().setText("Available: " + available + " Slots");
        view.getOccupiedLabel().setText("Occupied: " + occupied + " Slots");
    }

    public static void main(String[] args) {
        new ParkingSlotController();
    }
}
