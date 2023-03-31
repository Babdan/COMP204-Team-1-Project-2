import java.awt.Color; // the color type used in StdDraw
import java.awt.Font; // the font type used in StdDraw
import java.awt.event.KeyEvent; // for the key codes used in StdDraw
import java.util.Random;

// The main class to run the Tetris 2048 game
public class Tetris2048 {
   public static void main(String[] args) {
      // set the size of the game grid
      int gridH = 18, gridW = 12;
      // set the size of the drawing canvas
      int canvasH = 40 * gridH, canvasW = 40 * gridW;
      StdDraw.setCanvasSize(canvasW, canvasH);
      // set the scale of the coordinate system
      StdDraw.setXscale(-0.5, gridW - 0.5);
      StdDraw.setYscale(-0.5, gridH - 0.5);
      // double buffering enables computer animations, creating an illusion of
      // motion by repeating four steps: clear, draw, show and pause
      StdDraw.enableDoubleBuffering();

      // set the dimension values stored and used in the Tetromino class
      Tetromino.gridHeight = gridH;
      Tetromino.gridWidth = gridW;

      // create the game grid
      GameGrid grid = new GameGrid(gridH, gridW);
      // create the first tetromino to enter the game grid
      // by using the createTetromino method defined below
      Tetromino currentTetromino = createTetromino();
      grid.setCurrentTetromino(currentTetromino);

      // display a simple menu before opening the game
      // by using the displayGameMenu method defined below
      displayGameMenu(gridH, gridW);

      // the main game loop (using some keyboard keys for moving the tetromino)
      // -----------------------------------------------------------------------
      int iterationCount = 0; // used for the speed of the game
      while (true) {
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
            boolean gameOver = grid.updateGrid(tiles, pos);
            // end the main game loop if the game is over
            if (gameOver)
               break;
            // create the next tetromino to enter the game grid
            // by using the createTetromino function defined below
            currentTetromino = createTetromino();
            grid.setCurrentTetromino(currentTetromino);
         }

         // display the game grid and the current tetromino
         grid.display();

      }

      // print a message on the console that the game is over
      System.out.println("Game over!");
   }

   // A method for creating a random shaped tetromino to enter the game grid
   public static Tetromino createTetromino() {
      // the type (shape) of the tetromino is determined randomly
      char[] tetrominoTypes = { 'I', 'O', 'Z' };
      Random random = new Random();
      int randomIndex = random.nextInt(tetrominoTypes.length);
      char randomType = tetrominoTypes[randomIndex];
      // create and return the tetromino
      Tetromino tetromino = new Tetromino(randomType);
      return tetromino;
   }

   // A method for displaying a simple menu before starting the game
   public static void displayGameMenu(int gridHeight, int gridWidth) {
      // colors used for the menu
      Color backgroundColor = new Color(42, 69, 99);
      Color buttonColor = new Color(25, 255, 228);
      Color textColor = new Color(31, 160, 239);
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
}