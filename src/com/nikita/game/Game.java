package com.nikita.game;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import com.nikita.game.graphics.Renderer;

public class Game extends Canvas{

    public static int width = 300;
    public static int height = width / 16 * 9;
    public static int scale = 3;

    private Thread thread;
    private boolean running = false;
    private JFrame frame;
    private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    private Renderer renderer;
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    private BufferStrategy bs;

    public Game() {
        Dimension size = new Dimension(width * scale, height * scale);
        setPreferredSize(size);
        frame = new JFrame();
        renderer = new Renderer(width, height, pixels);
        frame.setVisible(true);
    }

    public synchronized void start() {
        running = true;
        thread = new Thread(() -> {
            init();
            while (running) {
                render();
                update();
            }
        });
        thread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void render() {
        if (bs == null) {
            createBufferStrategy(3);
            bs = getBufferStrategy();
        }
        renderer.clear();
        renderer.render();
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

    private void update() {

    }

    public static void main(String[] args) {
        new Game().start();
    }
}
