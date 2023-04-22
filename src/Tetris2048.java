import java.awt.Color; // the color type used in StdDraw
import java.awt.Font; // the font type used in StdDraw
import java.awt.event.KeyEvent; // for the key codes used in StdDraw
import java.util.Random;

// The main class to run the Tetris 2048 game
public class Tetris2048 {
   public static void main(String[] args) throws CloneNotSupportedException {
      // set the size of the game grid
      int gridH = 20, gridW = 16;
      // set the size of the drawing canvas
      int canvasH = 20 * gridH, canvasW = 40 * gridW;
      StdDraw.setCanvasSize(canvasW, canvasH);
      // set the scale of the coordinate system
      StdDraw.setXscale(-0.5, gridW - 0.5);
      StdDraw.setYscale(-0.5, gridH - 0.5);
      StdDraw.enableDoubleBuffering();
      Tetromino.gridHeight = gridH;
      Tetromino.gridWidth = gridW;

   while (true) {
      // create the game grid
      GameGrid grid = new GameGrid(gridH, gridW);
      StdDraw.filledRectangle(90, 50, 10, 50);
      displayGameMenu(gridH, gridW);
      game(grid);
      System.out.println("Game Over");
      displayGameOverPopup(gridH, gridW);
      }
   }

   public static void game(GameGrid grid) throws CloneNotSupportedException{
      Tetromino currentTetromino = createTetromino();
      grid.setCurrentTetromino(currentTetromino);

      Tetromino nextTetromino = createTetromino();
      grid.setNextTetromino(nextTetromino);
      // the main game loop (using some keyboard keys for moving the tetromino)
      // -----------------------------------------------------------------------
      int iterationCount = 0; // used for the speed of the game
      boolean gameOver = false;
      while (!gameOver) {
         // check user interactions via the keyboard
         // --------------------------------------------------------------------
         // if the left arrow key is being pressed
         if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT))
            // move the active tetromino left by one
            currentTetromino.move("left", grid);
         // if the right arrow key is being pressed
         else if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT))
            // move the active tetromino right by one
            currentTetromino.move("right", grid);
         // if the down arrow key is being pressed
         else if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN))
            // move the active tetromino down by one
            currentTetromino.move("down", grid);
         else if (StdDraw.isKeyPressed(KeyEvent.VK_SPACE))
            if(currentTetromino.canBeRotated(grid))
               currentTetromino.rotate();
         if (StdDraw.isKeyPressed(KeyEvent.VK_0)){
            gameOver = true;
            displayGameOverPopup(Tetromino.gridHeight, Tetromino.gridWidth);
            }


         // move the active tetromino down by 1 once in 10 iterations (auto fall)
         boolean success = true;
         if (iterationCount % 10 == 0)
            success = currentTetromino.move("down", grid);
         iterationCount++;

         // place the active tetromino on the grid when it cannot go down anymore
         if (!success) {
            // get the tile matrix of the tetromino without empty rows and columns
            currentTetromino.createMinBoundedTileMatrix();
            Tile[][] tiles = currentTetromino.getMinBoundedTileMatrix();
            Point pos = currentTetromino.getMinBoundedTileMatrixPosition();
            // update the game grid by locking the tiles of the landed tetromino
            gameOver = grid.updateGrid(tiles, pos);

            // end the main game loop if the game is over
            if (gameOver){
               displayGameOverPopup(Tetromino.gridHeight, Tetromino.gridWidth);
               break;
            }
            // create the next tetromino to enter the game grid
            // by using the createTetromino function defined below
            currentTetromino = nextTetromino;
            grid.setCurrentTetromino(nextTetromino);
            nextTetromino = createTetromino();
            grid.setNextTetromino(nextTetromino);


         }

         // display the game grid and the current tetromino
         grid.fall();
         grid.merge();
         grid.clearLines();
         grid.display();

      }

   }

   // A method for displaying the game over popup
   public static void displayGameOverPopup(int gridHeight, int gridWidth) {
      Color backgroundColor = new Color(171, 187, 120, 180);
      Color popupColor = new Color(255, 251, 167);
      Color textColor = new Color(76, 100, 55);

      double popupWidth = gridWidth / 2.0;
      double popupHeight = gridHeight / 4.0;
      double popupCenterX = gridWidth / 2.0;
      double popupCenterY = gridHeight / 2.0;

      // Draw semi-transparent background
      StdDraw.setPenColor(backgroundColor);
      StdDraw.filledRectangle(gridWidth / 2.0, gridHeight / 2.0, gridWidth / 2.0, gridHeight / 2.0);

      // Draw popup box
      StdDraw.setPenColor(popupColor);
      StdDraw.filledRectangle(popupCenterX, popupCenterY, popupWidth / 2, popupHeight / 2);
      StdDraw.setPenColor(textColor);
      StdDraw.rectangle(popupCenterX, popupCenterY, popupWidth / 2, popupHeight / 2);

      // Draw text
      Font font = new Font("Arial", Font.BOLD, 25);
      StdDraw.setFont(font);
      StdDraw.text(popupCenterX, popupCenterY + popupHeight / 6, "Game Over");
      font = new Font("Arial", Font.PLAIN, 18);
      StdDraw.setFont(font);
      StdDraw.text(popupCenterX, popupCenterY - popupHeight / 6, "Press R to restart or Q to quit");

      // Wait for user input
      while (true) {
         // If R key is pressed
         if (StdDraw.isKeyPressed(KeyEvent.VK_R)) {
            StdDraw.pause(200); // To prevent holding key from triggering multiple restarts
            return; // Return control to the main loop to restart the game
         }
         // If Q key is pressed
         if (StdDraw.isKeyPressed(KeyEvent.VK_Q)) {
            System.exit(0);
         }
         StdDraw.show();
         StdDraw.pause(50);
      }

   }

   // A method for creating a random shaped tetromino to enter the game grid
   public static Tetromino createTetromino() {
      // the type (shape) of the tetromino is determined randomly
      char[] tetrominoTypes = { 'I', 'O', 'J', 'S', 'Z', 'L', 'T' };
      Random random = new Random();
      int randomIndex = random.nextInt(tetrominoTypes.length);
      char randomType = tetrominoTypes[randomIndex];
      // create the tetromino
      Tetromino tetromino = new Tetromino(randomType);

      // set the starting position of the tetromino to be at the top-middle of the grid
      int gridMiddle = (Tetromino.gridWidth - tetromino.getTileMatrix()[0].length) / 2;
      tetromino.getPosition().move(gridMiddle, Tetromino.gridHeight - tetromino.getTileMatrix().length);

      return tetromino;
   }


   // A method for displaying a simple menu before starting the game
   public static void displayGameMenu(int gridHeight, int gridWidth) {
      // colors used for the menu
      Color backgroundColor = new Color(171, 187, 120);
      Color buttonColor = new Color(255, 251, 167);
      Color textColor = new Color(76, 100, 55);
      // clear the background canvas to background_color
      StdDraw.clear(backgroundColor);
      // the relative path of the image file
      String imgFile = "images/menu_image.png";
      // center coordinates to display the image
      double imgCenterX = (gridWidth - 1) / 2.0, imgCenterY = gridHeight - 7;
      // display the image
      StdDraw.picture(imgCenterX, imgCenterY, imgFile);
      // the width and the height of the start game button
      double buttonW = gridWidth - 1.5, buttonH = 2;
      // the center point coordinates of the start game button
      double buttonX = imgCenterX, buttonY = 5;
      // display the start game button as a filled rectangle
      StdDraw.setPenColor(buttonColor);
      StdDraw.filledRectangle(buttonX, buttonY, buttonW / 2, buttonH / 2);
      // display the text on the start game button
      Font font = new Font("Arial", Font.PLAIN, 25);
      StdDraw.setFont(font);
      StdDraw.setPenColor(textColor);
      String textToDisplay = "Click Here to Start the Game";
      StdDraw.text(buttonX, buttonY, textToDisplay);
      // menu interaction loop
      while (true) {
         // display the menu and wait for a short time (50 ms)
         StdDraw.show();
         StdDraw.pause(50);
         // check if the mouse is being pressed on the button
         if (StdDraw.isMousePressed()) {
            // get the x and y coordinates of the position of the mouse
            double mouseX = StdDraw.mouseX(), mouseY = StdDraw.mouseY();
            // check if these coordinates are inside the button
            if (mouseX > buttonX - buttonW / 2 && mouseX < buttonX + buttonW / 2)
               if (mouseY > buttonY - buttonH / 2 && mouseY < buttonY + buttonH / 2)
                  break; // break the loop to end the method and start the game
         }
      }
   }

   public static void playGame(GameGrid grid) throws CloneNotSupportedException {
      // Move the code from the main() method and game() method into this method
   }

}
