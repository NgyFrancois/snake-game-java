package snake;

import javafx.scene.input.KeyCode;
import snake.Snake;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import java.util.Map.Entry;
import java.util.Random;
import java.util.ArrayList;

import javafx.application.Platform;

public class GameController {
    private GameBoard gameBoard;
    public static List<Player> joueurs = new LinkedList<>();
    private boolean lock_food = false;

    public GameController(int humain, int ia) {
        for (int i = 0; i < humain; i++) {
            Player p = new Player(i + 1, false);
            joueurs.add(p);
        }
        for (int i = 0; i < ia; i++) {
            Player p = new Player(i + 2, true);
            joueurs.add(p);
        }
    }

    public void setPlateau(GameBoard plateau) {
        this.gameBoard = plateau;
    }

    public synchronized void acquire_lock_food() throws InterruptedException {
        while (lock_food) {
            wait();
        }
        if (Thread.interrupted()) {
            throw new InterruptedException(); // Lève l'exception s'il y a une interruption pendant l'attente
        }
        lock_food = true;
    }

    public synchronized void release_lock_food() {
        lock_food = false;
        notifyAll();
    }

    public synchronized boolean isDead(Snake snake) {
        // Si le serpent se dépasse les bords du terrain
        /*if (snake.getHead().getX() < 0 || snake.getHead().getY() < 0 || snake.getHead().getX() >= gameBoard.getWidth()
                || snake.getHead().getY() >= gameBoard.getHeight()) {
            return true;
        }*/
        // Si le serpent se mange lui-même
        for (int i = 1; i < snake.getSize(); i++) {
            Point t = new Point(snake.getBody().get(i));
            if (t.equals(snake.getHead())) {
                System.out.println("suicide");
                return true;
            }
        }

        // Si la tête du serpent se cogne avec un autre
        for (int j = 0; j < joueurs.size(); j++) {
            Snake s = joueurs.get(j).getSnake();
            if (!snake.equals(s)) {
                if (s.contains(snake.getHead()) && !gameBoard.enSortie(s)){
                    System.out.println("coallision");
                    return true;}
            }
        }
        // si le serpent touche un obstacle
        for (Point obs : gameBoard.getObstacle()){
            if(obs.equals(snake.getHead())){
                System.out.println("obstacle");
                return true;
            }
        }
        return false;
    }

    // empeche le serpent de se suicider
    public boolean noSuicide(Player joueur, Direction direction) {
        Point nextPoint = null;

        switch (direction) {
            case UP:
                nextPoint = new Point(joueur.getSnake().getHead().getX(), joueur.getSnake().getHead().getY() - 1);
                break;
            case DOWN:
                nextPoint = new Point(joueur.getSnake().getHead().getX(), joueur.getSnake().getHead().getY() + 1);
                break;
            case LEFT:
                nextPoint = new Point(joueur.getSnake().getHead().getX() - 1, joueur.getSnake().getHead().getY());
                break;
            case RIGHT:
                nextPoint = new Point(joueur.getSnake().getHead().getX() + 1, joueur.getSnake().getHead().getY());
                break;
        }

        // Si le serpent se dépasse les bords du terrain
        if (nextPoint.getX() < 0 || nextPoint.getY() < 0 || nextPoint.getX() >= gameBoard.getWidth()
                || nextPoint.getY() >= gameBoard.getHeight()) {
            return false;
        }

        // Si la tête du serpent se cogne avec un autre ou lui meme
        for (int i = 0; i < joueurs.size(); i++) {
            Snake s = joueurs.get(i).getSnake();
            if (s.contains(nextPoint))
                return false;
        }

        // si le serpent touche un obstacle
        for (Point obs : gameBoard.getObstacle()){
            if(obs.equals(nextPoint)){
                return false;
            }
        }

        return true;
    }

