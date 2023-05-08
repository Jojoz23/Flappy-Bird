 /**
 * 
 */
package ahmed;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This program allows user to play a flappy bird. This program also keep track
 * off score and high score. There is also sounds for points and dying.
 * 
 * @author Zuhayr Ahmed
 * @version 20/06/2022
 */
public class FlappyBird extends Application {

	// Window size constants
	public final static int GAME_WIDTH = 933;
	public final static int GAME_HEIGHT = 700;

	// Game states
	public static final int TITLE_SCREEN = 0;
	public static final int PLAYING = 1;
	public static final int GAME_OVER = 2;

	private int gameState = TITLE_SCREEN;

	// Used to track keys as they are pressed/released.
	private final HashSet<KeyCode> keyboard = new HashSet<KeyCode>();

	// Updating elapsed time
	private GameTimer gameTimer = new GameTimer(time -> updateGame(time));

	// Background
	private Sprite background = new Sprite(new Image(
			"file:Images/ahmed/background.png", GAME_WIDTH, 0, true, true));

	// Bottom will be displayed by two sprite images
	private Sprite[] bottom = {
			new Sprite(new Image(
					"file:Images/ahmed/bottom.png", GAME_WIDTH, 0, true, true)),
			new Sprite(new Image(
					"file:Images/ahmed/bottom.png", GAME_WIDTH, 0, true, true))

	};

	private Group bottomDisplay = new Group(bottom[0], bottom[1]);

	// The Bird
	private Flappy bird = new Flappy(
			new Image("file:Images/ahmed/flappy bird.png", 65, 0, true, true));

	// Group for all pipes
	private Group topPipes = new Group();

	private Group bottomPipes = new Group();

	// Delay for adding pipes
	private double newPipeTimer = 0;

	// Interface elements for game screens
	private Text title = new Text("FLAPPY BIRD");
	private Text subtitle = new Text("Press Space to Begin");

	private Text endTitle = new Text("Game Over");
	private Text endSubtitle = new Text("Press Right Arrow to Continue ");

	// Score and end states
	private int score = 0;
	private Text scoreDisplay = new Text("" + score);
	private Label endScoreDisplay = new Label("Score " + score);

	private int highScore = 0;
	private Label endHighScoreDisplay = new Label("High Score " + highScore);

	private VBox endStats = new VBox(endScoreDisplay, endHighScoreDisplay);

	// Game Screen Groups
	private Group[] gameScreens = { new Group(title, subtitle),
			new Group(background, topPipes, bottomPipes, bird, 
					bottomDisplay, scoreDisplay),
			new Group(endTitle, endSubtitle, endStats) };

	// Font
	private Font titleFont = Font.loadFont("file:Font/ahmed/Flappy-Bird.ttf", 100);
	private Font endTitleFont = Font.loadFont("file:Font/ahmed/Flappy-Bird.ttf", 75);
	private Font subTitleFont = Font.loadFont("file:Font/ahmed/Flappy-Bird.ttf", 50);
	private Font scoreFont = Font.loadFont("file:Font/ahmed/Flappy-Bird.ttf", 45);
	private Font endStatFont = Font.loadFont("file:Font/ahmed/Flappy-Bird.ttf", 30);

	// Audio
	public static final AudioClip point = new AudioClip(
			new File("sound/ahmed/point.mp3").toURI().toString());

	public static final AudioClip die = new AudioClip(
			new File("sound/ahmed/hit.mp3").toURI().toString());

	// Checks if dead audio played
	private boolean overPlayed = false;

	// Screen's velocity
	private int screenVelocity = -200;

	// Counts total pipes
	private int pipes = 0;

