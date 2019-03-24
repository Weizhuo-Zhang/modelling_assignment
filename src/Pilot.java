public class Pilot extends Thread{
    private int _pilotId;
    private WaitZone _arrivalZone;
    private WaitZone _departureZone;
    private Tugs _tugs;
    private Berth _berth;
    private Ship _ship;
    private Params _params;

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

    private void acquireShip() {
        this._ship = this._arrivalZone.allocateShip();
        if (null != this._ship) {
            System.out.println(
                    this.toString() + " acquires " + this._ship.toString()+".");
        } else {
            System.err.println("[ERROR]" + this.toString() + " acquires ship" +
                    " failed.");
            this.interrupt();
        }
    }

    private void acquireTugs(int numOfAcquiring) {
        this._tugs.allocateTugs(this.toString(), numOfAcquiring);
    }

    private void releaseTugs(int numOfReleasing) {
        this._tugs.releaseTugs(this.toString(), numOfReleasing);
    }

    private void dockAtBerth() {
        try {
            this._berth.waitForBeris();
            this._berth.allocateBerth();
            sleep(_params.DOCKING_TIME);
            System.out.println(this._ship.toString() + "docks at berth.");
        } catch (InterruptedException e) { }
    }

    private void unload() {
        try {
            sleep(this._params.UNLOADING_TIME);
            System.out.println(this._ship.toString() + " being unloaded.");
        } catch (InterruptedException e) { }
    }

    private void undockFromBerth() {
        try {
            this._berth.waitForBeris();
            this._berth.releaseBerth();
            sleep(this._params.UNDOCKING_TIME);
            System.out.println(this._ship.toString() + " undocks from berth.");
        } catch (InterruptedException e) { }
    }

    private void waitForDepart() {
        this._departureZone.releaseShip(this._ship);
        System.out.println(
                this.toString() + " releases " + this._ship.toString() + ".");
        this._ship = null;
    }

    @Override
    public void run() {
        while(!isInterrupted()) {
            try {
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
                this.releaseTugs(_params.UNDOCKING_TUGS);
            } catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }

    @Override
    public String toString() {
        return "pilot " + this._pilotId;
    }
}
