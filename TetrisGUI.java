package eecs40.tetris;

import eecs40.tetris.TetrisBoard;
import eecs40.tetris.TetrisObserver;
import eecs40.tetris.TetrisShape;

import javax.swing.*;
import java.awt.*;

public class TetrisGUI extends JPanel implements TetrisObserver {

    TetrisBoard tb ;
    int [][] board;

    public TetrisGUI (int height, int width) {
        tb = new TetrisBoard();
        tb.init(height,width);
    }
    public void left () {
        tb .moveLeft();
    }
    public void right () {
        tb .moveRight();
    }
    public void down () {
        tb .moveDown();
    }
    public void downToEnd () {
        tb .moveDownToEnd();
    }
    public void rotate () {
        tb .rotateLeft();
    }

    public void paint (Graphics g) {
        board = tb.board;
        int width = board[0].length;
        int height = board.length;
        int xloc = 0, yloc = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 2)
                    g.setColor(Color.BLACK);
                else if (board[i][j] == 1) {
                    g.setColor(Color.CYAN);
                }
                else
                    g.setColor(Color.LIGHT_GRAY);
                g.fillRect(xloc,yloc,500/width - 1,800/height -1);
                xloc += 500/width;
            }
            xloc = 0;
            yloc += 800/height;
        }
    }

    public static void main (String[] args) {
        TetrisGUI gui = new TetrisGUI(5, 5);
        gui.tb.addObserver(gui);

        JFrame tetrisFrame = new JFrame("Tetris Bejeweled Remastered!");
        tetrisFrame.add(new TetrisGUI(5, 5));
        tetrisFrame.setDefaultCloseOperation(JFrame. EXIT_ON_CLOSE);
        tetrisFrame.setSize(500, 800);
        tetrisFrame.setVisible(true);

        tetrisFrame.addKeyListener(new java.awt.event.KeyAdapter () {
            @Override
            public void keyPressed ( java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == 37 ) // left key
                    gui .left();
                if (e.getKeyCode() == 39 ) // right key
                    gui .right();
                if (e.getKeyCode() == 40 ) // down key
                    gui .down();
                if (e.getKeyCode() == 38 ) // up key
                    gui .rotate();
                if (e.getKeyCode() == 32 ) // space key
                    gui .downToEnd();
                if (e.getKeyCode() == 27 ) // esc key
                    System . exit ( 0 );
            }
        });
        gui.board = gui.tb.getBoardArray();
        new Thread() {
            @Override
            public void run () {

                gui. tb .addShape( TetrisShape . getRandomShape ());
                tetrisFrame.repaint();
                while ( true ) {
                    gui. tb .moveDown();
                    tetrisFrame.repaint();
                    for (int i = 0; i < gui.board.length; i++) {
                        for (int j = 0; j < gui.board[i].length; j++) {
                            System.out.print(gui.board[i][j]);
                        }
                        System.out.println();
                    }
                    System.out.printf("\n");
                    gui.board = gui.tb.board;
                    try {
                        Thread . sleep ( 1000 );
                    } catch ( InterruptedException ie) {
                        System . err .println( "Interrupted..." );
                    }
                }

            }
        }.start();



    }

    @Override
    public void notifyLinesClear ( int num) {
    }
    @Override
    public void notifyNeedShape ( TetrisBoard b) {
        b.addShape( TetrisShape. getRandomShape ()); // give random shape
    }
    @Override
    public void notifyGameOver ( TetrisBoard b) {
        System . out .println( "Game Over" );
        System . exit ( 1 );
    }
}
