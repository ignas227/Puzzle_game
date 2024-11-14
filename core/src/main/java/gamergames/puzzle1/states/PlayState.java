package gamergames.puzzle1.states;


import static java.lang.Math.abs;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import gamergames.puzzle1.Main;
import gamergames.puzzle1.Point;
import gamergames.puzzle1.TextureManager;
import gamergames.puzzle1.objects.Tile;

public class PlayState extends State{
    Random rand;
    TextureManager textureManager;
    int tx = 9;
    int ty = 11;
    Tile[][] tiles = new Tile[tx][ty];
    int tileWidth;

    float tileSpawnTimer = 0f;
    float tileSpawnRate = 0.4f;
    Boolean tileSpawnedThisFrame = false;
    boolean wasTouched = false;

    ArrayList<Point> activated;

    Music music;

    Sound activateSound;
    Sound boomSound;

    StretchViewport viewport;


    protected PlayState(GameStateManager gsm) {
        super(gsm);
        rand = new Random();
        viewport = new StretchViewport(Main.WIDTH, Main.HEIGHT, cam);

        textureManager = new TextureManager();
        activated = new ArrayList<>();
        for(int a = 0; a < tx; a++){
            for(int b = 0; b < ty; b++){
                float bounds = 0.1f;
                float boardArea = 1.f - 2 * bounds;
                float width = (boardArea / tx) * Main.WIDTH;
                tileWidth = (int)width;
                float cx = a * width;
                float cy = b * width;
                tiles[a][b] = new Tile(rand.nextInt(5) + 1, (int)(cx + bounds * Main.WIDTH), (int)cy, (int)(cx + bounds * Main.WIDTH), (int)cy, (int)width);
            }
        }
        ///cam.setToOrtho(false, Main.WIDTH, Main.HEIGHT);
        cam.setToOrtho(true, Main.WIDTH, Main.HEIGHT);
        ///cam.position.set(Main.WIDTH / 2, Main.HEIGHT / 2, 0);
        ///cam.update();
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        music.setVolume(0.35f);
        music.setLooping(true);
        music.play();

        activateSound = Gdx.audio.newSound(Gdx.files.internal(("select sound.wav")));
        boomSound = Gdx.audio.newSound(Gdx.files.internal(("boom sound.wav")));

    }

    @Override
    protected void handleInput() {
        ///System.out.println(Gdx.input.getX() + " " + Gdx.input.getY());
        if ((Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && (Gdx.app.getType() == Application.ApplicationType.Desktop) || ((Gdx.app.getType() == Application.ApplicationType.Android) && (Gdx.input.isTouched()) && wasTouched == false))  && isSomethingFalling() == false) {
            wasTouched = true;
            ///float mouseX = Gdx.input.getX();
            ///float mouseY = Gdx.input.getY();
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            cam.unproject(touchPos);
            float mouseX = touchPos.x;
            float mouseY = touchPos.y;
            int x = -1;
            int y = -1;
            for(int a = 0; a < tx; a++){
                for(int b = 0; b < ty; b++){
                    Tile tile = tiles[a][b];
                    if(tile.getCx() < mouseX  && mouseX < tile.getCx() + tile.getWidth()){
                        if(tile.getCy() < mouseY  && mouseY < tile.getCy() + tile.getWidth()){
                            x = a;
                            y = b;
                        }
                    }
                }
            }
            if((x != -1) && (y != -1)) {
                activateSound.play(0.6f);
                if(activated.size() == 0){
                    activated.add(new Point(x, y));
                }else{ /// 2 selected
                    activated.add(new Point(x, y));
                    int x0 = activated.get(0).x;
                    int x1 = activated.get(1).x;
                    int y0 = activated.get(0).y;
                    int y1 = activated.get(1).y;
                    int dx = abs(activated.get(0).x - activated.get(1).x);
                    int dy = abs(activated.get(0).y - activated.get(1).y);
                    if(dx + dy == 1){ /// right next to each other
                        int color = tiles[x0][y0].getColor();
                        tiles[x0][y0].setColor(tiles[x1][y1].getColor());
                        tiles[x1][y1].setColor(color);
                        if(hypotheticallyCheckPoint(x0, y0) + hypotheticallyCheckPoint(x1, y1) > 0){ /// we good

                        }else{
                            color = tiles[x0][y0].getColor();
                            tiles[x0][y0].setColor(tiles[x1][y1].getColor());
                            tiles[x1][y1].setColor(color);
                        }
                    }
                    activated.clear();
                }
                ///tiles[x][y].setColor(0);
                ///checkBoard(x, y);

                ///checkPoint(x, y);

                ///System.out.println(x + " " + y);
            }
        }if(Gdx.input.isTouched() == false){
            wasTouched = false;
        }if(Gdx.input.isKeyJustPressed(Input.Keys.R)){
            gsm.set(new PlayState(gsm));
        }
    }

    public int setToBlank(int x, int y){
        if(y - 1 < 0){
            return 0;
        }
        tiles[x][y].setColor(tiles[x][y - 1].getColor());
        ///tiles[x][y].setGy(tiles[x][y].getCy() - (float)tileWidth);
        ///tiles[x][y].setGy(tiles[x][y - 1].getGy() - (float)tileWidth);
        tiles[x][y].setGy(tiles[x][y - 1].getGy());
        tiles[x][y - 1].setColor(0);
        return 0;
    }

