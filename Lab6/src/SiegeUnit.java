import java.util.*;

public abstract class SiegeUnit extends GameEntity {
    protected int health;
    protected int maxHealth;
    protected int attack;
    protected int defense;
    protected int movementRange;
    protected int experience;
    protected int level;
    protected MovementSystem movementSystem;
    protected boolean isAttacker;

    public SiegeUnit(String name, Position position, int health, int attack, int defense, int movementRange, boolean isAttacker) {
        super(name, position);
        this.maxHealth = health;
        this.health = health;
        this.attack = attack;
        this.defense = defense;
        this.movementRange = movementRange;
        this.experience = 0;
        this.level = 1;
        this.isAttacker = isAttacker;
    }

    public void setMovementSystem(MovementSystem movementSystem) {
        this.movementSystem = movementSystem;
    }

    public boolean moveTo(Position target) {
        if (movementSystem == null) return false;
        return movementSystem.moveUnit(this, target);
    }

    public boolean moveAlongPath(Position target) {
        if (movementSystem == null) return false;
        return movementSystem.moveUnitAlongPath(this, target);
    }

    public List<Position> getReachablePositions() {
        if (movementSystem == null) return new ArrayList<>();
        return movementSystem.getReachablePositions(this);
    }

    public List<Position> getPathTo(Position target) {
        if (movementSystem == null) return new ArrayList<>();
        return movementSystem.getOptimalPath(this, target);
    }

    public boolean canMoveTo(Position target) {
        if (movementSystem == null) return false;
        return movementSystem.canMoveTo(this, target);
    }

    public void takeDamage(int damage) {
        this.health = Math.max(0, this.health - damage);
    }

    public void heal(int amount) {
        this.health = Math.min(maxHealth, this.health + amount);
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int getMovementRange() { return movementRange; }
    public int getHealth() { return health; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public boolean isAttacker() { return isAttacker; }

    public abstract void attack(SiegeUnit target);

    @Override
    public void update() {
        // Базовая логика обновления
        if (health < maxHealth * 0.3) {
            heal(2); // Автоматическое лечение при низком здоровье
        }
    }
}

class Knight extends SiegeUnit {
    public Knight(String name, Position position, boolean isAttacker) {
        super(name, position, 100, 25, 15, 3, isAttacker);
    }

    @Override
    public void attack(SiegeUnit target) {
        // УБИРАЕМ накапливающийся бонус к атаке!
        int baseAttack = 25; // Базовая атака всегда 25

        // Учитываем бонус защиты цели
        int damage = Math.max(1, baseAttack - target.getDefense());
        target.takeDamage(damage);
        System.out.println(name + " атакует " + target.getName() +
                " и наносит " + damage + " урона!");
    }

    @Override
    public void update() {
        super.update();
    }
}

class Archer extends SiegeUnit {
    private int arrows;

    public Archer(String name, Position position, boolean isAttacker) {
        super(name, position, 60, 20, 8, 2, isAttacker);
        this.arrows = 10;
    }

    @Override
    public void attack(SiegeUnit target) {
        if (arrows > 0) {
            int damage = Math.max(1, attack - target.getDefense());
            target.takeDamage(damage);
            arrows--;
            System.out.println(name + " стреляет в " + target.getName() +
                    " и наносит " + damage + " урона! Осталось стрел: " + arrows);
        } else {
            System.out.println(name + " не может атаковать: закончились стрелы!");
        }
    }

    public void replenishArrows(int amount) {
        arrows += amount;
    }

    @Override
    public void update() {
        super.update();
        // Лучники получают бонус к защите на башнях
        Cell currentCell = null; // Нужно получить ссылку на игровое поле
        if (currentCell != null && currentCell.getTerrainType() == TerrainType.TOWER) {
            defense += 10;
        }
    }
}

class SiegeWeapon extends SiegeUnit {
    private boolean isAssembled;

    public SiegeWeapon(String name, Position position, boolean isAttacker) {
        super(name, position, 80, 35, 5, 1, isAttacker);
        this.isAssembled = false;
    }

    @Override
    public void attack(SiegeUnit target) {
        if (isAssembled) {
            int damage = Math.max(5, attack - target.getDefense() / 3);
            target.takeDamage(damage);
            System.out.println(name + " стреляет по " + target.getName() + " и наносит " + damage + " урона!");
        } else {
            System.out.println(name + " не может атаковать: требуется сборка!");
        }
    }

    public void assemble() {
        isAssembled = true;
        System.out.println(name + " собран и готов к бою!");
    }

    public void disassemble() {
        isAssembled = false;
    }

    public boolean isAssembled() { return isAssembled; }

    @Override
    public boolean moveTo(Position target) {
        if (isAssembled) {
            disassemble();
            System.out.println(name + " разобран для перемещения");
        }
        return super.moveTo(target);
    }
}