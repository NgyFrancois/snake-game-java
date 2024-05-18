package snake;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class SnakeGame extends Application {
    private GameBoard plateau;
    private GameController control;
    private Snake snake;
    private GridPane gridPane;

    @Override
    public void start(Stage primaryStage) {
        // config de la partie
        Button mode1 = new Button("1 joueur");
        Button mode2 = new Button("2 joueur");
        HBox buttonsBox = new HBox(mode1, mode2);
        StackPane root1 = new StackPane(buttonsBox);
        Scene scene1 = new Scene(root1, 300, 200);

        mode1.setOnAction(event -> gameStart(primaryStage, true));
        mode2.setOnAction(event -> gameStart(primaryStage, false));

        primaryStage.setScene(scene1);
        primaryStage.setTitle("Accueil");
        primaryStage.show();
    }

    private void gameStart(Stage primaryStage, boolean ia) {
        primaryStage.close();
        Stage secondStage = new Stage();

        if (ia)
            control = new GameController(1, 1);
        else
            control = new GameController(2, 0);

        plateau = new GameBoard(60, 40);
        control.setPlateau(plateau);

        try {
            control.startGame();
        } catch (Exception e) {
            System.out.println(e);
        }

        Scene scene2 = plateau.getScene();

        // Déplacement du snake
        scene2.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            control.keyControl(keyCode);
            // System.out.println("clique");
        });

        secondStage.setScene(scene2);
        secondStage.setTitle("Snake Battle");
        secondStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}