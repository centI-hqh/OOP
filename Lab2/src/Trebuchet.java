public class Trebuchet extends SiegeEquipment {
    private int counterweight;
    private boolean isAimed;

    public Trebuchet(String name, int damage, int range) {
        super(name, damage, range, 5);
        this.counterweight = 1000;
        this.isAimed = false;
    }

    @Override
    public void attack(CastleElement target) {
        if (!isOperational) {
            System.out.println(name + " не может атаковать - сломан!");
            return;
        }

        if (ammunition <= 0) {
            System.out.println(name + ": нет боеприпасов!");
            return;
        }

        if (!isAimed) {
            System.out.println(name + " должен быть прицелен перед атакой!");
            return;
        }

        ammunition--;
        isAimed = false;
        int actualDamage = damage + (counterweight / 100);

        System.out.println(name + " метает снаряд с противовесом " + counterweight + " кг");
        System.out.println("Снаряд летит к " + target.getName() + " с уроном " + actualDamage);

        target.takeDamage(actualDamage);
    }

    @Override
    public void reload() {
        if (ammunition > 0 && !isAimed) {
            isAimed = true;
            System.out.println(name + " заряжен и прицелен");
        }
    }

    @Override
    public String getType() {
        return "Требушет";
    }

    public void setCounterweight(int weight) {
        if (weight >= 500 && weight <= 2000) {
            this.counterweight = weight;
            this.damage = weight / 20; // Урон зависит от противовеса
            this.range = weight / 50;  // Дальность тоже зависит
        }
    }

    public boolean isAimed() {
        return isAimed;
    }
}