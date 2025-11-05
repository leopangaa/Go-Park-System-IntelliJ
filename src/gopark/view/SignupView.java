package gopark.view;

import gopark.controller.AuthController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SignupView extends JFrame {

    JTextField fullnameField, usernameField;
    JPasswordField passwordField;
    AuthController controller;

    public SignupView() {
        controller = new AuthController();

        setTitle("GoPark Sign Up");
        setSize(900, 550);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel form = new JPanel(null);

        JLabel fnLbl = new JLabel("Full Name:");
        fnLbl.setBounds(40, 80, 200, 30);
        form.add(fnLbl);

        fullnameField = new JTextField();
        fullnameField.setBounds(40, 110, 250, 30);
        form.add(fullnameField);

        JLabel userLbl = new JLabel("Username:");
        userLbl.setBounds(40, 150, 200, 30);
        form.add(userLbl);

        usernameField = new JTextField();
        usernameField.setBounds(40, 180, 250, 30);
        form.add(usernameField);

        JLabel passLbl = new JLabel("Password:");
        passLbl.setBounds(40, 220, 200, 30);
        form.add(passLbl);

        passwordField = new JPasswordField();
        passwordField.setBounds(40, 250, 250, 30);
        form.add(passwordField);

        JButton signupBtn = new JButton("Sign Up");
        signupBtn.setBounds(40, 300, 250, 35);
        form.add(signupBtn);

        JLabel loginLbl = new JLabel("Already have an account? Login.");
        loginLbl.setBounds(70, 340, 220, 35);
        loginLbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        form.add(loginLbl);

        add(form, BorderLayout.CENTER);
        setVisible(true);

        signupBtn.addActionListener(e ->
                controller.signup(
                        fullnameField.getText(),
                        usernameField.getText(),
                        String.valueOf(passwordField.getPassword())
                )
        );

        loginLbl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dispose();
                new LoginView();
            }
        });
    }
}
