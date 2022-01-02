package eecs40.tetris;

import javax.swing.*;

import eecs40.tetris.TetrisObserver;
import eecs40.tetris.TetrisGameTUI;

import java.awt.*;
import java.util.*;
import java.util.List;

public class TetrisBoard extends AbstractTetrisBoard {

    private TetrisObserver ob = new TetrisObserver() {
        String msg = "";
        // Uncomment the items in the functions to enable the TetrisGUI to function
        // Or revert them to how TetrisGameTUI to run TUI
        @Override
        public void notifyLinesClear(int num) {
//            if (num == 4 ) {
//                msg = "Tetris!" ;
//            } else {
//                msg = num + " lines cleared" ;
//            }
        }

        @Override
        public void notifyNeedShape(TetrisBoard b) {
//            b.addShape( TetrisShape . getRandomShape ()); // give random shape
        }

        @Override
        public void notifyGameOver(TetrisBoard b) {
//            System . out .println( "Game Over" );
            System . exit ( 1 );
        }
    };

    public void setObserver(TetrisObserver ob) {
        this.ob = ob;
    }
    public void getObserver(TetrisObserver ob) {
        setObserver(ob);
    }

    final List<TetrisObserver> observer = new ArrayList<TetrisObserver>();
    public int board[][];
    private int tBoard[][]; // Tracks current item of board
    private int shape[][];
    private TetrisShape item;
    private int xStart, xEnd, yStart, yEnd; // Track Location of item
    private int begin = 1, settle = 1, rotate = 0, downSettle = 0;

    // Make the tetris board
    public void init(int height, int width) {
        board = new int[height+1][width+2];
        tBoard = new int[height+3][width+2];

        for (int i = 0; i < height+1; i++) {
            for (int j = 0; j < width+2; j++) {
                if (j == 0 || i == height || j == width+1)
                    board[i][j] = 2;
                else
                    board[i][j] = 0;
            }
        }
        for (int i = 0; i < height+3; i++) {
            for (int j = 0; j < width+2; j++) {
                if (j == 0 || i == height+2 || j == width+1)
                    tBoard[i][j] = 2;
                else
                    tBoard[i][j] = 0;
            }
        }

    }

    public void addShape(TetrisShape s) {
        int valid = 1, isEmpty = 0, hasItem = 0;
        int count = 0, size = xEnd - xStart;

        // Check if item has reached to lowest it can to add shape or not
        for (int j = xStart; j < xEnd + 1; j++) {
            if (tBoard[yEnd+1][j+1] == 0)
                count++;
        }
        if (count != size + 1) {
            settle = 1;
        }
        if (settle == 1) {
            clearLines();
            if (begin == 0) {
                // Empty slot below item before adding
                for (int i = 0; i  < tBoard.length; i++) {
                    for (int j = 1; j < tBoard[i].length - 1; j++) {
                        if (tBoard[i][j] == 0)
                            isEmpty++;
                    }
                    if (isEmpty == tBoard[i].length-2) {
                        if (hasItem == 1) {
                            valid = 0;
                            break;
                        }
                    }
                    else
                        hasItem = 1;
                    isEmpty = 0;
                }
            }
            else {
                for (int k  = tBoard.length - 2; k < yEnd; k--) {
                    count = 0;
                    for (int j = xStart; j <= xEnd; j++) {
                        if (tBoard[k][j+1] == 0) {
                            count++;
                        }
                    }
                    if (count == size + 1) {
                        valid = 0;
                        break;
                    }
                }
            }

            if (valid == 1) {
                int width = tBoard[0].length - 2;
                xStart = (width - 4) / 2;
                int x = 0, y = 0, boxFlag = 0;
                alterTBoard();
                shape = s.getShapeArray();

                switch (s) {
                    case BAR:
                        xEnd = xStart + 3;
                        yStart = 2;
                        yEnd = 2;
                        break;
                    case BOX:
                        x = 1;
                        boxFlag = 1;
                        xStart = xStart + 1;
                        xEnd = xStart + 1;
                        yStart = 1;
                        yEnd = 2;
                        break;
                    default: // T, S, SR, L, LR
                        xEnd = xStart + 2;
                        yStart = 1;
                        yEnd = 2;
                        break;
                }
                // Moves item to first row of board
                for (int i = 1; i < 3; i++) {
                    for (int j = xStart; j < xEnd + 1; j++) {
                        if (shape[y][x] == 1 && tBoard[i][j+1] == 1)
                            ob.notifyGameOver(this);
                        if (shape[y][x] == 1)
                            tBoard[i][j+1] = 3;
                        x++;
                    }
                    if (boxFlag == 1)
                        x = 1;
                    else
                        x = 0;
                    y++;
                }

                item = s;
                begin = 0;
                settle = 0;
                rotate = 0;
                downSettle = 0;
            }
        }
        tBoard_to_Board();
    }

