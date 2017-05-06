package wit.cgd.warbirds.game.objects.enemies;

import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import wit.cgd.warbirds.game.Assets;
import wit.cgd.warbirds.game.objects.Level;
import wit.cgd.warbirds.game.objects.Player;
import wit.cgd.warbirds.game.util.Constants;
import wit.cgd.warbirds.game.objects.AbstractGameObject;
import wit.cgd.warbirds.game.objects.AbstractGameObject.State;

public class EnemySimple extends AbstractEnemy{

	public EnemySimple(Level level, int health) {
		super(level, health);
		init();
	}
	
	public void init() {
		dimension.set(1, 1);
		enemyType = "enemySimple";
		
		animation = Assets.instance.enemySimple.animationNormal;
		setAnimation(animation);

		// Center image on game object
		origin.set(dimension.x / 2, dimension.y / 2);
		timeShootDelay = 0;
		state = State.ACTIVE;
	}
	
	public void update(float deltaTime, Player player){
		super.update(deltaTime);
		//if (player.position.y > position.y) return;
		super.turnTowards(player);
		super.moveTowards(player);
		super.shootAt(player);
		//position.x = level.player.position.x;
		//position.y = level.end - 3;
		//shoot();
	}

	@Override
	public void reset(){
		super.reset();
		health = Constants.ENEMY_SIMPLE_HEALTH;
	}
}
