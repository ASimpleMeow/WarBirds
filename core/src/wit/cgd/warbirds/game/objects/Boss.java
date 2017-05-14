package wit.cgd.warbirds.game.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import wit.cgd.warbirds.game.Assets;
import wit.cgd.warbirds.game.objects.AbstractGameObject.State;
import wit.cgd.warbirds.game.objects.enemies.AbstractEnemy;
import wit.cgd.warbirds.game.util.AudioManager;
import wit.cgd.warbirds.game.util.Constants;

public class Boss extends AbstractEnemy{
	
public static final String TAG = Player.class.getName();
	
	public Boss (Level level) {
		super(level);
		init();
	}
	
	public void init() {
		health = Constants.BOSS_HEALTH;
		dimension.set(4, 4);
		enemyType = "boss";
		velocity.x = 1f;
		score = 100;
		
		animation = Assets.instance.boss.animation;
		setAnimation(animation);
		
		// Center image on game object
		origin.set(dimension.x / 2, dimension.y / 2);
		timeShootDelay = 0;
		state = State.ACTIVE;
	}
	
	@Override
	public void update (float deltaTime) {
		super.update(deltaTime);
		if(isDead()) return;
		shoot();
		if(level.rng.nextDouble() < 0.01) level.spawnPowerup(position);
		timeShootDelay -= deltaTime;
	}
	
	@Override
	protected void updateMotionX(){
		if(position.x >= Constants.VIEWPORT_WIDTH/2 - 1f)
			velocity.x = -1f;
		else if (position.x <= -Constants.VIEWPORT_WIDTH/2 + 1f)
			velocity.x = 1f;
	}
	
	@Override
	protected void updateMotionY(){
		if(position.y < level.end - 2) position.y = level.end - 2;
	}

	@Override
	public void shoot() {
		if (timeShootDelay>0) return;
		// get bullet
		for(int i = 3; --i>0;){
			Bullet bullet = level.bulletPool.obtain();
			bullet.reset();
			bullet.position.y = position.y;
			bullet.position.x = position.x + ((i == 2)?-dimension.x/4 : dimension.x/4);
			bullet.velocity.y = -(3*Constants.BULLET_SPEED)/4;
			bullet.setRegion(Assets.instance.enemyBullet.region);
			level.bullets.add(bullet);
		}
		AudioManager.instance.play(Assets.instance.sounds.gun1);
		timeShootDelay = Constants.ENEMY_SHOOT_DELAY/2;
	}
}
