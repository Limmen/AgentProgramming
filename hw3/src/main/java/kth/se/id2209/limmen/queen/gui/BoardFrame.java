package kth.se.id2209.limmen.queen.gui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * @author Kim Hammar on 2016-11-22.
 */
public class BoardFrame extends JFrame {
    private int[][] board;

    public BoardFrame(int[][] board) throws HeadlessException {
        this.board = board;
        setLayout(new MigLayout());
        setTitle("Queens Puzzle");
        setContentPane(new Container());
        pack();
        setLocationRelativeTo(null); //center on screen
        setVisible(false);
    }

    private class Container extends JPanel {
        public Container() {
            setLayout(new MigLayout("wrap 1"));
            add(new JLabel("test"), "wrap 1, center");
            add(new Square(), "wrap 1, center");
        }
    }

    private class Square extends JPanel{
        public Square() {
            setLayout(new MigLayout("wrap 1"));
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.RED);
            g.fillRect(0, 0, 50, 50);
        }
    }
}
