package gamergames.puzzle1.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import gamergames.puzzle1.Main;

public class MenuState extends State{
    private Texture background;
    private Texture playButton;
    public MenuState(GameStateManager gsm) {
        super(gsm);
        background = new Texture ("background.png");
        playButton = new Texture ("playButton.png");
        cam.setToOrtho(true, Main.WIDTH, Main.HEIGHT);
    }

    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()){
            gsm.set(new PlayState(gsm));
            dispose();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(background, 0, 0, Main.WIDTH, Main.HEIGHT, 0, 0, background.getWidth(), background.getHeight(), false, true);
        sb.draw(playButton, (Main.WIDTH / 2) - (playButton.getWidth() / 2), (Main.HEIGHT / 2) - (playButton.getHeight() / 2), playButton.getWidth(), playButton.getHeight() , 0, 0, playButton.getWidth(), playButton.getHeight(), false, true);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        playButton.dispose();
    }
}
