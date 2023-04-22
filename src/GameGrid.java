import java.awt.*;

// A class used for modelling the game grid
public class GameGrid {
   // data fields
   private int gridHeight, gridWidth; // the size of the game grid
   private Tile[][] tileMatrix; // to store the tiles locked on the game grid
   // the tetromino that is currently being moved on the game grid
   private Tetromino currentTetromino = null;
   private static Tetromino nextTetromino = null;
   private int points = 0;

   // the gameOver flag shows whether the game is over or not
   private boolean gameOver = false;
   private Color emptyCellColor; // the color used for the empty grid cells
   private Color lineColor; // the color used for the grid lines
   private Color boundaryColor; // the color used for the grid boundaries
   private double lineThickness; // the thickness used for the grid lines
   private double boxThickness; // the thickness used for the grid boundaries


   public Tile[][] tileUpdate;

   // A constructor for creating the game grid based on the given parameters
   public GameGrid(int gridH, int gridW) {
      // set the size of the game grid as the given values for the parameters
      gridHeight = gridH;
      gridWidth = gridW;
      // create the tile matrix to store the tiles locked on the game grid
      tileMatrix = new Tile[gridHeight][gridWidth];
      // set the color used for the empty grid cells
      emptyCellColor = new Color(206, 195, 181);
      // set the colors used for the grid lines and the grid boundaries
      lineColor = new Color(187, 173, 160);
      boundaryColor = new Color(167, 160, 151);

      // set the thickness values used for the grid lines and the grid boundaries
      lineThickness = 0.002;
      boxThickness = 10 * lineThickness;
   }

   // A setter method for the currentTetromino data field
   public void setCurrentTetromino(Tetromino currentTetromino) {
      this.currentTetromino = currentTetromino;
   }
   public void setNextTetromino(Tetromino nextTetromino){
      this.nextTetromino = nextTetromino;
   }

   public void merge() {
      boolean keep = true;

      while (keep) {
         keep = false;
         for (int i = 0; i < gridWidth; i++) {
            for (int j = 1; j < gridHeight; j++) {
               if (tileMatrix[j][i] == null || tileMatrix[j-1][i] == null) {
                  continue;
               } else if (tileMatrix[j-1][i].getNumber() == tileMatrix[j][i].getNumber()) {
                  tileMatrix[j-1][i].updateTile(tileMatrix[j-1][i].getNumber()*2);
                  points += tileMatrix[j-1][i].getNumber();
                  tileMatrix[j][i] = null;
                  this.fixColumn(i, j);
                  keep = true;
               }
            }
         }
      }
   }

   public void ShowNextTetromino() throws CloneNotSupportedException {
      Font font = new Font("Arial", Font.BOLD, 40);
      StdDraw.setFont(font);
      StdDraw.setPenColor(Color.WHITE); // Set the pen color to white
      StdDraw.text(13, 17, "SCORE");
      StdDraw.text(13, 15, ""+get_points());
      // Add the code to display the future Tetromino

      StdDraw.text(13, 7, "NEXT:");

      Tetromino copyNext = this.nextTetromino.clone();
      if(copyNext.getType() == 'I'){
         double width = this.gridWidth-3;
         copyNext.bottomLeftCell.setX((int) width);
         int height = 2;
         copyNext.bottomLeftCell.setY(height);
      }
      else if(copyNext.getType() == 'O' ){
         double width =this.gridWidth- 3.5;
         copyNext.bottomLeftCell.setX((int) width);
         int height = 2;
         copyNext.bottomLeftCell.setY(height);
      }
      else if(copyNext.getType() == 'J' || copyNext.getType() == 'L' ){
         double width =this.gridWidth- 3.5;
         copyNext.bottomLeftCell.setX((int) width);
         int height = 2;
         copyNext.bottomLeftCell.setY(height);
      }

      else if(copyNext.getType() == 'S' ){
         double width =this.gridWidth- 4;
         copyNext.bottomLeftCell.setX((int) width);
         int height = 2;
         copyNext.bottomLeftCell.setY(height);

      }
      else if(copyNext.getType() == 'T' ){
         double width =this.gridWidth- 4;
         copyNext.bottomLeftCell.setX((int) width);
         int height = 2;
         copyNext.bottomLeftCell.setY(height);
      }

      else{
         double width = this.gridWidth-4;
         copyNext.bottomLeftCell.setX((int) width);
         int height = 2;
         copyNext.bottomLeftCell.setY(height);

      }
      copyNext.draw();

   }

   public void fall() {
      boolean save = true;

      while (save) {
         save = false;
         for (int column = 1; column < gridWidth - 1; column++) {
            for (int row = 1; row < gridHeight - 1; row++) {
               Tile currentTile = tileMatrix[row][column];
               if (currentTile == null) {
                  continue;
               }
               boolean hasAdjacentTiles = tileMatrix[row][column-1] != null ||
                       tileMatrix[row][column+1] != null;
               boolean hasTileBelow = tileMatrix[row+1][column] != null;
               if (!hasAdjacentTiles && !hasTileBelow) {
                  points += currentTile.getNumber();
                  tileMatrix[row][column] = null;
                  save = true;
               }
            }
         }
      }
   }

