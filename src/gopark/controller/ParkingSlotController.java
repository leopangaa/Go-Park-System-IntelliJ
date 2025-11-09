package gopark.controller;

import gopark.dao.ParkingSlotDAO;
import gopark.model.ParkingSlot;
import gopark.view.dialog.NewEntryDialog;
import gopark.view.tabs.ParkingSlotView;

import javax.swing.*;
import java.util.List;

public class ParkingSlotController {
    private List<ParkingSlot> slots;
    private ParkingSlotView view;

    public ParkingSlotController() {
        loadSlots();
        view = new ParkingSlotView(slots);

        view.setRefreshCallback(() -> reloadSlots());

        setupListeners();
    }

    private void setupListeners() {
        view.getNewEntryButton().addActionListener(e -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(view);
            new NewEntryDialog(parent);
            reloadSlots();
        });
    }

    private void loadSlots() {
        slots = ParkingSlotDAO.getAllSlots();
    }

    private void reloadSlots() {
        loadSlots();
        view.updateSlots(slots);
    }

    public JPanel getView() {
        return view;
    }
}
