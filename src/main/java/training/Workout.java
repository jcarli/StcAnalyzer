package training;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class Workout {



    private LocalDateTime start;
    private LocalDateTime end;

    private int totalSlots;
    private int emptySlots;

    @JsonProperty("duration")
    private void unpackDuration(Map<String,Object> duration) {
        this.start = LocalDateTime.parse((String)duration.get("start"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        this.end = LocalDateTime.parse((String)duration.get("end"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
    }

    @JsonProperty("slots")
    private void unpackSlots(Map<String,Object> slots) {
        this.totalSlots = (Integer)slots.get("total");
        this.emptySlots = (Integer)slots.get("leftToBook");
    }


    public int totalSlots() {
        return totalSlots;
    }

    public int emptySlots() {
        return emptySlots;
    }
}
