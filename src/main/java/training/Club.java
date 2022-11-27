package training;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Club {


    @JsonProperty
    private int id;
    @JsonProperty
    private String name;

    @JsonIgnore
    private List<Workout> workouts;
    public int id() {
        return id;
    }

    public String name() {
        return name;
    }

    public List<Workout> workouts() {
        return workouts;
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
    }

}
