package wit.cgd.warbirds.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Rectangle;

import wit.cgd.warbirds.game.objects.AbstractGameObject;
import wit.cgd.warbirds.game.objects.AbstractGameObject.State;
import wit.cgd.warbirds.game.objects.AbstractPowerup;
import wit.cgd.warbirds.game.objects.Bullet;
import wit.cgd.warbirds.game.objects.Level;
import wit.cgd.warbirds.game.objects.enemies.AbstractEnemy;
import wit.cgd.warbirds.game.objects.enemies.EnemyDifficult;
import wit.cgd.warbirds.game.objects.enemies.EnemyNormal;
import wit.cgd.warbirds.game.objects.enemies.EnemySimple;
import wit.cgd.warbirds.game.util.CameraHelper;
import wit.cgd.warbirds.game.util.Constants;
import wit.cgd.warbirds.game.util.EnemyPoolCollection;

public class WorldController extends InputAdapter {

	private static final String	TAG	= WorldController.class.getName();

	private Game				game;
	public CameraHelper			cameraHelper;
	public Level				level;
	public int					levelNumber;
	
	private Rectangle			collisionObject1 = new Rectangle();
	private Rectangle			collisionObject2 = new Rectangle();

	public WorldController(Game game) {
		this.game = game;
		init();
	}

	private void init() {
		Gdx.input.setInputProcessor(this);
		level = EnemyPoolCollection.level;//new Level();
		cameraHelper = new CameraHelper();
		cameraHelper.setTarget(level);
	}


	public void update(float deltaTime) {
		handleDebugInput(deltaTime);
		handleGameInput(deltaTime);
		cameraHelper.update(deltaTime);
		level.update(deltaTime);
		cullObjects();
		testCollisions();
	}

	/**
	 * Remove object because they are out of screen bounds or because they have died
	 */
	public void cullObjects() {
		
		// cull bullets 
		for (int k=level.bullets.size; --k>=0; ) { 	// traverse array backwards !!!
			Bullet it = level.bullets.get(k);
			if (it.state == AbstractGameObject.State.DEAD) {
				level.bullets.removeIndex(k);
				level.bulletPool.free(it);
			} else if (it.state==AbstractGameObject.State.ACTIVE && !isInScreen(it)) {
				it.state = AbstractGameObject.State.DYING;
				it.timeToDie = Constants.BULLET_DIE_DELAY;
			}
		}
		
		// cull enemies
		for(int k=level.enemies.size; --k>=0;){
			AbstractEnemy it = level.enemies.get(k);
			if(it.state == AbstractGameObject.State.DEAD){
				if(it.health <= 0){
					level.player.score += it.score;
					level.spawnPowerup(it.position);		
				}else{	//Enemy was not killed
					if(it.enemyType.equals("enemySimple")) level.enemySimpleLimit++;
					else if(it.enemyType.equals("enemyNormal")) level.enemyNormalLimit++;
					else level.enemyDifficultLimit++;
				}
				level.enemies.removeIndex(k);
				if(it.enemyType.equals("enemySimple")) level.enemyPools.enemySimplePool.free((EnemySimple) it);
				else if(it.enemyType.equals("enemyNormal")) level.enemyPools.enemyNormalPool.free((EnemyNormal) it);
				else level.enemyPools.enemyDifficultPool.free((EnemyDifficult) it);
			} else if(it.state==AbstractGameObject.State.ACTIVE && !isInScreen(it)){
				it.state = AbstractGameObject.State.DYING;
				it.timeToDie = Constants.ENEMY_DIE_DELAY;
			}
		}
		
		//cull powerups
		for(int k=level.powerups.size; --k>=0;){
			AbstractPowerup it = level.powerups.get(k);
			System.out.println("!!!STATE ------"+it.state);
			if (it.state == AbstractGameObject.State.DEAD) {
				level.powerups.removeIndex(k);
				level.powerupsPool.free(it);
			} else if (it.state==AbstractGameObject.State.ACTIVE && !isInScreen(it)) {
				it.state = AbstractGameObject.State.DEAD;
			}
		}
	}

	// Collision detection methods
	private void testCollisions(){
		collisionObject1.set(level.player.position.x, level.player.position.y,level.player.dimension.x,
				level.player.dimension.y);
		
		for(Bullet bullet : level.bullets){
			collisionObject2.set(bullet.position.x, bullet.position.y, bullet.dimension.x,
					bullet.dimension.y);
			if (!collisionObject1.overlaps(collisionObject2)) continue;
			if(bullet.isSourcePlayer) continue;
			checkBulletPlayerCollision(bullet);
		}
		
		for(AbstractPowerup powerup : level.powerups){
			collisionObject2.set(powerup.position.x, powerup.position.y, powerup.dimension.x,
					powerup.dimension.y);
			if (!collisionObject1.overlaps(collisionObject2)) continue;
			checkPlayerPowerCollision(powerup);
		}
		
		for(AbstractEnemy currentEnemy : level.enemies){
			collisionObject1.set(level.player.position.x, level.player.position.y,level.player.dimension.x,
					level.player.dimension.y);
			
			collisionObject2.set(currentEnemy.position.x, currentEnemy.position.y, currentEnemy.dimension.x,
					currentEnemy.dimension.y);
			
			if(collisionObject1.overlaps(collisionObject2)){
				checkEnemyPlayerCollision(currentEnemy);
				continue;
			}
			
			for(Bullet bullet : level.bullets){
				collisionObject1.set(bullet.position.x, bullet.position.y, bullet.dimension.x,
						bullet.dimension.y);
				if (!collisionObject2.overlaps(collisionObject1)) continue;
				if(!bullet.isSourcePlayer) continue;
				checkBulletEnemyCollision(currentEnemy, bullet);
			}
			
			for(int i = 0; i < level.enemies.size; ++i){
				if(currentEnemy.equals(level.enemies.get(i))) continue;
				collisionObject1.set(level.enemies.get(i).position.x, level.enemies.get(i).position.y,
						level.enemies.get(i).dimension.x, level.enemies.get(i).dimension.y);
				if(!collisionObject2.overlaps(collisionObject1)) continue;
				checkEnemyEnemyCollision(currentEnemy, level.enemies.get(i));
			}
		}
	}
	
