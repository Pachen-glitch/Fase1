package main.java.com.edu.interpreter.engine;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class ScriptParser {
    public List<String> parse(String script) {
        if (script == null || script.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(script.trim().split("\\s+"));
    }
}