    public void moveLeft() {
        int valid = 0, size = yEnd - yStart;

        if (settle == 0) {
            // Checks if the left columns empty
            for (int i = yStart; i < yEnd + 1; i++) {
                if (tBoard[i][xStart] == 0)
                    valid++;
            }
            // Implements shift
            if (valid == size + 1) {
                if (begin == 0) {
                    // Shifts item to left
                    for (int i = yStart; i < yEnd + 1; i++) {
                        for (int j = xStart; j < xEnd + 1; j++) {
                            if (j == tBoard[i].length - 2)
                                break;
                            if (j != xEnd+1)
                                tBoard[i][j] = tBoard[i][j+1];
                            if (tBoard[i][j] == 1 && j <= xEnd + 1)
                                tBoard[i][j] = 0;
                            if (i == yStart && j == xEnd - 1 && tBoard[i][j+1] == 0 && tBoard[i][j] == 3)
                                tBoard[i][j-1] = 0;
                        }
                    }
                    // Clears right row
                    for (int i = yStart; i < yEnd + 1; i++) {
                        if (tBoard[i][xEnd+1] != 1)
                            tBoard[i][xEnd+1] = 0;
                    }
                    xStart--;
                    xEnd--;
                }
            }
            downSettle = 0;
        }
        tBoard_to_Board();
    }

    public void moveRight() {
        int valid = 0, size = yEnd - yStart, remove = 0;

        if (settle == 0) {
            // Checks if the right columns empty
            for (int i = yStart; i < yEnd + 1; i++) {
                if (tBoard[i][xEnd+2] == 0)
                    valid++;
            }
            // Implements shift
            if (valid == size + 1) {
                // Shifts item to right
                if (begin == 0) {
                    for (int i = yStart; i < yEnd + 1; i++) {
                        for (int j = xEnd + 1; j > xStart - 1; j--) {
                            if (j == 0)
                                break;
                            if (j != xStart - 1 && tBoard[i][j+2] != 2)
                                tBoard[i][j+2] = tBoard[i][j+1];
                            if (tBoard[i][j+1] == 1 && j >= xEnd - 1)
                                tBoard[i][j+1] = 0;
                            if (i == yStart && j == xStart + 1 && tBoard[i][j+1] == 3 && tBoard[i][j] == 0)
                                tBoard[i][j+1] = 0;
                        }
                    }
                    // Clears left row
                    for (int i = yStart; i < yEnd + 1; i++) {
                        if (tBoard[i][xStart+1] != 1)
                            tBoard[i][xStart+1] = 0;
                    }
                    xStart++;
                    xEnd++;
                }
            }
            downSettle = 0;
        }
        tBoard_to_Board();
    }

    public void moveDown() {
        int valid = 0, size = xEnd - xStart;

        if (settle == 0 && downSettle == 0) {
            // Checks if the bottom row empty
            for (int i = xStart; i < xEnd + 1; i++) {
                if (tBoard[yEnd+1][i+1] == 0 || tBoard[yEnd+1][i+1] == 1 && tBoard[yEnd][i+1] == 0)
                    valid++;

                if (tBoard[yEnd+1][i+1] == 1 && tBoard[yEnd][i+1] == 0)
                    downSettle = 1;
            }
            // Implements shift
            if (valid == size + 1) {
                // Shifts item to down
                if (begin == 0) {
                    // Moves down object
                    for (int i = yEnd + 1; i > yStart; i--) {
                        for (int j = xStart; j < xEnd + 1; j++) {
                            if (tBoard[i][j + 1] != 1)
                                tBoard[i][j + 1] = tBoard[i - 1][j + 1];
                        }
                    }
                    // Releases item from prev line
                    for (int j = xStart; j < xEnd + 1; j++) {
                        tBoard[yStart][j + 1] = 0;
                    }
                    yStart += 1;
                    yEnd += 1;
                }
            }
            else {
                downSettle = 1;
                settle = 1;
                ob.notifyNeedShape(this);
                clearLines();
            }
        }
        tBoard_to_Board();
    }

