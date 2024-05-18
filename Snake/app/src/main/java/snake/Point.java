package snake;

public class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        // Initialisation des coordonnées.
        this.x = x;
        this.y = y;
    }

    public Point(Point p) {
        x = p.getX();
        y = p.getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int v) {
        x = v;
    }

    public void setY(int v) {
        y = v;
    }

    public double distanceTo(Point otherPoint) {
        return Math.sqrt(Math.pow(otherPoint.getX() - this.x, 2) + Math.pow(otherPoint.getY() - this.y, 2));
    }

    public boolean equals(Point p) {
        return p.getX() == x && p.getY() == y;
    }

    public String toString() {
        return "x : " + x + " y : " + y;
    }
}
