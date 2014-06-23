package gameoflife;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Cypher
 */
public class Frame extends Canvas
{

    private BufferStrategy bs;
    private JFrame frame;
    private JPanel panel;
    private int fps = 0, avgFps = 0;
    private long now;
    private boolean runGame, isPause;
    private Board board;

    public Frame()
    {
        runGame = true;
        isPause = true;
        frame = new JFrame("Ian's Game of Life");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(800, 600));

        panel = (JPanel) frame.getContentPane();
        panel.add(Frame.this);

        this.setPreferredSize(new Dimension(800, 600));
        this.setIgnoreRepaint(true);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        this.createBufferStrategy(2);
        bs = this.getBufferStrategy();

        board = new Board(80, 60);
        
        this.requestFocus();
        this.addMouseListener(new MouseListener()
        {

            @Override
            public void mouseClicked(MouseEvent e)
            {
                
            }

            @Override
            public void mousePressed(MouseEvent e)
            {
                if (e.getButton() == 1)
                {
                    isPause = !isPause;
                } else if (e.getButton() == 3)
                {
                    int x = e.getX();
                    int y = e.getY();
                    board.changeNode(x / 10, y / 10);
                } else if(e.getButton() == 2 && isPause)
                {
                    board = new Board(80, 60);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {

            }

            @Override
            public void mouseEntered(MouseEvent e)
            {

            }

            @Override
            public void mouseExited(MouseEvent e)
            {

            }

        });
        
        render();
        runLoop();
    }

    public final void render()
    {
        // Create the graphics object.
        Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();

        // Set the background colour.
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, 800, 600);

        g2d.setColor(Color.black);
        for (int i = 0; i < 800; i += 10)
        {
            for (int j = 0; j < 600; j += 10)
            {
                g2d.fillRect(i, 0, 1, 600);
                g2d.fillRect(0, j, 800, 1);
            }
        }
        board.drawBoard(g2d);
        
        if(isPause)
        {
            g2d.setColor(Color.red);
            g2d.drawString("Paused", 10, 20);
        }
        // Swap buffer and show.
        g2d.dispose();
        bs.show();
    }

    private void runLoop()
    {
        Thread theLoop = new Thread()
        {
            @Override
            public void run()
            {
                loop();
            }
        };
        theLoop.start();
    }

    private void loop()
    {
        long lastFpsTime = 0;
        long lastTime = System.nanoTime();
        final int TARGET_FPS = 5;  //The intended FPS of the game.
        final long OPT_TIME = 1000000000 / TARGET_FPS;

        while (runGame)
        {
            if (!isPause)
            {
                now = System.nanoTime();
                long updateLength = now - lastTime;
                lastTime = now;

                /*
                 * This value will be used as a form of timing for ship and
                 * meteor updates.
                 */
                double delta = updateLength / ((double) OPT_TIME);
                lastFpsTime += updateLength;
                fps++;

                if (lastFpsTime >= 1000000000)
                {
                    avgFps = fps;
                    lastFpsTime = 0;
                    fps = 0;
                }

                
                board.playTurn();
                board.changeAll();
            }
            render();
            try
            {
                long sleepTimer = ((lastTime - System.nanoTime() + OPT_TIME) / 1000000);
                if (sleepTimer > 0)
                {
                    Thread.sleep((lastTime - System.nanoTime() + OPT_TIME) / 1000000);
                } else
                {
                    /*
                     * Was getting an error of negative numbers on first frame.
                     * This was my fix. It works...
                     */
                    Thread.sleep(15);
                }

            } catch (Exception e)
            {
                System.out.println("Error in thread sleep: " + e);
            }

        }
    }
}
