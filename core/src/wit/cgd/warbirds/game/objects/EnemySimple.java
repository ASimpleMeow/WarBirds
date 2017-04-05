package wit.cgd.warbirds.game.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import wit.cgd.warbirds.game.Assets;
import wit.cgd.warbirds.game.objects.AbstractGameObject.State;

public class EnemySimple extends AbstractGameObject{

public static final String TAG = Player.class.getName();
	
	private Animation<TextureRegion> animation;
	private TextureRegion region;
	private float timeShootDelay;
	
	public EnemySimple(Level level) {
		super(level);
		init();
	}
	
	private void init(){
		dimension.set(1,1);
		
		animation = Assets.instance.enemySimple.animationNormal;
		
		// Center image on game object
		origin.set(dimension.x / 2, dimension.y / 2);
		timeShootDelay = 0;
		state = State.ASLEEP;
		velocity.set(0,0.02f);
	}
	
	@Override
	public void update(float deltaTime){
		position.y += -velocity.y;
	}

	@Override
	public void render(SpriteBatch batch) {
		region = animation.getKeyFrame(stateTime, true);

		batch.draw(region.getTexture(), position.x-origin.x, position.y-origin.y, origin.x, origin.y, 
			dimension.x, dimension.y, scale.x, scale.y, rotation, 
			region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(), 
			false, false);
	}


}
