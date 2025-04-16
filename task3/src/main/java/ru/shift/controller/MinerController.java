package ru.shift.controller;

import ru.shift.dto.GameType;
import ru.shift.model.MinerGameModel;
import ru.shift.timer.GameTimer;
import ru.shift.data.RecordManager;
import ru.shift.view.GameStateListener;
import ru.shift.view.HighScoresWindow;
import ru.shift.view.MainWindow;
import ru.shift.view.RecordsWindow;

public class MinerController implements Controller, GameStateListener {
    private final MinerGameModel model;
    private final MainWindow view;
    private final GameTimer timer = new GameTimer();
    private final RecordManager recordManager = new RecordManager();
    private final HighScoresWindow highScoresWindow;
    private boolean firstClick = true;

    public MinerController(MinerGameModel model, MainWindow view, HighScoresWindow highScoresWindow) {
        this.model = model;
        this.view = view;
        this.highScoresWindow = highScoresWindow;
        timer.addObserver(view);
        this.view.setGameStateListener(this);
        this.model.addObserver(view);
        this.highScoresWindow.updateHighScores(recordManager);
    }

    @Override
    public void onLeftClick(int x, int y) {
        if (firstClick) {
            model.getMineField().placeBombsExcluding(x, y);
            timer.start();
            firstClick = false;
        }
        model.openCell(x, y);
    }

    @Override
    public void onRightClick(int x, int y) {
        model.flagAndNotify(x, y);
    }

    @Override
    public void onMiddleClick(int x, int y) {
        model.attemptOpenAround(x, y);
    }

    @Override
    public void resetGame() {
        model.resetGame();
        firstClick = true;
        timer.reset();
    }

    @Override
    public void setDifficulty(GameType difficulty) {
        model.setDifficulty(difficulty);
        resetGame();
    }

    @Override
    public void onGameWon() {
        timer.stop();
        handleGameWin();
    }

    @Override
    public void onGameLost() {
        timer.stop();
    }

    private void handleGameWin() {
        int time = timer.getElapsedSeconds();

        if (recordManager.isNewRecord(model.getDifficulty(), time)) {
            RecordsWindow recordsWindow = new RecordsWindow(view);
            recordsWindow.setNameListener(name -> {
                recordManager.tryAddRecord(model.getDifficulty(), name, time);
                highScoresWindow.updateHighScores(recordManager);
            });
            recordsWindow.setVisible(true);
        }
    }
}
