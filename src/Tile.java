import java.awt.Color; // the color type used in StdDraw
import java.awt.Font; // the font type used in StdDraw
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

// A class used for modeling numbered tiles as in 2048
public class Tile {
   // Data fields: instance variables
   // --------------------------------------------------------------------------
   private int number; // the number on the tile
   private Color backgroundColor; // background (tile) color
   private Color foregroundColor; // foreground (number) color
   private Color boxColor; // box (boundary) color

   // Declare a private Map that associates integer keys with Color values
   private Map<Integer, Color> tileColors = new HashMap<>();

   // Data fields: class variables
   // --------------------------------------------------------------------------
   // the value of the boundary thickness (for the boundaries around the tiles)
   private static double boundaryThickness = 0.004;
   // the font used for displaying the tile number
   private static Font font = new Font("Arial", Font.PLAIN, 14);

   // Methods
   // --------------------------------------------------------------------------
   // the default constructor that creates a tile with 2 as the number on it
   public Tile() {
      tileColors.put(2, new Color(238, 228, 218));
      tileColors.put(4, new Color(237, 224, 200));
      tileColors.put(8, new Color(242, 177, 121));
      tileColors.put(16, new Color(245, 149, 99));
      tileColors.put(32, new Color(246, 124, 95));
      tileColors.put(64, new Color(246, 94, 59));
      tileColors.put(128, new Color(237, 207, 114));
      tileColors.put(256, new Color(237, 204, 97));
      tileColors.put(512, new Color(237, 200, 80));
      tileColors.put(1024, new Color(237, 197, 63));
      tileColors.put(2048, new Color(237, 194, 46));

      // set the number on the tile
      int[] numberList = {2, 4};
      Random random = new Random();
      int randomTileNumber = random.nextInt(numberList.length);
      number = numberList[randomTileNumber];
      // set the colors of the tile
      backgroundColor = new Color(91, 251, 174);
      foregroundColor = new Color(0, 0, 0);
      boxColor = new Color(225, 201, 119);
   }

   public int getNumber() {
      return number;
   }
   public void updateTile(int number) {
      this.number = number;
      this.backgroundColor = tileColors.get(number);
   }

   // a method for drawing the tile
   public void draw(Point position, double... sideLength) {
      // the default value for the side length (sLength) is 1
      double sLength;
      if (sideLength.length == 0) // sideLength is a variable-length parameter
         sLength = 1;
      else
         sLength = sideLength[0];
      // draw the tile as a filled square
      StdDraw.setPenColor(backgroundColor);
      StdDraw.filledSquare(position.getX(), position.getY(), sLength / 2);
      // draw the bounding box around the tile as a square
      StdDraw.setPenColor(boxColor);
      StdDraw.setPenRadius(boundaryThickness);
      StdDraw.square(position.getX(), position.getY(), sLength / 2);
      StdDraw.setPenRadius(); // reset the pen radius to its default value
      // draw the number on the tile
      StdDraw.setPenColor(foregroundColor);
      StdDraw.setFont(font);
      StdDraw.text(position.getX(), position.getY(), "" + number);
   }
}