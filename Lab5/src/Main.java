import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== ЛАБОРАТОРНАЯ РАБОТА 5: СРЕДНЕВЕКОВАЯ ОСАДА - ИСКЛЮЧЕНИЯ ===\n");

        try {
            BattleManager battleManager = new BattleManager("SIEGE_001");
            initializeBattle(battleManager);

            for (int i = 0; i < 5; i++) {
                battleManager.playTurn();
                battleManager.displayBattleStatus();
                demonstrateExceptionHandling(battleManager, i);
                System.out.println();
            }

            displayFinalStats(battleManager);

        } catch (Exception e) {
            // Объединяем оба catch в один, так как GameException никогда не будет выброшено в main
            System.err.println("Критическая ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void initializeBattle(BattleManager battleManager) {
        try {
            CastleElement tower = new Tower("Сторожевая башня", 300, 10, 50, battleManager.getLogger());
            CastleElement wall = new Wall("Северная стена", 500, 15, 10, battleManager.getLogger());
            CastleElement gate = new Wall("Главные ворота", 600, 20, 12, battleManager.getLogger());

            SiegeEquipment catapult = new Catapult("Громовержец", 80, 150, 10, battleManager.getLogger());
            SiegeEquipment trebuchet = new Catapult("Требушет", 90, 170, 8, battleManager.getLogger());

            Defender archer = new Defender("Леголас", "Лучник", 80, 25, 8, battleManager.getLogger());
            Defender knight = new Defender("Артур", "Рыцарь", 150, 35, 20, battleManager.getLogger());

            battleManager.addCombatant(tower);
            battleManager.addCombatant(wall);
            battleManager.addCombatant(gate);
            battleManager.addCombatant(catapult);
            battleManager.addCombatant(trebuchet);
            battleManager.addCombatant(archer);
            battleManager.addCombatant(knight);

        } catch (Exception e) {
            battleManager.getLogger().logError("Ошибка инициализации битвы", e);
        }
    }

    private static void demonstrateExceptionHandling(BattleManager battleManager, int turn) {
        try {
            switch (turn) {
                case 0:
                    System.out.println(">>> Демонстрация: Атака несуществующего бойца");
                    battleManager.performAttack("Леголас", "Призрак");
                    break;
                case 1:
                    System.out.println(">>> Демонстрация: Многократное улучшение");
                    battleManager.upgradeCombatant("Сторожевая башня");
                    battleManager.upgradeCombatant("Сторожевая башня");
                    battleManager.upgradeCombatant("Сторожевая башня");
                    battleManager.upgradeCombatant("Сторожевая башня");
                    break;
                case 2:
                    System.out.println(">>> Демонстрация: Атака разрушенной цели");
                    battleManager.performAttack("Громовержец", "Северная стена");
                    battleManager.performAttack("Громовержец", "Северная стена");
                    break;
                case 3:
                    System.out.println(">>> Демонстрация: Использование сломанного орудия");
                    SiegeEquipment equipment = (SiegeEquipment) battleManager.getCombatCollection().getByName("Громовержец");
                    if (equipment != null) {
                        for (int i = 0; i < 5; i++) equipment.takeDamage(1000);
                        battleManager.performAttack("Громовержец", "Главные ворота");
                    }
                    break;
            }
        } catch (Exception e) {
            System.out.println("Демонстрация исключения: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }

    private static void displayFinalStats(BattleManager battleManager) {
        System.out.println("\n=== ИТОГОВАЯ СТАТИСТИКА ===");
        System.out.println("Всего ходов: " + battleManager.getTurnCount());
        System.out.println("Записей в логе: " + battleManager.getLogger().getLogHistory().size());
        System.out.println("Боевых событий: " + battleManager.getLogger().getBattleLog().size());

        System.out.println("\nИстория ошибок:");
        List<GameLogger.LogEntry> errorLogs = battleManager.getLogger().getErrorLog();
        if (errorLogs.isEmpty()) {
            System.out.println("  Ошибок не зафиксировано!");
        } else {
            errorLogs.forEach(log -> System.out.println("  " + log.getMessage()));
        }

        System.out.println("\nТипы исключений:");
        Map<String, Long> exceptionTypes = errorLogs.stream()
                .map(log -> log.getMessage())
                .filter(msg -> msg.contains("Игровое исключение"))
                .map(msg -> {
                    if (msg.contains("[CASTLE_ERROR]")) return "CastleException";
                    if (msg.contains("[SIEGE_ERROR]")) return "SiegeException";
                    if (msg.contains("[DEFENSE_ERROR]")) return "DefenseException";
                    if (msg.contains("[BATTLE_ERROR]")) return "GameException (Battle)";
                    return "GameException";
                })
                .collect(Collectors.groupingBy(type -> type, Collectors.counting()));

        exceptionTypes.forEach((type, count) -> System.out.println("  " + type + ": " + count + " раз"));
    }
}