    public void checkForSpawn(){/// create a new tile at the top
        for(int a = 0; a < tx; a++){
            Tile tile = tiles[a][0];
            if(tile.getColor() == 0) {
                if(tileSpawnTimer >= tileSpawnRate || tileSpawnedThisFrame == true) {
                    tile.setColor(rand.nextInt(5) + 1);
                    tile.setGy(tile.getCy() - 2 * tileWidth);
                    tileSpawnedThisFrame = true;
                    tileSpawnTimer = 0;
                }
            }
        }
    }

    public int checkBoard(int x, int y){

        int answer = 0;
        if(y > 0) {
            if ((tiles[x][y].getColor() == 0) && tiles[x][y - 1].getColor() != 0) {
                setToBlank(x, y);
                checkBoard(x, y - 1);
                answer = 1;
            }
        }
        return answer;
    }

    @Override
    public void update(float dt) {
        handleInput();
        for(int a = 0; a < tx; a++){
            for(int b = 0; b < ty; b++){
                tiles[a][b].update(dt);
            }
        }

        for (int a = 0; a < tx; a++) {
            for (int b = 0; b < ty; b++) {
                checkBoard(a, b);
            }
        }


        checkForSpawn();

        if(isSomethingFalling() == false) {
            for (int a = 0; a < tx; a++) {
                for (int b = 0; b < ty; b++) {
                    checkPoint(a, b);
                }
            }
        }
        tileSpawnedThisFrame = false;
        tileSpawnTimer += dt;
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        for(int a = 0; a < tx; a++){
            for(int b = 0; b < ty; b++){
                Tile tile = tiles[a][b];
                Texture texture = textureManager.getTexture(tile.getColor());
                sb.draw(texture, tile.getGx(), tile.getGy(), tile.getWidth(), tile.getWidth(), 0, 0, texture.getWidth(), texture.getWidth(), false, true);
            }
        }
        sb.end();
    }

    public boolean isSomethingFalling(){
        boolean answer = false;
        for(int a = 0 ; a < tx; a++){
            for(int b = 0; b < ty; b++){
                if(tiles[a][b].isFalling() == true){
                    answer = true;
                }
            }
        }
        return answer;
    }

    public int hypotheticallyCheckPoint(int x, int y) {
        int color = tiles[x][y].getColor();
        if (color == 0) {
            return 0;
        }
        int px0 = x, px1 = x, py0 = y, py1 = y;
        while ((px0 - 1 >= 0) && (tiles[px0 - 1][y].getColor() == color)) {
            px0 = px0 - 1;
        }
        while ((px1 + 1 < tx) && (tiles[px1 + 1][y].getColor() == color)) { /// might be off
            px1 = px1 + 1;
        }
        while ((py0 - 1 >= 0) && (tiles[x][py0 - 1].getColor() == color)) {
            py0 = py0 - 1;
        }
        while ((py1 + 1 < ty) && (tiles[x][py1 + 1].getColor() == color)) { /// might be off
            py1 = py1 + 1;
        }
        ArrayList<Point> points = new ArrayList<Point>();
        if (px1 - px0 >= 2) {
            for (int a = px0; a <= px1; a++) {
                points.add(new Point(a, y));
            }
        }
        if (py1 - py0 >= 2) {
            for (int b = py0; b <= py1; b++) {
                points.add(new Point(x, b));
            }
        }
        return points.size();
    }

    public int checkPoint(int x, int y){
        if(isSomethingFalling() == true){
            return 0;
        }
        int color = tiles[x][y].getColor();
        if(color == 0){
            return 0;
        }
        int px0 = x, px1 = x, py0 = y, py1 = y;
        while((px0 - 1 >= 0) && (tiles[px0 - 1][y].getColor() == color)){
            px0 = px0 - 1;
        }while((px1 + 1 < tx) && (tiles[px1 + 1][y].getColor() == color)){ /// might be off
            px1 = px1 + 1;
        }
        while((py0 - 1 >= 0) && (tiles[x][py0 - 1].getColor() == color)){
            py0 = py0 - 1;
        }while((py1 + 1 < ty) && (tiles[x][py1 + 1].getColor() == color)){ /// might be off
            py1 = py1 + 1;
        }
        ArrayList<Point> points = new ArrayList<Point>();
        if(px1 - px0 >= 2){
            for(int a = px0; a <= px1; a++){
                points.add(new Point(a, y));
            }
        }
        if(py1 - py0 >= 2){
            for(int b = py0; b <= py1; b++){
                points.add(new Point(x, b));
            }
        }

        System.out.println(points.size());

        for(int a = 0; a < points.size(); a++){
            Point point = points.get(a);
            ///setToBlank(point.x, point.y);
            tiles[point.x][point.y].setColor(0);
            boomSound.play(0.8f);
        }
        for(int a = 0; a < points.size(); a++){
            Point point = points.get(a);
            checkBoard(point.x, point.y);
        }


        return 0;
    }


    @Override
    public void dispose() {
        textureManager.dispose();
        music.dispose();
        activateSound.dispose();
        boomSound.dispose();
    }
}

