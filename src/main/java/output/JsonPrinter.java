package output;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Paths;

public class JsonPrinter implements OutputHandler {

    @Override
    public void output(String path, Object value) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get(path).toFile(), value);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write JSON", e);
        }
    }
}
