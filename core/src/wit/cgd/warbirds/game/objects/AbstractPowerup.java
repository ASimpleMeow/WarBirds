package wit.cgd.warbirds.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool.Poolable;

import wit.cgd.warbirds.game.Assets;
import wit.cgd.warbirds.game.util.Constants;

public class AbstractPowerup extends AbstractGameObject implements Poolable{
	
	public int powerType;
	private TextureRegion region;
	
	public AbstractPowerup(Level level){
		super(level);
		init();
	}
	
	public void init() {
		dimension.set(0.5f, 0.5f);
		powerType = 0;
		
		region = Assets.instance.powerups.get(powerType).region;

		// Center image on game object
		origin.set(dimension.x / 2, dimension.y / 2);
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(region.getTexture(), position.x-origin.x, position.y-origin.y, origin.x, origin.y, 
				dimension.x, dimension.y, scale.x, scale.y, rotation, 
				region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(), 
				false, false);	
	}
	
	public void setPower(int powerType){
		this.powerType = powerType;
		region = Assets.instance.powerups.get(powerType).region;
	}
	
	public void executePowerup(Player player){
		switch(powerType){
		case 0:						//Speed up powerup
			player.extraSpeed = true;
			break;
		case 1:						//Double bullet powerup
			player.doubleBullet = true;
			break;
		case 2:						//Health powerup
			player.health += ((player.health+5) <= Constants.PlAYER_HEALTH)?
					5 : (Constants.PlAYER_HEALTH - player.health);
			break;
		case 3:
			player.shield = 6;		//Shield powerup
			break;
		}
		player.score += 5;
		state = State.DEAD;
	}
	
	@Override
	public void reset() {
		powerType = 0;
		position.set(0, 0);
		region = Assets.instance.powerups.get(powerType).region;
		state = State.ACTIVE;
	}
}
