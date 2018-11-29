package domain.interfaces;

import java.io.IOException;
import java.util.List;

public interface IJsonHandler {
    void handle(String[] parameters) throws IOException;
}
