public abstract class Appliance {
    protected String brand;

    public Appliance(String brand) {
        this.brand = brand;
    }
    public void displayBrand() {
        System.out.println("Brand: " + brand);
    }

    public void turnOn() {
        System.out.println("Power is on.");
    }

    public void turnOff() {
        System.out.println("Power is off.");
    }

    public abstract void operate();
}
