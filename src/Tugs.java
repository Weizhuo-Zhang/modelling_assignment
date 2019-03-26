import java.util.concurrent.Semaphore;

/**
 * The tugs simulator.
 *
 * It is responsible for:
 *  - defining tugs and its resource allocation method.
 *
 * @author weizhuoz@student.unimelb.edu.au
 *         Weizhuo Zhang (1018329)
 */

public class Tugs {
    // The total number of tugs
    private volatile int _numOfTugs;
    // The semaphore is used for controlling the operation the tugs resources
    private Semaphore _operationSemaphore;
    // The parameters used for this program
    private Params _params;
    // The berth instance
    private Berth _berth;

    /**
     * Create a Tugs instance for allocating the shared tug resources
     * @param numOfTugs
     *      The total number of tugs.
     * @param params
     *      The parameters is used for this program.
     * @param berth
     *      The Berth
     */
    Tugs(int numOfTugs, Params params, Berth berth) {
        this._numOfTugs = numOfTugs;
        // This is a binary semaphore used to avoid mutual exclusion.
        this._operationSemaphore = new Semaphore(1, true);
        this._params = params;
        this._berth = berth;
    }

    /**
     * Allocate required tugs for given pilot.
     * @param pilotName
     *      The name of the pilot
     * @param numOfAcquiring
     *      The number of required tugs
     */
    public synchronized void allocateTugs(String pilotName, int numOfAcquiring){
        try {
            /**
             * The thread will wait when
             *      1. There is no enough tugs available to finish this task
             *      or
             *      2. There is a ship at the berth currently and we need to
             *      preserve enough tugs for the docked ship to finish
             *      undocking process. In this situation, if the available tugs
             *      are no less than (DOCKING_TUGS + UNDOCKING_TUGS), the
             *      ships are going to berth is allowed to acquire tugs,
             *      unless the available tugs after taken by one ship that
             *      are going to berth is not enough for the ship that are
             *      about to undocking. This will cause deadlock.
             *      or
             *      3. Let us assume the DOCKING_TUGS < UNDOCKING_TUGS, when
             *      there is no ship at berth and 0 tugs available. Under this
             *      circumstance, the available tugs after one ship docking
             *      is not enough for this ship undocking. Therefore, we need
             *      to reserve enough number of tugs for undocking.
             */
            while ((this._berth.hasShip() &&
                this._params.DOCKING_TUGS == numOfAcquiring &&
                (this._params.DOCKING_TUGS + this._params.UNDOCKING_TUGS) >
                    _numOfTugs) ||
                (_numOfTugs < numOfAcquiring) ||
                (false == this._berth.hasShip() &&
                    (this._params.DOCKING_TUGS < this._params.UNDOCKING_TUGS) &&
                    (_numOfTugs <= this._params.UNDOCKING_TUGS))){
                wait();
            }
            _operationSemaphore.acquire();
            _numOfTugs = _numOfTugs - numOfAcquiring;
            System.out.println(
                pilotName + " acquires " + numOfAcquiring +
                " tugs (" + _numOfTugs + " available).");
            _operationSemaphore.release();
        } catch (InterruptedException e) { }
    }

    /**
     * The pilot will release tugs when they finish the undocking process.
     * And all the thread ready for acquiring tugs will be notified.
     * @param pilotName
     *      The name of the pilot
     * @param numOfReleasing
     *      The number of tugs that about to be released.
     */
    public synchronized void releaseTugs(String pilotName, int numOfReleasing) {
        try {
            _operationSemaphore.acquire();
            _numOfTugs = _numOfTugs + numOfReleasing;
            System.out.println(pilotName + " releases " + numOfReleasing +
                    " tugs (" + _numOfTugs + " available).");
            notifyAll();
            _operationSemaphore.release();
        } catch (InterruptedException e) { }
    }
}
