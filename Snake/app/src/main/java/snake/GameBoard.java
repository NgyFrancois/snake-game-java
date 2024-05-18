package snake;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import snake.Direction;
import snake.GameController;

public class GameBoard {
    private GridPane gridPane;
    private Scene scene;
    private List<Point> food;
    private List<Point> obstacle;
    private final int width;
    private final int height;

    public GameBoard(int width, int height) {
        food = new ArrayList<Point>();
        obstacle = new ArrayList<Point>();
        this.width = width / 2;
        this.height = height / 2;

        gridPane = new GridPane();
        for (int row = 0; row < height / 2; row++) {
            for (int col = 0; col < width / 2; col++) {
                // Case de base
                Rectangle cell = new Rectangle(40, 40, Color.CYAN);
                cell.setStroke(Color.WHITE);
                gridPane.add(cell, col, row);
                if((row == 10 && col == 5) || (row == 5 && col == 15)){
                    Rectangle obs = new Rectangle(40, 40, Color.BLACK);
                    gridPane.add(obs, col, row);
                    obstacle.add(new Point(col, row));
                }
            }
        }
        scene = new Scene(gridPane, width * 20, height * 20);
    }

    public synchronized Scene getScene() {
        return scene;
    }

    // Valeur absolue
    private synchronized int valAbs(int a){
        if(a < 0)return -a;
        return a;
    }

    //retourne vrai si le serpent est en train de sortir d'un bord
    public synchronized boolean enSortie(Snake snake){
        for (int i = 0; i < snake.getSize(); i++) {
            Point p = snake.getBody().get(i);
            Point prec = new Point(p);
            if(i != 0) prec = snake.getBody().get(i-1);
            // Pour chaque case du serpent regarder la distance entre lui et son précédent pour savoir le serpent est sorti du bord
            if(valAbs(p.getX() - prec.getX()) > 1 || valAbs(p.getY() - prec.getY()) > 1){
                return true;
            }
        }
        return false;
    }

    // Dessine le corps du serpent
    public synchronized void drawSnake(Snake snake) {
        for (int i = 0; i < snake.getSize(); i++) {
            Point p = snake.getBody().get(i);
            Point prec = new Point(p);
            if(i != 0) prec = snake.getBody().get(i-1);
            Rectangle part = new Rectangle(40, 40, Color.ORANGE);
            if (snake.getId() == 2)
                part = new Rectangle(40, 40, Color.MAGENTA);
            // Pour chaque case du serpent regarder la distance entre lui et son précédent pour savoir le serpent est sorti du bord
            if(valAbs(p.getX() - prec.getX()) > 1 || valAbs(p.getY() - prec.getY()) > 1){
                break;
            }
            gridPane.add(part, p.getX(), p.getY());
        }
    }

    // Dessine toutes les nourritures du board
    public synchronized void drawFood() {
        for (Point f : food) {
            Rectangle part = new Rectangle(40, 40, Color.RED);
            part.setStroke(Color.WHITE);
            gridPane.add(part, f.getX(), f.getY());
        }
    }

    // remet une case du plateau vide
    public synchronized void setCell(Point coor) {
        Rectangle part = new Rectangle(40, 40, Color.CYAN);
        part.setStroke(Color.WHITE);
        gridPane.add(part, coor.getX(), coor.getY());
    }

    public int getWidth() {
        return width;
    }

    public synchronized List<Point> getFood() {
        ArrayList<Point> copy = new ArrayList<Point>();
        for (Point p : food) {
            copy.add(p);
        }
        return copy;
    }

    private synchronized boolean containsObstacle(Point p){
        for(Point obs : obstacle){
            if(p.equals(obs))return true;
        }
        return false;
    }

     public synchronized List<Point> getObstacle() {
        ArrayList<Point> copy = new ArrayList<Point>();
        for (Point p : obstacle) {
            copy.add(p);
        }
        return copy;
    }

    // ajoute de la nourriture sur le plateau sur une case non occupée
    public synchronized void addFood() {
        Random r = new Random();
        int x = r.nextInt(getWidth() );
        int y = r.nextInt(getHeight() );
        Point f = new Point(x, y);
        for (int i = 0; i < GameController.joueurs.size(); i++) {
            Snake snake = GameController.joueurs.get(i).getSnake();
            while (snake.contains(f) || containsFood(f) || containsObstacle(f)) {
                x = r.nextInt(getWidth());
                y = r.nextInt(getHeight());
                f = new Point(x, y);
            }
        }
        food.add(f);
    }

    public synchronized boolean containsFood(Point f) {
        for (Point p : food) {
            if (p.equals(f))
                return true;
        }
        return false;
    }

    public synchronized void removeFood(Point f) {
        for (int i = 0; i < food.size(); i++) {
            if (food.get(i).equals(f))
                food.remove(i);
        }
    }

    public int getHeight() {
        return height;
    }

    public synchronized int nbFood() {
        return food.size();
    }
}
