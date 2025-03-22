import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

class Question {
    private String questionText;
    private String[] options;
    private int correctAnswerIndex;

    public Question(String questionText, String[] options, int correctAnswerIndex) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options;
    }

    public boolean isCorrectAnswer(int answerIndex) {
        return answerIndex == correctAnswerIndex;
    }
}

class Quiz {
    private List<Question> questions;
    private int score;

    public Quiz() {
        questions = new ArrayList<>();
        score = 0;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        Timer timer = new Timer();

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            System.out.println("\nQuestion " + (i + 1) + ": " + question.getQuestionText());
            String[] options = question.getOptions();
            for (int j = 0; j < options.length; j++) {
                System.out.println((j + 1) + ". " + options[j]);
            }

            // Timer for the question
            final int questionIndex = i;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("\nTime's up! Moving to the next question.");
                    // Automatically submit an answer if time runs out
                    submitAnswer(questionIndex, -1);
                }
            }, 10000); // 10 seconds for each question

            System.out.print("Your answer (1-" + options.length + "): ");
            int userAnswer = scanner.nextInt() - 1; // Convert to 0-based index
            timer.cancel(); // Cancel the timer if the user answers in time
            submitAnswer(questionIndex, userAnswer);
        }

        displayResult();
        scanner.close();
    }

    private void submitAnswer(int questionIndex, int userAnswer) {
        if (userAnswer >= 0 && questions.get(questionIndex).isCorrectAnswer(userAnswer)) {
            System.out.println("Correct!");
            score++;
        } else if (userAnswer == -1) {
            System.out.println("No answer submitted for this question.");
        } else {
            System.out.println("Incorrect! The correct answer was: " + (questions.get(questionIndex).isCorrectAnswer(questions.get(questionIndex).correctAnswerIndex) ? questions.get(questionIndex).getOptions()[questions.get(questionIndex).correctAnswerIndex] : "N/A"));
        }
    }

    private void displayResult() {
        System.out.println("\nQuiz Finished!");
        System.out.println("Your final score is: " + score + " out of " + questions.size());
    }
}

public class QuizApplication {
    public static void main(String[] args) {
        Quiz quiz = new Quiz();

        // Adding questions to the quiz
        quiz.addQuestion(new Question("What is the capital of France?", new String[]{"Berlin", "Madrid", "Paris", "Rome"}, 2));
        quiz.addQuestion(new Question("What is 2 + 2?", new String[]{"3", "4", "5", "6"}, 1));
        quiz.addQuestion(new Question("What is the largest planet in our solar system?", new String[]{"Earth", "Mars", "Jupiter", "Saturn"}, 2));
        quiz.addQuestion(new Question("What is the chemical symbol for water?", new String[]{"H2O", "O2", "CO2", "NaCl"}, 0));
        quiz.addQuestion(new Question("Who wrote 'Hamlet'?", new String[]{"Charles Dickens", "Mark Twain", "William Shakespeare", "Jane Austen"}, 2));

        // Start the quiz
        quiz.start();
    }
}
