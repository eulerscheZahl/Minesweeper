package minesweeper;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import java.util.ArrayList;
import java.util.Random;

public class Board {
    public static final int WIDTH = 30;
    public static final int HEIGHT = 16;
    public static final int MINE_COUNT = 99;

    private Cell[][] grid = new Cell[WIDTH][HEIGHT];
    private NumberView mineView;
    private NumberView timer;
    private Sprite smileyFace;
    private String[] faces;

    public Board(GraphicEntityModule graphics, TooltipModule tooltips) {
        faces = graphics
                .createSpriteSheetSplitter()
                .setSourceImage("faces.png")
                .setName("f")
                .setImagesPerRow(5)
                .setImageCount(5)
                .setWidth(27)
                .setHeight(27)
                .setOrigRow(0)
                .setOrigCol(0)
                .split();
        smileyFace = graphics.createSprite().setImage(faces[0]).setX(228).setY(-39);

        graphics.createSprite().setImage("background.png").setScale(2.5);
        ArrayList<Cell> allCells = new ArrayList<>();
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                grid[x][y] = new Cell(x, y);
                allCells.add(grid[x][y]);
            }
        }
        for (Cell cell : allCells) cell.addNeighbors(grid);

        Group board = graphics.createGroup(smileyFace).setX(667).setY(307).setScale(2.5);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                Sprite sprite = grid[x][y].createSprite(graphics);
                tooltips.setTooltipText(sprite, "x: " + x + "\ny: " + y);
                board.add(sprite);
            }
        }

        mineView = new NumberView(graphics, board, MINE_COUNT, -38, 6);
        timer = new NumberView(graphics, board, 0, -38, 435);
    }

    private Cell getCell(int x, int y) throws Exception {
        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) throw new Exception("Invalid coordinate");
        return grid[x][y];
    }

    public boolean play(String action, int turn) throws Exception {
        timer.setNumber(turn);
        String[] parts = action.trim().split(" ");
        ArrayList<Integer> nums = new ArrayList<>();
        for (String p : parts) {
            try {
                int n = Integer.parseInt(p);
                nums.add(n);
            } catch (Exception ex) {
            }
        }
        if (nums.size() < 2) throw new Exception("Too few coordinates");
        Cell guess = getCell(nums.get(0), nums.get(1));
        if (turn == 1) hideMines(guess);
        if (!guess.click()) {
            showSolution();
            smileyFace.setImage(faces[4]);
            return false;
        }
        for (int i = 2; i + 1 < nums.size(); i += 2) {
            getCell(nums.get(i), nums.get(i + 1)).markMine();
        }

        int remainingMines = 0;
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (grid[x][y].isHiddenMine()) remainingMines++;
            }
        }
        mineView.setNumber(remainingMines);
        if (isWin()) smileyFace.setImage(faces[3]);
        return true;
    }

    private void showSolution() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                grid[x][y].showSolution();
            }
        }
    }

    private void hideMines(Cell guess) {
        do {
            ArrayList<Cell> allCells = new ArrayList<>();
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    allCells.add(grid[x][y]);
                }
            }

            for (Cell cell : allCells) cell.removeMine();
            int minesToHide = MINE_COUNT;
            Random random = new Random();
            while (minesToHide > 0) {
                minesToHide--;
                Cell cell = allCells.get(random.nextInt(allCells.size()));
                allCells.remove(cell);
                cell.hideMine();
            }
        } while (guess.isMine() || guess.value() > 0);
    }

    public ArrayList<String> getPlayerInput() {
        ArrayList<String> result = new ArrayList<>();
        for (int y = 0; y < HEIGHT; y++) {
            String line = "";
            for (int x = 0; x < WIDTH; x++) line += grid[x][y].getPlayerInput() + " ";
            result.add(line.trim());
        }
        return result;
    }

    public boolean isWin() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (grid[x][y].needsReveal()) return false;
            }
        }
        return true;
    }
}
