public class Defender {
    protected String name;
    protected int health;
    protected int attack;
    protected int defense;
    protected boolean isAlive;
    protected String position;
    protected String type; // Тип защитника: "Лучник", "Рыцарь", "Ополченец"
    protected int specialAbilityCooldown;

    public Defender(String name, String type, int health, int attack, int defense) {
        this.name = name;
        this.type = type;
        this.health = health;
        this.attack = attack;
        this.defense = defense;
        this.isAlive = true;
        this.position = "стена";
        this.specialAbilityCooldown = 0;
    }

    // Методы защиты
    public void defendCastle() {
        if (!isAlive) return;

        switch (type) {
            case "Лучник":
                System.out.println(name + " выпускает стрелу по врагам!");
                break;
            case "Рыцарь":
                System.out.println(name + " атакует врагов с силой " + attack);
                break;
            case "Ополченец":
                System.out.println(name + " забрасывает врагов камнями!");
                break;
            default:
                System.out.println(name + " защищает замок!");
        }
    }

    public void specialAbility() {
        if (!isAlive || specialAbilityCooldown > 0) return;

        switch (type) {
            case "Лучник":
                System.out.println(name + " использует залповый выстрел! 3 стрелы выпущены одновременно!");
                specialAbilityCooldown = 3;
                break;
            case "Рыцарь":
                System.out.println(name + " совершает мощную рыцарскую атаку! Урон увеличен!");
                attack += 10;
                specialAbilityCooldown = 4;
                break;
            case "Ополченец":
                System.out.println(name + " призывает подкрепление!");
                specialAbilityCooldown = 5;
                break;
            default:
                System.out.println(name + " использует особую способность!");
        }
    }

    // Общие методы
    public void takeDamage(int damage) {
        int actualDamage = Math.max(1, damage - defense);
        health -= actualDamage;
        System.out.println(name + " получает урон " + actualDamage);

        if (health <= 0) {
            isAlive = false;
            System.out.println(name + " пал в бою!");
        }
    }

    public void heal(int amount) {
        if (isAlive) {
            health += amount;
            System.out.println(name + " исцелен на " + amount + " HP");
        }
    }

    public void moveTo(String newPosition) {
        this.position = newPosition;
        System.out.println(name + " перемещается к " + newPosition);
    }

    public void update() {
        if (specialAbilityCooldown > 0) {
            specialAbilityCooldown--;
        }
    }

    // Геттеры
    public String getName() { return name; }
    public String getType() { return type; }
    public int getHealth() { return health; }
    public boolean isAlive() { return isAlive; }
    public String getPosition() { return position; }
    public int getSpecialAbilityCooldown() { return specialAbilityCooldown; }

    @Override
    public String toString() {
        return name + " [" + type + "] " + health + " Здоровье; " +
                attack + " Атака; " + defense + " Защита; " +
                (specialAbilityCooldown > 0 ? " [Перезарядка: " + specialAbilityCooldown + "]" : "");
    }
}