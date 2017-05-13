package wit.cgd.warbirds.game.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import wit.cgd.warbirds.game.Assets;
import wit.cgd.warbirds.game.objects.AbstractGameObject.State;
import wit.cgd.warbirds.game.util.Constants;

public class Boss extends AbstractGameObject{
	
public static final String TAG = Player.class.getName();
	
	private Animation<TextureRegion> animation;
	private TextureRegion region;
	private float timeShootDelay;
	
	public Boss (Level level) {
		super(level);
		init();
	}
	
	public void init() {
		dimension.set(5, 5);	
		
		velocity.x = 1f;
		animation = Assets.instance.boss.animation;
		setAnimation(animation);

		// Center image on game object
		origin.set(dimension.x / 2, dimension.y / 2);
		timeShootDelay = 0;
		
		health = Constants.BOSS_HEALTH;
		score = 100;
		
		state = State.ACTIVE;
	}
	
	@Override
	public void update (float deltaTime) {
		super.update(deltaTime);
		
		shoot(true);
		shoot(false);
		
		if(position.x >= Constants.VIEWPORT_WIDTH/2 - 1f)
			velocity.x = -1f;
		else if (position.x <= -Constants.VIEWPORT_WIDTH/2 + 1f)
			velocity.x = 1f;
		
		if(position.y < level.end - 2) position.y = level.end - 2;
		
		timeShootDelay -= deltaTime;
	}

	public void shoot(boolean leftSide) {
		if (timeShootDelay>0) return;
		// get bullet
		Bullet bullet = level.bulletPool.obtain();
		bullet.reset();
		bullet.position.y = position.y;
		bullet.position.x = position.x + ((leftSide)? -dimension.x/4 : dimension.x/4);
		bullet.velocity.y = -(3*Constants.BULLET_SPEED)/4;
		bullet.setRegion(Assets.instance.enemyBullet.region);
		level.bullets.add(bullet);
		if(!leftSide) timeShootDelay = Constants.ENEMY_SHOOT_DELAY/2;
	}
	
	public void render (SpriteBatch batch) {
		
		region = animation.getKeyFrame(stateTime, true);

		batch.draw(region.getTexture(), position.x-origin.x, position.y-origin.y, origin.x, origin.y, 
			dimension.x, dimension.y, scale.x, scale.y, rotation, 
			region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(), 
			false, false);
	}
}
