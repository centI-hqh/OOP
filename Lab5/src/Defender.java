public class Defender implements Combatable, Upgradeable {
    private String name;
    private String type;
    private int health;
    private int maxHealth;
    private int attack;
    private int defense;
    private int level;
    private boolean isAlive;
    private final GameLogger logger;

    public Defender(String name, String type, int health, int attack, int defense, GameLogger logger) {
        this.name = name;
        this.type = type;
        this.maxHealth = health;
        this.health = health;
        this.attack = attack;
        this.defense = defense;
        this.level = 1;
        this.isAlive = true;
        this.logger = logger;
    }

    @Override
    public int attack(Combatable target) {
        try {
            if (!isAlive) throw new DefenseException(name, type, "Мертвый защитник не может атаковать");

            int damage = attack + (int)(Math.random() * 5);
            target.takeDamage(damage);
            logger.logBattleAction("АТАКА ЗАЩИТНИКА", name, "урон: " + damage + ", цель: " + target.getName());
            return damage;
        } catch (DefenseException e) {
            logger.logException(e);
            return 0;
        }
    }

    @Override
    public void takeDamage(int damage) {
        try {
            if (!isAlive) throw new DefenseException(name, type, "Нельзя нанести урон мертвому защитнику");

            int actualDamage = Math.max(1, damage - defense);
            health -= actualDamage;
            logger.logBattleAction("УРОН ЗАЩИТНИКУ", name,
                    String.format("урон: %d, здоровье: %d/%d", actualDamage, health, maxHealth));

            if (health <= 0) {
                isAlive = false;
                logger.logBattleAction("ГИБЕЛЬ ЗАЩИТНИКА", name, "защитник пал в бою!");
            }
        } catch (DefenseException e) {
            logger.logException(e);
        }
    }

    @Override
    public boolean canAttack(Combatable target) { return isAlive; }

    @Override
    public int getAttack() { return attack; }

    @Override
    public int getDefense() { return defense; }

    @Override
    public boolean isAlive() { return isAlive; }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean upgrade() {
        try {
            if (!canUpgrade()) throw new DefenseException(name, type, "Невозможно улучшить защитника");

            level++;
            maxHealth += 20;
            health = maxHealth;
            attack += 5;
            defense += 3;
            logger.logInfo(name + " улучшен до уровня " + level + "!");
            return true;
        } catch (DefenseException e) {
            logger.logException(e);
            return false;
        }
    }

    @Override
    public int getLevel() { return level; }

    @Override
    public int getMaxLevel() { return 5; }

    @Override
    public boolean canUpgrade() { return isAlive && level < getMaxLevel(); }

    @Override
    public int getUpgradeCost() { return level * 50; }

    @Override
    public String getLevelBonuses() { return "+20 HP, +5 атака, +3 защита"; }

    public void heal(int amount) {
        try {
            if (!isAlive) throw new DefenseException(name, type, "Нельзя исцелить мертвого защитника");
            health = Math.min(maxHealth, health + amount);
            logger.logInfo(name + " исцелен на " + amount + " HP");
        } catch (DefenseException e) {
            logger.logException(e);
        }
    }

    public int getHealth() { return health; }

    @Override
    public String toString() {
        return name + " [" + type + "] Ур." + level + " [" + health + "/" + maxHealth + " HP] " +
                "Атака: " + attack + " Защита: " + defense + (isAlive ? " [ЖИВ]" : " [МЕРТВ]");
    }
}