package wit.cgd.warbirds.game.objects.enemies;

import wit.cgd.warbirds.game.Assets;
import wit.cgd.warbirds.game.objects.Level;
import wit.cgd.warbirds.game.objects.Player;
import wit.cgd.warbirds.game.util.Constants;

public class EnemyNormal extends AbstractEnemy{

	public EnemyNormal(Level level) {
		super(level);
		init();
	}
	
	public void init() {
		dimension.set(1, 1);
		enemyType = "enemyNormal";
		score = 5;

		// Center image on game object
		origin.set(dimension.x / 2, dimension.y / 2);
		timeShootDelay = 0;
		state = State.ACTIVE;
	}
	
	@Override
	public void update(float deltaTime){
		super.update(deltaTime);
		if(health <= 0) return;
		super.turnTowards((level.player.position.y > position.y)? null : level.player.position);
		super.moveTowards((level.player.position.y > position.y)? null : level.player.position);
		super.shoot();
	}

	@Override
	public void reset(){
		super.reset();
		health = Constants.ENEMY_NORMAL_HEALTH;
		animation = Assets.instance.enemyNormal.animation;
		setAnimation(animation);
	}
}
