package api;


import com.google.common.base.Suppliers;
import output.OutputHandler;
import training.Club;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class StcAnalyzerImpl implements StcAnalyzer {

    private final Supplier<List<Club>> clubSupplier;
    private final OutputHandler outputHandler;

    public StcAnalyzerImpl(Supplier<List<Club>> clubSupplier, OutputHandler outputHandler) {
        this.clubSupplier = Suppliers.memoizeWithExpiration(clubSupplier::get, 30, TimeUnit.MINUTES);
        this.outputHandler = outputHandler;
    }

    public List<Club> clubs() {
        return clubSupplier.get();
    }

    @Override
    public void clubsWithoutWorkouts() {
        List<Club> clubsWithoutWorkouts = clubSupplier.get().stream()
                .filter(club -> club.workouts().isEmpty())
                .collect(Collectors.toList());
        outputHandler.output("clubs-without-workouts.json", clubsWithoutWorkouts);
    }

    @Override
    public void workoutsPerClub() {
        Map<String, Integer> workoutsPerClub = clubSupplier.get().stream()
                .collect(
                        Collectors.toMap(
                                Club::name,
                                club -> club.workouts().size()));

        outputHandler.output("workouts-per-club.json", workoutsPerClub);
    }

    @Override
    public void fullyBookedWorkoutsPerClub() {
        Map<String, Double> fullyBookedWorkouts = clubSupplier.get().stream()
                .collect(
                        Collectors.toMap(
                                Club::name,
                                club -> {
                                    int nrOfWorkouts = club.workouts().size();
                                    long fullWorkouts = club.workouts().stream()
                                            .filter(workout -> workout.emptySlots() == 0)
                                            .count();

                                    return (double)fullWorkouts / nrOfWorkouts * 100;
                                }));
        outputHandler.output("fully-booked-workouts.json", fullyBookedWorkouts);
    }
}
