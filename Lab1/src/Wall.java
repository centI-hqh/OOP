class Wall {
    private String name;
    private Position position;
    private int level;
    private int maxLevel;
    private boolean isConstructed;

    public Wall(String name, Position position) {
        this.name = name;
        this.position = position;
        this.level = 1;
        this.maxLevel = 3;
        this.isConstructed = true;
    }
    //геттеры
    public String getName() { return name; }
    public Position getPosition() { return position; }
    public int getLevel() { return level; }
    public int getMaxLevel() { return maxLevel; }
    public boolean isConstructed() { return isConstructed; }

    public boolean upgrade() {
        if (level < maxLevel && isConstructed) {
            level++;
            return true;
        }
        return false;
    }

    public boolean construct() {
        if (!isConstructed) {
            isConstructed = true;
            level = 1;
            return true;
        }
        return false;
    }

    public String getStatus() {
        if (!isConstructed) {
            return "В строительстве";
        }
        return "Уровень " + level;
    }

    public String toString() {
        return name + " " + getStatus() + " " + position;
    }
}