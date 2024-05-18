package snake;

public class Player {
    private Snake snake;
    private Direction direction;
    private boolean ia;
    private final int id;

    public Player(int id, boolean ia) {
        this.id = id;
        this.ia = ia;
        snake = new Snake(id);
        this.direction = snake.getDirection();
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isIA() {
        return ia;
    }

    public void setIA(boolean ia) {
        this.ia = ia;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        snake.setDirection(direction);
    }

    public Snake getSnake() {
        return snake;
    }
}
