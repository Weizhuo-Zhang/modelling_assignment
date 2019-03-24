import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 * The wait zone simulator.
 *
 * It is responsible for:
 *  - defining wait zones, such as arrive wait zone and departure wait zone.
 *
 * @author weizhuoz@student.unimelb.edu.au
 *
 */

public class WaitZone {
    public static enum ZoneTypes{
        ARRIVAL, DEPARTURE;
    }

    private ZoneTypes   _zoneType;
    private Params      _params;
    private Queue<Ship> _arrivalShipQueue;
    private Semaphore   _arrivalQueueSemaphore;
    private Semaphore   _arrivalOperateSemaphore;
    private Semaphore   _arrivalProductSemaphore;

    private Queue<Ship> _departureShipQueue;
    private Semaphore   _departureQueueSemaphore;
    private Semaphore   _departureOperateSemaphore;
    private Semaphore   _departureProductSemaphore;

    WaitZone (ZoneTypes zoneType, Params params){
        this._zoneType = zoneType;
        this._params = params;
        if (this._zoneType.equals(ZoneTypes.ARRIVAL)) {
            this._arrivalShipQueue = new LinkedList<>();
            this._arrivalQueueSemaphore = new Semaphore(
                    this._params.NUM_PILOTS, true);
            this._arrivalOperateSemaphore = new Semaphore(1, true);
            this._arrivalProductSemaphore = new Semaphore(0, true);
        } else if (this._zoneType.equals(ZoneTypes.DEPARTURE)){
            this._departureShipQueue = new LinkedList<>();
            this._departureQueueSemaphore = new Semaphore(
                    this._params.NUM_PILOTS, true);
            this._departureOperateSemaphore = new Semaphore(1, true);
            this._departureProductSemaphore = new Semaphore(0);
        }
    }

    public void arrive(Ship ship) {
        if (this._zoneType.equals(ZoneTypes.ARRIVAL)) {
            try {
                this._arrivalQueueSemaphore.acquire();
                this._arrivalOperateSemaphore.acquire();
                this._arrivalShipQueue.offer(ship);
                System.out.println(
                        ship.toString() + " arrives at arrive zone.");
                this._arrivalOperateSemaphore.release();
                this._arrivalProductSemaphore.release();
            } catch (InterruptedException e) {}
        } else {
            System.err.println(
                    "This function can only call by a arrival wait zone.");
        }

    }

    public Ship allocateShip() {
        Ship ship = null;
        if (this._zoneType.equals(ZoneTypes.ARRIVAL)) {
            try {
                this._arrivalProductSemaphore.acquire();
                this._arrivalOperateSemaphore.acquire();
                ship = this._arrivalShipQueue.poll();
                this._arrivalOperateSemaphore.release();
                this._arrivalQueueSemaphore.release();
            } catch (InterruptedException e) { }
            finally {
                return ship;
            }
        } else {
            System.err.println(
                    "This function can only call by a arrival wait zone.");
            return null;
        }
    }

    public void depart(){
        if (this._zoneType.equals(ZoneTypes.DEPARTURE)) {
            try {
                this._departureProductSemaphore.acquire();
                this._departureOperateSemaphore.acquire();
                Ship ship = this._departureShipQueue.poll();
                System.out.println(
                        ship.toString() + " departs departure zone.");
                this._departureOperateSemaphore.release();
                this._departureQueueSemaphore.release();
            } catch (InterruptedException e) { }
        } else {
            System.err.println(
                    "This function can only call by a departure wait zone.");
        }
    }

    public void releaseShip(Ship ship) {
        if (this._zoneType.equals(ZoneTypes.DEPARTURE)) {
            try {
                this._departureQueueSemaphore.acquire();
                this._departureOperateSemaphore.acquire();
                this._departureShipQueue.offer(ship);
                this._departureOperateSemaphore.release();
                this._departureProductSemaphore.release();
            } catch (InterruptedException e) { }
        } else {
            System.err.println(
                    "This function can only call by a departure wait zone.");
        }
    }
}
