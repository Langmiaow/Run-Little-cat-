import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Objects;

// Game version : 1.10   2025/5/29

public class Start_menu extends Application {
    static int ball_color;
    static Text check = new Text("1");
    @Override
    public void start(Stage primaryStage) throws Exception {
        ImageView cat = new ImageView(Game.loadImage("/image/cat_standing.png"));
        cat.setX(10);
        cat.setY(100);

        ball_color = Game.get_random(1);
        ImageView ball = new ImageView(Game.loadImage("/image/ball" + ball_color + ".png"));
        ball.setX(75);
        ball.setY(140);

        Rectangle start_button = new Rectangle(365, 50, 70, 35);
        start_button.setFill(Color.GRAY);
        start_button.setArcHeight(5);
        start_button.setArcWidth(5);

        Text start_text = new Text(380, 73, "Start!");
        start_text.setFill(Color.WHITE);
        start_text.setFont(Font.font(16));


        Text introduction = new Text(300, 140, "Control the cat with W S D");
        introduction.setFont(Font.font(16));
        introduction.setFill(Color.GRAY);

        Pane start_menu = new Pane(start_button, start_text, introduction, cat, ball); //start menu part end

        Scene scene = new Scene(start_menu, 800, 250);
        start_button.setOnMouseClicked(event -> {
            primaryStage.setScene(Game.game());
        });

        start_text.setOnMouseClicked(event -> {
            primaryStage.setScene(Game.game());
        });


        primaryStage.setScene(scene);
        primaryStage.setResizable(false); //Users are not allowed to change the size of the window.
        primaryStage.setTitle("Run! Little cat!");
        primaryStage.getIcons().add(Game.loadImage("/image/icon.png"));
        primaryStage.show();

        check.textProperty().addListener((observable, oldValue, newValue) -> {
            if (Objects.equals(newValue, "0")) {
                primaryStage.setScene(scene);
                check.setText("1");
            }
        });
    }

}
