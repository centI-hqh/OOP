public class Gate extends CastleElement {
    private boolean isOpen;
    private boolean isBarricaded;
    private int reinforcement;

    public Gate(String name, int health, int defense) {
        super(name, health, defense);
        this.isOpen = false;
        this.isBarricaded = true;
        this.reinforcement = 50;
    }

    @Override
    public void defend() {
        if (!isDestroyed && isBarricaded) {
            System.out.println(name + " забаррикадированы и держат оборону");
        }
    }

    @Override
    public void takeDamage(int damage) {
        if (!isDestroyed) {
            int actualDamage = Math.max(1, damage - defense - (isBarricaded ? reinforcement : 0));
            health -= actualDamage;
            System.out.println(name + " получает урон " + actualDamage +
                    " (защита: " + defense + ", усиление: " +
                    (isBarricaded ? reinforcement : 0) + ")");

            if (health <= 0) {
                isDestroyed = true;
                isOpen = true; // Ворота автоматически открываются при разрушении
                System.out.println(name + " разрушены! Замок открыт для штурма!");
            }

            // Усиление уменьшается при повреждениях
            if (getHealthPercentage() < 60 && reinforcement > 0) {
                reinforcement = Math.max(0, reinforcement - 10);
                System.out.println("Усиление ворот ослаблено до: " + reinforcement);
            }
        }
    }

    @Override
    public String getStatus() {
        if (isDestroyed) return "[РАЗРУШЕНЫ]";
        if (isOpen) return "[ОТКРЫТЫ]";
        if (isBarricaded) return "[ЗАБАРРИКАДИРОВАНЫ]";
        return "[ЗАКРЫТЫ]";
    }



}