    // bouge aléatoirement le snake
    public synchronized void randomMove(Player joueur) {
        Random random = new Random();
        int rdm = random.nextInt(4);

        switch (rdm) {
            case 0:
                if (joueur.getDirection() != Direction.UP && noSuicide(joueur, Direction.DOWN)) {
                    joueur.setDirection(Direction.DOWN);
                }
                break;

            case 1:
                if (joueur.getDirection() != Direction.DOWN && noSuicide(joueur, Direction.UP)) {
                    joueur.setDirection(Direction.UP);
                }
                break;

            case 2:
                if (joueur.getDirection() != Direction.LEFT && noSuicide(joueur, Direction.RIGHT)) {
                    joueur.setDirection(Direction.RIGHT);
                }
                break;

            case 3:
                if (joueur.getDirection() != Direction.RIGHT && noSuicide(joueur, Direction.LEFT)) {
                    joueur.setDirection(Direction.LEFT);
                }
                break;

            default:
                randomMove(joueur);
                break;
        }
    }

    // bouge de maniere un peu plus rapide l'ia
    public synchronized void randomMove2(Player joueur) {
        if (joueur.getDirection() != Direction.LEFT && noSuicide(joueur, Direction.RIGHT)) {
            joueur.setDirection(Direction.RIGHT);
        } else if (joueur.getDirection() != Direction.DOWN && noSuicide(joueur, Direction.UP)) {
            joueur.setDirection(Direction.UP);
        } else if (joueur.getDirection() != Direction.RIGHT && noSuicide(joueur, Direction.LEFT)) {
            joueur.setDirection(Direction.LEFT);
        } else {
            joueur.setDirection(Direction.DOWN);
        }
    }

    // bouge la tete du serpent en fonction d'ou se trouve la nourriture
    public synchronized void directionIA(Player joueur) throws InterruptedException {
        acquire_lock_food(); // bloque la nourriture pendant la recherche
        Point tete = joueur.getSnake().getHead();

        Point search = gameBoard.getFood().get(0); // la nourriture a chercher

        if (gameBoard.getFood().size() > 1) { // s'il y a plus de 2 nourriture sur le plateau va chercher la nourriture
                                              // la
            // plus proche
            double tmp = search.distanceTo(tete); // dist de la premiere nourriture

            for (int i = 1; i < gameBoard.getFood().size(); i++) {
                double dist = gameBoard.getFood().get(i).distanceTo(tete);
                if (tmp < tmp) {
                    search = gameBoard.getFood().get(i);
                    tmp = dist;
                }
            }
        }

        // mtn qu'on a la nourriture bouge la tete du snake pour y aller
        if (search.getY() > tete.getY() && joueur.getDirection() != Direction.UP
                && noSuicide(joueur, Direction.DOWN)) {
            joueur.setDirection(Direction.DOWN);
        } else if (search.getY() < tete.getY() && joueur.getDirection() != Direction.DOWN
                && noSuicide(joueur, Direction.UP)) {
            joueur.setDirection(Direction.UP);
        } else if (search.getX() > tete.getX() && joueur.getDirection() != Direction.LEFT
                && noSuicide(joueur, Direction.RIGHT)) {
            joueur.setDirection(Direction.RIGHT);
        } else if (search.getX() < tete.getX() && joueur.getDirection() != Direction.RIGHT
                && noSuicide(joueur, Direction.LEFT)) {
            joueur.setDirection((Direction.LEFT));
        } else {
            randomMove(joueur);
        }
        release_lock_food(); // quand une direction a été trouvé je débloque la food
    }