    public void moveDownToEnd() {
        int valid, size = xEnd - xStart;

        for (int k = yEnd + 1; k < tBoard.length - 1; k++) {
            valid = 0;
            if (settle == 0 && downSettle == 0) {
                // Checks if the bottom row empty
                for (int i = xStart; i < xEnd + 1; i++) {
                    if (tBoard[k][i+1] == 0 || tBoard[k][i+1] == 1 && tBoard[k-1][i+1] == 0)
                        valid++;

                    if (tBoard[k][i+1] == 1 && tBoard[k-1][i+1] == 0)
                        downSettle = 1;
                }
                // Implements shift
                if (valid == size + 1) {
                    // Shifts item to down
                    if (begin == 0) {
                        // Moves down object
                        for (int i = yEnd + 1; i > yStart; i--) {
                            for (int j = xStart; j < xEnd + 1; j++) {
                                if (tBoard[i][j+1] != 1)
                                    tBoard[i][j+1] = tBoard[i-1][j+1];
                            }
                        }
                        // Releases item from prev line
                        for (int j = xStart; j < xEnd + 1; j++) {
                            tBoard[yStart][j + 1] = 0;
                        }
                        yStart += 1;
                        yEnd += 1;
                    }
                }
                else {
                    downSettle = 1;
                    settle = 1;
                    ob.notifyNeedShape(this);
                    break;
                }
            }
        }
        clearLines();
        downSettle = 1;
        settle = 1;
        ob.notifyNeedShape(this);
        tBoard_to_Board();
    }

