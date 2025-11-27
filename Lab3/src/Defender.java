public class Defender implements Combatable, Movable, Comparable<Defender>, Cloneable, java.io.Serializable {
    private String name;
    private int health;
    private int maxHealth;
    private int attack;
    private int defense;
    private boolean isAlive;
    private int x, y;
    private int movementRange;

    public Defender(String name, int health, int attack, int defense) {
        this.name = name;
        this.maxHealth = health;
        this.health = health;
        this.attack = attack;
        this.defense = defense;
        this.isAlive = true;
        this.x = 0;
        this.y = 0;
        this.movementRange = 3;
    }

    // Реализация Combatable
    @Override
    public int attack(Combatable target) {
        if (canAttack(target)) {
            int damage = attack + (int)(Math.random() * 3);
            target.takeDamage(damage);
            System.out.println(name + " атакует с уроном " + damage);
            return damage;
        }
        return 0;
    }

    @Override
    public void takeDamage(int damage) {
        int actualDamage = Math.max(1, damage - defense);
        health -= actualDamage;
        System.out.println(name + " получает урон " + actualDamage);

        if (health <= 0) {
            isAlive = false;
            System.out.println(name + " пал в бою!");
        }
    }

    @Override
    public boolean canAttack(Combatable target) {
        return isAlive && target.isAlive();
    }

    @Override
    public int getAttack() {
        return attack;
    }

    @Override
    public int getDefense() {
        return defense;
    }

    @Override
    public boolean isAlive() {
        return isAlive;
    }

    // Реализация Movable
    @Override
    public boolean moveTo(int x, int y) {
        if (canMoveTo(x, y)) {
            this.x = x;
            this.y = y;
            System.out.println(name + " переместился в (" + x + ", " + y + ")");
            return true;
        }
        return false;
    }

    @Override
    public boolean canMoveTo(int x, int y) {
        int distance = Math.abs(this.x - x) + Math.abs(this.y - y);
        return distance <= movementRange;
    }

    @Override
    public int getMovementRange() {
        return movementRange;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    // Реализация Comparable
    @Override
    public int compareTo(Defender other) {
        return Integer.compare(this.attack, other.attack);
    }

    // Реализация Cloneable
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    @Override
    public String toString() {
        return name + " [" + health + "/" + maxHealth + " HP] " +
                "Атака: " + attack + " Защита: " + defense +
                " Позиция: (" + x + ", " + y + ")";
    }
}