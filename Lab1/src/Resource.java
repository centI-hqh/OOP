class Resource {
    private String name;
    private int amount;
    private int maxAmount;

    public Resource(String name, int initialAmount, int maxAmount) {
        this.name = name;
        this.amount = Math.min(initialAmount, maxAmount);
        this.maxAmount = maxAmount;
    }

    public String getName() { return name; }
    public int getAmount() { return amount; }
    public int getMaxAmount() { return maxAmount; }

    public boolean addAmount(int value) {
        if (amount + value <= maxAmount) {
            amount += value;
            return true;
        }
        return false;
    }

    public boolean consumeAmount(int value) {
        if (amount >= value) {
            amount -= value;
            return true;
        }
        return false;
    }

    public boolean isFull() {
        return amount >= maxAmount;
    }

    public boolean isEmpty() {
        return amount <= 0;
    }

    public String toString() {
        return name + ": " + amount + "/" + maxAmount;
    }
}