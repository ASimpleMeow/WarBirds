package wit.cgd.warbirds.game.objects.enemies;

import wit.cgd.warbirds.game.Assets;
import wit.cgd.warbirds.game.objects.Level;
import wit.cgd.warbirds.game.objects.Player;
import wit.cgd.warbirds.game.util.Constants;

public class EnemySimple extends AbstractEnemy{
	
	public EnemySimple(Level level) {
		super(level);
		init();
	}
	
	public void init() {
		dimension.set(1, 1);
		
		enemyType = "enemySimple";
		velocity.x = (level.rng.nextInt(2) + 1) * ((level.rng.nextBoolean())? -1 : 1);
		score = 2;
		
		// Center image on game object
		origin.set(dimension.x / 2, dimension.y / 2);
		timeShootDelay = 0;
		state = State.ACTIVE;
	}
	
	@Override
	public void update(float deltaTime){
		super.update(deltaTime);
		if(health <= 0) return;
		super.shoot();
	}
	
	@Override
	protected void updateMotionX(){
		if(position.x >= Constants.VIEWPORT_WIDTH/2 - 1f)
			velocity.x = -2f;
		else if (position.x <= -Constants.VIEWPORT_WIDTH/2 + 1f)
			velocity.x = 2f;
	}

	@Override
	public void reset(){
		super.reset();
		health = Constants.ENEMY_SIMPLE_HEALTH;
		velocity.x = (level.rng.nextInt(2) + 1) * ((level.rng.nextBoolean())? -1 : 1);
		animation = Assets.instance.enemySimple.animation;
		setAnimation(animation);
	}
}
