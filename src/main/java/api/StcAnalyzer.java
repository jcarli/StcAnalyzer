package api;

import training.Club;

import java.util.List;
import java.util.Map;

public interface StcAnalyzer {

    void clubsWithoutWorkouts();
    void workoutsPerClub();
    void fullyBookedWorkoutsPerClub();
}
