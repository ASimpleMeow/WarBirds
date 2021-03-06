package wit.cgd.warbirds.game.objects;

/**
 * @file        Player
 * @author      Oleksandr Kononov 20071032
 * @assignment  WarBirds
 * @brief       The player for the game
 *
 * @notes       
 */

import wit.cgd.warbirds.game.Assets;
import wit.cgd.warbirds.game.util.AudioManager;
import wit.cgd.warbirds.game.util.Constants;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Player extends AbstractGameObject {

	public static final String TAG = Player.class.getName();
	
	private Animation<TextureRegion> animation;
	private TextureRegion region;
	private float timeShootDelay;
	private float timeWarningDelay;
	
	public boolean doubleBullet;
	private float doubleBulletTimer;
	
	public boolean extraSpeed;
	private float extraSpeedTimer;
	
	public float shield;
	
	public Player (Level level) {
		super(level);
		init();
	}
	
	public void init() {
		dimension.set(1, 1);	
		
		animation = Assets.instance.player.animation;
		setAnimation(animation);

		// Center image on game object
		origin.set(dimension.x / 2, dimension.y / 2);
		timeShootDelay = 0;
		timeWarningDelay = 0;
		
		health = Constants.PlAYER_HEALTH;
		score = 0;
		
		doubleBullet = false;
		doubleBulletTimer = Constants.DOUBLE_BULLET_TIMER;
		
		extraSpeed = false;
		extraSpeedTimer = Constants.EXTRA_SPEED_TIMER;
		
		shield = 0;
		
		state = State.ACTIVE;
	}
	
	@Override
	public void update (float deltaTime) {
		super.update(deltaTime);
		position.x = MathUtils.clamp(position.x,-Constants.VIEWPORT_WIDTH/2+0.5f,Constants.VIEWPORT_WIDTH/2-0.5f);
		position.y = MathUtils.clamp(position.y,level.start+2, level.end-2);
		
		if(isDead()){
			if(state == State.ACTIVE){
				state = State.DEAD;
				animation = Assets.instance.explosionLarge.animation;
				AudioManager.instance.play(Assets.instance.sounds.blast);
			}
			return;
		}
		
		if(doubleBullet) doubleBulletTimer -= deltaTime;
		if(doubleBulletTimer <= 0){
			doubleBullet = false;
			doubleBulletTimer = Constants.DOUBLE_BULLET_TIMER;
		}
		
		if(extraSpeed) extraSpeedTimer -= deltaTime;
		if(extraSpeedTimer <= 0){
			extraSpeed = false;
			extraSpeedTimer = Constants.EXTRA_SPEED_TIMER;
		}
		
		if(shield <= 0) shield = 0;
		
		if(health <= 5 && shield <= 0) {
			timeWarningDelay -= deltaTime;
			soundWarning();
		}
		
		timeShootDelay -= deltaTime;
	}

	public void shoot() {

		if (timeShootDelay>0) return;
		
		// get bullet
		Bullet bullet = level.bulletPool.obtain();
		bullet.reset();
		bullet.position.set(position);
		bullet.isSourcePlayer =true;
		level.bullets.add(bullet);
		timeShootDelay = (doubleBullet)?Constants.PLAYER_SHOOT_DELAY/2 : Constants.PLAYER_SHOOT_DELAY;
		AudioManager.instance.play(Assets.instance.sounds.gun2);
	}
	
	private void soundWarning() {
		if(timeWarningDelay > 0) return;
		AudioManager.instance.play(Assets.instance.sounds.warning);
		timeWarningDelay = Constants.HEALTH_WARNING_DELAY;
	}
	
	public void render (SpriteBatch batch) {
		
		region = animation.getKeyFrame(stateTime, true);

		batch.draw(region.getTexture(), position.x-origin.x, position.y-origin.y, origin.x, origin.y, 
			dimension.x, dimension.y, scale.x, scale.y, rotation, 
			region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(), 
			false, false);
		if(shield <= 0) return;
		region = Assets.instance.shield.region;
		batch.draw(region.getTexture(), position.x-origin.x, position.y-origin.y, origin.x, origin.y, 
				dimension.x, dimension.y, scale.x, scale.y, rotation, 
				region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(), 
				false, false);
	}
	
	public boolean canMove(){
		return !(level.levelStartTimer > 0 || level.levelEndTimer != Constants.LEVEL_END_DELAY);
	}
}
