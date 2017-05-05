package wit.cgd.warbirds.game.util;

import com.badlogic.gdx.utils.Pool;

import wit.cgd.warbirds.game.objects.AbstractGameObject;
import wit.cgd.warbirds.game.objects.Level;
import wit.cgd.warbirds.game.objects.enemies.AbstractEnemy;
import wit.cgd.warbirds.game.objects.enemies.EnemySimple;

public class EnemyPoolCollection{

	public static final Level level = new Level();

	public final Pool<EnemySimple> enemySimplePool = new Pool<EnemySimple>(){
		@Override
		protected EnemySimple newObject() {
			return new EnemySimple(level, Constants.ENEMY_SIMPLE_HEALTH);
		}
	};
}
