public class Catapult extends SiegeEquipment {
    private boolean isLoaded;

    public Catapult(String name, int health, int attack, int defense, int ammunition) {
        super(name, health, attack, defense, ammunition);
        this.isLoaded = false;
    }

    @Override
    public boolean canAttack(Combatable target) {
        return super.canAttack(target) && isLoaded;
    }

    @Override
    public int attack(Combatable target) {
        if (!isLoaded) {
            System.out.println(name + " не заряжена!");
            return 0;
        }
        isLoaded = false;
        return super.attack(target);
    }

    public void load() {
        if (ammunition > 0 && !isLoaded) {
            isLoaded = true;
            System.out.println(name + " заряжена");
        }
    }

    public boolean isLoaded() {
        return isLoaded;
    }
}