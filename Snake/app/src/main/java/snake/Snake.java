package snake;

import java.util.ArrayList;
import java.util.List;

public class Snake {
    private List<Point> body;
    private Direction direction;
    private int size;
    private final int id;

    public Snake(int id) {
        // Initialisation du serpent (longueur, position initiale, etc.).
        body = new ArrayList<Point>();
        this.id = id;
        if(id == 1){
            for (int i = 0; i < 5; i++) {
                body.add(new Point(4, 4 + i));
            }
            direction = Direction.RIGHT;
        }else{
            for (int i = 0; i < 5; i++) {
                body.add(new Point(20, 8+i));
            }
            direction = Direction.LEFT;
        }
        size = body.size();
    }

    public int getId(){
        return id;
    }

    public synchronized int getSize() {
        return size;
    }

    public synchronized Point getHead() {
        return new Point(body.get(0));
    }

    public synchronized boolean contains(Point b) {
        for (Point p : body) {
            if (p.equals(b))
                return true;
        }
        return false;
    }

    public synchronized List<Point> getBody() {
        List<Point> copy = new ArrayList<Point>();
        for (Point p : body) {
            copy.add(new Point(p));
        }
        return copy;
    }

    public synchronized Direction getDirection() {
        return direction;
    }

    public synchronized void setDirection(Direction d) {
        direction = d;
    }

    public synchronized void move() {
        // Déplacer le serpent en fonction de la direction actuelle.
        Point nextPos = new Point(getHead());
        switch (direction) {
            case UP:
                nextPos = new Point(getHead().getX(), getHead().getY() - 1);
                break;
            case DOWN:
                nextPos = new Point(getHead().getX(), getHead().getY() + 1);
                break;
            case LEFT:
                nextPos = new Point(getHead().getX() - 1, getHead().getY());
                break;
            case RIGHT:
                nextPos = new Point(getHead().getX() + 1, getHead().getY());
                break;
        }
        // si snake sort d'un des bords
        if(nextPos.getX() < 0 )nextPos.setX(29);
        if(nextPos.getX() > 29 )nextPos.setX(0);
        if(nextPos.getY() < 0 )nextPos.setY(19);
        if(nextPos.getY() > 19 )nextPos.setY(0);
        for (int i = body.size() - 1; i > 0; i--) {
            Point next = body.get(i - 1);
            body.set(i, next);
        }
        body.set(0, nextPos);
    }

    public synchronized boolean equals(Snake s){
        for(int i = 0; i < body.size(); i++){
            if(!body.get(i).equals(s.getBody().get(i)))return false;
        }
        return true;
    }

    public synchronized void grow() {
        // Faire grandir le serpent (appelé lorsqu'il mange une nourriture).
        Point newHead = new Point(0, 0);
        switch (direction) {
            case UP:
                newHead = new Point(getHead().getX(), getHead().getY() - 1);
                break;
            case DOWN:
                newHead = new Point(getHead().getX(), getHead().getY() + 1);
                break;
            case LEFT:
                newHead = new Point(getHead().getX() - 1, getHead().getY());
                break;
            case RIGHT:
                newHead = new Point(getHead().getX() + 1, getHead().getY());
                break;
        }
        body.add(0, newHead);
        size++;
    }

    // Autres méthodes nécessaires.
}
