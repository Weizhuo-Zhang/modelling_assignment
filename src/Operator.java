/**
 * Operates the shield of berth.
 *
 * @author weizhuoz@student.unimelb.edu.au
 *         Weizhuo Zhang (1018329)
 */

public class Operator extends Thread{
    // The berth to be operated.
    private Berth _berth;
    private Params _params;

    // creates a new operator for the given berth
    Operator(Berth berth, Params params) {
        this._berth = berth;
        this._params = params;
    }

    // repeatedly activate and deactivate shield of the berth
    @Override
    public void run() {
        while(!isInterrupted()) {
            try {
                // Waiting for space debris coming
                sleep(this._params.debrisLapse());

                // Activate the berth shield
                this._berth.activate();
                sleep(this._params.DEBRIS_TIME);
                // Deactivate the berth shield
                this._berth.deactivate();
            } catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }
}
