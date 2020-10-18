package minesweeper;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Sprite;

import java.util.ArrayList;

public class Cell {
    private ArrayList<Cell> neighbors = new ArrayList<>();
    private int x;
    private int y;
    private boolean mine;
    private boolean revealed;
    private Sprite sprite;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void addNeighbors(Cell[][] grid) {
        int[] dx = new int[]{0, 1, 1, 1, 0, -1, -1, -1};
        int[] dy = new int[]{-1, -1, 0, 1, 1, 1, 0, -1};
        for (int dir = 0; dir < dx.length; dir++) {
            int x = this.x + dx[dir];
            int y = this.y + dy[dir];
            if (x >= 0 && x < Board.WIDTH && y >= 0 && y < Board.HEIGHT) neighbors.add(grid[x][y]);
        }
    }

    public void hideMine() {
        mine = true;
    }

    public boolean needsReveal() {
        return !mine && !revealed;
    }

    private static String[] mineSheet;

    public Sprite createSprite(GraphicEntityModule graphics) {
        if (mineSheet == null) {
            mineSheet = graphics
                    .createSpriteSheetSplitter()
                    .setSourceImage("tiles.png")
                    .setName("t")
                    .setImagesPerRow(8)
                    .setImageCount(16)
                    .setWidth(17)
                    .setHeight(17)
                    .setOrigRow(0)
                    .setOrigCol(0)
                    .split();
        }

        sprite = graphics.createSprite().setX(16 * x).setY(16 * y).setImage(mineSheet[0]);
        return sprite;
    }

    public boolean click() {
        if (mine) {
            revealed = true;
            sprite.setImage(mineSheet[6]);
            return false;
        }
        if (revealed) return true;
        revealed = true;
        if (value() == 0) {
            for (Cell neighbor : neighbors) neighbor.click();
            sprite.setImage(mineSheet[1]);
        }
        else sprite.setImage(mineSheet[7 + value()]);
        return true;
    }

    public int value() {
        return (int) neighbors.stream().filter(c -> c.mine).count();
    }

    public char getPlayerInput() {
        if (!revealed) return '?';
        if (value() == 0) return '.';
        return (char) (value() + '0');
    }

    public void markMine() {
        if (sprite.getImage().equals(mineSheet[0])) sprite.setImage(mineSheet[2]);
    }

    public boolean isMine() {
        return mine;
    }

    public void removeMine() {
        mine = false;
    }

    public void showSolution() {
        if (revealed) return;
        if (!mine && sprite.getImage().equals(mineSheet[2])) sprite.setImage(mineSheet[7]);
        else if (mine && sprite.getImage().equals(mineSheet[0])) sprite.setImage(mineSheet[5]);
    }

    public boolean isHiddenMine() {
        return mine && !sprite.getImage().equals(mineSheet[2]);
    }
}
