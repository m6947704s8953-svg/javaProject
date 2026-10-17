public class UserProfile {

    private String name;
    private int quizzesTaken;
    private int highestScore;
    private double highestPercentage;

    private int totalScore;
    private int fullScoreCount = 0;

    public UserProfile(String name) {
        this.name = name;
    }

    public void addResult(int score, double percentage) {
        quizzesTaken++;
        totalScore += score;

        if (score > highestScore) {
            highestScore = score;
        }

	if (percentage > highestPercentage) {
	    highestPercentage = percentage;
	}

	if (percentage == 100) {
	    fullScoreCount++;
	}
    }

    public String getName() {
        return name;
    }

    public int getQuizzesTaken() {
        return quizzesTaken;
    }

    public String getHighestScore() {
        return highestScore + "/20 (" + highestPercentage + "%)";
    }

    public double getAverageScore() {
	if (quizzesTaken == 0) return 0;
	return totalScore / quizzesTaken;
    }

    public double getAveragePercentage() {
        if (quizzesTaken == 0) return 0;
	double averageScore = getAverageScore();
        return averageScore / 20 * 100;
    }

    public String getAvgScoreString() {
	return String.format("%.2f", getAverageScore()) + "/20 (" + String.format("%.2f", getAveragePercentage()) + "%)";
    }

    public String getFullScoreCount() {
	return fullScoreCount + " time(s)";
    }

}