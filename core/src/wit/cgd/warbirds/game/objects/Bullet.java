package wit.cgd.warbirds.game.objects;

import wit.cgd.warbirds.game.Assets;
import wit.cgd.warbirds.game.util.AudioManager;
import wit.cgd.warbirds.game.util.Constants;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool.Poolable;


public class Bullet extends AbstractGameObject implements Poolable {

	public static final String TAG = Bullet.class.getName();
	
	public boolean isSourcePlayer;	
	
	private TextureRegion region;
	
	public Bullet(Level level) {
		super(level);
		init();
	}
	
	public void init() {
		dimension.set(0.5f, 0.5f);
		isSourcePlayer = false;
		
		region = Assets.instance.doubleBullet.region;

		// Center image on game object
		origin.set(dimension.x / 2, dimension.y / 2);
		velocity.y = Constants.BULLET_SPEED;
	}
	
	public void setRegion(TextureRegion region){
		this.region = region;
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(region.getTexture(), position.x-origin.x, position.y-origin.y, origin.x, origin.y, 
				dimension.x, dimension.y, scale.x, scale.y, rotation, 
				region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(), 
				false, false);		
	}

	@Override
	public void reset() {
		System.out.println("Bullet Reset");
		velocity.y = Constants.BULLET_SPEED;
		region = Assets.instance.doubleBullet.region;
		velocity.x = 0;
		rotation = 0;
		isSourcePlayer = false;
		state = State.ACTIVE;
	}
}
