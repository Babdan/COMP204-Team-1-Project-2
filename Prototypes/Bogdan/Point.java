// A class for modeling a point as a location in 2D space
public class Point {
   // data fields
   private int x, y; // coordinates in 2D space (specified in integer precision)

   // default constructor that initializes both x and y data fields as zero
   public Point() {
      x = y = 0;
   }

   // a constructor that creates a point at a given location (x, y)
   public Point(int x, int y) {
      this.x = x;
      this.y = y;
   }

   // getter method for the x coordinate of this point
   public int getX() {
      return x;
   }

   // getter method for the y coordinate of this point
   public int getY() {
      return y;
   }

   // setter method for the x coordinate of this point
   public void setX(int x) {
      this.x = x;
   }

   // setter method for the y coordinate of this point
   public void setY(int y) {
      this.y = y;
   }

   // moves this point by dx along the x axis and by dy along the y axis
   public void translate(int dx, int dy) {
      x += dx;
      y += dy;
   }

   // moves this point to a given location (x, y)
   public void move(int x, int y) {
      this.x = x;
      this.y = y;
   }
}