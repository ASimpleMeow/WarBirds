package wit.cgd.warbirds.game.objects;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.badlogic.gdx.utils.Pool;

import wit.cgd.warbirds.game.Assets;
import wit.cgd.warbirds.game.objects.AbstractGameObject.State;
import wit.cgd.warbirds.game.objects.enemies.AbstractEnemy;
import wit.cgd.warbirds.game.objects.enemies.EnemyDifficult;
import wit.cgd.warbirds.game.objects.enemies.EnemyNormal;
import wit.cgd.warbirds.game.objects.enemies.EnemySimple;
import wit.cgd.warbirds.game.util.Constants;
import wit.cgd.warbirds.game.util.EnemyPoolCollection;

public class Level extends AbstractGameObject {

	public static final String	TAG		= Level.class.getName();

	public Player				player	= null;
	public Boss					boss = null;
	public LevelDecoration		levelDecoration;
	public Random				rng;
	public Array<AbstractEnemy>	enemies;
	public float				start;
	public float				end;
	public int 					levelNumber = 1;
	
	//Enemy spawn limits - the minimum enemies to be killed before ceasing to spawn them per level
	public int					enemySimpleLimit;
	public int					enemyNormalLimit;
	public int					enemyDifficultLimit;
	
	private String[] islands = {"islandBig","islandSmall","islandTiny"};
	private float islandTimer;
	public float levelStartTimer;
	public float levelBossTimer;
	public boolean startBoss;
	
	private final float ISLAND_DELAY_TIME = 1.2f;
	
	public final Array<Bullet> bullets = new Array<Bullet>();
	
	public final Pool<Bullet> bulletPool = new Pool<Bullet>() {
    	@Override
    	protected Bullet newObject() {
    		return new Bullet(level);
    	}
    };
    
    public final Array<AbstractPowerup> powerups = new Array<AbstractPowerup>();
    
    public final Pool<AbstractPowerup> powerupsPool = new Pool<AbstractPowerup>() {
    	@Override
    	protected AbstractPowerup newObject() {
    		return new AbstractPowerup(level);
    	}
    };
    
    public final EnemyPoolCollection enemyPools = new EnemyPoolCollection();
    
	/**
	 * Simple class to store generic object in level.
	 */
	public static class LevelObject {
		String	name;	//Enemy type name
		int		limit;	//Number of enemies that are allowed in this level
	}

	/**
	 * Collection of all objects in level
	 */
	public static class LevelMap {
		long					seed;
		ArrayList<LevelObject>	enemies;
	}

	public Level() {
		super(null);
	}

	private void init() {
		// player
		player = new Player(this);
		player.position.set(0, 0);

		enemies = new Array<AbstractEnemy>();
						
		levelDecoration = new LevelDecoration(this);
		
		position.set(0, 0);
		velocity.y = Constants.SCROLL_SPEED;
		state = State.ACTIVE;
		levelStartTimer = Constants.LEVEL_START_DELAY;
		levelBossTimer = Constants.LEVEL_BOSS_DELAY;
		startBoss = false;
	}
	
	public void loadLevel(int levelNumber){
		init();
		
		this.levelNumber = levelNumber;
		// read and parse level map (form a json file)
		String map = Gdx.files.internal(String.format("levels/level-%d.json",levelNumber)).readString();

		Json json = new Json();
		json.setElementType(LevelMap.class, "enemies", LevelObject.class);
		LevelMap data = new LevelMap();
		data = json.fromJson(LevelMap.class, map);
		Gdx.app.log(TAG, "Level Seed...");
		rng = new Random(data.seed);
				
		Gdx.app.log(TAG, "Enemies . . . ");
		for(Object e : data.enemies){
			LevelObject p = (LevelObject) e;
			Gdx.app.log(TAG, "type = " + p.name + "\tlimit = " + p.limit);
			if(p.name.equals("enemySimple")) enemySimpleLimit = p.limit;
			else if(p.name.equals("enemyNormal")) enemyNormalLimit = p.limit;
			else enemyDifficultLimit = p.limit;
		}
	}

