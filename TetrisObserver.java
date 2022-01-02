package eecs40.tetris ;

public interface TetrisObserver {
    public void notifyLinesClear ( int num);
    public void notifyNeedShape ( TetrisBoard b);
    void notifyGameOver(TetrisBoard b);
}