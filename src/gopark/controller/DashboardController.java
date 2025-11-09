package gopark.controller;

import gopark.view.*;
import gopark.view.auth.LoginView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardController {

    private MainDashboardView mainView;

    public DashboardController(MainDashboardView mainView) {
        this.mainView = mainView;

        SidebarView sidebar = mainView.getSidebarView();

        sidebar.getBtnDashboard().addActionListener(new SidebarListener("Dashboard"));
        sidebar.getBtnParking().addActionListener(new SidebarListener("Parking"));
        sidebar.getBtnTransactions().addActionListener(new SidebarListener("Transactions"));
        sidebar.getBtnRevenue().addActionListener(new SidebarListener("Revenue"));
        sidebar.getBtnSettings().addActionListener(new SidebarListener("Settings"));

        mainView.getSidebarView().getBtnLogout().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
    }

    private class SidebarListener implements ActionListener {
        private String panelName;

        public SidebarListener(String panelName) {
            this.panelName = panelName;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            mainView.getCardLayout().show(mainView.getMainPanel(), panelName);
        }
    }

    private void logout() {
        mainView.dispose();
        new LoginView();
    }
}
