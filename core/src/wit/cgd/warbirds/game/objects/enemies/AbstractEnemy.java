package wit.cgd.warbirds.game.objects.enemies;

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
import com.badlogic.gdx.utils.Pool.Poolable;

public abstract class AbstractEnemy extends AbstractGameObject implements Poolable{

	public static final String TAG = Player.class.getName();
	
	protected Animation<TextureRegion> animation;
	protected TextureRegion region;
	protected float timeShootDelay;
	public String enemyType;
	
	public AbstractEnemy (Level level, int health) {
		super(level);
		this.health = health;
	}
	
	@Override
	public void update (float deltaTime) {
		super.update(deltaTime);
		timeShootDelay -= deltaTime;
	}

	public void shootAt(AbstractGameObject obj) {
		if (timeShootDelay>0) return;
		// get bullet
		Bullet bullet = level.bulletPool.obtain();
		bullet.reset();
		bullet.position.set(position);
		bullet.rotation = rotation;
		bullet.velocity.y = -Constants.BULLET_SPEED;
		if(obj != null){
			if((obj.position.x - position.x < -1)) bullet.velocity.x = -1 * (Constants.BULLET_SPEED/2);
			else if((obj.position.x - position.x > 1)) bullet.velocity.x = 1 * (Constants.BULLET_SPEED/2);
			if((obj.position.y - position.y < -1)) bullet.velocity.y = -1 * Constants.BULLET_SPEED;
			else if((obj.position.y - position.y > 1)) bullet.velocity.y = 1 * Constants.BULLET_SPEED/2;
			//bullet.velocity.y = ((obj.position.y - position.y < 0)? -1 : 2) * Constants.BULLET_SPEED;
		}
		level.bullets.add(bullet);
		timeShootDelay = Constants.ENEMY_SHOOT_DELAY;
	}
	
	public void turnTowards(AbstractGameObject obj){
		if(obj == null) return;
		float x = position.x-obj.position.x;
		float y = position.y-obj.position.y;
		float rotation = (float)Math.toDegrees(MathUtils.atan2(y, x)) - 90;
		if(this.rotation > rotation && rotation > 0) rotation -= 0.5f;
		else if(this.rotation < rotation && rotation < 0) rotation += 0.5f;
		this.rotation = rotation;
	}
	
	public void moveTowards(AbstractGameObject obj){
		if(obj.position.x-position.x < -1) velocity.x = -0.75f;
		else if (obj.position.x-position.x > 1) velocity.x = 0.75f;
		else velocity.x = 0;
		if(obj.position.y-position.y < -1) velocity.y = -Constants.SCROLL_SPEED;
		else if (obj.position.y-position.y > 1) velocity.y = 2 * Constants.SCROLL_SPEED;
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
		velocity.y = -Constants.SCROLL_SPEED;
		velocity.x = 0;
		rotation = 0;
		state = State.ACTIVE;
	}
}
