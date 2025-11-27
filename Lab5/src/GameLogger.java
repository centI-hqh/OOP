import java.util.*;
import java.util.logging.*;
import java.time.LocalDateTime;

public class GameLogger {
    private static final Logger logger = Logger.getLogger(GameLogger.class.getName());
    private final String gameId;
    private final List<LogEntry> logHistory;

    public GameLogger(String gameId) {
        this.gameId = gameId;
        this.logHistory = new ArrayList<>();
        setupLogger();
    }

    private void setupLogger() {
        try {
            FileHandler fileHandler = new FileHandler("medieval_siege_" + gameId + ".log");
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
        } catch (Exception e) {
            System.err.println("Ошибка настройки логгера: " + e.getMessage());
        }
    }

    public void logInfo(String message) {
        String fullMessage = String.format("[%s] %s", gameId, message);
        logger.info(fullMessage);
        addToHistory("INFO", fullMessage);
    }

    public void logWarning(String message) {
        String fullMessage = String.format("[%s] %s", gameId, message);
        logger.warning(fullMessage);
        addToHistory("WARNING", fullMessage);
    }

    public void logError(String message, Throwable exception) {
        String fullMessage = String.format("[%s] %s", gameId, message);
        logger.severe(fullMessage);
        logger.log(Level.SEVERE, message, exception);
        addToHistory("ERROR", fullMessage + " - " + exception.getMessage());
    }

    public void logException(GameException exception) {
        String message = String.format("Игровое исключение [%s]: %s",
                exception.getErrorCode(), exception.getMessage());
        logger.severe(message);
        addToHistory("EXCEPTION", message);

        if (exception instanceof CastleException) {
            CastleException ce = (CastleException) exception;
            logger.warning("Затронутый элемент замка: " + ce.getCastleElementName());
        } else if (exception instanceof SiegeException) {
            SiegeException se = (SiegeException) exception;
            logger.warning("Осадное орудие: " + se.getSiegeEquipmentName() + ", действие: " + se.getAction());
        } else if (exception instanceof DefenseException) {
            DefenseException de = (DefenseException) exception;
            logger.warning("Защитник: " + de.getDefenderName() + ", тип: " + de.getDefenseType());
        }
    }

    public void logBattleAction(String action, String participant, String details) {
        String message = String.format("[БОЙ] %s: %s - %s", participant, action, details);
        logger.info(message);
        addToHistory("BATTLE", message);
    }

    private void addToHistory(String level, String message) {
        logHistory.add(new LogEntry(level, message, LocalDateTime.now()));
    }

    public List<LogEntry> getLogHistory() { return new ArrayList<>(logHistory); }
    public List<LogEntry> getBattleLog() {
        return logHistory.stream()
                .filter(log -> log.getLevel().equals("BATTLE"))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public List<LogEntry> getErrorLog() {
        return logHistory.stream()
                .filter(log -> log.getLevel().equals("ERROR") || log.getLevel().equals("EXCEPTION"))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public static class LogEntry {
        private final String level;
        private final String message;
        private final LocalDateTime timestamp;

        public LogEntry(String level, String message, LocalDateTime timestamp) {
            this.level = level;
            this.message = message;
            this.timestamp = timestamp;
        }

        public String getLevel() { return level; }
        public String getMessage() { return message; }
        public LocalDateTime getTimestamp() { return timestamp; }

        @Override
        public String toString() {
            return String.format("[%s] %s: %s", timestamp, level, message);
        }
    }
}