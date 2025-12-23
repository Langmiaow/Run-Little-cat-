import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.Objects;

// Game version : 1.10   2025/5/29

public class Game {
    static double score_count = 0, ball_generate_check = 1, cat_y_position = 25, bonus_check = 1;
    static Timeline barrier_movement1, barrier_movement2, barrier_movement3, barrier_movement4;
    static ImageView barrier1, barrier2, barrier3, barrier4;

    static {
        barrier1 = new ImageView(loadImage("/image/barrier_default.png"));
        barrier2 = new ImageView(loadImage("/image/barrier_default.png"));
        barrier3 = new ImageView(loadImage("/image/barrier_default.png"));
        barrier4 = new ImageView(loadImage("/image/barrier_default.png"));
    }
    public static Image loadImage(String path) {
        return new Image(Objects.requireNonNull(Game.class.getResourceAsStream(path)));
    }
    public static Scene game(){
        ImageView cat = new ImageView(loadImage("/image/cat_standing.png"));
        cat.setX(10);
        cat.setY(100);

        reset_barrier_position();

        Text cat_attention = new Text("!!");
        cat_attention.xProperty().bind(cat.xProperty().add(60));
        cat_attention.yProperty().bind(cat.yProperty().add(15));
        cat_attention.setFill(Color.GRAY);
        cat_attention.setFont(Font.font(16));

        Text cat_question = new Text("?");
        cat_question.xProperty().bind(cat.xProperty().add(60));
        cat_question.yProperty().bind(cat.yProperty().add(15));
        cat_question.setFill(Color.GRAY);
        cat_question.setFont(Font.font(16));
        cat_question.setVisible(false);

        Text cat_ask_again = new Text(225, 80, "｢> ω < ｢ Better next time ~");
        cat_ask_again.setFill(Color.GRAY);
        cat_ask_again.setFont(Font.font(25));
        cat_ask_again.setVisible(false);

        Text check = new Text("1");

        Text get_bonus = new Text();
        get_bonus.setFill(Color.DARKGREEN);
        get_bonus.setFont(Font.font(16));
        get_bonus.xProperty().bind(cat.xProperty().add(20));
        get_bonus.yProperty().bind(cat.yProperty());
        get_bonus.setVisible(false);

        ImageView ball = new ImageView(loadImage("/image/ball" + Start_menu.ball_color + ".png"));
        ball.setX(75);
        ball.setY(140);

        ImageView pause = new ImageView(loadImage("/image/pause.png"));
        pause.setX(10);
        pause.setY(10);
        pause.setVisible(false);

        ImageView play = new ImageView(loadImage("/image/play.png"));
        play.setX(375);
        play.setY(50);
        play.setVisible(false);

        ImageView reset = new ImageView(loadImage("/image/reset.png"));
        reset.setX(740);
        reset.setY(190);
        reset.setVisible(false);

        ImageView try_again = new ImageView(loadImage("/image/reset.png"));
        try_again.setX(740);
        try_again.setY(190);
        try_again.setVisible(false);

        Text count_down = new Text(400, 50, "2");
        count_down.setFill(Color.GRAY);
        count_down.setFont(Font.font(25));

        Timeline ball_run_away = new Timeline(
                new KeyFrame(Duration.seconds(2),
                    new KeyValue(ball.xProperty(), 800),
                    new KeyValue(ball.visibleProperty(), false),
                    new KeyValue(cat_attention.visibleProperty(), false)
                ),
                new KeyFrame(Duration.seconds(1), new KeyValue(count_down.textProperty(), "1")),
                new KeyFrame(Duration.seconds(1.9), new KeyValue(count_down.textProperty(), "Start!")),
                new KeyFrame(Duration.seconds(0.15), new KeyValue(count_down.xProperty(), 40),
                    new KeyValue(count_down.yProperty(), 100)
                ),
                new KeyFrame(Duration.seconds(2.5), new KeyValue(count_down.visibleProperty(), false))
        );
        ball_run_away.play();

        Timeline cat_jump = new Timeline(
                new KeyFrame(Duration.seconds(0.25),
                        new KeyValue(cat.yProperty(), 25))
        );
        cat_jump.setAutoReverse(true);
        cat_jump.setCycleCount(2);

        Timeline cat_rush = new Timeline(
                new KeyFrame(Duration.seconds(0.1),
                        new KeyValue(cat.xProperty(), 50))
        );
        cat_rush.setAutoReverse(true);
        cat_rush.setCycleCount(2);

        Image catStandingRunning1 = loadImage("/image/cat_standing_running1.png");
        Image catStanding = loadImage("/image/cat_standing.png");
        Image catStandingRunning2 = loadImage("/image/cat_standing_running2.png");
        Image catCreepingRunning1 = loadImage("/image/cat_creeping_running1.png");
        Image catCreeping = loadImage("/image/cat_creeping.png");
        Image catCreepingRunning2 = loadImage("/image/cat_creeping_running2.png");
        Image catRushing = loadImage("/image/cat_rushing.png");

        Timeline cat_standing_running = new Timeline(new KeyFrame(Duration.seconds(0), event -> cat.setImage(catStandingRunning1)),
                new KeyFrame(Duration.seconds(0.125), event -> cat.setImage(catStanding)),
                new KeyFrame(Duration.seconds(0.25), event -> cat.setImage(catStandingRunning2)));
        cat_standing_running.setCycleCount(Timeline.INDEFINITE);
        cat_standing_running.setAutoReverse(true);

        Timeline cat_creeping_running = new Timeline(new KeyFrame(Duration.seconds(0), event -> cat.setImage(catCreepingRunning1)),
                new KeyFrame(Duration.seconds(0.125), event -> cat.setImage(catCreeping)),
                new KeyFrame(Duration.seconds(0.25), event -> cat.setImage(catCreepingRunning2)));

        barrier_movement1 = new Timeline(new KeyFrame(Duration.seconds(3.3), new KeyValue(barrier1.xProperty(), -25)));
        barrier_movement2 = new Timeline(new KeyFrame(Duration.seconds(3.3), new KeyValue(barrier2.xProperty(), -25)));
        barrier_movement3 = new Timeline(new KeyFrame(Duration.seconds(3.3), new KeyValue(barrier3.xProperty(), -25)));
        barrier_movement4 = new Timeline(new KeyFrame(Duration.seconds(3.3), new KeyValue(barrier4.xProperty(), -25)));

        barrier_movement1.setOnFinished(event -> {
            bonus_check = 1;
            barrier1.setX(800);
            barrier1.setY(125);
        });
        barrier_movement2.setOnFinished(event -> {
            barrier2.setX(800);
        });
        barrier_movement3.setOnFinished(event -> {
            barrier3.setX(800);
        });
        barrier_movement4.setOnFinished(event -> {
            barrier4.setX(800);
        });

        Timeline ball_fall_down = new Timeline(new KeyFrame(Duration.seconds(0.5), new KeyValue(ball.yProperty(), 140)));



        //game process

        ImageView first_game_background = new ImageView(loadImage("/image/game_front_scene" + get_random(0) + ".png"));
        ImageView followed_game_background = new ImageView(loadImage("/image/game_front_scene" + get_random(0) + ".png"));
        followed_game_background.xProperty().bind(first_game_background.xProperty().add(800));

        //score text

        Text score = new Text(600, 25,"Score : " + 0);
        score.setFill(Color.GRAY);
        score.setFont(Font.font(16));

        //game process

        Timeline game_process = new Timeline(new KeyFrame(Duration.seconds(0.002), event -> {

            first_game_background.setX(first_game_background.getX()-0.5);

            double distance1 = Math.sqrt(Math.pow(((cat.getX()+25)-(barrier1.getX()+12.5)), 2)+Math.pow(((cat.getY()+cat_y_position)-(barrier1.getY()+12.5)), 2));
            double distance2 = Math.sqrt(Math.pow(((cat.getX()+25)-(barrier2.getX()+12.5)), 2)+Math.pow(((cat.getY()+cat_y_position)-(barrier2.getY()+12.5)), 2));
            double distance3 = Math.sqrt(Math.pow(((cat.getX()+25)-(barrier3.getX()+12.5)), 2)+Math.pow(((cat.getY()+cat_y_position)-(barrier3.getY()+12.5)), 2));
            double distance4 = Math.sqrt(Math.pow(((cat.getX()+25)-(barrier4.getX()+12.5)), 2)+Math.pow(((cat.getY()+cat_y_position)-(barrier4.getY()+12.5)), 2));
            double distance5 = Math.sqrt(Math.pow(((cat.getX()+25)-(ball.getX()+5)), 2)+Math.pow(((cat.getY()+25)-(ball.getY()+5)), 2));

            if (distance1 < 37.5 || distance2 < 37.5 || distance3 < 37.5 || distance4 < 37.5) {
                check.setText("0");
            }
            else if (barrier1.getY()==105 && cat_jump.getStatus().equals(Animation.Status.RUNNING) && (barrier1.getX() < 0)) {
                bonus(get_bonus, 50);
                bonus_check = 0;
            }
            else if (can_generate(1) && (get_random(2) == 1) && (barrier_movement1.getStatus() != Animation.Status.RUNNING)) {
                if (get_random(4) == 1) {
                    barrier1.setImage(loadImage("/image/balloon" + get_random(1) + ".png"));
                    barrier1.setY(105);
                }
                else {
                    barrier1.setImage(loadImage("/image/barrier" + get_random(3) + ".png"));
                    barrier1.setY(125);
                }
                barrier_movement1.play();
            }
            else if (can_generate(1) && (get_random(2) == 1) && (barrier_movement2.getStatus() != Animation.Status.RUNNING)) {
                barrier2.setImage(loadImage("/image/barrier" + get_random(3) + ".png"));
                barrier2.setY(125);
                barrier_movement2.play();
            }
            else if (can_generate(1) && (get_random(2) == 1) && (barrier_movement3.getStatus() != Animation.Status.RUNNING)) {
                barrier3.setImage(loadImage("/image/barrier" + get_random(3) + ".png"));
                barrier3.setY(125);
                barrier_movement3.play();
            }
            else if (can_generate(1) && (get_random(2) == 1) && (barrier_movement4.getStatus() != Animation.Status.RUNNING)) {
                barrier4.setImage((loadImage("/image/barrier" + get_random(3) + ".png")));
                barrier4.setY(125);
                barrier_movement4.play();
            }
            else if (ball_fall_down.getStatus() != Animation.Status.RUNNING && ball_generate_check == 1 && (can_generate(2))) { //BALL GENERATION
                ball_generate_check = 0;
                ball.setImage(loadImage("/image/ball" + get_random(1) + ".png"));
                ball.setVisible(true);
                ball_fall_down.play();
            }

            if (distance5 < 30 || ball_strike_barrier(ball.getX(), ball.getY())) {
                if (distance5 < 30) bonus(get_bonus, 10);
                ball_fall_down.stop();
                ball.setY(-15);
                ball_generate_check = 1;
            }

            score_count += 0.02;
            score.setText("Score : " + (int) score_count);

            if (first_game_background.getX() == -800) {
                first_game_background.setImage(loadImage("/image/game_front_scene" + get_random(0) + ".png"));
                first_game_background.setX(800);
                followed_game_background.xProperty().bind(first_game_background.xProperty().subtract(800));
            } else if (followed_game_background.getX() == -800) {
                followed_game_background.setImage(loadImage("/image/game_front_scene" + get_random(0) + ".png"));
                followed_game_background.xProperty().bind(first_game_background.xProperty().add(800));
            }
        }));

        game_process.setCycleCount(Timeline.INDEFINITE);

        ball_run_away.setOnFinished(event -> {
            cat_standing_running.play();
            game_process.play();
            ball.setX(75);
            ball.setY(-15);
        });

        Pane game = new Pane(first_game_background, followed_game_background, cat, ball, score);
        game.getChildren().addAll(pause, reset, play, try_again);
        game.getChildren().addAll(barrier1, barrier2, barrier3, barrier4);
        game.getChildren().addAll(cat_question, cat_attention, count_down, cat_ask_again);
        game.getChildren().addAll(get_bonus);

        Scene game_scene = new Scene(game, 800, 250);

        game_scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            cat_standing_running.pause();
            if (game_process.getStatus() == Animation.Status.RUNNING) {
                switch (event.getCode()) {
                    case W:
                        cat_jump.play();
                        break;
                    case D:
                        cat.setImage(catRushing);
                        cat_rush.play();
                        cat_rush.setOnFinished(sub_event -> {
                            cat.setImage(catStanding);
                            cat_standing_running.play();
                        });
                        break;
                    case S:
                        cat_creeping_running.play();
                        cat_y_position = 55;
                        break;
                    default:
                        game_process.pause();
                        cat_standing_running.pause();
                        cat_creeping_running.pause();
                        barrier_movement_pause();
                        cat_rush.pause();
                        cat.setImage(catStanding);
                        cat_question.setVisible(true);
                        pause.setVisible(true);
                        play.setVisible(true);
                        reset.setVisible(true);

                        play.setOnMouseClicked(sub_event -> {
                            pause.setVisible(false);
                            play.setVisible(false);
                            reset.setVisible(false);
                            cat_question.setVisible(false);
                            check_nearby();
                            cat_standing_running.play();
                            game_process.play();
                            barrier_movement_play();
                        });

                        reset.setOnMouseClicked(sub_event -> {
                            score_count = 0;
                            score.setText("Score : " + 0);
                            pause.setVisible(false);
                            play.setVisible(false);
                            reset.setVisible(false);
                            cat_question.setVisible(false);
                            ball.setX(75);
                            ball.setY(140);
                            ball.setVisible(true);
                            count_down.setText("2");
                            count_down.setVisible(true);
                            if (cat_rush.getStatus() == Animation.Status.PAUSED) cat_rush.play();
                            barrier_movement_stop();
                            reset_barrier_position();
                            ball_run_away.play();
                            ball_generate_check = 1;
                        });
                }
                game_scene.addEventHandler(KeyEvent.KEY_RELEASED, sub_event -> {
                    if (game_process.getStatus() == Animation.Status.RUNNING && (sub_event.getCode() == KeyCode.S || sub_event.getCode() == KeyCode.W)) {
                        cat_y_position = 25;
                        cat_creeping_running.stop();
                        cat_standing_running.play();
                    }
                });
            }
        });

        check.textProperty().addListener((observable, oldValue, newValue) -> {
            if (Objects.equals(newValue, "0")) {
                game_process.pause();
                cat_rush.pause();
                cat_jump.pause();
                cat_standing_running.pause();
                cat_creeping_running.pause();
                barrier_movement_pause();
                ball_fall_down.pause();
                try_again.setVisible(true);
                cat_ask_again.setVisible(true);
                try_again.setOnMouseClicked(event -> {
                    bonus_check = 1;
                    ball_generate_check = 1;
                    Start_menu.check.setText("0");
                    score_count = 0;
                });
            };
        });


        return game_scene;
    }
    public static int get_random(int i) { // 0 : background, 1 : ball type, 2 : barrier generation, 3 : barrier, 4 : barrier type, 5 : ball generation
        if (i == 0) return (int) (1+Math.random()*4);
        else if (i == 1) return  (int) (1+Math.random()*10);
        else if (i == 2) {
            if (Math.random()<=0.001) return 1;
            else return -1;
        }
        else if (i == 3) return (int) (1+Math.random()*7);
        else if (i == 4) {
            if (Math.random()<0.8) return 1;
            else return -1;
        }
        else if (i == 5) {
            if (Math.random() <= 0.01) return 1;
            else return -1;
        }
        else return -1;
    }

    public static void reset_barrier_position() {
        barrier1.setX(800);
        barrier1.setY(125);
        barrier2.setX(800);
        barrier2.setY(125);
        barrier3.setX(800);
        barrier3.setY(125);
        barrier4.setX(800);
        barrier4.setY(125);
    }

    public static void barrier_movement_pause() {
        barrier_movement1.pause();
        barrier_movement2.pause();
        barrier_movement3.pause();
        barrier_movement4.pause();
    }

    public static void barrier_movement_stop() {
        barrier_movement1.stop();
        barrier_movement2.stop();
        barrier_movement3.stop();
        barrier_movement4.stop();
    }

    public static void barrier_movement_play() {
        if (barrier_movement1.getStatus().equals(Animation.Status.PAUSED)) barrier_movement1.play();
        if (barrier_movement2.getStatus().equals(Animation.Status.PAUSED)) barrier_movement2.play();
        if (barrier_movement3.getStatus().equals(Animation.Status.PAUSED)) barrier_movement3.play();
        if (barrier_movement4.getStatus().equals(Animation.Status.PAUSED)) barrier_movement4.play();
    }

    public static boolean can_generate(int i) { // 1 : barrier, 2 : ball
        if (i == 1) {
            if (barrier1.getX() > 675 && barrier1.getX() < 799) return false;
            else if (barrier2.getX() > 675 && barrier2.getX() < 799) return false;
            else if (barrier3.getX() > 675 && barrier3.getX() < 799) return false;
            else return !(barrier4.getX() > 675) || !(barrier4.getX() < 799);
        } else if (i == 2) {
            return (int) score_count % 10 == 0 && get_random(5) == 1;
        }
        return false;
    }

    public static boolean ball_strike_barrier(double x, double y) {
        double distance1 = Math.sqrt(Math.pow((x-barrier1.getX()), 2) + Math.pow((y-barrier1.getY()), 2));
        double distance2 = Math.sqrt(Math.pow((x-barrier2.getX()), 2) + Math.pow((y-barrier2.getY()), 2));
        double distance3 = Math.sqrt(Math.pow((x-barrier3.getX()), 2) + Math.pow((y-barrier3.getY()), 2));
        double distance4 = Math.sqrt(Math.pow((x-barrier4.getX()), 2) + Math.pow((y-barrier4.getY()), 2));

        return distance1 < 17.5 || distance2 < 17.5 || distance3 < 17.5 || distance4 < 17.5;
    }

    public static void check_nearby() {
        if (barrier1.getX() <= 200) {
            leave(barrier1);
        }
        if (barrier2.getX() <= 200) {
            leave(barrier2);
        }
        if (barrier3.getX() <= 200) {
            leave(barrier3);
        }
        if (barrier4.getX() <= 200) {
            leave(barrier4);
        }
    }

    public static void leave(ImageView barrier) {
        Timeline barrier_leave = new Timeline(new KeyFrame(Duration.seconds(0.25), new KeyValue(barrier.yProperty(), -25)));
        barrier_leave.play();
    }

    public static void bonus(Text bonus, int score) {
        if (bonus_check == 1){
            score_count += score;
            bonus.setText("+"+score);
            Timeline bonus_animation = new Timeline(new KeyFrame(Duration.seconds(0), event -> bonus.setVisible(true)), new KeyFrame(Duration.seconds(0.5), event -> bonus.setVisible(false)));
            bonus_animation.play();
        }

    }
}
