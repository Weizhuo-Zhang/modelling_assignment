import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 * The wait zone simulator.
 *
 * It is responsible for:
 *  - defining wait zones, such as arrive wait zone and departure wait zone.
 *  - allocate ships in arrival and departure wait zone.
 *
 * @author weizhuoz@student.unimelb.edu.au
 *         Weizhuo Zhang (1018329)
 */

public class WaitZone {
    /**
     * Define types of wait zone. There are two wait zone available currently
     * ARRIVAL and DEPARTURE
     * Enum is less likely to make typo mistakes than String and it is
     * clearly for given types of zone that we support currently.
     */
    public static enum ZoneTypes{
        ARRIVAL, DEPARTURE;
    }

    // The type of this wait zone
    private ZoneTypes   _zoneType;
    // The parameters of the instance
    private Params      _params;
    // The queue for ships in arrival wait zone
    private Queue<Ship> _arrivalShipQueue;

    /**
     * The semaphore is used for controlling the empty space of the arrival wait
     * zone.
     * If the arrival wait zone is empty, this Semaphore is full.
     * If the arrival wait zone is full and has no available space for ships,
     * this semaphore will be empty.
     */
    private Semaphore   _arrivalQueueSemaphore;

    /**
     * This semaphore is used for controlling the operation of the arrival
     * wait zone to avoid mutual exclusion.
     */
    private Semaphore   _arrivalOperateSemaphore;

    /**
     * The semaphore is used for controlling the available ships in the
     * arrival wait zone.
     * If the arrival wait zone has no ships available, this semaphore will
     * be empty.
     * If the arrival wait zone has no space for other ships, this semaphore
     * will be full.
     */
    private Semaphore   _arrivalProductSemaphore;

    // The queue for ships in departure wait zone
    private Queue<Ship> _departureShipQueue;

    /**
     * The semaphore is used for controlling the empty space of the departure
     * wait zone.
     * If the departure wait zone is empty, this Semaphore is full.
     * If the departure wait zone is full and has no available space for ships,
     * this semaphore will be empty.
     */
    private Semaphore   _departureQueueSemaphore;

    /**
     * This semaphore is used for controlling the operation of the departure
     * wait zone to avoid mutual exclusion.
     */
    private Semaphore   _departureOperateSemaphore;

    /**
     * The semaphore is used for controlling the available ships in the
     * departure wait zone.
     * If the departure wait zone has no ships available, this semaphore will
     * be empty.
     * If the departure wait zone has no space for other ships, this semaphore
     * will be full.
     */
    private Semaphore   _departureProductSemaphore;

    /**
     * Create new WaitZone instance for given parameters.
     *
     * If this is a arrival wait zone, it will initialize the arrival zone
     * variables. Otherwise, it will initialize the depart zone variables.
     * @param zoneType
     *      The type of this zone.
     * @param params
     *      The parameters used for this program.
     */
    WaitZone (ZoneTypes zoneType, Params params){
        this._zoneType = zoneType;
        this._params = params;
        //The space for wait zone is finite and equals to the number of pilots.
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

    /**
     * Arrange space for arriving ships.
     * @param ship
     *      The arriving ship
     */
    public void arrive(Ship ship) {
        if (this._zoneType.equals(ZoneTypes.ARRIVAL)) {
            try {
                // Occupy one space in arrival zone for this new ship
                this._arrivalQueueSemaphore.acquire();
                this._arrivalOperateSemaphore.acquire();
                this._arrivalShipQueue.offer(ship);
                System.out.println(
                        ship.toString() + " arrives at arrive zone.");
                this._arrivalOperateSemaphore.release();
                // Release one new ship for pilot
                this._arrivalProductSemaphore.release();
            } catch (InterruptedException e) {}
        } else {
            System.err.println(
                    "This function can only call by a arrival wait zone.");
        }

    }

    /**
     * Allocate ship for given pilot.
     * @param pilotID
     *      The pilot acquires the ship.
     * @return The allocated ship
     */
    public Ship allocateShip(String pilotID) {
        Ship ship = null;
        if (this._zoneType.equals(ZoneTypes.ARRIVAL)) {
            try {
                // Acquire a new ship from arrival zone
                this._arrivalProductSemaphore.acquire();
                this._arrivalOperateSemaphore.acquire();
                ship = this._arrivalShipQueue.poll();
                System.out.println(
                        pilotID + " acquires " + ship.toString()+".");
                this._arrivalOperateSemaphore.release();
                // Release a space for other ship in arrival zone
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

    /**
     * Make the ship departing from departure wait zone.
     */
    public void depart(){
        if (this._zoneType.equals(ZoneTypes.DEPARTURE)) {
            try {
                // Acquire a ship that is ready for depart
                this._departureProductSemaphore.acquire();
                this._departureOperateSemaphore.acquire();
                Ship ship = this._departureShipQueue.poll();
                System.out.println(
                        ship.toString() + " departs departure zone.");
                this._departureOperateSemaphore.release();
                // Release a space in depart zone
                this._departureQueueSemaphore.release();
            } catch (InterruptedException e) { }
        } else {
            System.err.println(
                    "This function can only call by a departure wait zone.");
        }
    }

    /**
     * Release ship and release the tugs for given pilot.
     * @param pilot
     *      The pilot who want to release the ship
     * @param ship
     *      The ship which is ready for depart.
     */
    public void releaseShip(Pilot pilot, Ship ship) {
        if (this._zoneType.equals(ZoneTypes.DEPARTURE)) {
            try {
                // Acquire a space for this ship in depart zone
                this._departureQueueSemaphore.acquire();
                this._departureOperateSemaphore.acquire();
                this._departureShipQueue.offer(ship);
                System.out.println(
                        pilot.toString() + " releases " + ship.toString() +".");
                pilot.releaseTugs(_params.UNDOCKING_TUGS);
                this._departureOperateSemaphore.release();
                // Release a ship resource for departure wait zone to depart
                this._departureProductSemaphore.release();
            } catch (InterruptedException e) { }
        } else {
            System.err.println(
                    "This function can only call by a departure wait zone.");
        }
    }
}