	private void checkBulletEnemyCollision(AbstractEnemy enemy, Bullet bullet) {
		enemy.health -= Constants.BULLET_DAMAGE;
		bullet.state = AbstractGameObject.State.DYING;
	}
	
	private void checkEnemyPlayerCollision(AbstractEnemy enemy){
		boolean hitLeftSide = level.player.position.x < (enemy.position.x + enemy.dimension.x/2);
		if(hitLeftSide) level.player.position.x -= 0.1f;
		else level.player.position.x += 0.1f;
		
		level.player.health -= 0.1f;
	}
	
	private void checkBulletPlayerCollision(Bullet bullet) {
		if(level.player.shield > 0) level.player.shield -= Constants.BULLET_DAMAGE;
		else level.player.health -= Constants.BULLET_DAMAGE;
		bullet.state = AbstractGameObject.State.DEAD;
	}
	
	private void checkEnemyEnemyCollision(AbstractEnemy current, AbstractEnemy other) {
		boolean hitLeftSide = current.position.x < (other.position.x + other.dimension.x/2);
		if(hitLeftSide) current.position.x = other.position.x - other.dimension.x;
		else current.position.x = other.position.x + other.dimension.x;
	}
	
	private void checkPlayerPowerCollision(AbstractPowerup power){
		power.executePowerup(level.player);
		power.state = AbstractGameObject.State.DEAD;
	}
	

	public boolean isInScreen(AbstractGameObject obj) {
		return ((obj.position.x>=-Constants.VIEWPORT_WIDTH/2 && obj.position.x<=Constants.VIEWPORT_WIDTH/2)
				&&
				(obj.position.y>=level.start && obj.position.y<=level.end));
	}
	
	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
			Gdx.app.exit();
		}
		return false;
	}

	private void handleGameInput(float deltaTime) {

		if (Gdx.input.isKeyPressed(Keys.A)) {
			level.player.velocity.x = (float) (-Constants.PLANE_H_SPEED * ((level.player.extraSpeed)? 1.5 : 1));
		} else if (Gdx.input.isKeyPressed(Keys.D)) {
			level.player.velocity.x = (float) (Constants.PLANE_H_SPEED * ((level.player.extraSpeed)? 1.5 : 1));
		} else {
			level.player.velocity.x = 0;
		}
		if (Gdx.input.isKeyPressed(Keys.W)) {
			level.player.velocity.y = (float) (Constants.PLANE_MAX_V_SPEED * ((level.player.extraSpeed)? 1.5 : 1));
		} else if (Gdx.input.isKeyPressed(Keys.S)) {
			level.player.velocity.y = (float) (Constants.PLANE_MIN_V_SPEED * ((level.player.extraSpeed)? 1.5 : 1));
		} else {
			level.player.velocity.y = Constants.SCROLL_SPEED;
		}
		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			level.player.shoot();
		}
	}

	private void handleDebugInput(float deltaTime) {
		if (Gdx.app.getType() != ApplicationType.Desktop) return;

		if (Gdx.input.isKeyPressed(Keys.ENTER)) {
			cameraHelper.setTarget(!cameraHelper.hasTarget() ? level : null);
		}

		if (!cameraHelper.hasTarget()) {
			// Camera Controls (move)
			float camMoveSpeed = 5 * deltaTime;
			float camMoveSpeedAccelerationFactor = 5;
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camMoveSpeed *= camMoveSpeedAccelerationFactor;
			if (Gdx.input.isKeyPressed(Keys.LEFT)) moveCamera(-camMoveSpeed, 0);
			if (Gdx.input.isKeyPressed(Keys.RIGHT)) moveCamera(camMoveSpeed, 0);
			if (Gdx.input.isKeyPressed(Keys.UP)) moveCamera(0, camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.DOWN)) moveCamera(0, -camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) cameraHelper.reset();
		}

		// Camera Controls (zoom)
		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camZoomSpeed *= camZoomSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Keys.COMMA)) cameraHelper.addZoom(camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.PERIOD)) cameraHelper.addZoom(-camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.SLASH)) cameraHelper.setZoom(1);
	}

	private void moveCamera(float x, float y) {
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		// TODO - implement touch pad type controls
		return true;
	}

}
