public class GameValidator {
    private final GameLogger logger;

    public GameValidator(GameLogger logger) {
        this.logger = logger;
    }

    public void validateCastleElement(CastleElement element) throws CastleException {
        try {
            if (element == null) {
                throw new CastleException("UNKNOWN", "Элемент замка не может быть null");
            }
            if (element.isDestroyed()) {
                throw new CastleException(element.getName(), "Элемент уже разрушен");
            }
            if (element.getHealth() <= 0) {
                throw new CastleException(element.getName(), "Здоровье элемента должно быть положительным");
            }
            logger.logInfo("Замковый элемент " + element.getName() + " валиден");
        } catch (CastleException e) {
            logger.logError("Ошибка валидации замкового элемента", e);
            throw e;
        }
    }

    public void validateSiegeEquipment(SiegeEquipment equipment, String action) throws SiegeException {
        try {
            if (equipment == null) {
                throw new SiegeException("UNKNOWN", action, "Осадное орудие не может быть null");
            }
            if (!equipment.isOperational) {
                throw new SiegeException(equipment.getName(), action, "Орудие неисправно");
            }
            if (equipment.ammunition <= 0) {
                throw new SiegeException(equipment.getName(), action, "Закончились боеприпасы");
            }
            logger.logInfo("Осадное орудие " + equipment.getName() + " готово к " + action);
        } catch (SiegeException e) {
            logger.logError("Ошибка валидации осадного орудия", e);
            throw e;
        }
    }

    public void validateDefender(Defender defender, String action) throws DefenseException {
        try {
            if (defender == null) {
                throw new DefenseException("UNKNOWN", "UNKNOWN", "Защитник не может быть null");
            }
            if (!defender.isAlive()) {
                throw new DefenseException(defender.getName(), defender.getClass().getSimpleName(), "Защитник мертв");
            }
            if (defender.getHealth() < 10) {
                throw new DefenseException(defender.getName(), defender.getClass().getSimpleName(),
                        "Здоровье защитника критически низкое: " + defender.getHealth());
            }
            logger.logInfo("Защитник " + defender.getName() + " готов к " + action);
        } catch (DefenseException e) {
            logger.logError("Ошибка валидации защитника", e);
            throw e;
        }
    }

    public void validateBattleAction(Combatable attacker, Combatable target) throws GameException {
        try {
            if (attacker == null || target == null) {
                throw new GameException("Атакующий или цель не могут быть null", "BATTLE_ERROR");
            }
            if (!attacker.isAlive()) {
                throw new GameException("Атакующий " + attacker.getName() + " мертв", "BATTLE_ERROR");
            }
            if (!target.isAlive()) {
                throw new GameException("Цель " + target.getName() + " уже уничтожена", "BATTLE_ERROR");
            }
            if (!attacker.canAttack(target)) {
                throw new GameException("Невозможно атаковать цель: " + target.getName(), "BATTLE_ERROR");
            }
            logger.logBattleAction("ВАЛИДАЦИЯ АТАКИ", attacker.getName(), "цель: " + target.getName() + " - разрешено");
        } catch (GameException e) {
            logger.logError("Ошибка валидации боевого действия", e);
            throw e;
        }
    }

    public void validateUpgrade(Upgradeable upgradable) throws GameException {
        try {
            if (!upgradable.canUpgrade()) {
                String name = "UNKNOWN";
                if (upgradable instanceof CastleElement) name = ((CastleElement)upgradable).getName();
                else if (upgradable instanceof Defender) name = ((Defender)upgradable).getName();
                else if (upgradable instanceof SiegeEquipment) name = ((SiegeEquipment)upgradable).getName();
                throw new GameException("Невозможно улучшить: " + name, "UPGRADE_ERROR");
            }
            logger.logInfo("Проверка улучшения прошла успешно");
        } catch (GameException e) {
            logger.logError("Ошибка валидации улучшения", e);
            throw e;
        }
    }
}