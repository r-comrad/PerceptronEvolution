import javax.swing.*;
import java.awt.*;

public class ObjectRender {
    private Image mVoidImage;
    private Image mPlantImage;
    private Image mBotImage;

    protected final int DOT_SIZE = 16;
    protected final int mGlobalCoordinatesOffset = 0;

    ObjectRender() {
        ImageIcon block = new ImageIcon("res/block.png");
        mVoidImage = block.getImage();

        block = new ImageIcon("res/plant.png");
        mPlantImage = block.getImage();

        block = new ImageIcon("res/bot.png");
        mBotImage = block.getImage();
    }

    public void drawVoid(Graphics2D g, Point aCoordinates) {
        draw(g, mVoidImage, aCoordinates);
    }

    public void drawPlant(Graphics2D g, Point aCoordinates) {
        draw(g, mPlantImage, aCoordinates);
    }

    public void drawBot(Graphics2D g, Point aCoordinates) {
        draw(g, mBotImage, aCoordinates);
    }

    private void draw(Graphics2D g, Image mImage, Point aCoordinates) {
        g.drawImage(mImage, aCoordinates.x * DOT_SIZE + mGlobalCoordinatesOffset,
                aCoordinates.y * DOT_SIZE + mGlobalCoordinatesOffset, null);

    }
}
