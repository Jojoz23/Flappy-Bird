package ahmed;

import javafx.scene.image.Image;

/**
 * Extends the sprite class, but has methods or a few more methods and objects
 * unique to pipes
 * 
 * @author Zuhayr Ahmed
 * @version 20/06/2022
 */
public class Pipe extends Sprite {

	// Constants for pipe parts
	public static final int topPipe = 0;
	public static final int bottomPipe = 1;

	// Array holding both pipe part images
	public static final Image[] pipe = { new Image("file:Images/ahmed/top pipe.png"),
			new Image("file:Images/ahmed/bottom pipe.png") };

	// Keeps track if pipe is dead or not
	private boolean isDead = false;

	// Track the amount of time bird is in pipe
	private int timesCounted = 0;

	/**
	 * Sets sprite's image to top ot bottom based on the parameter passed
	 * 
	 * @param ifTopPipe top pipe or not
	 */
	public Pipe(boolean ifTopPipe) {

		// Calling sprite constructor to set the whole sprite up properly
		super();

		// If parameter wants top pipe
		if (ifTopPipe == true) {

			// Set sprites image to top pipe
			super.setImage(pipe[topPipe]);
		}

		// If not
		else {

			// Set sprites image to bottom pipe
			super.setImage(pipe[bottomPipe]);

		}

		// Pipe doesnt have bird passing through it yet, no times counted
		timesCounted = 0;
	}

	/**
	 * Kills the pipe, by turning the isDead true
	 */
	public void kill() {
		isDead = true;
	}

	/**
	 * Check if pipe is dead
	 * 
	 * @return true if isDead is true, otherwise false
	 */
	public boolean isDead() {
		return isDead;
	}

	/**
	 * Checks if bird passes the pipe
	 * 
	 * @param bird the bird
	 * @return true, if bird passed the pipe once
	 */
	public boolean pass(Flappy bird) {

		// Increment times counted when bird is inside pipe
		if (getPositionX() < bird.getPositionX()
				&& getPositionX() + getWidth() > bird.getPositionX() + bird.getWidth()) {
			timesCounted++;
		}

		// When bird passed the pipe once, return true
		return timesCounted == 1;
	}

}
