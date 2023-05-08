package ahmed;

/**
 * Game Tick connects elapsed time with FlappyBird and GameTimer class
 * 
 * @author Zuhayr Ahmed
 * @version 20/06/2022
 */
public interface GameTick {

	/**
	 * Code to be executed for each tick of game time
	 * 
	 * @param elapsedTime amount of time that has passed since the previous game
	 *                    tick in seconds
	 */
	public void gameTick(double elapsedTime);
}
