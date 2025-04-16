package ru.shift.view;

import ru.shift.dto.CellViewData;
import ru.shift.dto.GameType;
import ru.shift.model.CellState;
import ru.shift.model.ModelObserver;
import ru.shift.timer.TimerObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class MainWindow extends JFrame implements ModelObserver, TimerObserver {
    private final Container contentPane;
    private final GridBagLayout mainLayout;
    private JMenuItem newGameMenu;
    private JMenuItem highScoresMenu;
    private JMenuItem settingsMenu;
    private JMenuItem exitMenu;
    private transient CellEventListener listener;
    private JButton[][] cellButtons;
    private JLabel timerLabel;
    private JLabel bombsCounterLabel;
    private transient ActionListener newGameActionListener;
    private transient GameStateListener gameStateListener;

    public MainWindow() {
        super("Miner");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        createMenu();

        contentPane = getContentPane();
        mainLayout = new GridBagLayout();
        contentPane.setLayout(mainLayout);

        contentPane.setBackground(new Color(178, 196, 230));
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");

        gameMenu.add(newGameMenu = new JMenuItem("New Game"));
        gameMenu.addSeparator();
        gameMenu.add(highScoresMenu = new JMenuItem("High Scores"));
        gameMenu.add(settingsMenu = new JMenuItem("Settings"));
        gameMenu.addSeparator();
        gameMenu.add(exitMenu = new JMenuItem("Exit"));

        menuBar.add(gameMenu);
        setJMenuBar(menuBar);
    }

    public void setNewGameMenuAction(ActionListener listener) {
        newGameMenu.addActionListener(listener);
        this.newGameActionListener = listener;

    }

    public void setHighScoresMenuAction(ActionListener listener) {
        highScoresMenu.addActionListener(listener);
    }

    public void setSettingsMenuAction(ActionListener listener) {
        settingsMenu.addActionListener(listener);
    }

    public void setExitMenuAction(ActionListener listener) {
        exitMenu.addActionListener(listener);
    }

    public void setCellListener(CellEventListener listener) {
        this.listener = listener;
    }

    public void setCellImage(int x, int y, GameImage gameImage) {
        cellButtons[y][x].setIcon(gameImage.getImageIcon());
    }

    public void setBombsCount(int bombsCount) {
        bombsCounterLabel.setText(String.valueOf(bombsCount));
    }

    public void setTimerValue(int value) {
        timerLabel.setText(String.valueOf(value));
    }

    public void createGameField(int rowsCount, int colsCount) {
        contentPane.removeAll();
        setPreferredSize(new Dimension(20 * colsCount + 70, 20 * rowsCount + 110));

        addButtonsPanel(createButtonsPanel(rowsCount, colsCount));
        addTimerImage();
        addTimerLabel(timerLabel = new JLabel("0"));
        addBombCounter(bombsCounterLabel = new JLabel("0"));
        addBombCounterImage();
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createButtonsPanel(int numberOfRows, int numberOfCols) {
        cellButtons = new JButton[numberOfRows][numberOfCols];
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setPreferredSize(new Dimension(20 * numberOfCols, 20 * numberOfRows));
        buttonsPanel.setLayout(new GridLayout(numberOfRows, numberOfCols, 0, 0));

        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfCols; col++) {
                final int x = col;
                final int y = row;

                cellButtons[y][x] = new JButton(GameImage.CLOSED.getImageIcon());
                cellButtons[y][x].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (listener == null) {
                            return;
                        }

                        switch (e.getButton()) {
                            case MouseEvent.BUTTON1:
                                listener.onMouseClick(x, y, ButtonType.LEFT_BUTTON);
                                break;
                            case MouseEvent.BUTTON2:
                                listener.onMouseClick(x, y, ButtonType.MIDDLE_BUTTON);
                                break;
                            case MouseEvent.BUTTON3:
                                listener.onMouseClick(x, y, ButtonType.RIGHT_BUTTON);
                                break;
                            default:
                                // Other mouse buttons are ignored
                        }
                    }
                });
                buttonsPanel.add(cellButtons[y][x]);
            }
        }

        return buttonsPanel;
    }

    private void addButtonsPanel(JPanel buttonsPanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.gridheight = 1;
        gbc.insets = new Insets(20, 20, 5, 20);
        mainLayout.setConstraints(buttonsPanel, gbc);
        contentPane.add(buttonsPanel);
    }

    private void addTimerImage() {
        JLabel label = new JLabel(GameImage.TIMER.getImageIcon());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 20, 0, 0);
        gbc.weightx = 0.1;
        mainLayout.setConstraints(label, gbc);
        contentPane.add(label);
    }

    private void addTimerLabel(JLabel timerLabel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 5, 0, 0);
        mainLayout.setConstraints(timerLabel, gbc);
        contentPane.add(timerLabel);
    }

    private void addBombCounter(JLabel bombsCounterLabel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.weightx = 0.7;
        mainLayout.setConstraints(bombsCounterLabel, gbc);
        contentPane.add(bombsCounterLabel);
    }

    private void addBombCounterImage() {
        JLabel label = new JLabel(GameImage.BOMB_ICON.getImageIcon());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 3;
        gbc.insets = new Insets(0, 5, 0, 20);
        gbc.weightx = 0.1;
        mainLayout.setConstraints(label, gbc);
        contentPane.add(label);
    }

    @Override
    public void onCellChanged(int x, int y, CellViewData data) {
        updateCell(x, y, data);
    }

    private void updateCell(int x, int y, CellViewData data) {
        GameImage imageToSet;

        CellState cellState = data.state();
        imageToSet = switch (cellState) {
            case CLOSED -> GameImage.CLOSED;
            case FLAGGED -> GameImage.MARKED;
            case INCORRECT_FLAG -> GameImage.INCORRECT_FLAG;
            case OPENED -> {
                int adjacentMines = data.neighboringMines();
                yield switch (adjacentMines) {
                    case 1 -> GameImage.NUM_1;
                    case 2 -> GameImage.NUM_2;
                    case 3 -> GameImage.NUM_3;
                    case 4 -> GameImage.NUM_4;
                    case 5 -> GameImage.NUM_5;
                    case 6 -> GameImage.NUM_6;
                    case 7 -> GameImage.NUM_7;
                    case 8 -> GameImage.NUM_8;
                    default -> GameImage.EMPTY;
                };
            }
            case EXPLODED -> GameImage.EXPLODED;
            case MINE -> GameImage.BOMB;
        };

        setCellImage(x, y, imageToSet);

    }

    @Override
    public void onGameWon() {

        WinWindow winWindow = new WinWindow(this);
        winWindow.setNewGameListener(newGameActionListener);
        winWindow.setExitListener(e -> dispose());

        if (gameStateListener != null) {
            gameStateListener.onGameWon();
        }
        SwingUtilities.invokeLater(() -> winWindow.setVisible(true));
    }

    @Override
    public void onGameLost() {
        LoseWindow loseWindow = new LoseWindow(this);
        loseWindow.setNewGameListener(newGameActionListener);
        loseWindow.setExitListener(e -> dispose());
        SwingUtilities.invokeLater(() -> loseWindow.setVisible(true));
        if (gameStateListener != null) {
            gameStateListener.onGameLost();
        }
    }

    @Override
    public void onRemainingMinesChanged(int remaining) {
        setBombsCount(remaining);
    }

    @Override
    public void onGameStarted(GameType gameType) {
        createGameField(gameType.getRows(), gameType.getColumns());
    }

    public void setGameStateListener(GameStateListener listener) {
        this.gameStateListener = listener;
    }

    @Override
    public void onTimerTick(int secondsElapsed) {
        setTimerValue(secondsElapsed);
    }


}
