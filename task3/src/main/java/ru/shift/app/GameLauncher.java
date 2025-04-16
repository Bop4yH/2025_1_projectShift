package ru.shift.app;

import ru.shift.controller.MinerController;
import ru.shift.model.MinerGameModel;
import ru.shift.view.HighScoresWindow;
import ru.shift.view.MainWindow;
import ru.shift.view.SettingsWindow;

public class GameLauncher {
    private final MinerGameModel model = new MinerGameModel();
    private final MainWindow mainWindow = new MainWindow();
    private final HighScoresWindow highScoresWindow = new HighScoresWindow(mainWindow);
    private final MinerController controller = new MinerController(model, mainWindow, highScoresWindow);
    private final SettingsWindow settingsWindow = new SettingsWindow(mainWindow);

    public void initialize() {
        setupSettingsWindow();
        setupMainWindowActions();
        startGame();
    }

    private void setupSettingsWindow() {
        settingsWindow.setGameTypeListener(controller::setDifficulty);
    }

    private void setupMainWindowActions() {
        mainWindow.setNewGameMenuAction(e -> controller.resetGame());
        mainWindow.setSettingsMenuAction(e -> settingsWindow.setVisible(true));
        mainWindow.setHighScoresMenuAction(e -> highScoresWindow.setVisible(true));
        mainWindow.setExitMenuAction(e -> mainWindow.dispose());
        mainWindow.setCellListener((x, y, buttonType) -> {
            switch (buttonType) {
                case LEFT_BUTTON -> controller.onLeftClick(x, y);
                case RIGHT_BUTTON -> controller.onRightClick(x, y);
                case MIDDLE_BUTTON -> controller.onMiddleClick(x, y);
            }
        });
    }

    private void startGame() {
        controller.resetGame();
        mainWindow.setVisible(true);
    }
}