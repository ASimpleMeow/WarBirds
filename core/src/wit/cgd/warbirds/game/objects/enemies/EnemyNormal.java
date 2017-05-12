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
	
	public void update(float deltaTime, Player player){
		super.update(deltaTime);
		if(health <= 0) return;
		super.turnTowards((player.position.y > position.y)? null : player.position);
		super.moveTowards((player.position.y > position.y)? null : player.position);
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
