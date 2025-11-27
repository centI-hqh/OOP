public class Wall extends CastleElement {
    private int height;
    private boolean hasBattlements;

    public Wall(String name, int health, int defense, int height) {
        super(name, health, defense);
        this.height = height;
        this.hasBattlements = true;
    }

    @Override
    public void defend() {
        if (!isDestroyed && hasBattlements) {
            System.out.println(name + " обеспечивает укрытие лучникам");
        }
    }

    @Override
    public void takeDamage(int damage) {
        if (!isDestroyed) {
            int actualDamage = Math.max(1, damage - defense);
            health -= actualDamage;
            System.out.println(name + " получает урон " + actualDamage + " (защита: " + defense + ")");

            if (health <= 0) {
                isDestroyed = true;
                System.out.println(name + " разрушена!");
            }

            // Бойницы разрушаются при серьезном повреждении
            if (getHealthPercentage() < 30 && hasBattlements) {
                hasBattlements = false;
                System.out.println("Бойницы на " + name + " разрушены!");
            }
        }
    }

    @Override
    public String getStatus() {
        if (isDestroyed) return "[РАЗРУШЕНА]";
        if (!hasBattlements) return "[БЕЗ БОЙНИЦ]";
        if (getHealthPercentage() < 50) return "[ПОВРЕЖДЕНА]";
        return "[В НОРМЕ]";
    }

}