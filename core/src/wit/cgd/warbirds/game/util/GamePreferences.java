package wit.cgd.warbirds.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GamePreferences {

	public static final String			TAG			= GamePreferences.class.getName();

	public static final GamePreferences	instance	= new GamePreferences();
	private Preferences					prefs;
	public int							levelNumber;

	private GamePreferences() {
		prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
	}

	public void load() {
		levelNumber = prefs.getInteger("levelNumber", 1);
	}

	public void save() {
		prefs.putInteger("levelNumber", levelNumber);
		prefs.flush();
	}

}
