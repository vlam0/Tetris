package eecs40.test ;
import eecs40.tetris.TetrisBoard ;
import eecs40.tetris.TetrisShape ;

public class Test {
    public static void main(String[] args) {
        TetrisBoard tb = new TetrisBoard();
        tb .init( 10 , 10 );
        System . out .println( tb .toString());
// will print out
/*
2000002
2000002
2000002
2000002
2000002
2000002
2000002
2000002
2000002
2000002
2222222
*/
        tb .addShape( TetrisShape . S );
        System . out .println( tb .toString());
//        tb. rotateLeft();
//        System . out .println( tb .toString());
//        tb. rotateLeft();
//        System . out .println( tb .toString());
//        tb. moveDown();
//        System . out .println( tb .toString());
//        tb. rotateLeft();
//        System . out .println( tb .toString());
//        tb. rotateLeft();
//        System . out .println( tb .toString());
//        tb. rotateLeft();
//        System . out .println( tb .toString());
//        tb. rotateLeft();
//        System . out .println( tb .toString());
        tb. moveDown();
        System . out .println( tb .toString());
        tb. moveDown();
        System . out .println( tb .toString());
        tb. moveDown();
        System . out .println( tb .toString());
//        tb. rotateLeft();
//        System . out .println( tb .toString());
        tb. moveRight();
        System . out .println( tb .toString());
        tb. moveDown();
        System . out .println( tb .toString());
        tb. moveDown();
        System . out .println( tb .toString());
        tb. moveDown();
        System . out .println( tb .toString());
        tb. moveDown();
        System . out .println( tb .toString());
        tb. moveDown();
        System . out .println( tb .toString());
        tb. moveDown();
        System . out .println( tb .toString());
//        tb. moveRight();
//        System . out .println( tb .toString());
        tb. moveRight();
        System . out .println( tb .toString());
        tb. moveRight();
        System . out .println( tb .toString());
        tb. moveRight();
        System . out .println( tb .toString());
        tb. moveLeft();
        System . out .println( tb .toString());
        tb. moveLeft();
        System . out .println( tb .toString());
        tb. moveLeft();
        System . out .println( tb .toString());
//        tb. moveLeft();
//        System . out .println( tb .toString());
//        tb. moveLeft();
//        System . out .println( tb .toString());
//        tb. moveLeft();
//        System . out .println( tb .toString());tb. moveLeft();
//        System . out .println( tb .toString());
//        tb. moveLeft();
//        System . out .println( tb .toString());
//        tb. moveLeft();
//        System . out .println( tb .toString());
//        tb. moveRight();
//        System . out .println( tb .toString());

//        tb .moveDownToEnd();
//        System . out .println( tb .toString());


    }

}





