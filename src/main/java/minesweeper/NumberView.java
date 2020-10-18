package minesweeper;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Sprite;

public class NumberView {
    private Sprite[] digits = new Sprite[3];
    private String[] numberSheet;

    public NumberView(GraphicEntityModule graphics, Group group, int number, int top, int left) {
        if (numberSheet == null) {
            numberSheet = graphics
                    .createSpriteSheetSplitter()
                    .setSourceImage("digits.png")
                    .setName("d")
                    .setImagesPerRow(5)
                    .setImageCount(10)
                    .setWidth(14)
                    .setHeight(24)
                    .setOrigRow(0)
                    .setOrigCol(0)
                    .split();
        }

        for (int i = 0; i < digits.length; i++) {
            digits[i] = graphics.createSprite().setImage(numberSheet[number % 10]).setX(left + 13 * (2 - i)).setY(top);
            group.add(digits[i]);
            number /= 10;
        }
    }

    public void setNumber(int number) {
        for (int i = 0; i < digits.length; i++) {
            digits[i].setImage(numberSheet[number % 10]);
            number /= 10;
        }
    }
}
