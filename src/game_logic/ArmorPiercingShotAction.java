package game_logic;

import droids.Droid;
import droids.SniperDroid;

public class ArmorPiercingShotAction implements Action {
    private SniperDroid sniper;
    private Droid target;

    public ArmorPiercingShotAction(SniperDroid sniper, Droid target) {
        this.sniper = sniper;
        this.target = target;
    }

    @Override
    public String execute() {
        if (!sniper.isAlive() || !target.isAlive()) return "";

        // Формула "Чистої шкоди":
        // Ми хочемо, щоб target.takeDamage() відняв броню, а потім ми її "повернули"
        // Тому ми додаємо броню цілі до шкоди атаки.
        // (damage + armor) - armor = damage.

        int trueDamage = sniper.getDamage() + target.getArmor();
        String log = target.takeDamage(trueDamage);

        return "🔥 " + sniper.getName() + " пробиває броню -> " + log;
    }
}