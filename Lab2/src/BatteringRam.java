public class BatteringRam extends SiegeEquipment {
    private int ramStrength;
    private boolean isProtected;

    public BatteringRam(String name, int damage, int range) {
        super(name, damage, range, 1); // Таран не использует боеприпасы традиционно
        this.ramStrength = 100;
        this.isProtected = true;
    }

    @Override
    public void attack(CastleElement target) {
        if (!isOperational) {
            System.out.println(name + " не может атаковать - сломан!");
            return;
        }

        if (!(target instanceof Gate)) {
            System.out.println(name + " эффективен только против ворот!");
            return;
        }

        System.out.println(name + " таранит " + target.getName() + " с силой " + ramStrength);
        target.takeDamage(damage + ramStrength);

        // Уменьшение прочности тарана
        ramStrength -= 10;
        if (ramStrength <= 0) {
            breakDown();
        }

        // Потеря защиты при атаке
        if (isProtected && Math.random() < 0.3) {
            isProtected = false;
            System.out.println("Защита " + name + " разрушена!");
        }
    }

    @Override
    public void reload() {

        if (ramStrength < 50) {
            ramStrength = 100;
            isProtected = true;
            System.out.println(name + " усилен и защищен");
        }
    }

    @Override
    public String getType() {
        return "Таран";
    }

}