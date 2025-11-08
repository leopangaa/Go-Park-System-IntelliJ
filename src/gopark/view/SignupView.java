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
        setResizable(false);
        setUndecorated(true);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(240, 240, 240));
        leftPanel.setPreferredSize(new Dimension(450, 550));

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(450, 550));

        ImageIcon bgIcon = new ImageIcon(getClass().getResource("/gopark/assets/gopark_bg.png"));
        JLabel bgLabel;
        if (bgIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            Image bgImage = bgIcon.getImage().getScaledInstance(450, 550, Image.SCALE_SMOOTH);
            bgLabel = new JLabel(new ImageIcon(bgImage));
        } else {
            bgLabel = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;

                    GradientPaint gradient = new GradientPaint(0, 0, new Color(70, 130, 180),
                            getWidth(), getHeight(), new Color(30, 80, 120));
                    g2.setPaint(gradient);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
            };
        }
        bgLabel.setBounds(0, 0, 450, 550);
        layeredPane.add(bgLabel, JLayeredPane.DEFAULT_LAYER);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBounds(0, 0, 450, 550);

        // Logo
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/gopark/assets/gopark_logo.png"));
        JLabel logoLabel;
        if (logoIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            Image scaledLogo = logoIcon.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
            logoLabel = new JLabel(new ImageIcon(scaledLogo));
        } else {
            logoLabel = new JLabel("") {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    g2.setColor(Color.WHITE);
                    g2.fillOval(50, 50, 300, 300);

                    g2.setColor(new Color(30, 80, 120));
                    g2.setFont(new Font("Arial", Font.BOLD, 120));
                    g2.drawString("P", 150, 220);
                }
            };
            logoLabel.setPreferredSize(new Dimension(400, 400));
        }

        GridBagConstraints gbcLogo = new GridBagConstraints();
        gbcLogo.gridx = 0;
        gbcLogo.gridy = 0;
        gbcLogo.anchor = GridBagConstraints.CENTER;
        contentPanel.add(logoLabel, gbcLogo);

        layeredPane.add(contentPanel, JLayeredPane.PALETTE_LAYER);

        leftPanel.add(layeredPane, BorderLayout.CENTER);

        // Right side - Signup form
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setPreferredSize(new Dimension(450, 550));

        // Form container
        JPanel formContainer = new JPanel();
        formContainer.setLayout(null);
        formContainer.setBackground(Color.WHITE);
        formContainer.setPreferredSize(new Dimension(350, 450));

        // Title Bar
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(Color.WHITE);
        titleBar.setBounds(0, 0, 350, 30);
        titleBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));

        // Title label
        JLabel titleLabel = new JLabel("  GoPark Sign Up");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setForeground(new Color(80, 80, 80));
        titleBar.add(titleLabel, BorderLayout.WEST);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setPreferredSize(new Dimension(70, 30));

        // Minimize button
        JButton minimizeButton = new JButton("−");
        minimizeButton.setPreferredSize(new Dimension(30, 25));
        minimizeButton.setBackground(Color.WHITE);
        minimizeButton.setForeground(Color.DARK_GRAY);
        minimizeButton.setFont(new Font("Arial", Font.BOLD, 14));
        minimizeButton.setBorder(BorderFactory.createEmptyBorder());
        minimizeButton.setFocusPainted(false);
        minimizeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        minimizeButton.addActionListener(e -> setState(JFrame.ICONIFIED));

        minimizeButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                minimizeButton.setBackground(new Color(240, 240, 240));
            }
            public void mouseExited(MouseEvent e) {
                minimizeButton.setBackground(Color.WHITE);
            }
        });

        // Exit button
        JButton exitButton = new JButton("×");
        exitButton.setPreferredSize(new Dimension(30, 25));
        exitButton.setBackground(Color.WHITE);
        exitButton.setForeground(Color.DARK_GRAY);
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.setBorder(BorderFactory.createEmptyBorder());
        exitButton.setFocusPainted(false);
        exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exitButton.addActionListener(e -> System.exit(0));

        exitButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                exitButton.setBackground(new Color(200, 0, 0));
                exitButton.setForeground(Color.WHITE);
            }
            public void mouseExited(MouseEvent e) {
                exitButton.setBackground(Color.WHITE);
                exitButton.setForeground(Color.DARK_GRAY);
            }
        });

        controlPanel.add(minimizeButton);
        controlPanel.add(exitButton);
        titleBar.add(controlPanel, BorderLayout.EAST);

        formContainer.add(titleBar);

        // Title
        JLabel signupTitle = new JLabel("Create Account");
        signupTitle.setFont(new Font("Arial", Font.BOLD, 24));
        signupTitle.setForeground(new Color(50, 50, 50));
        signupTitle.setBounds(0, 50, 350, 40);
        formContainer.add(signupTitle);

        // Subtitle
        JLabel signupSubtitle = new JLabel("Join GoPark today");
        signupSubtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        signupSubtitle.setForeground(Color.GRAY);
        signupSubtitle.setBounds(0, 85, 350, 25);
        formContainer.add(signupSubtitle);

        // Full Name section
        JLabel fullnameLbl = new JLabel("Full Name");
        fullnameLbl.setFont(new Font("Arial", Font.BOLD, 12));
        fullnameLbl.setForeground(new Color(80, 80, 80));
        fullnameLbl.setBounds(0, 120, 200, 20);
        formContainer.add(fullnameLbl);

        fullnameField = new JTextField();
        fullnameField.setBounds(0, 145, 350, 40);
        fullnameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        fullnameField.setFont(new Font("Arial", Font.PLAIN, 14));
        formContainer.add(fullnameField);

        JLabel fullnamePlaceholder = new JLabel("Enter your full name");
        fullnamePlaceholder.setFont(new Font("Arial", Font.ITALIC, 12));
        fullnamePlaceholder.setForeground(Color.GRAY);
        fullnamePlaceholder.setBounds(10, 155, 200, 20);
        formContainer.add(fullnamePlaceholder);

        // Username section
        JLabel userLbl = new JLabel("Username");
        userLbl.setFont(new Font("Arial", Font.BOLD, 12));
        userLbl.setForeground(new Color(80, 80, 80));
        userLbl.setBounds(0, 195, 200, 20);
        formContainer.add(userLbl);

        usernameField = new JTextField();
        usernameField.setBounds(0, 220, 350, 40);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        formContainer.add(usernameField);

        JLabel userPlaceholder = new JLabel("Choose a username");
        userPlaceholder.setFont(new Font("Arial", Font.ITALIC, 12));
        userPlaceholder.setForeground(Color.GRAY);
        userPlaceholder.setBounds(10, 230, 200, 20);
        formContainer.add(userPlaceholder);

        // Password section
        JLabel passLbl = new JLabel("Password");
        passLbl.setFont(new Font("Arial", Font.BOLD, 12));
        passLbl.setForeground(new Color(80, 80, 80));
        passLbl.setBounds(0, 270, 200, 20);
        formContainer.add(passLbl);

        passwordField = new JPasswordField();
        passwordField.setBounds(0, 295, 350, 40);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        formContainer.add(passwordField);

        JLabel passPlaceholder = new JLabel("Create a strong password");
        passPlaceholder.setFont(new Font("Arial", Font.ITALIC, 12));
        passPlaceholder.setForeground(Color.GRAY);
        passPlaceholder.setBounds(10, 305, 200, 20);
        formContainer.add(passPlaceholder);

        // Signup button
        JButton signupBtn = new JButton("Sign Up");
        signupBtn.setBounds(0, 355, 350, 45);
        signupBtn.setBackground(new Color(0, 100, 200));
        signupBtn.setForeground(Color.WHITE);
        signupBtn.setFont(new Font("Arial", Font.BOLD, 14));
        signupBtn.setBorder(BorderFactory.createEmptyBorder());
        signupBtn.setFocusPainted(false);
        signupBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        signupBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                signupBtn.setBackground(new Color(0, 80, 180));
            }
            public void mouseExited(MouseEvent e) {
                signupBtn.setBackground(new Color(0, 100, 200));
            }
        });
        formContainer.add(signupBtn);

        // Login link
        JLabel loginLbl = new JLabel("Already have an account? Login.");
        loginLbl.setFont(new Font("Arial", Font.PLAIN, 12));
        loginLbl.setForeground(Color.GRAY);
        loginLbl.setBounds(75, 415, 200, 20);
        loginLbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        loginLbl.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                loginLbl.setForeground(new Color(0, 100, 200));
            }
            public void mouseExited(MouseEvent e) {
                loginLbl.setForeground(Color.GRAY);
            }
        });
        formContainer.add(loginLbl);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        rightPanel.add(formContainer, gbc);

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        add(mainPanel);

        // Event listeners
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

        fullnameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                fullnamePlaceholder.setVisible(false);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (fullnameField.getText().isEmpty()) {
                    fullnamePlaceholder.setVisible(true);
                }
            }
        });

        usernameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                userPlaceholder.setVisible(false);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (usernameField.getText().isEmpty()) {
                    userPlaceholder.setVisible(true);
                }
            }
        });

        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                passPlaceholder.setVisible(false);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (passwordField.getPassword().length == 0) {
                    passPlaceholder.setVisible(true);
                }
            }
        });

        if (!fullnameField.getText().isEmpty()) {
            fullnamePlaceholder.setVisible(false);
        }
        if (!usernameField.getText().isEmpty()) {
            userPlaceholder.setVisible(false);
        }
        if (passwordField.getPassword().length > 0) {
            passPlaceholder.setVisible(false);
        }

        setVisible(true);
    }
}