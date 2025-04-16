package ru.shift.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.shift.dto.GameType;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static ru.shift.app.Main.log;

public class RecordManager {
    private static final String APP_DIR = System.getProperty("user.home") + File.separator + ".minesweeperShift";
    private static final String RECORDS_FILE = APP_DIR + File.separator + "records.json";
    private final Map<GameType, Record> records = new EnumMap<>(GameType.class);
    private final ObjectMapper mapper = new ObjectMapper();

    public RecordManager() {
        ensureAppDirExists();
        loadRecords();
    }

    private void ensureAppDirExists() {
        File dir = new File(APP_DIR);
        if (!dir.exists()) dir.mkdirs();
    }

    public void tryAddRecord(GameType gameType, String playerName, int timeInSeconds) {
        Record current = records.get(gameType);
        if (current == null || timeInSeconds < current.timeInSeconds) {
            records.put(gameType, new Record(playerName, timeInSeconds));
            saveRecords();
        }
    }

    public Record getRecord(GameType gameType) {
        return records.getOrDefault(gameType, new Record("Unknown", 999));
    }

    public boolean isNewRecord(GameType gameType, int time) {
        Record current = records.get(gameType);
        return current == null || time < current.timeInSeconds();
    }

    private void loadRecords() {
        File file = new File(RECORDS_FILE);
        if (file.exists()) {
            try {
                Map<String, Record> loaded = mapper.readValue(file, new TypeReference<>() {
                });
                for (Map.Entry<String, Record> entry : loaded.entrySet()) {
                    records.put(GameType.valueOf(entry.getKey()), entry.getValue());
                }
            } catch (IOException e) {
                log.error("Failed to load records", e);
            }
        }
    }

    private void saveRecords() {
        Map<String, Record> toSave = new HashMap<>();
        for (Map.Entry<GameType, Record> entry : records.entrySet()) {
            toSave.put(entry.getKey().name(), entry.getValue());
        }
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(RECORDS_FILE), toSave);
        } catch (IOException e) {
            log.error("Failed to save records", e);
        }
    }

    public record Record(String playerName, int timeInSeconds) {
    }
}