	public void update(float deltaTime) {

		super.update(deltaTime);
		
		// limits for rendering
		start = position.y - scale.y * Constants.VIEWPORT_HEIGHT;
		end = position.y + scale.y * Constants.VIEWPORT_HEIGHT;

		player.update(deltaTime);
		
		if(enemies.size+enemySimpleLimit+enemyNormalLimit+enemyDifficultLimit == 0) startBoss = true;
		
		if(startBoss) levelBossTimer -= deltaTime;
		
		if(levelBossTimer <= 0 && boss == null){
			boss = new Boss(this);
			boss.position.set(0, end);
		}
		
		if(levelStartTimer <= 0) spawnEnemy();
		else levelStartTimer -= deltaTime;
		
		islandTimer -= deltaTime;
		spawnIslands();
		
		if(boss != null) boss.update(deltaTime);
		
		for(AbstractEnemy enemy : enemies)
			enemy.update(deltaTime);
		
		for (Bullet bullet: bullets)
			bullet.update(deltaTime);
	}

	public void render(SpriteBatch batch) {

		levelDecoration.render(batch);
		player.render(batch);
		
		if(boss != null) boss.render(batch);
		
		for(AbstractEnemy enemies : enemies)
			enemies.render(batch);
		
		for (Bullet bullet: bullets)
			bullet.render(batch);
		
		for (AbstractPowerup powerup : powerups)
			powerup.render(batch);
	}
	
	public void spawnPowerup(Vector2 spawnPosition){
		if(rng.nextDouble() > 0.3) return;
		AbstractPowerup powerup = powerupsPool.obtain();
		powerup.setPower(rng.nextInt(4));
		powerup.position.set(spawnPosition);
		powerups.add(powerup);
	}
	
	private void spawnEnemy(){
		while(enemies.size < 3){
			float x = rng.nextInt(((int)Constants.VIEWPORT_WIDTH))*2 - Constants.VIEWPORT_WIDTH - 0.5f;
			x = MathUtils.clamp(x,-Constants.VIEWPORT_WIDTH/2+1f,Constants.VIEWPORT_WIDTH/2-1f);
			float y = end -1  + rng.nextInt(2);
			AbstractEnemy newEnemy;
			if(enemyDifficultLimit > 0 && rng.nextDouble() < 0.1){
				newEnemy = enemyPools.enemyDifficultPool.obtain();
				enemyDifficultLimit--;
				y = start + 1  - rng.nextInt(2);
				x = (rng.nextBoolean())? -Constants.VIEWPORT_WIDTH/2 + 1f : Constants.VIEWPORT_WIDTH/2 - 1f;
			}else if(enemyNormalLimit > 0 && rng.nextDouble() < 0.4){
				newEnemy = enemyPools.enemyNormalPool.obtain();
				enemyNormalLimit--;
			}else if(enemySimpleLimit > 0){
				newEnemy = enemyPools.enemySimplePool.obtain();
				enemySimpleLimit--;
			}else break;
			if(newEnemy.level == null) newEnemy.resetLevel(level);
			newEnemy.reset();
			newEnemy.position.set(x,y);
			enemies.add(newEnemy);
		}
	}
	
	private void spawnIslands(){
		if(rng.nextDouble() < 0.5) return;
		if(islandTimer > 0) return;
		float x = rng.nextInt(((int)Constants.VIEWPORT_WIDTH))*2 - Constants.VIEWPORT_WIDTH;
		x = MathUtils.clamp(x,-Constants.VIEWPORT_WIDTH/2+0.5f,Constants.VIEWPORT_WIDTH/2-0.5f);
		float y = end;
		float scale = rng.nextFloat() + 0.5f;
		levelDecoration.add(islands[rng.nextInt(3)], x/2, y, scale, rng.nextInt(360));
		islandTimer = ISLAND_DELAY_TIME;
	}

}
