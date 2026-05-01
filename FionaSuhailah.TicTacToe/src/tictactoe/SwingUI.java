package tictactoe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SwingUI extends JFrame {
    private final GameController controller;
    private final JButton[][] cells = new JButton[3][3];

    private CardLayout cardLayout;
    private JPanel rootPanel;

    private GradientPanel coverPanel;
    private GradientPanel gamePanel;

    private JLabel gameTitleLabel;
    private JLabel statusLabel;
    private JLabel scoreLabel;

    private JButton startBtn;
    private JButton changeThemeBtn;
    private JButton newGameBtn;
    private JButton backBtn;
    private JLabel hintLabel;

    private JPanel boardPanel;

    private int xWins = 0;
    private int oWins = 0;
    private int draws = 0;

    private boolean gameStarted = false;

    private static final Color SOFT_BORDER = new Color(112, 122, 142);

    private Theme currentTheme = Theme.BLUE;

    private enum Theme {
        BLUE("Blue",
                new Color(24, 26, 40), new Color(52, 58, 94),
                new Color(40, 44, 66), new Color(57, 70, 90), new Color(120, 130, 180),
                new Color(240, 242, 255), new Color(190, 198, 230),
                new Color(76, 201, 240), new Color(247, 37, 133), new Color(86, 110, 148)),

        SLATE("Slate",
                new Color(20, 26, 32), new Color(45, 56, 66),
                new Color(36, 44, 52), new Color(53, 66, 78), new Color(110, 126, 140),
                new Color(236, 242, 248), new Color(176, 190, 204),
                new Color(116, 226, 226), new Color(255, 138, 148), new Color(78, 102, 124)),

        LAVENDER("Lavender",
                new Color(30, 24, 42), new Color(66, 54, 92),
                new Color(50, 42, 70), new Color(70, 61, 96), new Color(138, 126, 176),
                new Color(242, 238, 255), new Color(204, 196, 230),
                new Color(117, 215, 255), new Color(255, 132, 188), new Color(98, 86, 140)),

        FOREST("Forest",
                new Color(20, 33, 30), new Color(42, 67, 61),
                new Color(31, 49, 45), new Color(47, 72, 66), new Color(103, 137, 127),
                new Color(233, 245, 240), new Color(176, 206, 196),
                new Color(110, 231, 183), new Color(244, 114, 182), new Color(74, 115, 102)),

        SUNSET("Sunset",
                new Color(38, 24, 28), new Color(92, 56, 72),
                new Color(58, 38, 46), new Color(82, 55, 66), new Color(166, 124, 140),
                new Color(248, 236, 240), new Color(222, 190, 201),
                new Color(125, 211, 252), new Color(251, 113, 133), new Color(132, 84, 102)),

        OCEAN("Ocean",
                new Color(17, 30, 46), new Color(42, 78, 108),
                new Color(29, 48, 68), new Color(44, 66, 92), new Color(112, 149, 184),
                new Color(236, 245, 255), new Color(182, 205, 229),
                new Color(103, 232, 249), new Color(244, 114, 182), new Color(73, 110, 146));

        final String label;
        final Color bg1, bg2, panel, cell, border, textMain, textSub, xColor, oColor, button;

        Theme(String label, Color bg1, Color bg2, Color panel, Color cell, Color border,
              Color textMain, Color textSub, Color xColor, Color oColor, Color button) {
            this.label = label;
            this.bg1 = bg1;
            this.bg2 = bg2;
            this.panel = panel;
            this.cell = cell;
            this.border = border;
            this.textMain = textMain;
            this.textSub = textSub;
            this.xColor = xColor;
            this.oColor = oColor;
            this.button = button;
        }
    }

    public SwingUI() {
        controller = new GameController("board.csv");

        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(560, 720);
        setLocationRelativeTo(null);
        setResizable(false);

        initUI();
        setVisible(true);
    }

    private void initUI() {
        cardLayout = new CardLayout();
        rootPanel = new JPanel(cardLayout);

        coverPanel = buildCoverPanel();
        gamePanel = buildGamePanel();

        rootPanel.add(coverPanel, "COVER");
        rootPanel.add(gamePanel, "GAME");

        setContentPane(rootPanel);
        cardLayout.show(rootPanel, "COVER");
    }

    private GradientPanel buildCoverPanel() {
        GradientPanel cover = new GradientPanel(currentTheme.bg1, currentTheme.bg2);
        cover.setLayout(new BorderLayout());

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(40, 40, 80, 40));
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("TIC TAC TOE");
        title.setFont(new Font("Avenir Next", Font.BOLD, 68));
        title.setForeground(currentTheme.textMain);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        startBtn = makeSoftButton("START");
        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        startBtn.setFont(new Font("Avenir Next", Font.BOLD, 24));
        startBtn.setMaximumSize(new Dimension(220, 56));
        startBtn.addActionListener(e -> {
            if (!gameStarted) {
                startNewRound();
                gameStarted = true;
            } else {
                applyThemeToGame();
                refreshBoard();
                updateTurnLabel();
                updateScoreLabel();
            }
            cardLayout.show(rootPanel, "GAME");
        });

        hintLabel = new JLabel("Press Start to Play");
        hintLabel.setFont(new Font("Avenir Next", Font.PLAIN, 18));
        hintLabel.setForeground(currentTheme.textSub);
        hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        changeThemeBtn = makeSoftButton("Change Theme");
        changeThemeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        changeThemeBtn.addActionListener(e -> showThemeChooser());

        center.add(Box.createVerticalStrut(145));
        center.add(title);
        center.add(Box.createVerticalStrut(70));
        center.add(startBtn);
        center.add(Box.createVerticalStrut(18));
        center.add(hintLabel);
        center.add(Box.createVerticalStrut(14));
        center.add(changeThemeBtn);
        center.add(Box.createVerticalGlue());

        cover.add(center, BorderLayout.CENTER);
        return cover;
    }

    private GradientPanel buildGamePanel() {
        GradientPanel game = new GradientPanel(currentTheme.bg1, currentTheme.bg2);
        game.setLayout(new BorderLayout(16, 16));
        game.setBorder(new EmptyBorder(18, 18, 18, 18));

        gameTitleLabel = new JLabel("Tic Tac Toe", SwingConstants.CENTER);
        gameTitleLabel.setFont(new Font("Avenir Next", Font.BOLD, 34));
        gameTitleLabel.setForeground(currentTheme.textMain);

        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Avenir Next", Font.PLAIN, 20));
        statusLabel.setForeground(currentTheme.textSub);

        scoreLabel = new JLabel("", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Avenir Next", Font.BOLD, 20));
        scoreLabel.setForeground(currentTheme.textMain);

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(gameTitleLabel);
        top.add(Box.createVerticalStrut(6));
        top.add(statusLabel);
        top.add(Box.createVerticalStrut(4));
        top.add(scoreLabel);

        boardPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        boardPanel.setOpaque(true);
        boardPanel.setBackground(currentTheme.panel);
        boardPanel.setBorder(new EmptyBorder(12, 12, 12, 12));

        Font cellFont = new Font("Avenir Next", Font.BOLD, 56);

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                JButton b = new JButton("");
                b.setFocusPainted(false);
                b.setFont(cellFont);
                b.setBackground(currentTheme.cell);
                b.setForeground(currentTheme.textMain);
                b.setBorder(BorderFactory.createLineBorder(currentTheme.border, 2));
                b.setCursor(new Cursor(Cursor.HAND_CURSOR));

                final int row = r;
                final int col = c;
                b.addActionListener(e -> handleMove(row, col));

                cells[r][c] = b;
                boardPanel.add(b);
            }
        }

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        bottom.setOpaque(false);

        newGameBtn = makeSoftButton("New Game");
        backBtn = makeSoftButton("Back to Cover");

        newGameBtn.addActionListener(e -> confirmAndResetAllGameData());
        backBtn.addActionListener(e -> cardLayout.show(rootPanel, "COVER"));

        bottom.add(newGameBtn);
        bottom.add(backBtn);

        game.add(top, BorderLayout.NORTH);
        game.add(boardPanel, BorderLayout.CENTER);
        game.add(bottom, BorderLayout.SOUTH);

        refreshBoard();
        updateScoreLabel();
        updateTurnLabel();

        return game;
    }

    private JButton makeSoftButton(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setBackground(currentTheme.button);
        b.setForeground(currentTheme.textMain);
        b.setFont(new Font("Avenir Next", Font.BOLD, 16));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SOFT_BORDER, 1),
                new EmptyBorder(10, 20, 10, 20)
        ));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void showThemeChooser() {
        JDialog dialog = new JDialog(this, "Choose Theme", true);
        dialog.setSize(560, 430);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        Color dlgBg = new Color(34, 40, 56);
        Color panelBg = new Color(44, 52, 72);
        Color text = new Color(228, 234, 246);

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBackground(dlgBg);
        content.setBorder(new EmptyBorder(8, 8, 8, 8));

        JLabel tip = new JLabel("Select a theme", SwingConstants.CENTER);
        tip.setFont(new Font("Avenir Next", Font.BOLD, 18));
        tip.setForeground(text);
        tip.setBorder(new EmptyBorder(8, 0, 0, 0));
        tip.setOpaque(true);
        tip.setBackground(dlgBg);

        JPanel cards = new JPanel(new GridLayout(2, 3, 12, 12));
        cards.setBackground(panelBg);
        cards.setBorder(new EmptyBorder(10, 10, 10, 10));
        cards.add(themeCard(Theme.BLUE, dialog));
        cards.add(themeCard(Theme.SLATE, dialog));
        cards.add(themeCard(Theme.LAVENDER, dialog));
        cards.add(themeCard(Theme.FOREST, dialog));
        cards.add(themeCard(Theme.SUNSET, dialog));
        cards.add(themeCard(Theme.OCEAN, dialog));

        content.add(tip, BorderLayout.NORTH);
        content.add(cards, BorderLayout.CENTER);

        dialog.setContentPane(content);
        dialog.setVisible(true);
    }

    private JPanel themeCard(Theme theme, JDialog dialog) {
        JPanel card = new JPanel(new BorderLayout(6, 6));
        card.setBackground(new Color(44, 52, 72));
        card.setBorder(BorderFactory.createLineBorder(SOFT_BORDER, 1));

        JPanel preview = new JPanel(new GridLayout(2, 1));
        JPanel p1 = new JPanel();
        p1.setBackground(theme.bg1);
        JPanel p2 = new JPanel();
        p2.setBackground(theme.bg2);
        preview.add(p1);
        preview.add(p2);

        JLabel label = new JLabel(theme.label, SwingConstants.CENTER);
        label.setFont(new Font("Avenir Next", Font.PLAIN, 14));
        label.setForeground(new Color(228, 234, 246));

        JButton pick = new JButton("Use");
        pick.setFont(new Font("Avenir Next", Font.BOLD, 13));
        pick.setBackground(new Color(70, 92, 122));
        pick.setForeground(new Color(245, 248, 255));
        pick.setFocusPainted(false);
        pick.setOpaque(true);
        pick.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(130, 150, 178), 1),
                new EmptyBorder(6, 14, 6, 14)
        ));
        pick.addActionListener(e -> {
            currentTheme = theme;
            applyThemeToCover();
            if (gameStarted) {
                applyThemeToGame(); // preserve board + score, only recolor
            }
            dialog.dispose();
        });

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(new Color(44, 52, 72));
        bottom.add(label, BorderLayout.NORTH);
        bottom.add(pick, BorderLayout.SOUTH);

        card.add(preview, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);
        return card;
    }

    private void applyThemeToCover() {
        coverPanel.setColors(currentTheme.bg1, currentTheme.bg2);
        hintLabel.setForeground(currentTheme.textSub);

        startBtn.setBackground(currentTheme.button);
        startBtn.setForeground(currentTheme.textMain);
        startBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SOFT_BORDER, 1),
                new EmptyBorder(10, 20, 10, 20)
        ));

        changeThemeBtn.setBackground(currentTheme.button);
        changeThemeBtn.setForeground(currentTheme.textMain);
        changeThemeBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SOFT_BORDER, 1),
                new EmptyBorder(10, 20, 10, 20)
        ));

        coverPanel.revalidate();
        coverPanel.repaint();
    }

    private void applyThemeToGame() {
        gamePanel.setColors(currentTheme.bg1, currentTheme.bg2);

        gameTitleLabel.setForeground(currentTheme.textMain);
        statusLabel.setForeground(currentTheme.textSub);
        scoreLabel.setForeground(currentTheme.textMain);

        boardPanel.setBackground(currentTheme.panel);

        if (newGameBtn != null) {
            newGameBtn.setBackground(currentTheme.button);
            newGameBtn.setForeground(currentTheme.textMain);
        }
        if (backBtn != null) {
            backBtn.setBackground(currentTheme.button);
            backBtn.setForeground(currentTheme.textMain);
        }

        refreshBoard();
        updateTurnLabel();

        gamePanel.revalidate();
        gamePanel.repaint();
    }

    private void handleMove(int row, int col) {
        if (controller.isGameOver()) return;

        boolean moved = controller.makeMove(row, col);
        if (!moved) {
            JOptionPane.showMessageDialog(this, "Invalid move. Pick an empty cell.");
            return;
        }

        refreshBoard();

        if (controller.isGameOver()) {
            if (controller.isDraw()) {
                draws++;
                statusLabel.setText("Game Over: Draw");
                updateScoreLabel();

                int choice = JOptionPane.showConfirmDialog(
                        this, "It's a draw!\nPlay again?", "Game Over", JOptionPane.YES_NO_OPTION
                );
                if (choice == JOptionPane.YES_OPTION) startNewRound();
                else disableBoard();
            } else {
                char winner = controller.getWinner();
                if (winner == 'X') xWins++;
                if (winner == 'O') oWins++;

                statusLabel.setText("Game Over: Player " + winner + " wins!");
                updateScoreLabel();

                int choice = JOptionPane.showConfirmDialog(
                        this, "Player " + winner + " wins!\nPlay again?", "Game Over", JOptionPane.YES_NO_OPTION
                );
                if (choice == JOptionPane.YES_OPTION) startNewRound();
                else disableBoard();
            }
        } else {
            updateTurnLabel();
        }
    }

    private void confirmAndResetAllGameData() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to start a new game?\nThis will clear the board and score.",
                "Start New Game",
                JOptionPane.YES_NO_OPTION
        );
        if (choice == JOptionPane.YES_OPTION) resetAllGameData();
    }

    private void startNewRound() {
        controller.resetBoard();
        enableBoard();
        refreshBoard();
        updateTurnLabel();
        revalidate();
        repaint();
    }

    private void resetAllGameData() {
        xWins = 0;
        oWins = 0;
        draws = 0;
        startNewRound();
        updateScoreLabel();
    }

    private void refreshBoard() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                char cell = controller.getCell(r, c);
                JButton b = cells[r][c];

                if (cell == 'E') {
                    b.setText("");
                    b.setBackground(currentTheme.cell);
                    b.setForeground(currentTheme.textMain);
                } else if (cell == 'X') {
                    b.setText("X");
                    b.setBackground(new Color(
                            Math.min(currentTheme.cell.getRed() + 22, 255),
                            Math.min(currentTheme.cell.getGreen() + 22, 255),
                            Math.min(currentTheme.cell.getBlue() + 22, 255)
                    ));
                    b.setForeground(currentTheme.xColor);
                } else {
                    b.setText("O");
                    b.setBackground(new Color(
                            Math.min(currentTheme.cell.getRed() + 30, 255),
                            Math.min(currentTheme.cell.getGreen() + 10, 255),
                            Math.min(currentTheme.cell.getBlue() + 30, 255)
                    ));
                    b.setForeground(currentTheme.oColor);
                }
            }
        }
    }

    private void updateTurnLabel() {
        char p = controller.getCurrentPlayer();
        statusLabel.setText("Current Turn: Player " + p);
        statusLabel.setForeground(p == 'X' ? currentTheme.xColor : currentTheme.oColor);
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score  |  X: " + xWins + "   O: " + oWins + "   Draw: " + draws);
    }

    private void disableBoard() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) cells[r][c].setEnabled(false);
        }
    }

    private void enableBoard() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) cells[r][c].setEnabled(true);
        }
    }

    private static class GradientPanel extends JPanel {
        private Color c1;
        private Color c2;

        public GradientPanel(Color c1, Color c2) {
            this.c1 = c1;
            this.c2 = c2;
        }

        public void setColors(Color c1, Color c2) {
            this.c1 = c1;
            this.c2 = c2;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            GradientPaint gp = new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2);
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SwingUI::new);
    }
}