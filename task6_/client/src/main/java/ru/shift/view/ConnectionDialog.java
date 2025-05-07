package ru.shift.view;

import ru.shift.data.ConnectionData;

import javax.swing.*;

public class ConnectionDialog {

    public ConnectionData askConnectionData() {
        JTextField ipField = new JTextField("localhost");
        JTextField portField = new JTextField("8899");

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Server IP:"));
        panel.add(ipField);
        panel.add(new JLabel("Port:"));
        panel.add(portField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Connect to Server", JOptionPane.OK_CANCEL_OPTION);

        if (result != JOptionPane.OK_OPTION) return null;

        String ip = ipField.getText().trim();
        int port = Integer.parseInt(portField.getText().trim());

        //String name = JOptionPane.showInputDialog(null, "Enter your name:", "Username", JOptionPane.PLAIN_MESSAGE);
        //if (name == null || name.trim().isEmpty()) return null;

        return new ConnectionData(ip, port);
    }
}
