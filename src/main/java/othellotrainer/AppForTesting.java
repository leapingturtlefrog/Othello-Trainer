package othellotrainer;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AppForTesting extends Application {
    @Override
    public void start(Stage primaryStage) {
        BoardTest board = new BoardTest();


        Pane pane = new Pane();
        Pane btOkPane = new Pane();

        Circle circle = new Circle();
        circle.centerXProperty().bind(pane.widthProperty().divide(2));
        circle.centerYProperty().bind(pane.heightProperty().divide(2));
        circle.setRadius(50);
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.BLACK);

        Rectangle btOkRec = new Rectangle();
        btOkRec.widthProperty().bind(btOkPane.widthProperty().multiply(0.8));
        btOkRec.heightProperty().bind(btOkPane.heightProperty().multiply(0.8));
        btOkRec.setStroke(Color.RED);
        btOkRec.setFill(null);

        Button btOk = new Button("Ok");
        btOk.setPrefSize(50, 199);

        btOkPane.setPrefSize(50, 75);

        btOkPane.getChildren().add(btOk);
        btOkPane.getChildren().add(btOkRec);

        Text blackScore = new Text();
        blackScore.setX(10);
        blackScore.setY(10);
        blackScore.textProperty().bind(
                Bindings.createStringBinding(
                        () -> "Black Score: " + board.blackScoreProperty().asString().get(),
                        board.blackScoreProperty()
                )
        );

        pane.getChildren().add(circle);
        //pane.getChildren().add(btOk);
        //pane.getChildren().add(btOkPane);
        pane.getChildren().add(blackScore);


        Scene scene = new Scene(pane, 200, 200);
        primaryStage.setTitle("Othello Trainer");
        primaryStage.setScene(scene);
        //primaryStage.setResizable(false);
        primaryStage.setMinWidth(100);
        primaryStage.setMinHeight(150);
        primaryStage.show();

        board.move(0, 3 * 8 + 2);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
