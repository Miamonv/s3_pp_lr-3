package droids;

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

        // Ігноруємо броню: наносимо чистий урон (sniper.damage)
        // Для реалізації цього нам, можливо, треба "тимчасово" обнулити броню цілі або мати метод takeTrueDamage
        // Але поки використаємо хак: додамо броню до урону, щоб вона віднялась назад

        int trueDamage = sniper.getDamage() + target.getArmor();
        String log = target.takeDamage(trueDamage); // takeDamage відніме броню, вийде чистий урон

        return sniper.getName() + " робить БРОНЕБІЙНИЙ постріл по " + log;
    }
}
