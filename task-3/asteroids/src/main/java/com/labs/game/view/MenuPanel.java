package com.labs.game.view;

import com.labs.game.model.GameModel;
import com.labs.game.model.ModelStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.Font.BOLD;

public class MenuPanel extends JPanel {
    private GameModel model;
    private JTextField ipField = new JTextField("127.0.0.1", 10);
    private JTextField portField = new JTextField("8080", 5);
    private JButton hostButton = new JButton("Host Server");
    private JButton connectButton = new JButton("Connect");
    private JButton exitButton = new JButton("Exit");
    private final Color bgColor = new Color(0, 0, 0, 150);
    private final Font gameOverFont = new Font(Font.MONOSPACED, BOLD, 50);

    MenuPanel(GameModel model, int width, int height){
        this.model = model;
        this.setOpaque(true);
        this.setBackground(new Color(0, 0, 0, 200));

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(Box.createVerticalGlue());

        add(Box.createVerticalStrut(20));

        JPanel networkPanel = new JPanel(new FlowLayout());
        networkPanel.setOpaque(false);
        networkPanel.add(new JLabel("IP: "));
        networkPanel.add(ipField);
        networkPanel.add(portField);
        add(networkPanel);

        addButton(hostButton);
        addButton(connectButton);
        add(Box.createVerticalStrut(20));
        addButton(exitButton);

        add(Box.createVerticalGlue());

        /*this.setLayout(null);
        this.setPreferredSize(new Dimension(width, height));

        this.addComponentListener(new MenuPanelAdapter(this));
        this.setPreferredSize(new Dimension(width, height));

        startGameButton.setBounds(width/4, height/4, width/2, height/10);
        startGameButton.setFont(new Font("Comic Sans", BOLD, width/20));
        startGameButton.setFocusable(false);

        this.exitButton.setBounds(width / 4, height / 2, width / 2, height / 10);
        this.exitButton.setFont(new Font("MONOSPACED", BOLD, width / 20));
        this.exitButton.setFocusable(false);

        this.add(startGameButton);
        this.add(this.exitButton);*/
    }

    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(bgColor);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        if (model.getStatus() == ModelStatus.GAMEOVER) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(gameOverFont);
            g2d.drawString("GAME OVER", getWidth()/4, getHeight()/6);
        }
    }

    public void setButtonListener(ActionListener listener) {
        connectButton.addActionListener(listener);
        hostButton.addActionListener(listener);
        exitButton.addActionListener(listener);
    }

    public JButton getExitButton() {
        return this.exitButton;
    }


    public void updateBounds(int width, int height){
        this.exitButton.setBounds(width / 4, height / 2, width / 2, height / 10);
    }
    private void addButton(JButton b) {
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(300, 50));
        add(b);
    }

    public JTextField getIpField() { return ipField; }
    public JTextField getPortField() { return portField; }
    public JButton getConnectButton() { return connectButton; }
    public JButton getHostButton() { return hostButton; }
}
