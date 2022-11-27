package api;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;
import output.OutputHandler;
import training.Club;
import training.Workout;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StcAnalyzerImplTest {

    @Test
    public void happyTest() {
        OutputHandler handler = Mockito.mock(OutputHandler.class);
        List<Club> clubs = Collections.singletonList(new Club());
        StcAnalyzerImpl instance = new StcAnalyzerImpl(() -> clubs, handler);
        Assert.assertEquals(instance.clubs(), clubs);
    }

    @Test
    public void testClubsWithoutWorkouts() {
        OutputHandler handler = Mockito.mock(OutputHandler.class);
        Club club1 = Mockito.mock(Club.class);
        Mockito.when(club1.workouts()).thenReturn(Arrays.asList(Mockito.mock(Workout.class)));
        Club club2 = Mockito.mock(Club.class);
        Mockito.when(club2.workouts()).thenReturn(Arrays.asList());

        List<Club> clubs = Arrays.asList(club1, club2);
        StcAnalyzerImpl instance = new StcAnalyzerImpl(() -> clubs, handler);
        instance.clubsWithoutWorkouts();
        Mockito.verify(handler).output(Mockito.anyString(), Mockito.eq(Arrays.asList(club2)));
    }

    @Test
    public void testWorkoutsPerClub() {
        OutputHandler handler = Mockito.mock(OutputHandler.class);
        Club club1 = Mockito.mock(Club.class);
        Mockito.when(club1.workouts()).thenReturn(Arrays.asList(Mockito.mock(Workout.class)));
        Mockito.when(club1.name()).thenReturn("club1");
        Club club2 = Mockito.mock(Club.class);
        Mockito.when(club2.workouts()).thenReturn(Arrays.asList());
        Mockito.when(club2.name()).thenReturn("club2");

        List<Club> clubs = Arrays.asList(club1, club2);
        StcAnalyzerImpl instance = new StcAnalyzerImpl(() -> clubs, handler);
        instance.workoutsPerClub();
        Mockito.verify(handler).output(Mockito.anyString(),
                Mockito.eq(Map.of(
                        "club1", 1,
                        "club2", 0
                )
        ));
    }

    @Test
    public void testFullyBookedWorkoutsPerClub() {
        OutputHandler handler = Mockito.mock(OutputHandler.class);
        Club club1 = Mockito.mock(Club.class);
        List<Workout> workouts1 = Arrays.asList(mockedWorkout(5, 100), mockedWorkout(0, 10));
                Mockito.when(club1.workouts()).thenReturn(workouts1);
        Mockito.when(club1.name()).thenReturn("club1");
        Club club2 = Mockito.mock(Club.class);
        List<Workout> workouts2 = Arrays.asList(mockedWorkout(0, 10), mockedWorkout(0, 100));
        Mockito.when(club2.workouts()).thenReturn(workouts2);
        Mockito.when(club2.name()).thenReturn("club2");

        List<Club> clubs = Arrays.asList(club1, club2);
        StcAnalyzerImpl instance = new StcAnalyzerImpl(() -> clubs, handler);
        instance.fullyBookedWorkoutsPerClub();
        Mockito.verify(handler).output(Mockito.anyString(),
                Mockito.eq(Map.of(
                                "club1", 50.0,
                                "club2", 100.0
                        )
                ));
    }

    private Workout mockedWorkout(int empty, int total) {
        Workout mock = Mockito.mock(Workout.class);
        Mockito.when(mock.emptySlots()).thenReturn(empty);
        Mockito.when(mock.totalSlots()).thenReturn(total);
        return mock;
    }
}