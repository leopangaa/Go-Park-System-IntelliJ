package gopark.view;

import gopark.controller.AuthController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginView extends JFrame {

    JTextField usernameField;
    JPasswordField passwordField;
    AuthController controller;

    public LoginView() {
        // LoginView.java
        controller = new AuthController();
        controller.setLoginView(this);


        setTitle("GoPark Login");
        setSize(900, 550);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel form = new JPanel(null);

        JLabel userLbl = new JLabel("Username:");
        userLbl.setBounds(40, 120, 200, 30);
        form.add(userLbl);

        usernameField = new JTextField();
        usernameField.setBounds(40, 150, 250, 30);
        form.add(usernameField);

        JLabel passLbl = new JLabel("Password:");
        passLbl.setBounds(40, 190, 200, 30);
        form.add(passLbl);

        passwordField = new JPasswordField();
        passwordField.setBounds(40, 220, 250, 30);
        form.add(passwordField);

        JButton loginBtn = new JButton("Log In");
        loginBtn.setBounds(40, 270, 250, 35);
        form.add(loginBtn);

        JLabel signupLbl = new JLabel("Don't have an account? Sign Up.");
        signupLbl.setBounds(70, 320, 200, 35);
        signupLbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        form.add(signupLbl);

        add(form, BorderLayout.CENTER);
        setVisible(true);

        loginBtn.addActionListener(e ->
                controller.login(usernameField.getText(), String.valueOf(passwordField.getPassword()))

        );

        signupLbl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dispose();
                new SignupView();
            }
        });
    }
}
