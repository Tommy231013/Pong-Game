package com.example.pong;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Pong extends Application {

    private Image backgroundImage;
    private Image playerImage;
    private Image ballImage;

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private double playerYPos = HEIGHT / 2 - 50;
    private double opponentYPos = HEIGHT / 2 - 50;
    private double ballXPos = WIDTH / 2;
    private double ballYPos = HEIGHT / 2;
    private double ballXSpeed = 3;
    private double ballYSpeed = 3;
    private double playerSpeed = 5;
    private double opponentSpeed = 2.8;

    private int playerScore = 0;
    private int opponentScore = 0;

    private boolean moveUp = false;
    private boolean moveDown = false;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Football-Themed Pong Game");

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        backgroundImage = new Image(getClass().getResource("/com/example/pong/Background.jpg").toString());
        playerImage = new Image(getClass().getResource("/com/example/pong/icons8-goalkeeper-64.png").toString());
        ballImage = new Image(getClass().getResource("/com/example/pong/icons8-football-100.png").toString());

        Scene scene = new Scene(new StackPane(canvas));
        stage.setScene(scene);
        stage.show();

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP) {
                moveUp = true;
            } else if (event.getCode() == KeyCode.DOWN) {
                moveDown = true;
            }
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.UP) {
                moveUp = false;
            } else if (event.getCode() == KeyCode.DOWN) {
                moveDown = false;
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateGame();
                drawGame(gc);
            }
        };
        timer.start();
    }

    private void updateGame() {
        if (moveUp && playerYPos > 0) {
            playerYPos -= playerSpeed;
        }
        if (moveDown && playerYPos < HEIGHT - 100) {
            playerYPos += playerSpeed;
        }

        if (Math.abs(ballYPos - opponentYPos) > 20) {
            if (ballYPos > opponentYPos) {
                opponentYPos += opponentSpeed;
            } else {
                opponentYPos -= opponentSpeed;
            }
        }

        if (ballXSpeed > 0) {
            opponentSpeed = 2.8;
        } else {
            opponentSpeed = 2.5;
        }

        ballXPos += ballXSpeed;
        ballYPos += ballYSpeed;

        if (ballYPos <= 0 || ballYPos >= HEIGHT - 20) {
            ballYSpeed *= -1;
        }

        if (ballXPos <= 60 && ballYPos + 20 >= playerYPos && ballYPos <= playerYPos + 100) {
            ballXSpeed *= -1;
            ballXPos = 60;
        }

        if (ballXPos >= WIDTH - 60 && ballYPos + 20 >= opponentYPos && ballYPos <= opponentYPos + 100) {
            ballXSpeed *= -1;
            ballXPos = WIDTH - 60;
        }

        if (ballXPos < 0) {
            opponentScore++;
            resetBall();
        } else if (ballXPos > WIDTH) {
            playerScore++;
            resetBall();
        }
    }

    private void resetBall() {
        ballXPos = WIDTH / 2;
        ballYPos = HEIGHT / 2;
        ballXSpeed = -ballXSpeed;
    }

    private void drawGame(GraphicsContext gc) {
        gc.drawImage(backgroundImage, 0, 0, WIDTH, HEIGHT);
        gc.drawImage(playerImage, 30, playerYPos, 30, 100);
        gc.drawImage(playerImage, WIDTH - 60, opponentYPos, 30, 100);
        gc.drawImage(ballImage, ballXPos, ballYPos, 20, 20);

        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Arial", 24));
        gc.fillText("Score: " + playerScore + " - " + opponentScore, WIDTH / 2 - 50, 40);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
