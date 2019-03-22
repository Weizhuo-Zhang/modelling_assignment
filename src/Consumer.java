/**
 *
 *
 * @author weizhuoz@student.unimelb.edu.au
 *
 */

public class Consumer extends Thread {

    // the wait zone from which cargo ships depart
    private WaitZone departureZone;
    private Params params;

    // creates a new consumer for the given wait zone
    Consumer(WaitZone newDepartureZone, Params params) {
        this.departureZone = newDepartureZone;
        this.params = params;
    }

    // repeatedly collect waiting ships from the departure zone
    public void run() {
        while (!isInterrupted()) {
            try {
                // remove a vessel that is in the departure wait zone
                departureZone.depart();

                // let some time pass before the next departure
                sleep(params.departureLapse());
            }
            catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }
}
