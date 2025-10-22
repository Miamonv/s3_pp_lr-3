package droids;

public class Droid {
    String name;
    int health;

    Droid(String droidName) {
        name = droidName;
        health = 100;
    }

    void performTask(String task) {
        System.out.println(name + " is performing: " + task);
        health -= 10;
    }

    void recharge(int amount) {
        health += amount;
        if (health > 100) {
            health = 100;
        }
        System.out.println(name + " recharged to: " + health + "%");
    }

    int getBatteryLevel() {
        return health;
    }

}
