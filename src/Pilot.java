/**
 * This class define the main process of a pilot thread.
 * while (true) {
 *      Acquire a ship ->
 *      Acquire tugs ->
 *      Travel to berth ->
 *      Docking ->
 *      Unloading ->
 *      Undocking ->
 *      Release tugs and ship.
 *  }
 *
 * @author weizhuoz@student.unimelb.edu.au
 *         Weizhuo Zhang (1018329)
 */
public class Pilot extends Thread{
    // The id for this pilot.
    private int _pilotId;
    // The arrival wait zone instance.
    private WaitZone _arrivalZone;
    // The departure wait zone instance.
    private WaitZone _departureZone;
    // The tugs instance.
    private Tugs _tugs;
    // The Berth instance.
    private Berth _berth;
    // The ship instance.
    private Ship _ship;
    // The params instance.
    private Params _params;

    /**
     * @description
     *      Create a new pilot instance for given input parameters.
     */
    Pilot(  int pilotId,
            WaitZone arrivalZone,
            WaitZone departureZone,
            Tugs tugs,
            Berth berth,
            Params params) {
        this._pilotId = pilotId;
        this._arrivalZone = arrivalZone;
        this._departureZone = departureZone;
        this._tugs = tugs;
        this._berth = berth;
        this._ship = null;
        this._params = params;
    }

    /**
     * @description
     *      The pilot in the arrival wait zone trying to acquire a ship and
     *      waiting for allocating by the arrival wait zone.
     * @param  Null
     * @return NUll
     */
    private void acquireShip() {
        this._ship = this._arrivalZone.allocateShip(this.toString());
        if (null == this._ship) {
            System.err.println("[ERROR]" + this.toString() + " acquires ship" +
                    " failed.");
            this.interrupt();
        }
    }

    /**
     * @description
     *      The pilot acquire tugs for the next step.
     * @param  Null
     * @return NUll
     */
    private void acquireTugs(int numOfAcquiring) {
        this._tugs.allocateTugs(this.toString(), numOfAcquiring);
    }

    /**
     * @description
     *      The pilot release tugs and complete current task.
     * @param  Null
     * @return NUll
     */
    public void releaseTugs(int numOfReleasing) {
        this._tugs.releaseTugs(this.toString(), numOfReleasing);
    }

    /**
     * @description
     *      The pilot waits for the berth to be deactivated and ready for
     *      docking at the berth.
     * @param  Null
     * @return NUll
     */
    private void dockAtBerth() {
        try {
            this._berth.waitForDebris();
            this._berth.allocateBerth();
            sleep(_params.DOCKING_TIME);
            System.out.println(this._ship.toString() + " docks at berth.");
        } catch (InterruptedException e) { }
    }

    /**
     * @description
     *      The pilot has already docked and ready for unloading.
     * @param  Null
     * @return NUll
     */
    private void unload() {
        try {
            sleep(this._params.UNLOADING_TIME);
            System.out.println(this._ship.toString() + " being unloaded.");
        } catch (InterruptedException e) { }
    }

    /**
     * @description
     *      The pilot waits for the berth to be deactivated and ready for
     *      undocking at the berth.
     * @param  Null
     * @return NUll
     */
    private void undockFromBerth() {
        try {
            this._berth.waitForDebris();
            this._berth.releaseBerth();
            sleep(this._params.UNDOCKING_TIME);
            System.out.println(this._ship.toString() + " undocks from berth.");
        } catch (InterruptedException e) { }
    }

    /**
     * @description
     *      The pilot has finished his job and arrives at the departure wait
     *      zone. If there are no space in departure wait zone, the pilot
     *      have to wait until ships in departure wait zone leaved.
     * @param  Null
     * @return NUll
     */
    private void waitForDepart() {
        this._departureZone.releaseShip(this, this._ship);
        this._ship = null;
    }

    /**
     * @description
     *      The loop working process of a pilot thread.
     * @param  Null
     * @return NUll
     */
    @Override
    public void run() {
        while(!isInterrupted()) {
            try {
                // Pilot has arrived at arrival wait zone and try to acquire
                // a ship
                this.acquireShip();
                // Approaching: travel from arrival wait zone to berth
                this.acquireTugs(_params.DOCKING_TUGS);
                sleep(_params.TRAVEL_TIME);

                // Docking
                this.dockAtBerth();
                this.releaseTugs(_params.DOCKING_TUGS);

                // Unloading
                this.unload();

                // Undocking
                this.acquireTugs(_params.UNDOCKING_TUGS);
                this.undockFromBerth();

                // Leaving berth and approaching departure wait zone.
                sleep(_params.TRAVEL_TIME);
                this.waitForDepart();
            } catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }

    /**
     * @description
     *      Return the name of this pilot in a particular format.
     * @param  Null
     * @return String: The name of this pilot.
     */
    @Override
    public String toString() {
        return "pilot " + this._pilotId;
    }
}
