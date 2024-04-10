package com.example.lesothotriviagame;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.net.URL;

public class HelloApplication extends Application {

    private int score = 0;
    private int questionIndex = 0;
    private TriviaQuestion[] questions = {
            new TriviaQuestion("What are capital city of Lesotho?",
                    new String[]{"Maseru", "Mokhotlong", "Mafeteng", "Leribe"},
                    "Maseru", "/maseru.jpg"),
            new TriviaQuestion("What is the Currency of lesotho?",
                    new String[]{"loti", "Maloti", "chelete", "khotso"},
                    "maloti", "/maluti.jpg"),
            new TriviaQuestion("Which mountain range covers much of Lesotho?",
                    new String[]{"The Drakensberg", "rokies", "thaba-bosiu", "Qiloane"},
                    "The Drakensberg", "/drakensburg.jpg","/sing.mp4"),
           // new TriviaQuestion("How many districts does Lesotho have?",
                  //  new String[]{"10", "1", "5", "7"},
                  //  "10", "/mokorotlo.jpeg"),
            //new TriviaQuestion("Who is the Current Prime Minister?",
                //    new String[]{"Mosisili", "Matekane", "Majoro", "Thabene"},
                  //  "Matekane", "/matekane.jpg"),
    };

    private Label scoreLabel = new Label("Score: 0");
    private Label questionLabel = new Label();
    private VBox optionsBox = new VBox(10);

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        root.getStyleClass().add("root"); // Apply "root" style class
        Scene scene = new Scene(root, 800, 600);

        // Load CSS file
        URL cssUrl = getClass().getResource("/style.css");
        if (cssUrl != null) {
            System.out.println("CSS file URL: " + cssUrl.toString());
            scene.getStylesheets().add(cssUrl.toExternalForm()); // Add CSS to scene
        } else {
            System.err.println("Failed to load CSS file");
        }

        // Apply style class to question label
        questionLabel.getStyleClass().add("question-label");

        // Load the initial question
        loadQuestion(root, scene);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Lesotho Trivia Game");
        primaryStage.show();
    }

    private void loadQuestion(StackPane root, Scene scene) {
        if (questionIndex < questions.length) {
            TriviaQuestion currentQuestion = questions[questionIndex];
            questionLabel.setText(currentQuestion.getQuestion());

            // Load the image
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(currentQuestion.getImagePath())));
            imageView.setFitWidth(scene.getWidth()); // Adjust image width to fit scene width
            imageView.setFitHeight(scene.getHeight()); // Adjust image height to fit scene height

            optionsBox.getChildren().clear();
            for (String option : currentQuestion.getOptions()) {
                Button optionButton = new Button(option);
                optionButton.getStyleClass().add("option-button");
                optionButton.setOnAction(e -> checkAnswer(optionButton.getText()));
                optionsBox.getChildren().add(optionButton);
            }
            // Create a VBox for image and video if they exist
            VBox mediaBox = new VBox(10);
            mediaBox.getStyleClass().add("content-box"); // Apply style class to the media box

            // Set position of optionsBox to top right corner
            StackPane.setAlignment(optionsBox, Pos.BOTTOM_CENTER);

            // Center question label
            StackPane.setAlignment(questionLabel, Pos.TOP_CENTER);

            // Add image and other components to the root
            root.getChildren().clear();
            root.getChildren().addAll(imageView, scoreLabel, questionLabel, optionsBox);

            // Check if it's the third question and add video
            if (questionIndex == 2 && currentQuestion.getVideoPath() != null) {
                // Load the video
                Media media = new Media(getClass().getResource(currentQuestion.getVideoPath()).toExternalForm());
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setAutoPlay(true);

                MediaView mediaView = new MediaView(mediaPlayer);
                mediaView.setFitWidth(scene.getWidth());
                mediaView.setFitHeight(scene.getHeight());

                // Add mediaView to root
                root.getChildren().add(mediaView);
            }

            questionIndex++; // Increment questionIndex here
        } else {
            // Display final score
            Label finalScoreLabel = new Label("Final Score: " + score + "/" + questions.length);
            finalScoreLabel.getStyleClass().add("final-score-label");
            StackPane.setAlignment(finalScoreLabel, Pos.CENTER);
            root.getChildren().clear();
            root.getChildren().add(finalScoreLabel);
            questionIndex=0;
        }

}




    private void checkAnswer(String selectedAnswer) {
        TriviaQuestion currentQuestion = questions[questionIndex - 1];
        if (currentQuestion.isCorrect(selectedAnswer)) {
            score++;
            scoreLabel.setText("Score: " + score);
        }

        if (questionIndex == questions.length) {
            // All questions have been answered, display final score
            displayFinalScore();
        } else {
            loadQuestion((StackPane) scoreLabel.getParent(), scoreLabel.getScene());
        }
    }

    private void displayFinalScore() {
        // Display final score
        Label finalScoreLabel = new Label("Final Score: " + score + "/" + questions.length);
        finalScoreLabel.getStyleClass().add("final-score-label");
        StackPane.setAlignment(finalScoreLabel, Pos.CENTER);

        ((StackPane) scoreLabel.getParent()).getChildren().add(finalScoreLabel); // Display final score
       // ((StackPane) scoreLabel.getParent()).getChildren().clear(); // Clear previous content
    }


    public static void main(String[] args) {
        launch(args);
    }
}

class TriviaQuestion {
    private String question;
    private String[] options;
    private String correctAnswer;
    private final String imagePath;
    private String videoPath;

    public TriviaQuestion(String question, String[] options, String correctAnswer, String imagePath) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.imagePath = imagePath;
    }

    public TriviaQuestion(String question, String[] options, String correctAnswer, String imagePath, String videoPath) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.imagePath = imagePath;
        this.videoPath = videoPath;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public boolean isCorrect(String selectedAnswer) {
        return selectedAnswer.equals(correctAnswer);
    }
}
