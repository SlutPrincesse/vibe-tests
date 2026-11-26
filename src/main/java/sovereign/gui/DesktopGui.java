package sovereign.gui;

import javax.swing.*;
import java.awt.*;

public class DesktopGui {
    public static void show() {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Sovereign Agent");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(800, 600);
            JTextArea chat = new JTextArea();
            chat.setEditable(false);
            JTextField input = new JTextField();
            JButton send = new JButton("Send");
            JPanel bottom = new JPanel(new BorderLayout());
            bottom.add(input, BorderLayout.CENTER);
            bottom.add(send, BorderLayout.EAST);
            f.setLayout(new BorderLayout());
            f.add(new JScrollPane(chat), BorderLayout.CENTER);
            f.add(bottom, BorderLayout.SOUTH);
            send.addActionListener(e -> {
                chat.append("You: " + input.getText() + "\n");
                chat.append("Agent: (processing...)\n");
                input.setText("");
            });
            f.setVisible(true);
        });
    }
}
