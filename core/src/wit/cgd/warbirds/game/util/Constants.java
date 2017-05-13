package wit.cgd.warbirds.game.util;

public class Constants {

	// Game world dimensions
	public static final float	VIEWPORT_WIDTH		= 8.0f;
	public static final float	VIEWPORT_HEIGHT		= 8.0f;

	// GUI dimensions
	public static final float	VIEWPORT_GUI_WIDTH	= 480.0f;
	public static final float	VIEWPORT_GUI_HEIGHT	= 800.0f;
	
	public static final float	BUTTON_PADDING		= 5f;

	// atlas for all game sprites
	public static final String	TEXTURE_ATLAS_GAME	= "images/game.atlas";
	
	// location of game specific skin and atlas
    public static final String  SKIN_UI                 = "images/ui.json";
    public static final String  TEXTURE_ATLAS_UI        = "images/ui.atlas";

    // location of libgdx default skin and atlas
    public static final String  SKIN_LIBGDX_UI          = "images/uiskin.json";
    public static final String  TEXTURE_ATLAS_LIBGDX_UI = "images/uiskin.atlas";

	// Persistent storage files
	public static final String	PREFERENCES			= "game.prefs";

	// Speed Constants (most relative to SCROLL_SPEED)
	public static final float	SCROLL_SPEED		= 1.0f;

	public static final float	PLANE_H_SPEED		= 5.0f;
	public static final float	PLANE_MIN_V_SPEED	= -3 * SCROLL_SPEED;
	public static final float	PLANE_MAX_V_SPEED	= 4 * SCROLL_SPEED;

	public static final float	PLAYER_SHOOT_DELAY	= 0.2f;
	public static final float	ENEMY_SHOOT_DELAY 	= 1.2f;
	public static final float	BULLET_SPEED		= 2.0f * PLANE_MAX_V_SPEED;

	//Delays
	public static final float	BULLET_DIE_DELAY	= 1.2f;
	public static final float	ENEMY_DIE_DELAY		= 0.5f;
	public static final float	LEVEL_START_DELAY	= 3f;
	public static final float	LEVEL_BOSS_DELAY	= 5f;
	
	//Health constants
	public static final float		PlAYER_HEALTH		= 20f;
	public static final float		PLAYER_SHIELD		= 6f;
	public static final float		ENEMY_SIMPLE_HEALTH	= 2f;
	public static final float		ENEMY_NORMAL_HEALTH	= 4f;
	public static final float		ENEMY_DIFFICULT_HEALTH = 6f;
	public static final float		BOSS_HEALTH			= 50f;
	
	public static final float		BULLET_DAMAGE		= 2f;
	
	//Powerup constants
	public static final float	DOUBLE_BULLET_TIMER	= 5f;
	public static final float	EXTRA_SPEED_TIMER	= 3f;
}
