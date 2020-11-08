import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.io.*;
import java.util.List;

public class World  extends JPanel implements ActionListener {
    private WorldObject mWorldMap[][];

    private final int N = 50;
    private final int M = 50;

    private ObjectRender mRender;

    private final int mPeriod = 10;
    private int mTimeCounter;
    private Timer mTimer;
    private final int initialTimePeriod = 10;

    private int mEatenPlant;
    private int mBotCount;

    private boolean mMutex = false;

    //private List<WorldObject> mBots;

    public List <WorldObject> reproduceObjects(List <WorldObject> aObjects, int aChildCount,
                                               List <Integer> aMutationSize) {
        List <WorldObject> result = new ArrayList<>();
        for(int i = 0; i < aObjects.size(); ++i)
        {
            WorldObject curtObj = aObjects.get(i);
            for(int j = 0; j < aChildCount; ++j)
            {
                try {
                    try {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream ous = new ObjectOutputStream(baos);
                        ous.writeObject(curtObj);
                        ous.close();
                        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                        ObjectInputStream ois = new ObjectInputStream(bais);
                        WorldObject newObj = (WorldObject)ois.readObject();

                        newObj.mutate(aMutationSize.get(j));
                        result.add(newObj);
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                } catch (ClassNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        return result;
    }

    private void placeObjects(List <WorldObject> aObjects)
    {
        /*
        Set<Point> points = new HashSet<>();
        Point p = new Point();
        while(points.size() < aObjects.size())
        {
            p.setRandomValue(N, M);
            points.add(new Point(p));
        }

        List<Point> pointsArray = new ArrayList<>();
        pointsArray.addAll(points);

        //Set<Point> = new Ha

        for(int i = 0; i < aObjects.size(); ++i)
        {
            Point curPoint = pointsArray.get(i);
            mWorldMap[curPoint.x][curPoint.y] = aObjects.get(i);
        }
        */

        Point p = new Point();
        int i = 0;
        while(i < aObjects.size())
        //for(int i = 0; i < aObjects.size(); ++i)
        {
            p.setRandomValue(N, M);
            if (mWorldMap[p.x][p.y].getmType() == WorldObject.ObjectsType.Void) {
                mWorldMap[p.x][p.y] = aObjects.get(i);
                ++i;
            }
        }
    }

    public void resetWorld(int aX, int aY)
    {
        mWorldMap = new WorldObject[aX][aY];
        for (int i = 0; i < aX; ++i) {
            for (int j = 0; j < aY; ++j) {
                mWorldMap[i][j] = new WorldObject();
            }
        }
    }

    public List<WorldObject> getBots(int aX, int aY)
    {
        List<WorldObject> result = new ArrayList<>();
        for (int i = 0; i < aX; ++i) {
            for (int j = 0; j < aY; ++j) {
                if (mWorldMap[i][j].getmType() == WorldObject.ObjectsType.Bot) {
                    result.add(mWorldMap[i][j]);
                }
            }
        }
        return result;
    }

    public void resetObjects(List<WorldObject> aList) {
        for (int i = 0; i < aList.size(); ++i) {
            aList.get(i).reset();
        }
    }

    public World() {
        setBackground(Color.blue);

        mRender = new ObjectRender();

        mTimeCounter=0;
        mTimer = new Timer(initialTimePeriod, this);
        mTimer.start();

        mEatenPlant = 0;
        mBotCount = 50;

        resetWorld(N, M);

        int count = 0;
        int plantCount = 50 * 10;
        while (count < plantCount) {
            Random rand = new Random();
            int x = rand.nextInt(50);
            int y = rand.nextInt(50);

            if (mWorldMap[x][y].getmType() == WorldObject.ObjectsType.Void) {
                mWorldMap[x][y] = new Plant();
                ++count;
            }
        }

        count = 0;
        while (count < mBotCount) {
            Random rand = new Random();
            int x = rand.nextInt(50);
            int y = rand.nextInt(50);

            if (mWorldMap[x][y].getmType() == WorldObject.ObjectsType.Void) {
                Bot bot = new Bot();
                //mBots.add(bot);
                mWorldMap[x][y] = bot;
                ++count;
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < M; ++j) {
                if (mWorldMap[i][j].getmType() == WorldObject.ObjectsType.Void) {
                    mRender.drawVoid(g2, new Point(i, j));
                }

                if (mWorldMap[i][j].getmType() == WorldObject.ObjectsType.Plant) {
                    mRender.drawPlant(g2, new Point(i, j));
                }

                if (mWorldMap[i][j].getmType() == WorldObject.ObjectsType.Bot) {
                    mRender.drawBot(g2, new Point(i, j));
                }
            }
        }
    }

    private List<Double> getBotEnvironment(int x, int y)
    {
        List<Double> result = new ArrayList<>();
        for(int i = x - 2; i < x + 3; ++i)
        {
            for(int j = y - 2; j < y + 3; ++j)
            {
                if (i >= 0 && j >= 0 && i < N && j < M)
                {
                    double typeValue = 0;
                    WorldObject.ObjectsType type = mWorldMap[i][j].getmType();
                    if (type == WorldObject.ObjectsType.Void) typeValue = 0;
                    else if (type == WorldObject.ObjectsType.Plant) typeValue = 1;
                    else if (type == WorldObject.ObjectsType.Bot) typeValue = 2;
                    result.add(typeValue);
                }
                else
                {
                    result.add(-1.);
                }
            }
        }
        return result;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mTimeCounter += mTimer.getDelay();
        if (mTimeCounter >= mPeriod && !mMutex) {
            mMutex = true;
            mTimeCounter %= mPeriod;

            /*if (mBotCount < 11)
            {
                reproduction();
                repaint();
                mEatenPlant = 0;
                mBotCount = 50;
                mMutex = false;
                return;
            }*/

            //for(int i =0; i < mBots.size(); ++=i)
            //{
            //}
            for (int i = 0; i < N; ++i) {
                for (int j = 0; j < M; ++j) {
                    WorldObject curObj = mWorldMap[i][j];
                    if (curObj.getmType() == WorldObject.ObjectsType.Bot)
                    {
                        int x = i, y = j;
                        int lifeChange = 0;
                        List <Bot.Commands> commands = curObj.move(getBotEnvironment(x, y));
                        for(int k = 0; k < commands.size(); ++k)
                        {
                            if (commands.get(k) == Bot.Commands.Up) --y;
                            else if (commands.get(k) == Bot.Commands.Down) ++y;
                            else if (commands.get(k) == Bot.Commands.Left) --x;
                            else if (commands.get(k) == Bot.Commands.Right) ++x;
                        }

                        if (x < 0) x = 0;
                        else if (x > N - 1) x = N - 1;
                        if (y < 0) y = 0;
                        else if (y > M - 1) y = M - 1;

                        WorldObject.ObjectsType cellType = mWorldMap[x][y].getmType();
                        if (cellType == WorldObject.ObjectsType.Bot)
                        {
                            x = i;
                            y = j;
                        }
                        if (cellType == WorldObject.ObjectsType.Plant)
                        {
                            lifeChange = 30;
                            ++mEatenPlant;
                        }

                        if (i != x || j != y)
                        {
                            mWorldMap[x][y] = mWorldMap[i][j];
                            mWorldMap[i][j] = new WorldObject();
                        }

                        WorldObject.ObjectsState updateState = curObj.update(lifeChange);
                        if (updateState == WorldObject.ObjectsState.Death)
                        {
                            mWorldMap[x][y] = new WorldObject();
                            --mBotCount;
                            if (mBotCount < 11)
                            {
                                repaint();
                                reproduction();

                                mEatenPlant = 0;
                                mBotCount = 50;
                                mMutex = false;
                                return;
                            }
                        }
                    }
                }
            }

            int potencial = 0;
            while (potencial < 5 && mEatenPlant > 0)
            {
                Random rand = new Random();
                int x = rand.nextInt(50);
                int y = rand.nextInt(50);

                if (mWorldMap[x][y].getmType() == WorldObject.ObjectsType.Void) {
                    mWorldMap[x][y] = new Plant();
                    ++potencial;
                    --mEatenPlant;
                    //mWorldMap[x][y] = new Plant();
                }
            }
            repaint();
            mMutex = false;
        }
    }

    private void reproduction()
    {
        List<WorldObject> bots = getBots(N, M);
        resetObjects(bots);
        while(bots.size() < 10)
        {
            bots.add(new Bot()); //TODO
        }
        List<Integer> mutationSize = new ArrayList<>();
        mutationSize.add(0);
        mutationSize.add(1);
        mutationSize.add(2);
        mutationSize.add(2);
        mutationSize.add(5);
        bots = reproduceObjects(bots, 5, mutationSize);
        if(bots.size() < 50)
        {
            bots.add(new Bot()); //TODO
        }
        List<WorldObject> newWorldObjects = new ArrayList<>();
        for(int i = 0; i < 50*10; ++i) newWorldObjects.add(new Plant());
        newWorldObjects.addAll(bots);
        resetWorld(N,M);
        placeObjects(newWorldObjects);

        bots = getBots(N, M);
        if(bots.size() < 50)
        {
            bots.add(new Bot()); //TODO
        }
    }

    class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

        }
    }
}