package droids;

public class ShieldAction implements Action {
    private TankDroid tank;

    public ShieldAction(TankDroid tank) {
        this.tank = tank;
    }

    @Override
    public String execute() {
        tank.setShield(true);
        return tank.getName() + " активує ЕНЕРГОЩИТ!";
    }
}
