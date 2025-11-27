public abstract class SiegeEquipment {
    protected String name;
    protected int damage;
    protected int range;
    protected int ammunition;
    protected boolean isOperational;

    public SiegeEquipment(String name, int damage, int range, int ammunition) {
        this.name = name;
        this.damage = damage;
        this.range = range;
        this.ammunition = ammunition;
        this.isOperational = true;
    }

    public abstract void attack(CastleElement target);
    public abstract void reload();
    public abstract String getType();

    public String getName() { return name; }
    public int getDamage() { return damage; }
    public int getRange() { return range; }
    public int getAmmunition() { return ammunition; }
    public boolean isOperational() { return isOperational; }

    public void breakDown() {
        isOperational = false;
        System.out.println(name + " сломалось!");
    }

    public void repair() {
        isOperational = true;
        System.out.println(name + " отремонтировано");
    }

    @Override
    public String toString() {
        return name + " [" + getType() + "] Урон: " + damage +
                " Дальность: " + range + " Боеприпасы: " + ammunition;
    }
}