    public void rotateLeft() {
        if (settle == 0) {

            switch (item) {
                case BAR:
                    if (rotate == 0) {
                        if (yStart > 2 && tBoard[yStart-3][xEnd+1] != 1 && tBoard[yStart-2][xEnd+1] != 1 &&
                                tBoard[yStart-1][xEnd+1] != 1 && tBoard[yStart][xEnd+1] != 1) {
                            clearItem();
                            tBoard[yStart-3][xEnd+1] = 3;
                            tBoard[yStart-2][xEnd+1] = 3;
                            tBoard[yStart-1][xEnd+1] = 3;
                            tBoard[yStart][xEnd+1] = 3;

                            xStart = xEnd;
                            yStart = yStart - 3;
                            rotate++;
                        }
                    }
                    else if (rotate == 1) {
                        if (tBoard[yStart][xStart-2] != 1 && tBoard[yStart][xStart-1] != 1 &&
                                tBoard[yStart][xStart] != 1 && tBoard[yStart][xStart+1] != 1 &&
                                xStart > 2) {
                            clearItem();
                            tBoard[yStart][xStart-2] = 3;
                            tBoard[yStart][xStart-1] = 3;
                            tBoard[yStart][xStart] = 3;
                            tBoard[yStart][xStart+1] = 3;

                            xStart = xStart - 3;
                            yEnd = yStart;

                            rotate++;
                        }
                    }
                    else if (rotate == 2) {
                        if (tBoard[yStart][xStart+1] != 1 && tBoard[yStart+1][xStart+1] != 1 &&
                                tBoard[yStart+2][xStart+1] != 1 && tBoard[yStart+3][xStart+1] != 1 &&
                                yStart < tBoard.length - 4) {
                            clearItem();
                            tBoard[yStart][xStart+1] = 3;
                            tBoard[yStart+1][xStart+1] = 3;
                            tBoard[yStart+2][xStart+1] = 3;
                            tBoard[yStart+3][xStart+1] = 3;

                            xEnd = xStart;
                            yEnd = yStart + 3;
                            rotate++;
                        }
                    }
                    else if (rotate == 3) {
                        if (tBoard[yEnd][xStart+1] != 1 && tBoard[yEnd][xStart+2] != 1 &&
                                tBoard[yEnd][xStart+3] != 1 && tBoard[yEnd][xStart+4] != 1 &&
                                xStart < tBoard[0].length - 5) {
                            clearItem();
                            tBoard[yEnd][xStart+1] = 3;
                            tBoard[yEnd][xStart+2] = 3;
                            tBoard[yEnd][xStart+3] = 3;
                            tBoard[yEnd][xStart+4] = 3;

                            xEnd = xStart + 3;
                            yStart = yEnd;
                            rotate = 0;
                        }
                    }
                    break;
                case T:
                    if (rotate == 0) {
                        if (tBoard[yStart][xEnd+1] != 1 && tBoard[yStart-1][xEnd+2] != 1 &&
                                tBoard[yStart][xEnd+2] != 1 && tBoard[yStart+1][xEnd+2] != 1 &&
                                xStart < tBoard[0].length - 5) {
                            clearItem();
                            tBoard[yStart][xEnd+1] = 3;
                            tBoard[yStart-1][xEnd+2] = 3;
                            tBoard[yStart][xEnd+2] = 3;
                            tBoard[yStart+1][xEnd+2] = 3;

                            xEnd = xEnd + 1;
                            xStart = xEnd - 1;
                            yStart = yStart - 1;
                            rotate++;
                        }
                    }
                    else if (rotate == 1) {
                        if (yStart > 0 && tBoard[yStart][xStart+1] != 1 && tBoard[yStart-1][xStart] != 1 &&
                                tBoard[yStart-1][xStart+1] != 1 && tBoard[yStart-1][xStart+2] != 1 &&
                                xStart >= 1) {
                            clearItem();
                            tBoard[yStart][xStart+1] = 3;
                            tBoard[yStart-1][xStart] = 3;
                            tBoard[yStart-1][xStart+1] = 3;
                            tBoard[yStart-1][xStart+2] = 3;

                            xStart = xStart - 1;
                            yStart = yStart - 1;
                            yEnd = yStart + 1;
                            rotate++;
                        }
                    }
                    else if (rotate == 2) {
                        if (tBoard[yStart+1][xStart+1] != 1 && tBoard[yStart][xStart] != 1 &&
                                tBoard[yStart+1][xStart] != 1 && tBoard[yStart+2][xStart] != 1 &&
                                xStart >= 1 && yStart < tBoard.length - 3) {
                            clearItem();
                            tBoard[yStart+1][xStart+1] = 3;
                            tBoard[yStart][xStart] = 3;
                            tBoard[yStart+1][xStart] = 3;
                            tBoard[yStart+2][xStart] = 3;

                            xStart = xStart - 1;
                            xEnd = xStart + 1;
                            yEnd = yStart + 2;
                            rotate++;
                        }
                    }
                    else if (rotate == 3) {
                        if (tBoard[yEnd][xStart+2] != 1 && tBoard[yEnd+1][xStart+1] != 1 &&
                                tBoard[yEnd+1][xStart+2] != 1 && tBoard[yEnd+1][xStart+3] != 1 &&
                                yEnd < tBoard.length - 2 && xEnd < tBoard[0].length - 3) {
                            clearItem();
                            tBoard[yEnd][xStart+2] = 3;
                            tBoard[yEnd+1][xStart+1] = 3;
                            tBoard[yEnd+1][xStart+2] = 3;
                            tBoard[yEnd+1][xStart+3] = 3;

                            xEnd = xStart + 2;
                            yStart = yEnd;
                            yEnd = yStart + 1;
                            rotate = 0;
                        }
                    }
                    break;
                case S:
                    if (rotate == 0) {
                        if (tBoard[yStart-1][xEnd+1] != 1 && tBoard[yStart][xEnd+1] != 1 &&
                                tBoard[yStart][xEnd+2] != 1 && tBoard[yStart+1][xEnd+2] != 1 &&
                                xEnd < tBoard[0].length - 3) {
                            clearItem();
                            tBoard[yStart-1][xEnd+1] = 3;
                            tBoard[yStart][xEnd+1] = 3;
                            tBoard[yStart][xEnd+2] = 3;
                            tBoard[yStart+1][xEnd+2] = 3;

                            xStart = xEnd;
                            xEnd = xEnd + 1;
                            yStart = yStart - 1;
                            rotate++;
                        }
                    }
                    else if (rotate == 1) {
                        if (yStart > 0 && tBoard[yStart][xStart] != 1 && tBoard[yStart][xStart+1] != 1 &&
                                tBoard[yStart-1][xStart+1] != 1 && tBoard[yStart-1][xStart+2] != 1 &&
                                xStart >= 1) {
                            clearItem();
                            tBoard[yStart][xStart] = 3;
                            tBoard[yStart][xStart+1] = 3;
                            tBoard[yStart-1][xStart+1] = 3;
                            tBoard[yStart-1][xStart+2] = 3;

                            xStart = xStart - 1;
                            yStart = yStart - 1;
                            yEnd = yStart + 1;
                            rotate++;
                        }
                    }
                    else if (rotate == 2) {
                        if (tBoard[yEnd+1][xStart+1] != 1 && tBoard[yStart+1][xStart+1] != 1 &&
                                tBoard[yStart+1][xStart] != 1 && tBoard[yStart][xStart] != 1 &&
                                xStart >= 1 && yEnd < tBoard.length - 2) {
                            clearItem();
                            tBoard[yEnd+1][xStart+1] = 3;
                            tBoard[yStart+1][xStart+1] = 3;
                            tBoard[yStart+1][xStart] = 3;
                            tBoard[yStart][xStart] = 3;

                            xStart = xStart - 1;
                            xEnd = xStart + 1;
                            yEnd = yStart + 2;
                            rotate++;
                        }
                    }
                    else if (rotate == 3) {
                        if (tBoard[yEnd][xEnd+2] != 1 && tBoard[yEnd][xStart+2] != 1 &&
                                tBoard[yEnd+1][xStart+1] != 1 && tBoard[yEnd+1][xStart+2] != 1 &&
                                yEnd < tBoard.length - 2 && xEnd < tBoard[0].length - 3) {
                            clearItem();
                            tBoard[yEnd][xEnd+2] = 3;
                            tBoard[yEnd][xStart+2] = 3;
                            tBoard[yEnd+1][xStart+1] = 3;
                            tBoard[yEnd+1][xStart+2] = 3;

                            xEnd = xEnd + 1;
                            yStart = yEnd;
                            yEnd = yStart + 1;
                            rotate = 0;
                        }
                    }
                    break;
                case SR:
                    if (rotate == 0) {
                        if (tBoard[yStart-1][xEnd+2] != 1 && tBoard[yStart][xEnd+2] != 1 &&
                                tBoard[yStart][xEnd+1] != 1 && tBoard[yStart+1][xEnd+1] != 1 &&
                                xEnd < tBoard[0].length - 3) {
                            clearItem();
                            tBoard[yStart-1][xEnd+2] = 3;
                            tBoard[yStart][xEnd+2] = 3;
                            tBoard[yStart][xEnd+1] = 3;
                            tBoard[yStart+1][xEnd+1] = 3;

                            xStart = xStart + 2;
                            xEnd = xStart + 1;
                            yStart = yStart - 1;
                            rotate++;
                        }
                    }
                    else if (rotate == 1) {
                        if (yStart > 0 && tBoard[yStart-1][xStart] != 1 && tBoard[yStart-1][xStart+1] != 1 &&
                                tBoard[yStart][xStart+1] != 1 && tBoard[yStart][xStart+2] != 1 &&
                                xStart >= 1) {
                            clearItem();
                            tBoard[yStart-1][xStart] = 3;
                            tBoard[yStart-1][xStart+1] = 3;
                            tBoard[yStart][xStart+1] = 3;
                            tBoard[yStart][xStart+2] = 3;

                            xStart = xStart - 1;
                            yStart = yStart - 1;
                            yEnd = yStart + 1;
                            rotate++;
                        }
                    }
                    else if (rotate == 2) {
                        if (tBoard[yEnd+1][xStart] != 1 && tBoard[yStart+1][xStart] != 1 &&
                                tBoard[yStart+1][xStart+1] != 1 && tBoard[yStart][xStart+1] != 1 &&
                                xStart >= 1 && yEnd < tBoard.length - 2) {
                            clearItem();
                            tBoard[yEnd+1][xStart] = 3;
                            tBoard[yStart+1][xStart] = 3;
                            tBoard[yStart+1][xStart+1] = 3;
                            tBoard[yStart][xStart+1] = 3;

                            xStart = xStart - 1;
                            xEnd = xStart + 1;
                            yEnd = yStart + 2;
                            rotate++;
                        }
                    }
                    else if (rotate == 3) {
                        if (tBoard[yEnd][xStart+1] != 1 && tBoard[yEnd][xStart+2] != 1 &&
                                tBoard[yEnd+1][xStart+2] != 1 && tBoard[yEnd+1][xStart+3] != 1 &&
                                yEnd < tBoard.length - 2 && xEnd < tBoard[0].length - 3) {
                            clearItem();
                            tBoard[yEnd][xStart+1] = 3;
                            tBoard[yEnd][xStart+2] = 3;
                            tBoard[yEnd+1][xStart+2] = 3;
                            tBoard[yEnd+1][xStart+3] = 3;

                            xEnd = xEnd + 1;
                            yStart = yEnd;
                            yEnd = yStart + 1;
                            rotate = 0;
                        }
                    }
                    break;
                case L:
                    if (rotate == 0) {
                        if (tBoard[yStart-1][xEnd+1] != 1 && tBoard[yStart-1][xEnd+2] != 1 &&
                                tBoard[yStart][xEnd+2] != 1 && tBoard[yStart+1][xEnd+2] != 1 &&
                                xEnd < tBoard[0].length - 3) {
                            clearItem();
                            tBoard[yStart-1][xEnd+1] = 3;
                            tBoard[yStart-1][xEnd+2] = 3;
                            tBoard[yStart][xEnd+2] = 3;
                            tBoard[yStart+1][xEnd+2] = 3;

                            xStart = xStart + 2;
                            xEnd = xStart + 1;
                            yStart = yStart - 1;
                            rotate++;
                        }
                    }
                    else if (rotate == 1) {
                        if (yStart > 0 && tBoard[yStart][xStart] != 1 && tBoard[yStart-1][xStart] != 1 &&
                                tBoard[yStart-1][xStart+1] != 1 && tBoard[yStart-1][xStart+2] != 1 &&
                                xStart >= 1) {
                            clearItem();
                            tBoard[yStart][xStart] = 3;
                            tBoard[yStart-1][xStart] = 3;
                            tBoard[yStart-1][xStart+1] = 3;
                            tBoard[yStart-1][xStart+2] = 3;

                            xStart = xStart - 1;
                            yStart = yStart - 1;
                            yEnd = yStart + 1;
                            rotate++;
                        }
                    }
                    else if (rotate == 2) {
                        if (tBoard[yEnd+1][xStart+1] != 1 && tBoard[yEnd+1][xStart] != 1 &&
                                tBoard[yStart+1][xStart] != 1 && tBoard[yStart][xStart] != 1 &&
                                xStart >= 1 && yEnd < tBoard.length - 2) {
                            clearItem();
                            tBoard[yEnd+1][xStart+1] = 3;
                            tBoard[yEnd+1][xStart] = 3;
                            tBoard[yStart+1][xStart] = 3;
                            tBoard[yStart][xStart] = 3;

                            xStart = xStart - 1;
                            xEnd = xStart + 1;
                            yEnd = yStart + 2;
                            rotate++;
                        }
                    }
                    else if (rotate == 3) {
                        if (tBoard[yEnd][xStart+3] != 1 && tBoard[yEnd+1][xStart+3] != 1 &&
                                tBoard[yEnd+1][xStart+2] != 1 && tBoard[yEnd+1][xStart+1] != 1 &&
                                yEnd < tBoard.length - 2 && xEnd < tBoard[0].length - 3) {
                            clearItem();
                            tBoard[yEnd][xStart+3] = 3;
                            tBoard[yEnd+1][xStart+3] = 3;
                            tBoard[yEnd+1][xStart+2] = 3;
                            tBoard[yEnd+1][xStart+1] = 3;

                            xEnd = xEnd + 1;
                            yStart = yEnd;
                            yEnd = yStart + 1;
                            rotate = 0;
                        }
                    }
                    break;
                case LR:
                    if (rotate == 0) {
                        if (tBoard[yStart-1][xEnd+2] != 1 && tBoard[yStart-1][xEnd+1] != 1 &&
                                tBoard[yStart][xEnd+1] != 1 && tBoard[yStart+1][xEnd+1] != 1 &&
                                xEnd < tBoard[0].length - 3) {
                            clearItem();
                            tBoard[yStart-1][xEnd+2] = 3;
                            tBoard[yStart-1][xEnd+1] = 3;
                            tBoard[yStart][xEnd+1] = 3;
                            tBoard[yStart+1][xEnd+1] = 3;

                            xStart = xStart + 2;
                            xEnd = xStart + 1;
                            yStart = yStart - 1;
                            rotate++;
                        }
                    }
                    else if (rotate == 1) {
                        if (yStart > 0 && tBoard[yStart-1][xStart] != 1 && tBoard[yStart][xStart] != 1 &&
                                tBoard[yStart][xStart+1] != 1 && tBoard[yStart][xStart+2] != 1 &&
                                xStart >= 1) {
                            clearItem();
                            tBoard[yStart-1][xStart] = 3;
                            tBoard[yStart][xStart] = 3;
                            tBoard[yStart][xStart+1] = 3;
                            tBoard[yStart][xStart+2] = 3;

                            xStart = xStart - 1;
                            yStart = yStart - 1;
                            yEnd = yStart + 1;
                            rotate++;
                        }
                    }
                    else if (rotate == 2) {
                        if (tBoard[yEnd+1][xStart] != 1 && tBoard[yEnd+1][xStart+1] != 1 &&
                                tBoard[yStart+1][xStart+1] != 1 && tBoard[yStart][xStart+1] != 1 &&
                                xStart >= 1 && yEnd < tBoard.length - 2) {
                            clearItem();
                            tBoard[yEnd+1][xStart] = 3;
                            tBoard[yEnd+1][xStart+1] = 3;
                            tBoard[yStart+1][xStart+1] = 3;
                            tBoard[yStart][xStart+1] = 3;

                            xStart = xStart - 1;
                            xEnd = xStart + 1;
                            yEnd = yStart + 2;
                            rotate++;
                        }
                    }
                    else if (rotate == 3) {
                        if (tBoard[yEnd+1][xStart+3] != 1 && tBoard[yEnd][xStart+3] != 1 &&
                                tBoard[yEnd][xStart+2] != 1 && tBoard[yEnd][xStart+1] != 1 &&
                                yEnd < tBoard.length - 2 && xEnd < tBoard[0].length - 3) {
                            clearItem();
                            tBoard[yEnd+1][xStart+3] = 3;
                            tBoard[yEnd][xStart+3] = 3;
                            tBoard[yEnd][xStart+2] = 3;
                            tBoard[yEnd][xStart+1] = 3;

                            xEnd = xEnd + 1;
                            yStart = yEnd;
                            yEnd = yStart + 1;
                            rotate = 0;
                        }
                    }
                    break;
                default:  // BOX
                    break;
            }
        }
        tBoard_to_Board();
    }

