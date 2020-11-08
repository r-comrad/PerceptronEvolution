import java.util.Random;

public class Point {
    //enum CreationType{Assign, Randomize}

    public int x;
    public int y;

    static private Random mRand = new Random();

    public Point() {
        x = 0;
        y = 0;
    }

    /*public Point(CreationType aType, int aX, int aY) {
        if (aType == CreationType.Assign)
        {
            x = aX;
            y = aY;
        }
        else if (aType == CreationType.Randomize)
        {
            x = mRand.nextInt(aX);
            y = mRand.nextInt(aY);
        }
    }*/

    public Point(int aX, int aY) {
            x = aX;
            y = aY;

    }

    public void setRandomValue(int aX, int aY)
    {
        x = mRand.nextInt(aX);
        y = mRand.nextInt(aY);
    }

    public Point(Point other) {
        x = other.x;
        y = other.y;
    }
}