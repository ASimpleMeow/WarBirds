package wit.cgd.warbirds.game.util;

import com.badlogic.gdx.utils.Pool;

import wit.cgd.warbirds.game.objects.AbstractGameObject;
import wit.cgd.warbirds.game.objects.Level;
import wit.cgd.warbirds.game.objects.enemies.AbstractEnemy;
import wit.cgd.warbirds.game.objects.enemies.EnemyDifficult;
import wit.cgd.warbirds.game.objects.enemies.EnemyNormal;
import wit.cgd.warbirds.game.objects.enemies.EnemySimple;

public class EnemyPoolCollection{

	public static final Level level = new Level();

	public final Pool<EnemySimple> enemySimplePool = new Pool<EnemySimple>(){
		@Override
		protected EnemySimple newObject() {
			return new EnemySimple(level);
		}
	};
	
	public final Pool<EnemyNormal> enemyNormalPool = new Pool<EnemyNormal>(){
		@Override
		protected EnemyNormal newObject() {
			return new EnemyNormal(level);
		}
	};
	
	public final Pool<EnemyDifficult> enemyDifficultPool = new Pool<EnemyDifficult>(){
		@Override
		protected EnemyDifficult newObject() {
			return new EnemyDifficult(level);
		}
	};
}
