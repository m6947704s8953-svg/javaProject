public class QuizAttempt {
    private String name;
    private int score;
    private double percentage;

    public QuizAttempt(String name, int score, double percentage) {
	this.name = name;
        this.score = score;
        this.percentage = percentage;
    }

    public String getName() {
	return name;
    }

    public int getScore() {
        return score;
    }

    public double getPercentage() {
        return percentage;
    }
}