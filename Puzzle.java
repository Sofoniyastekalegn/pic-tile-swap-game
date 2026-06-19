import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Puzzle extends JFrame implements ActionListener {

    private final JButton[] buttons = new JButton[9];
    private final JButton shuffle = new JButton("Shuffle!");
    private final JLabel counterLabel = new JLabel("Clicks: 0");
    private int counter = 0;

    public Puzzle() {
        setTitle("3x3 Puzzle");
        setSize(400, 400);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create buttons 1..8 and blank
        for (int i = 0; i < 8; i++) {
            buttons[i] = new JButton(String.valueOf(i + 1));
        }
        buttons[8] = new JButton(" "); // blank

        // add listeners and add to frame
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].addActionListener(this);
            add(buttons[i]);
            buttons[i].setBackground(Color.decode("#5adbb5"));
            buttons[i].setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));
        }

        shuffle.addActionListener(this);
        shuffle.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
        shuffle.setBackground(Color.LIGHT_GRAY);
        add(shuffle);

        add(counterLabel);
        counterLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));

        // layout positions
        int startX = 90, startY = 60, w = 50, h = 40, gapX = 70, gapY = 55;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int idx = row * 3 + col;
                buttons[idx].setBounds(startX + col * gapX, startY + row * gapY, w, h);
            }
        }
        shuffle.setBounds(135, 245, 100, 40);
        counterLabel.setBounds(145, 15, 180, 40);

        // initial shuffle
        shuffleBoard();

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == shuffle) {
            shuffleBoard();
            counter = 0;
            counterLabel.setText("Clicks: 0");
            return;
        }

        // find which button was clicked
        int clicked = -1;
        for (int i = 0; i < buttons.length; i++) {
            if (src == buttons[i]) {
                clicked = i;
                break;
            }
        }
        if (clicked == -1) return;

        int blank = findBlankIndex();
        if (isAdjacent(clicked, blank)) {
            // swap text
            String t = buttons[clicked].getText();
            buttons[blank].setText(t);
            buttons[clicked].setText(" ");
            counter++;
            counterLabel.setText("Clicks: " + counter);
            if (checkWin()) {
                JOptionPane.showMessageDialog(this, "YOU WON!\nYou clicked: " + counter + " times.");
            }
        }
    }

    private int findBlankIndex() {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].getText().equals(" ")) return i;
        }
        return -1;
    }

    private boolean isAdjacent(int i, int j) {
        int ri = i / 3, ci = i % 3;
        int rj = j / 3, cj = j % 3;
        return (Math.abs(ri - rj) + Math.abs(ci - cj)) == 1;
    }

    private boolean checkWin() {
        for (int i = 0; i < 8; i++) {
            if (!buttons[i].getText().equals(String.valueOf(i + 1))) return false;
        }
        return buttons[8].getText().equals(" ");
    }

    private void shuffleBoard() {
        // perform a number of random valid moves from solved state
        // start from solved
        for (int i = 0; i < 8; i++) buttons[i].setText(String.valueOf(i + 1));
        buttons[8].setText(" ");
        Random rnd = new Random();
        int moves = 100;
        for (int m = 0; m < moves; m++) {
            int blank = findBlankIndex();
            int dir = rnd.nextInt(4);
            int swapIdx = -1;
            int r = blank / 3, c = blank % 3;
            if (dir == 0 && r > 0) swapIdx = (r - 1) * 3 + c; // up
            else if (dir == 1 && r < 2) swapIdx = (r + 1) * 3 + c; // down
            else if (dir == 2 && c > 0) swapIdx = r * 3 + (c - 1); // left
            else if (dir == 3 && c < 2) swapIdx = r * 3 + (c + 1); // right
            if (swapIdx != -1) {
                String t = buttons[swapIdx].getText();
                buttons[swapIdx].setText(buttons[blank].getText());
                buttons[blank].setText(t);
            }
        }
    }

    // allow launching directly
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Puzzle());
    }

}
