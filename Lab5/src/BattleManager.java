import java.util.*;


public class BattleManager {
    private final GameLogger logger;
    private final GameValidator validator;
    private final GameCollection<Combatable> combatCollection;
    private final ResourceManager<Upgradeable> upgradeManager;
    private int turnCount;

    public BattleManager(String battleId) {
        this.logger = new GameLogger(battleId);
        this.validator = new GameValidator(logger);
        this.combatCollection = new GameCollection<>();
        this.upgradeManager = new ResourceManager<>(2000);
        this.turnCount = 0;
        logger.logInfo("Менеджер битвы инициализирован: " + battleId);
    }

    public void addCombatant(Combatable combatant) {
        try {
            combatCollection.add(combatant);
            if (combatant instanceof Upgradeable) {
                upgradeManager.addResource(combatant.getName(), (Upgradeable) combatant);
            }
            logger.logInfo("Боец добавлен: " + combatant.getName());
        } catch (Exception e) {
            logger.logError("Ошибка добавления бойца: " + combatant.getName(), e);
        }
    }

    public void performAttack(String attackerName, String targetName) {
        try {
            Combatable attacker = combatCollection.getByName(attackerName);
            Combatable target = combatCollection.getByName(targetName);

            if (attacker == null) throw new GameException("Атакующий не найден: " + attackerName, "BATTLE_ERROR");
            if (target == null) throw new GameException("Цель не найдена: " + targetName, "BATTLE_ERROR");

            validator.validateBattleAction(attacker, target);

            if (attacker instanceof Defender) validator.validateDefender((Defender) attacker, "атака");
            else if (attacker instanceof SiegeEquipment) validator.validateSiegeEquipment((SiegeEquipment) attacker, "атака");

            if (target instanceof CastleElement) validator.validateCastleElement((CastleElement) target);

            int damage = attacker.attack(target);
            logger.logBattleAction("УСПЕШНАЯ АТАКА", attackerName, "нанес " + damage + " урона " + targetName);

        } catch (GameException e) {
            logger.logException(e);
        } catch (Exception e) {
            logger.logError("Неожиданная ошибка при атаке", e);
        }
    }

    public void upgradeCombatant(String name) {
        try {
            Upgradeable upgradable = upgradeManager.getResource(name);
            if (upgradable == null) throw new GameException("Объект для улучшения не найден: " + name);

            validator.validateUpgrade(upgradable);

            if (upgradeManager.upgradeResource(name)) {
                logger.logBattleAction("УЛУЧШЕНИЕ", name, "до уровня " + upgradable.getLevel());
            } else {
                throw new GameException("Не удалось улучшить: " + name);
            }
        } catch (GameException e) {
            logger.logException(e);
        } catch (Exception e) {
            logger.logError("Неожиданная ошибка при улучшении", e);
        }
    }

    public void playTurn() {
        turnCount++;
        logger.logInfo("=== НАЧАЛО ХОДА " + turnCount + " ===");

        try {
            performDefenderActions();
            performSiegeActions();
            checkBattleState();
            logger.logInfo("=== ХОД " + turnCount + " ЗАВЕРШЕН ===");
        } catch (Exception e) {
            logger.logError("Критическая ошибка в ходе " + turnCount, e);
        }
    }

    private void performDefenderActions() {
        combatCollection.getActiveEntities().stream()
                .filter(e -> e instanceof Defender)
                .map(e -> (Defender) e)
                .forEach(defender -> {
                    try {
                        combatCollection.getActiveEntities().stream()
                                .filter(e -> e instanceof SiegeEquipment && e.isAlive())
                                .findFirst()
                                .ifPresent(target -> performAttack(defender.getName(), target.getName()));
                    } catch (Exception e) {
                        logger.logWarning("Ошибка действия защитника " + defender.getName() + ": " + e.getMessage());
                    }
                });
    }

    private void performSiegeActions() {
        combatCollection.getActiveEntities().stream()
                .filter(e -> e instanceof SiegeEquipment && e.isAlive())
                .map(e -> (SiegeEquipment) e)
                .forEach(siege -> {
                    try {
                        combatCollection.getActiveEntities().stream()
                                .filter(e -> e instanceof CastleElement && e.isAlive())
                                .findFirst()
                                .ifPresent(target -> performAttack(siege.getName(), target.getName()));
                    } catch (Exception e) {
                        logger.logWarning("Ошибка действия осадного орудия " + siege.getName() + ": " + e.getMessage());
                    }
                });
    }

    private void checkBattleState() {
        long defendersAlive = combatCollection.getActiveEntities().stream().filter(e -> e instanceof Defender).count();
        long castleElementsAlive = combatCollection.getActiveEntities().stream().filter(e -> e instanceof CastleElement && e.isAlive()).count();
        long siegeAlive = combatCollection.getActiveEntities().stream().filter(e -> e instanceof SiegeEquipment && e.isAlive()).count();

        logger.logInfo("Состояние битвы - Защитники: " + defendersAlive + ", Элементы замка: " + castleElementsAlive + ", Осадные орудия: " + siegeAlive);

        if (defendersAlive == 0 && castleElementsAlive == 0) {
            logger.logBattleAction("ПОБЕДА ОСАЖДАЮЩИХ", "СИСТЕМА", "Замок пал!");
        } else if (siegeAlive == 0) {
            logger.logBattleAction("ПОБЕДА ЗАЩИТНИКОВ", "СИСТЕМА", "Осада отбита!");
        }
    }

    public GameLogger getLogger() { return logger; }
    public GameCollection<Combatable> getCombatCollection() { return combatCollection; }
    public ResourceManager<Upgradeable> getUpgradeManager() { return upgradeManager; }
    public int getTurnCount() { return turnCount; }

    public void displayBattleStatus() {
        System.out.println("\n=== СТАТУС БИТВЫ (Ход " + turnCount + ") ===");
        System.out.println("Всего бойцов: " + combatCollection.size());
        System.out.println("Активных: " + combatCollection.getActiveEntities().size());
        System.out.println("Общая атака: " + combatCollection.getTotalAttackPower());
        System.out.println("Общая защита: " + combatCollection.getTotalDefensePower());

        System.out.println("\nПоследние события:");
        List<GameLogger.LogEntry> recentLogs = logger.getBattleLog();
        if (recentLogs.size() > 5) recentLogs = recentLogs.subList(recentLogs.size() - 5, recentLogs.size());
        recentLogs.forEach(log -> System.out.println("  " + log.getMessage()));
    }
}