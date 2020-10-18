package com.codingame.game;
import java.util.ArrayList;
import java.util.List;

import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.SoloGameManager;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.google.inject.Inject;
import minesweeper.Board;

public class Referee extends AbstractReferee {
    @Inject
    private SoloGameManager<Player> gameManager;
    @Inject
    private GraphicEntityModule graphicEntityModule;
    @Inject
    TooltipModule tooltips;

    private Board board;

    @Override
    public void init() {
        gameManager.setMaxTurns(600);
        board = new Board(graphicEntityModule, tooltips);
    }

    @Override
    public void gameTurn(int turn) {
        Player player = gameManager.getPlayer();
        ArrayList<String> input = board.getPlayerInput();
        for (String line : input) player.sendInputLine(line);
        player.execute();

        try {
            List<String> outputs = player.getOutputs();
            if (!board.play(outputs.get(0), turn)) gameManager.loseGame("You found a mine!");
        } catch (TimeoutException e) {
            gameManager.loseGame("timeout!");
        } catch (Exception e) {
            gameManager.loseGame(e.getMessage());
        }

        if (board.isWin()) gameManager.winGame("You won");
    }
}
