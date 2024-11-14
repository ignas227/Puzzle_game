package gamergames.puzzle1.objects;

import com.badlogic.gdx.graphics.Texture;

public class Tile {
    int color = 0;
    float gx, gy; /// from
    float cx, cy; /// coords\
    int width;
    float SPEED = 200;
    boolean falling;
    public Tile(int color, int gx, int gy, int cx, int cy, int width) {
        this.color = color;
        this.gx = gx;
        this.gy = gy;
        this.cx = cx;
        this.cy = cy;
        this.width = width;
        falling = false;
    }

    public void update(float dt){
        float move = dt * SPEED;
        if(cy - gy < move){
            gy = cy;
            falling = false;
        }else{
            gy += move;
            falling = true;
        }
    }

    public float getGx() {
        return gx;
    }

    public float getGy() {
        return gy;
    }

    public float getCx() {
        return cx;
    }

    public float getCy() {
        return cy;
    }

    public int getColor() {
        return color;
    }

    public int getWidth() {
        return width;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setGy(float gy) {
        this.gy = gy;
    }

    public boolean isFalling() {
        return falling;
    }
}
