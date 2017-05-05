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
import wit.cgd.warbirds.game.objects.enemies.EnemySimple;
import wit.cgd.warbirds.game.util.Constants;
import wit.cgd.warbirds.game.util.EnemyPoolCollection;

public class Level extends AbstractGameObject {

	public static final String	TAG		= Level.class.getName();

	public Player				player	= null;
	public LevelDecoration		levelDecoration;
	public Random				randomGenerator;
	public Array<AbstractEnemy>	enemies;
	public float				start;
	public float				end;
	public int					enemySimpleLimit;
	
	private String[] islands = {"islandBig","islandSmall","islandTiny"};
	private float islandTimer;
	private final float ISLAND_DELAY_TIME = 1f;
	
	public final Array<Bullet> bullets = new Array<Bullet>();
	
	public final Pool<Bullet> bulletPool = new Pool<Bullet>() {
    	@Override
    	protected Bullet newObject() {
    		return new Bullet(level);
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
		String					name;
		float					length;
	}

	public Level() {
		super(null);
		init();

	}

	private void init() {
		// player
		player = new Player(this);
		player.position.set(0, 0);

		enemies = new Array<AbstractEnemy>();
		
		levelDecoration = new LevelDecoration(this);

		// read and parse level map (form a json file)
		String map = Gdx.files.internal("levels/level-01.json").readString();

		Json json = new Json();
		json.setElementType(LevelMap.class, "enemies", LevelObject.class);
		LevelMap data = new LevelMap();
		data = json.fromJson(LevelMap.class, map);
		Gdx.app.log(TAG, "Level Seed...");
		randomGenerator = new Random(data.seed);
		
		Gdx.app.log(TAG, "Enemies . . . ");
		for(Object e : data.enemies){
			LevelObject p = (LevelObject) e;
			Gdx.app.log(TAG, "type = " + p.name + "\tlimit = " + p.limit);
			if(p.name.equals("enemySimple")) enemySimpleLimit = p.limit;
		}
		/*
		for (Object e : data.enemies) {
			LevelObject p = (LevelObject) e;
			Gdx.app.log(TAG, "type = " + p.name + "\tx = " + p.x + "\ty =" + p.y);
			AbstractEnemy newEnemy = enemyPool.obtain();
			if(p.name.equals("enemySimple"))
				newEnemy = new EnemySimple(this);
			newEnemy.position.set(p.x,p.y);
			enemies.add(newEnemy);
			// TODO add enemies
		}*/

		position.set(0, 0);
		velocity.y = Constants.SCROLL_SPEED;
		state = State.ACTIVE;
	}

	public void update(float deltaTime) {

		super.update(deltaTime);
		
		// limits for rendering
		start = position.y - scale.y * Constants.VIEWPORT_HEIGHT;
		end = position.y + scale.y * Constants.VIEWPORT_HEIGHT;

		player.update(deltaTime);
		
		while(enemySimpleLimit > 0 && enemies.size < 3){
			float x = (randomGenerator.nextInt(((int)Constants.VIEWPORT_WIDTH*2) + 1) - Constants.VIEWPORT_WIDTH);
			float y = player.position.y;
			EnemySimple newEnemy = enemyPools.enemySimplePool.obtain();
			if(newEnemy.level == null) newEnemy.resetLevel(level);
			newEnemy.reset();
			newEnemy.position.set(x,y);
			enemies.add(newEnemy);
			--enemySimpleLimit;
			System.out.println("ENEMY ADDED!!! LIMIT : "+enemySimpleLimit+"   SIZE : "+enemies.size);
		}
		
		for(AbstractEnemy enemy : enemies){
			if(enemy.enemyType.equals("enemySimple")) ((EnemySimple) enemy).update(deltaTime, player);
		}
		
		for (Bullet bullet: bullets)
			bullet.update(deltaTime);
		
		islandTimer -= deltaTime;
		
		if(randomGenerator.nextDouble() < 0.5) return;
		if(islandTimer > 0) return;
		float x = (randomGenerator.nextInt(((int)Constants.VIEWPORT_WIDTH*2) + 1) - Constants.VIEWPORT_WIDTH);
		float y = end;
		float scaleX = randomGenerator.nextFloat() + 1f;
		float scaleY = randomGenerator.nextFloat() + 1f;
		levelDecoration.add(islands[randomGenerator.nextInt(3)], x/2, y, scaleX, scaleY, randomGenerator.nextInt(360));
		islandTimer = ISLAND_DELAY_TIME;
	}

	public void render(SpriteBatch batch) {

		levelDecoration.render(batch);
		player.render(batch);
		
		for(AbstractEnemy enemies : enemies)
			enemies.render(batch);
		
		for (Bullet bullet: bullets)
			bullet.render(batch);
		
		//System.out.println("Bullets " + bullets.size);
		System.out.println("LIMIT : "+enemySimpleLimit);
	}

}
