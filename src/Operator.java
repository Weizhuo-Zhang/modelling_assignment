public class Operator extends Thread{
    private Berth berth;

    public Operator(Berth berth) {
        this.berth = berth;
    }

    @Override
    public void run() {
        super.run();
    }

    public Berth getBerth() {
        return berth;
    }

    public void setBerth(Berth berth) {
        this.berth = berth;
    }
}
