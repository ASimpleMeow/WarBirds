package wit.cgd.warbirds.game.objects;

/**
 * @file        AbstractGameObject
 * @author      Oleksandr Kononov 20071032
 * @assignment  WarBirds
 * @brief       Abstract object in the game used as a super class for all
 * 				other game objects
 *
 * @notes       
 */

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class AbstractGameObject {

	public Level 		level;
	
	public Vector2		position;
	public Vector2		dimension;
	public Vector2		origin;
	public Vector2		scale;
	public float		rotation;
	public Vector2		velocity;
	
	public float		health;
	public int 			score;

	public float		stateTime;
	public Animation<TextureRegion>	animation;
	
	public float 		timeToDie;

	public enum State {
		ASLEEP, // not yet in screen area 
		ACTIVE, // in screen area 
		DYING,	// outside screen area but has short time to enter it 
		DEAD	// to be removed from game
	}
	public State state;
	
	public AbstractGameObject(Level level) {
		this.level = level;
		position = new Vector2();
		dimension = new Vector2(1, 1);
		origin = new Vector2();
		scale = new Vector2(1, 1);
		rotation = 0;
		velocity = new Vector2();
		state = State.ASLEEP;
	}

	public void update(float deltaTime) {
		
		if (state == State.ASLEEP) return; 
		
		stateTime += deltaTime;

		// Move to new position
		position.x += velocity.x * deltaTime;
		position.y += velocity.y * deltaTime;
		
		if (state == State.DYING) {
			timeToDie -= deltaTime;
			if (timeToDie<0) state = State.DEAD;
		}
	}

	public void setAnimation(Animation<TextureRegion> animation) {
		this.animation = animation;
		stateTime = 0;
	}

	public abstract void render(SpriteBatch batch);
	
	public void setLevel(Level level){
		this.level = level;
	}
	
	public boolean isDead(){
		return health <= 0;
	}
	
}
