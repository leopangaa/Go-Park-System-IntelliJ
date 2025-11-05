package gopark;

import gopark.controller.DashboardController;
import gopark.view.LoginView;
import gopark.view.MainDashboardView;

public class App {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}