    // fait bouger le serpent
    public synchronized void moveSnake(Player joueur) throws InterruptedException {
        for (int i = 0; i < joueur.getSnake().getSize(); i++) {
            gameBoard.setCell(joueur.getSnake().getBody().get(i));
        }

        if (joueur.isIA()) {
            directionIA(joueur);
        }

        joueur.getSnake().move();

        Point newP = null;
        switch (joueur.getSnake().getDirection()) {
            case UP:
                if (joueur.getSnake().getDirection() != Direction.DOWN) {
                    joueur.getSnake().setDirection(Direction.UP);
                    newP = new Point(joueur.getSnake().getHead().getX(), joueur.getSnake().getHead().getY() - 1);
                }
                break;
            case DOWN:
                if (joueur.getSnake().getDirection() != Direction.UP) {
                    joueur.getSnake().setDirection(Direction.DOWN);
                    newP = new Point(joueur.getSnake().getHead().getX(), joueur.getSnake().getHead().getY() + 1);
                }
                break;
            case LEFT:
                if (joueur.getSnake().getDirection() != Direction.RIGHT) {
                    joueur.getSnake().setDirection(Direction.LEFT);
                    newP = new Point(joueur.getSnake().getHead().getX() - 1, joueur.getSnake().getHead().getY());
                }
                break;
            case RIGHT:
                if (joueur.getSnake().getDirection() != Direction.LEFT) {
                    joueur.getSnake().setDirection(Direction.RIGHT);
                    newP = new Point(joueur.getSnake().getHead().getX() + 1, joueur.getSnake().getHead().getY());
                }
                break;
        }

        acquire_lock_food();
        if (gameBoard.containsFood(newP)) {
            joueur.getSnake().grow();
            gameBoard.removeFood(newP);
            gameBoard.addFood();
            gameBoard.drawFood();
        }
        release_lock_food();
        if (isDead(joueur.getSnake())) {
            if (joueur.getSnake().getId() == 1) {
                System.out.println("Joueur 2 a gagné");
            } else {
                System.out.println("Joueur 1 a gagné");
            }
            System.exit(0);
        }
        gameBoard.drawSnake(joueur.getSnake());
    }

    public void startGame() throws InterruptedException {
        // Initialisation des snake
        for (int i = 0; i < joueurs.size(); i++) {
            gameBoard.drawSnake(joueurs.get(i).getSnake());

            // food
            gameBoard.addFood();
            gameBoard.drawFood();
        }

        Timer timer = new Timer();

        TimerTask tasksnake = new TimerTask() {
            @Override
            public synchronized void run() {
                Platform.runLater(() -> {
                    for (int i = 0; i < joueurs.size(); i++) {
                        try {
                            moveSnake(joueurs.get(i));
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                });
            }
        };

        timer.scheduleAtFixedRate(tasksnake, 0, 400);

    }

    public synchronized void keyControl(KeyCode event) {
        // controle du joueur1
        switch (event) {
            case UP:
                if (joueurs.get(0).getDirection() != Direction.DOWN) {
                    joueurs.get(0).setDirection(Direction.UP);
                }
                break;
            case DOWN:
                if (joueurs.get(0).getDirection() != Direction.UP) {
                    joueurs.get(0).setDirection(Direction.DOWN);
                }
                break;
            case LEFT:
                if (joueurs.get(0).getDirection() != Direction.RIGHT) {
                    joueurs.get(0).setDirection(Direction.LEFT);
                }
                break;
            case RIGHT:
                if (joueurs.get(0).getDirection() != Direction.LEFT) {
                    joueurs.get(0).setDirection(Direction.RIGHT);
                }
                break;
        }
        // Controle d'un joueur 2 avec ZQSD
        switch (event) {
            case Z:
                if (joueurs.get(1).getDirection() != Direction.DOWN) {
                    joueurs.get(1).setDirection(Direction.UP);
                }
                break;
            case S:
                if (joueurs.get(1).getDirection() != Direction.UP) {
                    joueurs.get(1).setDirection(Direction.DOWN);
                }
                break;
            case Q:
                if (joueurs.get(1).getDirection() != Direction.RIGHT) {
                    joueurs.get(1).setDirection(Direction.LEFT);
                }
                break;
            case D:
                if (joueurs.get(1).getDirection() != Direction.LEFT) {
                    joueurs.get(1).setDirection(Direction.RIGHT);
                }
                break;
        }
    }
}
