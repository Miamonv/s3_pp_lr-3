package droids;

public class ShieldAllyAction implements Action {
    private TankDroid tank;
    private Droid targetAlly;

    public ShieldAllyAction(TankDroid tank, Droid targetAlly) {
        this.tank = tank;
        this.targetAlly = targetAlly;
    }

    @Override
    public String execute() {
        if (!tank.isAlive() || !targetAlly.isAlive()) return "";
        targetAlly.setShield(true);
        return tank.getName() + " кидає щит на " + targetAlly.getName() + "!";
    }
}
