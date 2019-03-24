public class Operator extends Thread{
    private Berth berth;
    private Params params;

    Operator(Berth berth, Params params) {
        this.berth = berth;
        this.params = params;
    }

    @Override
    public void run() {
        while(!isInterrupted()) {
            try {
                // Waiting for space debris coming
                sleep(this.params.debrisLapse());

                // Activate the berth shield
                this.berth.activate();
                sleep(this.params.DEBRIS_TIME);
                // Deactivate the berth shield
                this.berth.deactivate();
            } catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }
}
