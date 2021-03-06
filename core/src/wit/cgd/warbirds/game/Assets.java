package wit.cgd.warbirds.game;

/**
 * @file        Assets
 * @author      Oleksandr Kononov 20071032
 * @assignment  WarBirds
 * @brief       Contains all Assets used in the game - 
 * 				Textures, Animations, Fonts, Sounds and Music
 *
 * @notes       
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import wit.cgd.warbirds.game.util.Constants;

public class Assets implements Disposable, AssetErrorListener {

	public static final String	TAG			= Assets.class.getName();
	public static final Assets	instance	= new Assets();

	private AssetManager		assetManager;

	public AssetFonts			fonts;

	public AssetSounds			sounds;
	public AssetMusic			music;

	public AssetAnimation		player;
	public AssetAnimation		enemySimple;
	public AssetAnimation		enemyNormal;
	public AssetAnimation		enemyDifficult;
	public AssetAnimation		boss;
	public AssetAnimation		explosionBig;
	public AssetAnimation		explosionLarge;
	
	public Asset				bullet;
	public Asset				doubleBullet;
	public Asset				enemyBullet;
	public AssetLevelDecoration	levelDecoration;
	public Asset				shield;
	public Asset				levelNumbers;
	public Asset				powerups;

	private Assets() {}

	public void init(AssetManager assetManager) {

		this.assetManager = assetManager;
		assetManager.setErrorListener(this);

		// load texture for game sprites
		assetManager.load(Constants.TEXTURE_ATLAS_GAME, TextureAtlas.class);

		// load sounds
		assetManager.load("sounds/sfx_blast.wav", Sound.class);
		assetManager.load("sounds/sfx_click.wav", Sound.class);
		assetManager.load("sounds/sfx_explosion.wav", Sound.class);
		assetManager.load("sounds/sfx_gun1.wav", Sound.class);
		assetManager.load("sounds/sfx_gun2.wav", Sound.class);
		assetManager.load("sounds/sfx_pickup.wav", Sound.class);
		assetManager.load("sounds/sfx_warning.wav", Sound.class);

		// load music
		assetManager.load("music/song_theme.mp3", Music.class);
		assetManager.load("music/song_boss.mp3", Music.class);

		assetManager.finishLoading();


		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames())
			Gdx.app.debug(TAG, "asset: " + a);

		// create atlas for game sprites
		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_GAME);
		for (Texture t : atlas.getTextures())
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		//Create fonts
		fonts = new AssetFonts();

		// create game resource objects
		player = new AssetAnimation(atlas,"player");
		
		enemySimple = new AssetAnimation(atlas, "enemy_plane_green");
		enemyNormal = new AssetAnimation(atlas, "enemy_plane_yellow");
		enemyDifficult = new AssetAnimation(atlas, "enemy_plane_white");
		boss = new AssetAnimation(atlas, "boss");
		
		explosionBig = new AssetAnimation(atlas, "explosion_big");
		explosionLarge = new AssetAnimation(atlas, "explosion_large");
		
		levelDecoration = new AssetLevelDecoration(atlas);
		shield = new Asset(atlas, "shield");
		bullet = new Asset(atlas, "bullet");
		doubleBullet  = new Asset(atlas, "bullet_double");
		enemyBullet = new Asset(atlas, "emeny_bullet");
		levelNumbers = new Asset(atlas, "wave1","wave2","wave3","wave4","wave5","wave6","wave7","wave8");
		powerups = new Asset(atlas, "powerup_icon", "double_bullets_icon", "health_icon", "shield_icon");
		
		// create sound and music resource objects
		sounds = new AssetSounds(assetManager);
		music = new AssetMusic(assetManager);

	}

	@Override
	public void dispose() {
		assetManager.dispose();
	}

	@Override
	public void error(@SuppressWarnings("rawtypes") AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '" + asset + "'", (Exception) throwable);
	}

	public class Asset {
		public final AtlasRegion		region;
		public final Array<AtlasRegion>	regions;

		public Asset(TextureAtlas atlas, String imageName) {
			region = atlas.findRegion(imageName);
			regions = null;
			Gdx.app.log(TAG, "Loaded asset '" + imageName + "'");
		}
		
		public Asset(TextureAtlas atlas, String...imageNames) {
			region = null;
			regions = new Array<AtlasRegion>();
			for(String imageName : imageNames){
				regions.add(atlas.findRegion(imageName));
				Gdx.app.log(TAG, "Loaded asset '" + imageName + "'");
			}
		}
	}

	public class AssetAnimation {
		public final Animation<TextureRegion>		animation;

		public AssetAnimation(TextureAtlas atlas, String name) {
			Array<AtlasRegion> regions = atlas.findRegions(name);
			animation = new Animation<TextureRegion>(1.0f / 15.0f, regions, Animation.PlayMode.LOOP);
			Gdx.app.log(TAG, "Loaded asset animation '" + name + "'");
		}
	}

	public class AssetLevelDecoration {

		public final AtlasRegion	islandBig;
		public final AtlasRegion	islandSmall;
		public final AtlasRegion	islandTiny;
		public final AtlasRegion	water;

		public AssetLevelDecoration(TextureAtlas atlas) {
			water = atlas.findRegion("water");
			islandBig = atlas.findRegion("island_big");
			islandSmall = atlas.findRegion("island_small");
			islandTiny = atlas.findRegion("island_tiny");
		}
	}

	public class AssetFonts {
		public final BitmapFont	defaultSmall;
		public final BitmapFont	defaultNormal;
		public final BitmapFont	defaultBig;

		public AssetFonts() {
			// create three fonts 
			defaultSmall = new BitmapFont(Gdx.files.internal("images/game_font.fnt"), true);
			defaultNormal = new BitmapFont(Gdx.files.internal("images/game_font.fnt"), true);
			defaultBig = new BitmapFont(Gdx.files.internal("images/game_font.fnt"), true);
			// set font sizes
			defaultSmall.getData().setScale(0.75f);
			defaultNormal.getData().setScale(2.5f, 1.0f);
			defaultBig.getData().setScale(3.0f, 2.0f);
			// enable linear texture filtering for smooth fonts
			defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
	}

	public class AssetSounds {
		public final Sound blast;
		public final Sound click;
		public final Sound explosion;
		public final Sound gun1;
		public final Sound gun2;
		public final Sound pickup;
		public final Sound warning;

		public AssetSounds(AssetManager am) {
			blast = am.get("sounds/sfx_blast.wav", Sound.class);
			click = am.get("sounds/sfx_click.wav", Sound.class);
			explosion = am.get("sounds/sfx_explosion.wav", Sound.class);
			gun1 = am.get("sounds/sfx_gun1.wav", Sound.class);
			gun2 = am.get("sounds/sfx_gun2.wav", Sound.class);
			pickup = am.get("sounds/sfx_pickup.wav", Sound.class);
			warning = am.get("sounds/sfx_warning.wav", Sound.class);
		}
	}

	public class AssetMusic {
		public final Music themeSong;
		public final Music bossSong;

		public AssetMusic(AssetManager am) {
			themeSong = am.get("music/song_theme.mp3", Music.class);
			bossSong = am.get("music/song_boss.mp3", Music.class);
		}
	}

}
