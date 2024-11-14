package gamergames.puzzle1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class TextureManager {
    Texture[] textures;
    int size = 7;

    public TextureManager() {
        textures = new Texture[size]; /// MIGHT NEED 0 ADDED
        textures[0] = new Texture(Gdx.files.internal("blank.png"));
        textures[1] = new Texture(Gdx.files.internal("element_blue_square.png"));
        textures[2] = new Texture(Gdx.files.internal("element_green_square.png"));
        textures[3] = new Texture(Gdx.files.internal("element_yellow_square.png"));
        textures[4] = new Texture(Gdx.files.internal("element_red_square.png"));
        textures[5] = new Texture(Gdx.files.internal("element_purple_square.png"));
        textures[6] = new Texture(Gdx.files.internal("ballBlue.png"));
    }

    public Texture getTexture(int a) {
        return textures[a];
    }

    public void dispose(){
        for(int a = 0; a < size; a++){
            textures[a].dispose();
        }
    }
}
