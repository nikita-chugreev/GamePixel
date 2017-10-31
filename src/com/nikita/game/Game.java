package com.nikita.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import com.nikita.game.graphics.Renderer;
import com.nikita.game.input.InputHandler;

public class Game extends Canvas{

    public static int width = 300;
    public static int height = width / 16 * 9;
    public static int scale = 3;
    private int x = 0, y = 0;
    private String title = "Game";
    private Thread thread;
    private boolean running = false;
    private JFrame frame = new JFrame(title);
    private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    private Renderer renderer;
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    private BufferStrategy bs;

    public Game() {
        Dimension size = new Dimension(width * scale, height * scale);
        setPreferredSize(size);
        renderer = new Renderer(width, height, pixels);
        frame.addKeyListener(new InputHandler());
        frame.setVisible(true);

    }

    public synchronized void start() {
        running = true;
        init();

        new Thread(() -> {
            long jvmLastTime = System.nanoTime();
            long time = System.currentTimeMillis();
            double jvmPartTime = 1_000_000_000.0 / 60.0;
            double delta = 0;
            int updates = 0;
            int frames = 0;
            while (running) {
                long jvmNow = System.nanoTime();
                delta += (jvmNow - jvmLastTime);
                jvmLastTime = jvmNow;
                if (delta >= jvmPartTime) {
                    update();
                    updates++;
                    delta = 0;
                }
                render();
                frames++;
                if (System.currentTimeMillis() - time > 1000) {
                    time += 1000;
                    frame.setTitle(title + " | " + "Updates: " + updates + ", " + "Frames: " + frames);
                    updates = 0;
                    frames = 0;
                }
            }
        }).start();
    }

    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void update() {
        if (InputHandler.isKeyPressed(KeyEvent.VK_UP)) y--;
        if (InputHandler.isKeyPressed(KeyEvent.VK_DOWN)) y++;
        if (InputHandler.isKeyPressed(KeyEvent.VK_RIGHT)) x++;
        if (InputHandler.isKeyPressed(KeyEvent.VK_LEFT)) x--;
    }

    private void render() {
        if (bs == null) {
            createBufferStrategy(3);
            bs = getBufferStrategy();
        }
        renderer.clear();
        renderer.render(x, y);
        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        bs.show();
    }

    private void init() {
        frame.setResizable(false);
        frame.setTitle("Game");
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Game().start();
    }
}
