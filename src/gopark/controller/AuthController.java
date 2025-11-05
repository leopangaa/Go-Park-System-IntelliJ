package gopark.controller;

import gopark.model.UserModel;
import gopark.view.LoginView;
import gopark.view.MainDashboardView;

import javax.swing.*;
import java.awt.*;

public class AuthController {

    private UserModel userModel;
    private LoginView loginView;


    public AuthController() {
        userModel = new UserModel();
    }

    public void setLoginView(LoginView lv) { this.loginView = lv; }

    public void login(String username, String password) {
        if (userModel.login(username, password)) {
            JOptionPane.showMessageDialog(null, "Login successful!");
            MainDashboardView view = new MainDashboardView();
            new DashboardController(view);
            view.setVisible(true);
            view.setExtendedState(JFrame.MAXIMIZED_BOTH);
            loginView.dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Invalid username or password.");
        }
    }

    public void signup(String fullname, String username, String password) {
        if (userModel.register(fullname, username, password)) {
            JOptionPane.showMessageDialog(null, "Account created!");
        } else {
            JOptionPane.showMessageDialog(null, "Username already exists.");
        }
    }
}
