package wit.cgd.warbirds.game.objects.enemies;

import wit.cgd.warbirds.game.Assets;
import wit.cgd.warbirds.game.objects.AbstractGameObject;
import wit.cgd.warbirds.game.objects.Bullet;
import wit.cgd.warbirds.game.objects.Level;
import wit.cgd.warbirds.game.objects.Player;
import wit.cgd.warbirds.game.objects.AbstractGameObject.State;
import wit.cgd.warbirds.game.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public abstract class AbstractEnemy extends AbstractGameObject implements Poolable{

	public static final String TAG = Player.class.getName();
	
	protected Animation<TextureRegion> animation;
	protected TextureRegion region;
	protected float timeShootDelay;
	public String enemyType;
	
	public AbstractEnemy (Level level) {
		super(level);
		timeToDie = Constants.ENEMY_DIE_DELAY;
	}
	
	@Override
	public void update (float deltaTime) {
		super.update(deltaTime);
		updateMotionX();
		updateMotionY();
		timeShootDelay -= deltaTime;
		if(health <= 0){
			if(state == State.ACTIVE){
				state = State.DYING;
				animation = (enemyType.equals("boss"))? 
						Assets.instance.explosionLarge.animation : Assets.instance.explosionBig.animation;
				setAnimation(animation);
			}
		}
	}
	
	protected void updateMotionX(){}
	
	protected void updateMotionY(){
		velocity.y = -Constants.SCROLL_SPEED * (level.rng.nextFloat() + 0.25f);
	}

	public void shoot() {
		if (timeShootDelay>0) return;
		// get bullet
		Bullet bullet = level.bulletPool.obtain();
		bullet.reset();
		bullet.position.set(position);
		bullet.velocity.y = -Constants.BULLET_SPEED/2;
		bullet.velocity.rotate(rotation);
		bullet.setRegion(Assets.instance.enemyBullet.region);
		level.bullets.add(bullet);
		timeShootDelay = Constants.ENEMY_SHOOT_DELAY;
	}
	
	public void turnTowards(Vector2 obj){
		if(obj == null){
			if(rotation > 0) rotation -= 2;
			else if (rotation < 0) rotation += 2;
			return;
		}
		float angle = (float) Math.atan2(obj.y - this.position.y, obj.x - this.position.x);
	    angle = (float) (angle * (180 / Math.PI));
	    rotation = angle + 90;
	}
	
	public void moveTowards(Vector2 obj){
		velocity.y = -Constants.SCROLL_SPEED;
		velocity.x = 0;
		if(obj == null) return;
		if(obj.x-position.x < -1) velocity.x = -0.75f;
		else if (obj.x-position.x > 1) velocity.x = 0.75f;
		else velocity.x = 0;
		if(obj.y-position.y < -1) velocity.y = -Constants.SCROLL_SPEED;
		else if (obj.y-position.y > 1) velocity.y = 2 * Constants.SCROLL_SPEED;
		else velocity.y = 0;
	}
	
	
	public void render (SpriteBatch batch) {
		
		region = animation.getKeyFrame(stateTime, true);

		batch.draw(region.getTexture(), position.x-origin.x, position.y-origin.y, origin.x, origin.y, 
			dimension.x, dimension.y, scale.x, scale.y, rotation, 
			region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(), 
			false, false);
	}

	@Override
	public void reset() {
		System.out.println("Enemy Reset");
		rotation = 0;
		state = State.ACTIVE;
		timeToDie = Constants.ENEMY_DIE_DELAY;
	}
}
