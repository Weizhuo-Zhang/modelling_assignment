/**
 * Consumes unloaded cargo ships from the departure zone.
 *
 * @author ngeard@unimelb.edu.au
 *         weizhuoz@student.unimelb.edu.au
 *         Weizhuo Zhang (1018329)
 *
 */

public class Consumer extends Thread {

    // the wait zone from which cargo ships depart
    private WaitZone _departureZone;
    private Params _params;

    // creates a new consumer for the given wait zone
    Consumer(WaitZone newDepartureZone, Params params) {
        this._departureZone = newDepartureZone;
        this._params = params;
    }

    // repeatedly collect waiting ships from the departure zone
    public void run() {
        while (!isInterrupted()) {
            try {
                // remove a vessel that is in the departure wait zone
                _departureZone.depart();

                // let some time pass before the next departure
                sleep(_params.departureLapse());
            }
            catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }
}
