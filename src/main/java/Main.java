import api.AnalyzerFactory;
import api.StcAnalyzer;

public class Main {
    public static void main(String[] args) {
        StcAnalyzer analyzer = AnalyzerFactory.createAnalyzer();
        analyzer.workoutsPerClub();
        analyzer.clubsWithoutWorkouts();
        analyzer.fullyBookedWorkoutsPerClub();
    }
}
