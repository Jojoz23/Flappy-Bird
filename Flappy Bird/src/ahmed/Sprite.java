package ahmed;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This group is the sprite class. This class takes a image, and has methods to
 * add/change velocity and position of this image. This also detects
 * intersection with another image.
 * 
 * @author Zuhayr Ahmed
 * @version 20/06/2022
 */
public class Sprite extends Group {

	// Attributes
	@SuppressWarnings("unused")
	private Image image;
	private ImageView imgView = new ImageView();
	private double positionX = 0;
	private double positionY = 0;
	private double velocityX = 0;
	private double velocityY = 0;
	private double width = 0;
	private double height = 0;

	/**
	 * Construct a sprite image without parameters
	 */
	public Sprite() {
		this.getChildren().add(imgView);
	}

	/**
	 * Create a new Sprite based on a source image
	 * 
	 * @param i - the image that the sprite will display
	 */
	public Sprite(Image i) {
		image = i;
		width = (i != null) ? i.getWidth() : 0;
		height = (i != null) ? i.getHeight() : 0;
		imgView.setImage(i);

		this.getChildren().add(imgView);
	}

	/**
	 * Change the image displayed by this sprite
	 * 
	 * @param i - the new image to be displayed
	 */
	public void setImage(Image i) {
		image = i;
		width = (i != null) ? i.getWidth() : 0;
		height = (i != null) ? i.getHeight() : 0;
		imgView.setImage(i);
	}

	/**
	 * @return the x position
	 */
	public double getPositionX() {
		return positionX;
	}

	/**
	 * @param positionX the x position to set
	 */
	public void setPositionX(double positionX) {
		this.positionX = positionX;
		this.setLayoutX(positionX);
	}

	/**
	 * @return the y position
	 */
	public double getPositionY() {
		return positionY;
	}

	/**
	 * @param positionY the y position to set
	 */
	public void setPositionY(double positionY) {
		this.positionY = positionY;
		this.setLayoutY(positionY);
	}

	/**
	 * @return the x velocity
	 */
	public double getVelocityX() {
		return velocityX;
	}

	/**
	 * @param velocityX the x velocity to set
	 */
	public void setVelocityX(double velocityX) {
		this.velocityX = velocityX;
	}

	/**
	 * @return the y velocity
	 */
	public double getVelocityY() {
		return velocityY;
	}

	/**
	 * @param velocityY the y velocity to set
	 */
	public void setVelocityY(double velocityY) {
		this.velocityY = velocityY;
	}

	/**
	 * @return the width
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Move the sprite to a new location
	 * 
	 * @param x - the left side of the sprite
	 * @param y - the top of the sprite
	 */
	public void setPosition(double x, double y) {
		positionX = x;
		positionY = y;
		this.relocate(positionX, positionY);
	}

	/**
	 * Set the speed of the sprite in both directions.
	 * 
	 * @param x - speed in the x direction
	 * @param y - speed in the y direction
	 */
	public void setVelocity(double x, double y) {
		velocityX = x;
		velocityY = y;
	}

	/**
	 * Add to the the speed of the sprite in both directions.
	 * 
	 * @param x - speed increase in x direction
	 * @param y - speed increase in y direction
	 */
	public void addVelocity(double x, double y) {
		velocityX += x;
		velocityY += y;
	}

	/**
	 * Update the position of the sprite based on an an elapsed time in seconds
	 * 
	 * @param elapsedTime the amount of time passed since last update
	 */
	public void update(double elapsedTime) {
		positionX = positionX + velocityX * elapsedTime;
		positionY = positionY + velocityY * elapsedTime;

		this.relocate(positionX, positionY);
	}

	/**
	 * Returns a rectangle that encloses the sprite.
	 * 
	 * @return rectangle enclosing the sprite
	 */
	public Rectangle2D getBoundary() {
		return new Rectangle2D(positionX, positionY, width, height);
	}

	/**
	 * Determines if the given sprite intersects with this sprite
	 * 
	 * @param s - the other sprite to test
	 * @return true if the two sprites are touching, false otherwise.
	 */
	public boolean intersect(Sprite s) {
		return this.getBoundary().intersects(s.getBoundary());
	}
}