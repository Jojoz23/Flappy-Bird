/**
 * 
 */
package ahmed;

import javafx.scene.image.Image;

/**
 * Extends the sprite class, but has methods or a few more methods and objects
 * unique to the flappy bird
 * 
 * @author Zuhayr Ahmed
 * @version 20/06/2022
 */
public class Flappy extends Sprite {

	// Bottom of the screen/playing area
	private final static int screenBottom = 583;

	// Keeps track if bird is dead or not
	private boolean isDead = false;

	/**
	 * Constructor without parameters
	 */
	public Flappy() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Takes the bird image, and creates a sprite with it
	 * 
	 * @param i the bird image
	 */
	public Flappy(Image i) {
		super(i);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Makes the bird jump
	 */
	public void jump() {

		// When bird isnt dead
		if (!isDead()) {

			// Make the bird jump
			setVelocityY(-300);
			// update(time);
		}
	}

	/**
	 * Makes the bird fall based on elapsed time
	 * 
	 * @param elapsedTime time passed since last update
	 */
	public void fall(double elapsedTime) {

		// If bird is above bottom of screen
		if (getPositionY() < screenBottom) {

			// Make bird fall
			addVelocity(0, 20);
		}

		// Otherwise dont do anything
		else {
			setVelocityY(0);
			addVelocity(0, 0);
		}

		// Update the bird based on elapsed time
		update(elapsedTime);

		// If bird is below playing area, set it's position to above bottom screen
		if (getPositionY() > screenBottom) {
			setPositionY(screenBottom);
		}
	}

	/**
	 * Check if bird is dead
	 * 
	 * @return true if isDead is true, otherwise false
	 */
	public boolean isDead() {
		return isDead;
	}

	/**
	 * Kills the bird, by turning the isDead true, moving the bird along with screen
	 */
	public void kill(int screenVelocity) {
		isDead = true;
		setVelocityX(screenVelocity);

		// To stop the bird from forever moving off the screen
		if (getPositionX() + getWidth() * 2 <= 0) {
			setVelocityX(0);
		}
	}

	/**
	 * Resets bird's sprite to how it started
	 */
	public void revive() {
		isDead = false;
		setVelocityX(0);
	}

}
