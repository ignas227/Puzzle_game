package gamergames.puzzle1;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import gamergames.puzzle1.states.GameStateManager;
import gamergames.puzzle1.states.MenuState;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    public static final String TITLE = "Template1";

    private GameStateManager gsm;
    private SpriteBatch batch;

    @Override
    public void create() {
        ///System.out.println("1asdasdsa");
        batch = new SpriteBatch();
        gsm = new GameStateManager();
        ScreenUtils.clear(0.15f, 0.15f, 0.8f, 1f);
        gsm.push(new MenuState(gsm));
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.8f, 1f);
        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render(batch);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
