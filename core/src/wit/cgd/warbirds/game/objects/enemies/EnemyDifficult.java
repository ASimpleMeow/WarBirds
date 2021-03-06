package wit.cgd.warbirds.game.objects.enemies;

/**
 * @file        EnemyDifficult
 * @author      Oleksandr Kononov 20071032
 * @assignment  WarBirds
 * @brief       Difficult variation of enemies who spawn behind the player
 * 				move in front of the player, turn around and shoot - Ambush style
 *
 * @notes       
 */

import wit.cgd.warbirds.game.Assets;
import wit.cgd.warbirds.game.objects.Level;
import wit.cgd.warbirds.game.util.Constants;

public class EnemyDifficult extends AbstractEnemy{
	
	private boolean canAttack;
	
	public EnemyDifficult(Level level) {
		super(level);
		init();
	}
	
	public void init() {
		dimension.set(1, 1);
		enemyType = "enemyDifficult";
		score = 10;
		canAttack = false;
		
		// Center image on game object
		origin.set(dimension.x / 2, dimension.y / 2);
		timeShootDelay = 0;
		state = State.ACTIVE;
	}
	
	@Override
	public void update(float deltaTime){
		super.update(deltaTime);
		if(isDead()) return;
		if(position.y > level.player.position.y + 4 || position.y >= level.end - 3) 
			canAttack = true;
		if(!canAttack) return;
		super.turnTowards((level.player.position.y > position.y || level.player.isDead())?
				null : level.player.position);
		super.moveTowards((level.player.position.y > position.y || level.player.isDead())?
				null : level.player.position);
		super.shoot();
	}
	
	@Override
	protected void updateMotionY(){
		if(canAttack) return;
		rotation = 180;
		velocity.y = Constants.SCROLL_SPEED * 4;
	}

	@Override
	public void reset(){
		super.reset();
		health = Constants.ENEMY_DIFFICULT_HEALTH;
		canAttack = false;
		velocity.x = 0;
		animation = Assets.instance.enemyDifficult.animation;
		setAnimation(animation);
	}
}
