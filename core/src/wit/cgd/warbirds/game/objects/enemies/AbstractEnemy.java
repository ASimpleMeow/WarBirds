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
		timeShootDelay -= deltaTime;		
	}

	public void shootAt(AbstractGameObject obj) {
		if (timeShootDelay>0) return;
		// get bullet
		Bullet bullet = level.bulletPool.obtain();
		bullet.reset();
		bullet.position.set(position);
		bullet.rotation = rotation;
		bullet.velocity.y = -Constants.BULLET_SPEED/2;
		bullet.setRegion(Assets.instance.enemyBullet.region);
		if(obj != null) bullet.velocity.rotate(rotation);
		level.bullets.add(bullet);
		timeShootDelay = Constants.ENEMY_SHOOT_DELAY;
	}
	
	public void turnTowards(AbstractGameObject obj){
		if(obj == null){
			if(rotation > 0) rotation -= 2;
			else if (rotation < 0) rotation += 2;
			return;
		}
		float angle = (float) Math.atan2(obj.position.y - this.position.y, obj.position.x - this.position.x);
	    angle = (float) (angle * (180 / Math.PI));
	    rotation = angle + 90;
	}
	
	public void moveTowards(AbstractGameObject obj){
		velocity.y = -Constants.SCROLL_SPEED;
		velocity.x = 0;
		if(obj == null) return;
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
		timeToDie = Constants.ENEMY_DIE_DELAY;
	}
}
