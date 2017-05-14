package wit.cgd.warbirds;

/**
 * @file        WarBirds
 * @author      Oleksandr Kononov 20071032
 * @assignment  WarBirds
 * @brief       The main class for the WarBirds game
 *
 * @notes       
 */

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

import wit.cgd.warbirds.game.Assets;
import wit.cgd.warbirds.game.screens.MenuScreen;
import wit.cgd.warbirds.game.util.AudioManager;
import wit.cgd.warbirds.game.util.GamePreferences;

public class WarBirds extends Game {

	@Override
	public void create() {

		// Set LibGdx log level
		Gdx.app.setLogLevel(Application.LOG_INFO);

		// Load assets
		Assets.instance.init(new AssetManager());

		// Load preferences for audio settings and difficulty
		GamePreferences.instance.load();
		
		// start playing music
		AudioManager.instance.play(Assets.instance.music.themeSong);

		//Start MenuScreen
		setScreen(new MenuScreen(this));

	}

}