package wit.cgd.warbirds.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

public class GamePreferences {

	public static final String			TAG			= GamePreferences.class.getName();

	public static final GamePreferences	instance	= new GamePreferences();
	private Preferences					prefs;
	public int							levelNumber;
	public int							enemySpawnLimit;
	
	public boolean						sound;
	public boolean						music;
	public float						soundVolume;
	public float						musicVolume;

	private GamePreferences() {
		prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
	}

	public void load() {
		levelNumber = prefs.getInteger("levelNumber", 1);
		enemySpawnLimit = MathUtils.clamp(prefs.getInteger("enemySpawnLimit", 3), 1, 5);
		sound = prefs.getBoolean("sound", true);
		music = prefs.getBoolean("music", true);
		soundVolume = MathUtils.clamp(prefs.getFloat("soundVolume", 1f), 0f, 1f);
		musicVolume = MathUtils.clamp(prefs.getFloat("musicVolume", 1f), 0f, 1f);
	}

	public void save() {
		prefs.putInteger("levelNumber", levelNumber);
		prefs.putInteger("enemySpawnLimit", enemySpawnLimit);
		prefs.putBoolean("sound", sound);
    	prefs.putBoolean("music", music);
    	prefs.putFloat("soundVolume", soundVolume);
    	prefs.putFloat("musicVolume", musicVolume);
		prefs.flush();
	}

}
