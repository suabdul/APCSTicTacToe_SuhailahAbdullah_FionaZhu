package tictactoe;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SwingU extends JFrame {

    // ── COLORS (space theme) ───────────────────────────────
    private static final Color BG_CARD    = new Color(20, 25, 40);
    private static final Color ACCENT_X   = new Color(255, 200, 50);
    private static final Color ACCENT_O   = new Color(180, 80, 255);
    private static final Color GRID_LINE  = new Color(70, 90, 120);
    private static final Color TEXT_LIGHT = new Color(200, 220, 255);

    private static final String BOARD_FILE = "board.csv";

    // ── GAME STATE ─────────────────────────────────────────
    private Board board;
    private GameLogic logic;
    private CellButton[][] cells = new CellButton[3][3];
    private JLabel statusLabel, turnIndicator;

    // ── MAIN ───────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> showSplash(() -> new SwingU(true)));
    }

    // ── SPLASH ─────────────────────────────────────────────
    private static void showSplash(Runnable after) {
        JWindow splash = new JWindow();
        StarPanel panel = new StarPanel();
        panel.setLayout(new BorderLayout());

        JLabel title = new JLabel("✦ TIC TAC TOE ✦", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(180, 220, 255));

        panel.add(title, BorderLayout.CENTER);
        splash.add(panel);

        splash.setSize(400, 200);
        splash.setLocationRelativeTo(null);
        splash.setVisible(true);

        new Timer(1500, e -> {
            splash.dispose();
            after.run();
        }) {{
            setRepeats(false);
            start();
        }};
    }

    // ── CONSTRUCTOR ────────────────────────────────────────
    public SwingU(boolean showMenu) {
        setTitle("✦ Space Tic-Tac-Toe");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(420, 520);
        setLocationRelativeTo(null);
        setResizable(true);

        if (showMenu) {
            setContentPane(buildMenu());
        } else {
            initGame();
        }

        addFullscreenToggle();
        setVisible(true);
    }

    // ── MENU ───────────────────────────────────────────────
    private JPanel buildMenu() {
        JPanel panel = new StarPanel();
        panel.setLayout(new GridLayout(3, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(80, 60, 80, 60));

        JButton play = new JButton("Play");
        JButton stats = new JButton("Stats");
        JButton exit = new JButton("Exit");

        play.addActionListener(e -> {
            dispose();
            new SwingU(false);
        });

        stats.addActionListener(e ->
            JOptionPane.showMessageDialog(this, "Stats coming soon ✦")
        );

        exit.addActionListener(e -> System.exit(0));

        panel.add(play);
        panel.add(stats);
        panel.add(exit);

        return panel;
    }

    // ── GAME INIT ──────────────────────────────────────────
    private void initGame() {
        board = new Board(BOARD_FILE);
        logic = new GameLogic();
        board.clearBoard();
        board.saveBoardToFile();

        JPanel root = new StarPanel();
        root.setLayout(new BorderLayout(10,10));
        root.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildGrid(), BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);

        setContentPane(root);
        refreshUI();
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel();
        p.setOpaque(false);

        JLabel title = new JLabel("✦ TIC TAC TOE ✦");
        title.setForeground(TEXT_LIGHT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        p.add(title);
        return p;
    }

    private JPanel buildGrid() {
        JPanel grid = new JPanel(new GridLayout(3,3,5,5));
        grid.setBackground(GRID_LINE);

        for (int r=0;r<3;r++) {
            for (int c=0;c<3;c++) {
                int row=r, col=c;
                CellButton btn = new CellButton(row,col);
                btn.addActionListener(e -> onCellClicked(row,col));
                cells[r][c] = btn;
                grid.add(btn);
            }
        }
        return grid;
    }

    private JPanel buildFooter() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        turnIndicator = new JLabel(" ");
        turnIndicator.setForeground(TEXT_LIGHT);
        turnIndicator.setAlignmentX(CENTER_ALIGNMENT);

        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.GREEN);
        statusLabel.setAlignmentX(CENTER_ALIGNMENT);

        JButton newGame = new JButton("New Game");
        newGame.setAlignmentX(CENTER_ALIGNMENT);
        newGame.addActionListener(e -> startNewGame());

        JButton back = new JButton("← Menu");
        back.setAlignmentX(CENTER_ALIGNMENT);
        back.addActionListener(e -> {
            dispose();
            new SwingU(true);
        });

        p.add(turnIndicator);
        p.add(statusLabel);
        p.add(Box.createVerticalStrut(10));
        p.add(newGame);
        p.add(back);

        return p;
    }

    // ── GAME LOGIC HOOK ────────────────────────────────────
    private void onCellClicked(int r, int c) {
        if (logic.isGameOver(board)) return;
        if (board.getCell(r,c)!='E') return;

        logic.makeMove(board,r,c);
        board.saveBoardToFile();
        refreshUI();

        if (logic.isGameOver(board)) handleGameOver();
    }

    private void handleGameOver() {
        if (logic.checkWin(board,'X')) {
            statusLabel.setText("X Wins!");
        } else if (logic.checkWin(board,'O')) {
            statusLabel.setText("O Wins!");
        } else {
            statusLabel.setText("Draw!");
        }
    }

    private void refreshUI() {
        char[][] g = board.getGrid();
        for (int r=0;r<3;r++)
            for (int c=0;c<3;c++)
                cells[r][c].setValue(g[r][c]);

        if (!logic.isGameOver(board)) {
            turnIndicator.setText("Turn: "+logic.getCurrentPlayer(board));
        }
    }

    private void startNewGame() {
        board.clearBoard();
        board.saveBoardToFile();
        refreshUI();
    }

    // ── FULLSCREEN ─────────────────────────────────────────
    private void addFullscreenToggle() {
        addKeyListener(new KeyAdapter() {
            boolean full=false;
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_F11) {
                    dispose();
                    setUndecorated(!full);
                    setExtendedState(full?JFrame.NORMAL:JFrame.MAXIMIZED_BOTH);
                    full=!full;
                    setVisible(true);
                }
            }
        });
        setFocusable(true);
    }

    // ── STARFIELD (animated) ───────────────────────────────
    static class StarPanel extends JPanel {
        int[] x = new int[100];
        int[] y = new int[100];
        int[] speed = new int[100];
        Random rand = new Random();

        StarPanel() {
            setBackground(new Color(5,10,25));

            for (int i=0;i<x.length;i++) {
                x[i]=rand.nextInt(800);
                y[i]=rand.nextInt(600);
                speed[i]=1+rand.nextInt(3);
            }

            new Timer(30, e -> animate()).start();
        }

        private void animate() {
            for (int i=0;i<y.length;i++) {
                y[i]+=speed[i];
                if (y[i]>getHeight()) {
                    y[i]=0;
                    x[i]=rand.nextInt(getWidth());
                }
            }
            repaint();
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.WHITE);
            for (int i=0;i<x.length;i++) {
                g.fillOval(x[i],y[i],2,2);
            }
        }
    }

    // ── CELL BUTTON ────────────────────────────────────────
    private class CellButton extends JButton {
        char value='E';

        CellButton(int r,int c) {
            setPreferredSize(new Dimension(100,100));
            setFocusPainted(false);
        }

        void setValue(char v) {
            value=v;
            repaint();
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2=(Graphics2D)g;

            if (value=='X') {
                g2.setColor(ACCENT_X);
                g2.setStroke(new BasicStroke(5));
                g2.drawLine(20,20,getWidth()-20,getHeight()-20);
                g2.drawLine(getWidth()-20,20,20,getHeight()-20);
            }
            if (value=='O') {
                g2.setColor(ACCENT_O);
                g2.setStroke(new BasicStroke(5));
                g2.drawOval(20,20,getWidth()-40,getHeight()-40);
            }
        }
    }
}