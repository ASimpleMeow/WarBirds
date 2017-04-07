package wit.cgd.warbirds.game.objects.enemies;

import java.util.concurrent.ThreadLocalRandom;

import wit.cgd.warbirds.game.Assets;
import wit.cgd.warbirds.game.objects.Level;
import wit.cgd.warbirds.game.objects.AbstractGameObject.State;

public class EnemySimple extends AbstractEnemy{

	public EnemySimple(Level level) {
		super(level);
		init();
	}
	
	public void init() {
		dimension.set(1, 1);
		
		animation = Assets.instance.enemySimple.animationNormal;
		setAnimation(animation);

		// Center image on game object
		origin.set(dimension.x / 2, dimension.y / 2);
		timeShootDelay = 0;
		state = State.ACTIVE;
	}
	
	@Override
	public void update(float deltaTime){
		super.update(deltaTime);
		position.x = level.player.position.x;
		//position.y = level.end - 3;
		shoot();
	}

}
