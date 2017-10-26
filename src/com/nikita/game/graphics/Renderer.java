package com.nikita.game.graphics;

public class Renderer {

    private int width, height;
    private int pixels[] = null;
    private int foo, bar;


    public Renderer(int width, int height, int pixels[]) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }

    public void clear() {
        for (int i=0; i < pixels.length; i++) {
            pixels[i] = 0;
        }
    }

    public void render() {
        foo++;
        if(foo % 300 == 0) {
            bar++;
        }

        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                pixels[bar + bar * width] = 0x00E1FF;

            }
        }
    }
}