	/**
	 * Constructor, loads the high score and sets up for new game
	 */
	public FlappyBird() {
		// Load high score from from highScore.txt
		Scanner inputFile;
		try {

			// Open highScore.txt
			inputFile = new Scanner(new File("highScore.txt"));

			// Make highScore the value in the file
			try {
				highScore = inputFile.nextInt();
			}
			// If file is blank, make highScore 0
			catch (Exception e) {
				highScore = 0;
			}

			// Display the high score
			endHighScoreDisplay.setText("High Score " + highScore);

			// Close file
			inputFile.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// Set up for newGame
		newGame();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		// Group to hold everything
		Group root = new Group();

		// Setting up scene for group holding everything
		Scene gameScene = new Scene(root, GAME_WIDTH, GAME_HEIGHT, Color.DARKGRAY);

		// Setting up the stage
		primaryStage.setScene(gameScene);
		primaryStage.setResizable(false);
		primaryStage.setTitle("Flappy Bird!");
		primaryStage.show();

		// Check to see which keys are pressed and released
		gameScene.setOnKeyPressed(key -> keyPressed(key));
		gameScene.setOnKeyReleased(key -> keyReleased(key));

		// Add all screens to the group holding everything
		root.getChildren().addAll(gameScreens[PLAYING], gameScreens[TITLE_SCREEN], 
				gameScreens[GAME_OVER]);

		// Displays score based on certain screens
		whenScoreDisplay();

		// Title screen state
		gameScreens[TITLE_SCREEN].setVisible(true);
		gameScreens[PLAYING].setVisible(true);
		gameScreens[GAME_OVER].setVisible(false);

		// Setting up everything
		title.setFont(titleFont);
		endTitle.setFont(endTitleFont);
		subtitle.setFont(subTitleFont);
		endSubtitle.setFont(subTitleFont);
		scoreDisplay.setFont(scoreFont);
		endScoreDisplay.setFont(endStatFont);
		endHighScoreDisplay.setFont(endStatFont);

		scoreDisplay.relocate(GAME_WIDTH / 2, GAME_HEIGHT / 6);
		title.relocate(GAME_WIDTH / 2 - title.getLayoutBounds().getWidth() / 2,
				GAME_HEIGHT / 8);
		subtitle.relocate(GAME_WIDTH / 2 - subtitle.getLayoutBounds().getWidth() / 2,
				GAME_HEIGHT / 2 + 150);
		endTitle.relocate(GAME_WIDTH / 2 - endTitle.getLayoutBounds().getWidth() / 2,
				GAME_HEIGHT / 8);
		endSubtitle.relocate(GAME_WIDTH / 2 - 
				endSubtitle.getLayoutBounds().getWidth() / 2, GAME_HEIGHT / 2 + 150);
		endStats.relocate(GAME_WIDTH / 2 - 60, GAME_HEIGHT / 2 - 50);

		title.setFill(Color.MEDIUMSEAGREEN);

		subtitle.setFill(Color.GOLD);
		endSubtitle.setFill(Color.GOLD);
		endTitle.setFill(Color.GOLD);

		scoreDisplay.setFill(Color.WHITE);
		endScoreDisplay.setTextFill(Color.WHITE);
		endHighScoreDisplay.setTextFill(Color.WHITE);

		title.setEffect(new DropShadow());
		scoreDisplay.setEffect(new DropShadow());
		endScoreDisplay.setEffect(new DropShadow());
		endHighScoreDisplay.setEffect(new DropShadow());

		endStats.setSpacing(5);
		endStats.setAlignment(Pos.CENTER);
	}

	/**
	 * Stop method, prints the high score to highScore.txt at the end of program,
	 * and whenever called
	 */
	public void stop() {
		try {

			// Open highScore.txt through PrintWriter
			PrintWriter out = new PrintWriter(new File("highScore.txt"));

			// Write high score
			out.println(highScore);

			// Close file
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Shows score only in the playing state
	 */
	public void whenScoreDisplay() {
		if (gameState == PLAYING) {
			scoreDisplay.setVisible(true);
		} else {
			scoreDisplay.setVisible(false);
		}
	}

	/**
	 * Setup all sprites for a new game. Also, reset all objects/elements for a new
	 * game
	 */
	public void newGame() {

		// Reset number of pipes and screen velocity
		pipes = 0;
		screenVelocity = -200;

		// Reset scores
		score = 0;
		scoreDisplay.setText("" + score);
		endScoreDisplay.setText("Score " + score);

		// Reset dead sound to false
		overPlayed = false;

		// Beginning position for bird
		bird.setPosition(GAME_WIDTH / 2 - bird.getWidth() / 2,
				GAME_HEIGHT / 2 - bird.getHeight() / 2);
		bird.setVelocityY(100);

		// Background's position
		background.setPosition(0, 0);

		// Set the bottoms up for the beginning of the game
		bottom[0].setPositionX(0);
		bottom[1].setPositionX(-bottom[1].getWidth());
		bottom[0].setVelocityX(screenVelocity);
		bottom[1].setVelocityX(screenVelocity);
		bottom[0].setPositionY(background.getHeight() - bottom[0].getHeight() / 2);
		bottom[1].setPositionY(background.getHeight() - bottom[1].getHeight() / 2);

		// Revive the bird
		bird.revive();

		// Clear all pipes
		topPipes.getChildren().clear();
		bottomPipes.getChildren().clear();

		// Reset new pipe timer to how it started
		newPipeTimer = 0;
	}

	/**
	 * Game updates happen as often as the timer can cause an event
	 */
	public void updateGame(double elapsedTime) {

		// Only show score in playing state
		whenScoreDisplay();

		// Update the background
		updateBackground(elapsedTime);

		// Update pipes
		updatePipe(elapsedTime);

		// Check for bird passing pipe
		countPipePass();

		// Clean up pipes
		cleanUp();

		// Update the bird
		updateBird(elapsedTime);

		// See if bird hit the pipes or the top or bottom of the screen
		checkForCollisions();

		// If game is over and total pipes are lower than 30,
		// Automatically go back to title screen, when pipes are off the screen
		if (gameState == GAME_OVER) {
			if (bird.getPositionX() + bird.getWidth() <= 0 && 
					bottomPipes.getChildren().size() == 0 && pipes < 30) {
				backToTitleScreen();
			}
		}
	}

	/**
	 * Updates the background to create moving animation. Moves both bottom images
	 * left. When an image passes the left side of the window, this method resets it
	 * to the right behind the other image.
	 * 
	 * @param elapsedTime amount of time passed since last update.
	 */
	public void updateBackground(double elapsedTime) {

		// Move the bottom sprites down based on elapsed time
		// And at the current speed of screen
		bottom[0].setVelocityX(screenVelocity);
		bottom[1].setVelocityX(screenVelocity);

		bottom[0].update(elapsedTime);
		bottom[1].update(elapsedTime);

		// When one sprites move past the left of the window,
		// move it to the right of the other one
		if (bottom[0].getPositionX() + bottom[0].getWidth() <= 0) {
			bottom[0].setPositionX(bottom[1].getPositionX() + bottom[1].getWidth());
		}

		if (bottom[1].getPositionX() + bottom[1].getWidth() <= 0) {
			bottom[1].setPositionX(bottom[0].getPositionX() + bottom[0].getWidth());
		}

	}

	/**
	 * Adds new pipes to the groups, and moves every pipe based on elapsed time and
	 * screen velocity
	 * 
	 * @param elapsedTime time passed since last update
	 */
	public void updatePipe(double elapsedTime) {

		// When game state is playing and
		// The pipe update delay goes below 0, add a new pipe
		if (newPipeTimer < 0 && gameState == PLAYING) {

			// Select a random height value for the pipe
			double y = (Math.random() * 325) - 620;

			// Create a pipe with top and bottom parts, which are set apart by some
			// Space and move according to the screenVelocity
			Pipe topPipe = new Pipe(true);
			Pipe bottomPipe = new Pipe(false);

			topPipe.setPosition(GAME_WIDTH, y);
			bottomPipe.setPosition(GAME_WIDTH, y + 850);

			topPipe.setVelocityX(screenVelocity);
			bottomPipe.setVelocityX(screenVelocity);

			// Add the pipes to their respective groups
			topPipes.getChildren().add(topPipe);
			bottomPipes.getChildren().add(bottomPipe);

			// Increment the number of pipes, now that a pipe is created
			pipes++;

			// Set the pipe create delay to 2 seconds
			newPipeTimer = 2;

			// Every 5 new pipes increase the screen velocity
			if (pipes % 5 == 0) {
				screenVelocity *= 1.1;
			}

		}

		// If new pipe update delay isnt below 0, subtract elapsed time from the
		// New pipe update delay
		else {
			newPipeTimer -= elapsedTime;
		}

		// Move every pipe based on elapsed time
		for (int i = 0; i < topPipes.getChildren().size(); i++) {
			Pipe pipe = (Pipe) topPipes.getChildren().get(i);
			pipe.update(elapsedTime);

			// If top part of pipe goes past left of the window get it killed
			if (pipe.getPositionX() + pipe.getWidth() <= 0) {
				pipe.kill();
			}
		}

		for (int i = 0; i < bottomPipes.getChildren().size(); i++) {
			Pipe pipe = (Pipe) bottomPipes.getChildren().get(i);
			pipe.update(elapsedTime);

			// If bottom part of pipe goes past left of the window get it killed
			if (pipe.getPositionX() + pipe.getWidth() <= 0) {
				pipe.kill();

			}
		}

	}

	/**
	 * Gets rid of any pipe which is ready to be removed from the game
	 */
	public void cleanUp() {

		// Go through all pipes, if any is dead remove it from the group
		// For top pipes,
		for (int i = 0; i < topPipes.getChildren().size(); i++) {

			Pipe pipe = (Pipe) topPipes.getChildren().get(i);

			if (pipe.isDead()) {
				topPipes.getChildren().remove(pipe);
				i--;
			}
		}

		// For bottom pipes,
		for (int i = 0; i < bottomPipes.getChildren().size(); i++) {

			Pipe pipe = (Pipe) bottomPipes.getChildren().get(i);

			if (pipe.isDead()) {
				bottomPipes.getChildren().remove(pipe);
				i--;
			}
		}
	}

	/**
	 * Makes the bird fall down the screen based on elapsed time
	 * 
	 * @param elapsedTime time passed since last update
	 */
	public void updateBird(double elapsedTime) {

		bird.fall(elapsedTime);

	}

	/**
	 * Checks if any pipe, or the top or bottom of the screen intersects the bird,
	 * and if they do kill them
	 */
	public void checkForCollisions() {

		// Look through all pipes
		for (int i = 0; i < topPipes.getChildren().size(); i++) {
			Pipe top = (Pipe) topPipes.getChildren().get(i);
			Pipe bottom = (Pipe) bottomPipes.getChildren().get(i);

			// If they intersect the bird
			if (top.intersect(bird) || bottom.intersect(bird)) {

				// Kill the bird
				kill();
			}
		}

		// If bird goes past the top or bottom of the screen
		if (bird.getPositionY() <= 0 || bird.getPositionY() >= 
				bottom[0].getPositionY() - bird.getHeight() || bird.getPositionY() 
				>= bottom[1].getPositionY() - bird.getHeight()) {

			// Kill the bird
			kill();
		}
	}

	/**
	 * Play the dead sound, kill the bird and end the game
	 */
	public void kill() {

		// If end sound hasnt been played yet, play it
		if (!overPlayed) {
			die.play();
			overPlayed = true;
		}

		// Kill the bird and end the game
		bird.kill(screenVelocity);
		gameOver();
	}

	/**
	 * When bird goes past a pipe, score is updated. If score is greater than high
	 * score, it is also updated
	 */
	public void countPipePass() {

		// Look through all pipes
		for (int i = 0; i < topPipes.getChildren().size(); i++) {
			Pipe thisPipe = (Pipe) topPipes.getChildren().get(i);

			// If the pipe passes the bird
			if (thisPipe.pass(bird)) {

				// Play point sound
				point.play();

				// Update score
				score++;
				scoreDisplay.setText("" + score);
				endScoreDisplay.setText("Score " + score);

				// If score is higher than high score
				if (score > highScore) {

					// Score becomes high score
					highScore = score;
					endHighScoreDisplay.setText("High Score " + highScore);

					// Save the high score to highScore.txt
					stop();
				}
			}
		}

	}

	/**
	 * Respond to key press events by checking for the pause key (P), and starting
	 * or stopping the game timer appropriately
	 * 
	 * Other key presses are stored in the "Keyboard" HashSet for polling during the
	 * main game update.
	 * 
	 * @param key KeyEvent program is responding to
	 */
	public void keyPressed(KeyEvent key) {

		// When game state is playing
		if (gameState == PLAYING) {

			// And hash set doesn't already contain p
			if (!keyboard.contains(KeyCode.P)) {

				// And p is pressed
				if (key.getCode() == KeyCode.P) {

					// Pause or unpause the game
					pause();
				}
			}
		}

		// When game state is title screen
		if (gameState == TITLE_SCREEN) {

			// And hash set doesn't already contain space
			if (!keyboard.contains(KeyCode.SPACE)) {

				// And space is pressed
				if (key.getCode() == KeyCode.SPACE) {

					// Start the game
					startGame();
				}
			}
		}

		// When game state is playing
		if (gameState == PLAYING) {

			// And hash set doesn't already contain space
			if (!keyboard.contains(KeyCode.SPACE)) {

				// And space is pressed
				if (key.getCode() == KeyCode.SPACE) {

					// Make the bird jump
					bird.jump();
				}

			}
		}

		// When game state is game over
		if (gameState == GAME_OVER) {

			// And hash set doesn't already contain right arrow
			if (!keyboard.contains(KeyCode.RIGHT)) {

				// And right arrow is pressed
				if (key.getCode() == KeyCode.RIGHT) {

					// Go back to title screen
					backToTitleScreen();
				}
			}
		}

		// If hash set doesn't already contain esc
		if (!keyboard.contains(KeyCode.ESCAPE)) {

			// And esc is pressed
			if (key.getCode() == KeyCode.ESCAPE) {

				// Close the game
				Platform.exit();
			}
		}

		// Record this particular key has been pressed:
		keyboard.add(key.getCode());
	}

	/**
	 * Removes a key from the "keyboard" HashSet when it is released
	 * 
	 * @param key KeyEvent that triggered this method call
	 */
	public void keyReleased(KeyEvent key) {

		// Remove the record of the key being pressed:
		keyboard.remove(key.getCode());
	}

	/**
	 * Starts the game play from the title screen
	 */
	public void startGame() {

		// Only show the playing screen
		gameScreens[TITLE_SCREEN].setVisible(false);
		gameScreens[GAME_OVER].setVisible(false);

		// Set game state to playing
		gameState = PLAYING;

		// Start the gameTimer
		gameTimer.start();
	}

	/**
	 * Switch the program to a new state when the player loses (by dying).
	 */
	public void gameOver() {

		// Save the high score to highScore.txt
		stop();

		// Show only the game over screen and the playing screen
		gameScreens[TITLE_SCREEN].setVisible(false);
		gameScreens[GAME_OVER].setVisible(true);

		// Set game state to game over
		gameState = GAME_OVER;

	}

	public void backToTitleScreen() {

		// Show only the title screen, playing screen and the score
		gameScreens[TITLE_SCREEN].setVisible(true);
		gameScreens[GAME_OVER].setVisible(false);

		// Show the score only during playing
		whenScoreDisplay();

		// Set game state to title screen
		gameState = TITLE_SCREEN;

		// Stop the gameTimer
		gameTimer.stop();

		// Reset everything for a new game
		newGame();
	}

	/**
	 * Pause or unpause the game
	 */
	public void pause() {
		if (gameTimer.isPaused()) {
			gameTimer.start();
		} else {
			gameTimer.stop();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch();
	}

}
