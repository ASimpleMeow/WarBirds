package wit.cgd.warbirds.game.screens;

/**
 * @file        GameScreen
 * @author      Oleksandr Kononov 20071032
 * @assignment  WarBirds
 * @brief       The screen where the actual games takes place in,
 * 				updates the WorldController and WorldRenderer
 *
 * @notes       
 */

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import wit.cgd.warbirds.game.WorldController;
import wit.cgd.warbirds.game.WorldRenderer;

public class GameScreen extends AbstractGameScreen {

	@SuppressWarnings("unused")
	private static final String	TAG	= GameScreen.class.getName();

	private WorldController		worldController;
	private WorldRenderer		worldRenderer;

	private boolean				paused;

	public GameScreen(Game game) {
		super(game);
	}

	@Override
	public void render(float deltaTime) {

		Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Do not update game world when paused.
		if (!paused) {
			worldController.update(deltaTime);
		}
		
		worldRenderer.render();
	}

	@Override
	public void resize(int width, int height) {
		worldRenderer.resize(width, height);
	}

	@Override
	public void show() {
		worldController = new WorldController(game);
		worldRenderer = new WorldRenderer(worldController);
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void hide() {
		worldRenderer.dispose();
		Gdx.input.setCatchBackKey(false);
	}

	@Override
	public void pause() {
		paused = true;
	}

	@Override
	public void resume() {
		super.resume();
		paused = false;
	}
}