    // Gets the displayable board array
    public int[][] getBoardArray() {
        return board;
        //return tBoard;
    }

    // Add or remove observer
    public void addObserver(TetrisObserver o) {
        observer.add(o);
    }
    public void removeObserver(TetrisObserver o) {
        observer.remove(o);
    }

    // Convert Hidden board to viewable board
    public void tBoard_to_Board() {
        for (int i = 2; i < tBoard.length; i++) {
            for (int j = 0; j < tBoard[i].length; j++) {
                if (tBoard[i][j] == 3 || tBoard[i][j] == 1)
                    board[i-2][j] = 1;
                else if (tBoard[i][j] == 0)
                    board[i-2][j] = 0;
            }
        }
    }

    // Alters the current item to previous item for hidden board
    public void alterTBoard() {
        for (int i = 0; i < tBoard.length; i++) {
            for (int j = 0; j < tBoard[i].length; j++) {
                if (tBoard[i][j] == 3)
                    tBoard[i][j] = 1;
            }
        }
    }

    // Check clear lines before shapes gets added
    public void clearLines () {
        int count, lines = 0, lastFilledLine = 0;

        getObserver(ob);
        // Check if row is full then clear
        for (int i = 0; i < tBoard.length - 1; i++) {
            count = 0;
            for (int j = 0; j < tBoard[i].length - 2; j++) {
                if (tBoard[i][j+1] == 1 || tBoard[i][j+1] == 3)
                    count++;

                if (count == tBoard[i].length - 2) {
                    lines++;
                    // Clears line
                    for (int k = 0; k < tBoard[i].length - 2; k++) {
                        tBoard[i][k+1] = 0;
                    }
                }
                else
                    lastFilledLine = i;
            }
        }
        int start = 0;
        // Moves down items to the empty lines
        if (lines != 0) {
            for (int k = lastFilledLine - 1; k >= 0; k--) {
                count = 0;
                for (int j = 0; j < tBoard[k].length - 2; j++) {
                    if (tBoard[k][j+1] == 0)
                        count++;

                }
                if (count != tBoard[k].length - 2) {
                    for (int j = 0; j < tBoard[k].length - 2; j++) {
                        tBoard[lastFilledLine-1][j+1] = tBoard[k][j+1];
                    }
                    lastFilledLine--;
                    start = k;
                }
            }
        }
        // Remove item from the top
        for (int j = 0; j < tBoard[start].length - 2; j++) {
            tBoard[start][j+1] = 0;
        }

        lastFilledLine++;
        start++;
        if (lines != 0) {
            redefine(lastFilledLine);

            // Move items from before the line cleared down if able to
            for (int k = lastFilledLine; k < tBoard.length - 1; k++) {
                for (int i = k; i >= start; i--) {
                    count = 0;
                    for (int j = 0; j < tBoard[i].length - 2; j++) {
                        if (tBoard[i+1][j+1] == 0 && tBoard[i][j+1] == 3 ||
                                tBoard[i+1][j+1] == 1 && tBoard[i][j+1] == 0 ||
                                tBoard[i+1][j+1] == 0 && tBoard[i][j+1] == 0 ||
                                tBoard[i+1][j+1] == 1 && tBoard[i][j+1] == 1)
                            count++;
                    }
                    // Shift items down
                    if (count == tBoard[i].length - 2) {
                        for (int j = 0; j < tBoard[i].length - 2; j++) {
                            if (tBoard[i][j+1] == 3) {
                                tBoard[i+1][j+1] = 3;
                                tBoard[i][j+1] = 0;
                            }
                        }
                    }
                }
            }
            alterTBoard();
            ob.notifyLinesClear(lines);
        }

    }

    // Remove Current shape for the rotation update
    public void clearItem () {
        for (int i = yStart;  i < yEnd + 1; i++) {
            for (int j = xStart; j < xEnd + 1; j++) {
                if (tBoard[i][j+1] == 3)
                    tBoard[i][j+1] = 0;
            }
        }
    }

    // Check if GameOver
    public boolean checkGameOver () {
        boolean valid = false;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < tBoard[i].length - 1; j++) {
                if (tBoard[i][j] == 1) {
                    valid = true;
                    break;
                }
            }
            if (valid == true)
                break;
        }

        if (valid == true)
            return true;
        else
            return false;
    }

    // Redefine items to assist on moving to lower level for clear lines
    public void redefine(int lastFilledLine) {
        for (int i = 0; i <= lastFilledLine; i++) {
            for (int j = 0; j < tBoard[i].length - 2; j++) {
                if (tBoard[i][j+1] == 1)
                    tBoard[i][j+1] = 3;
            }
        }
    }

}
