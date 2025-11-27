public class Catapult extends SiegeEquipment {
    private boolean isLoaded;
    private int stoneWeight;

    public Catapult(String name, int damage, int range) {
        super(name, damage, range, 10);
        this.isLoaded = false;
        this.stoneWeight = 50;
    }

    @Override
    public void attack(CastleElement target) {
        if (!isOperational) {
            System.out.println(name + " не может атаковать - сломана!");
            return;
        }

        if (ammunition <= 0) {
            System.out.println(name + ": нет боеприпасов!");
            return;
        }

        if (!isLoaded) {
            System.out.println(name + " должна быть заряжена перед атакой!");
            return;
        }

        ammunition--;
        isLoaded = false;
        System.out.println(name + " запускает " + stoneWeight + " кг камень в " + target.getName());
        target.takeDamage(damage);

        // Шанс поломки после выстрела
        if (Math.random() < 0.2) {
            breakDown();
        }
    }

    @Override
    public void reload() {
        if (ammunition > 0 && !isLoaded) {
            isLoaded = true;
            System.out.println(name + " заряжена " + stoneWeight + " кг камнем");
        }
    }

    @Override
    public String getType() {
        return "Катапульта";
    }

}