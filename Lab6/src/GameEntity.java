public abstract class GameEntity {
    protected String name;
    protected Position position;

    public GameEntity(String name, Position position) {
        this.name = name;
        this.position = position;
    }

    public String getName() { return name; }
    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }

    public abstract void update();

    @Override
    public String toString() {
        return name + " на позиции " + position;
    }
}