   public void clearLines() {
      for (int r = gridHeight - 1; r >= 0; r--) {
         boolean isFilled = true;
         for (int c = 0; c < gridWidth - 6; c++) {
            if (tileMatrix[r][c] == null) {
               isFilled = false;
               break;
            }
         }
         if (isFilled) {
            clearLine(r);
            shiftDown(r);
         }

      }
   }

   public void clearLine(int r) {
      int score = 0;
      for (int c = 0; c < gridWidth; c++) {
         if (tileMatrix[r][c] != null) {
            score += tileMatrix[r][c].getNumber();
         }
         tileMatrix[r][c] = null;
      }
      points += score;
   }

   public void shiftDown(int r) {
      for (int row = r; row < gridHeight - 1; row++) {
         for (int col = 0; col < gridWidth; col++) {
            tileMatrix[row][col] = tileMatrix[row + 1][col];
         }
      }
   }

   private void fixColumn(int col, int row) {
      for (int i = row; i < gridHeight - 1; i++) {
         tileMatrix[i][col] = tileMatrix[i + 1][col];
      }
      tileMatrix[gridHeight - 1][col] = null;
   }


   public int get_points() {
      return points;
   }

   public void display() throws CloneNotSupportedException {
      // clear the background to emptyCellColor
      StdDraw.clear(emptyCellColor);

      // draw the game grid
      drawGrid();
      // draw the current/active tetromino
      if (currentTetromino != null)
         currentTetromino.draw();
      // draw a box around the game grid
      drawBoundaries();
      ShowNextTetromino();
      // show the resulting drawing with a pause duration of 50 ms
      StdDraw.show();
      StdDraw.pause(50);

   }

   // A method for drawing the cells and the lines of the game grid
   public void drawGrid() {
      // for each cell of the game grid
      for (int row = 0; row < gridHeight; row++)
         for (int col = 0; col < gridWidth; col++)
            // draw the tile if the grid cell is occupied by a tile
            if (tileMatrix[row][col] != null)
               tileMatrix[row][col].draw(new Point(col, row));
      // draw the inner lines of the grid
      StdDraw.setPenColor(lineColor);
      StdDraw.setPenRadius(lineThickness);
      // x and y ranges for the game grid
      double startX = -0.5, endX = gridWidth - 5;
      double startY = -0.5, endY = gridHeight - 0.5;
      for (double x = startX + 1; x < endX; x++) // vertical inner lines
         StdDraw.line(x, startY, x, endY);
      for (double y = startY + 1; y < endY; y++) // horizontal inner lines
         StdDraw.line(startX, y, endX-0.5, y);
      StdDraw.setPenRadius(); // reset the pen radius to its default value
   }

   // A method for drawing the boundaries around the game grid
   public void drawBoundaries() {
      // draw a bounding box around the game grid as a rectangle
      StdDraw.setPenColor(boundaryColor); // using boundaryColor
      // set the pen radius as boxThickness (half of this thickness is visible
      // for the bounding box as its lines lie on the boundaries of the canvas)
      StdDraw.setPenRadius(boxThickness);
      // the center point coordinates for the game grid
      double centerX = (gridWidth / 2 - 0.5), centerY = gridHeight / 2 - 0.5;
      StdDraw.rectangle(centerX, centerY, gridWidth / 2, gridHeight / 2);
      StdDraw.setPenRadius(); // reset the pen radius to its default value
   }


   // A method for checking whether the grid cell with given row and column
   // indexes is occupied by a tile or empty
   public boolean isOccupied(int row, int col) {
      if (!isInside(row, col))
         return false;
      // the cell is occupied by a tile if it is not null
      return tileMatrix[row][col] != null;
   }

   public boolean isInside(int row, double d) {
      if (row < 0 || row >= gridHeight)
         return false;
      if (d < 0 || d >= gridWidth - 5) // using double precision for the column index
         return false;
      return true;
   }



   public boolean updateGrid(Tile[][] tilesToLock, Point blcPosition) {
      // necessary for the display method to stop displaying the tetromino
      currentTetromino = null;
      // lock the tiles of the current tetromino (tilesToLock) on the game grid
      int nRows = tilesToLock.length, nCols = tilesToLock[0].length;
      for (int col = 0; col < nCols; col++) {
         for (int row = 0; row < nRows; row++) {
            // place each tile onto the game grid
            if (tilesToLock[row][col] != null) {
               // compute the position of the tile on the game grid
               Point pos = new Point();
               pos.setX(blcPosition.getX() + col);
               pos.setY(blcPosition.getY() + (nRows - 1) - row);
               if (isInside(pos.getY(), pos.getX()))
                  tileMatrix[pos.getY()][pos.getX()] = tilesToLock[row][col];
                  // the game is over if any placed tile is above the game grid
               else
                  gameOver = true;
            }
         }
      }
      // return the value of the gameOver flag
      return gameOver;
   }}