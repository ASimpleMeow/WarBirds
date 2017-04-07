package wit.cgd.warbirds.game.objects.enemies;

import wit.cgd.warbirds.game.Assets;
import wit.cgd.warbirds.game.objects.AbstractGameObject;
import wit.cgd.warbirds.game.objects.Bullet;
import wit.cgd.warbirds.game.objects.Level;
import wit.cgd.warbirds.game.objects.Player;
import wit.cgd.warbirds.game.objects.AbstractGameObject.State;
import wit.cgd.warbirds.game.util.Constants;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool.Poolable;

public class AbstractEnemy extends AbstractGameObject implements Poolable{

	public static final String TAG = Player.class.getName();
	
	protected Animation<TextureRegion> animation;
	protected TextureRegion region;
	protected float timeShootDelay;
	public String enemyType;
	
	public AbstractEnemy (Level level) {
		super(level);
	}
	
	@Override
	public void update (float deltaTime) {
		super.update(deltaTime);
		timeShootDelay -= deltaTime;
	}

	public void shoot() {

		if (timeShootDelay>0) return;
		
		// get bullet
		Bullet bullet = level.bulletPool.obtain();
		bullet.reset();
		bullet.position.set(position);
		bullet.velocity.y = -Constants.BULLET_SPEED;
		
		level.bullets.add(bullet);
		timeShootDelay = Constants.PLAYER_SHOOT_DELAY;

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
		System.out.println("sdfsd");
		state = State.ACTIVE;
	}
	
}
