import java.util.concurrent.Semaphore;

/**
 * The definition of Berth class.
 *
 * It is responsible for:
 * - define status of Berth whether there is a ship currently docking in the
 *   Berth.
 * - define status of shield and provide methods for activating or
 *   deactivating the shield.
 *
 * @author weizhuoz@student.unimelb.edu.au
 *         Weizhuo Zhang (1018329)
 */

public class Berth {
    // The name for each Berth instance.
    private String _name;
    // true:  The shield is activated.
    // false: The shield is deactivated.
    private volatile boolean _isShieldActivated;

    // true:  There is a ship currently in this berth.
    // false: There is no ship currently in this berth.
    private volatile boolean _hasShip;

    // This semaphore is used for controlling the synchronization of _hasShip.
    private Semaphore _semaphore;

    /**
     * @description
     *      The constructor for this Berth class.
     * @param name
     *      The name for this Berth instance.
     */
    Berth (String name) {
        this._name = name;
        // The shield is deactivated as default.
        this._isShieldActivated = false;
        // There is no ship in this Berth when it is just constructed.
        this._hasShip = false;
        // There is only 1 permits needed in this semaphore as there is only
        // one ship can in this Berth at the same time.
        this._semaphore = new Semaphore(1, true);
    }

    /**
     * @description
     *      This method is used for activating shield and change
     *      _isShieldActivated to true.
     * @param  Null
     * @return Null
     */
    public void activate(){
        this._isShieldActivated = true;
        System.out.println("Shield is activated.");
    }

    /**
     * @description
     *      This method is used for deactivating shield and change
     *      _isShieldActivated to false.
     *      This method is a synchronized method which can notify all the
     *      threads that are waiting for deactivation of the shield.
     * @param  Null
     * @return Null
     */
    public synchronized void deactivate(){
        this._isShieldActivated = false;
        System.out.println("Shield is deactivated.");
        notifyAll();
    }

    /**
     * @description
     *      This method allocate the space of this berth to ship.
     * @param  Null
     * @return Null
     */
    public void allocateBerth() {
        try {
            /** Use binary semaphore to avoid mutual exclusion. When the pilot
             *  or she ship successfully acquired this berth for docking,
             *  _hasship changed to true. Otherwise, this thread will wait the
             * release of this semaphore.
             */
            _semaphore.acquire();
            this._hasShip = true;
        } catch (InterruptedException e) { }
    }

    /**
     * @description
     *      This synchronized method is used for waiting for deactivation of
     *      shield.
     * @param  Null
     * @return Null
     */
    public synchronized void waitForDebris() {
        try {
            /**
             * When the shield is activated, this thread will wait until been
             * notified of deactivating shield.
             */
            while (this.isShieldActivated()) {
                wait();
            }
        } catch (InterruptedException e) { }

    }

    /**
     * @description
     *      When the ship commences undocking, it will release this Berth
     *      resource and give space for other ships that are
     *      ready for docking.
     * @param  Null
     * @return Null
     */
    public void releaseBerth() {
        /**
         * When the ship is about to undocking, it will release this
         * semaphore and _hasShip will be changed to false.
         */
        this._hasShip = false;
        _semaphore.release();
    }

    /**
     * @description
     *      The getter method for _hasShip.
     * @param  Null
     * @return _hasShip
     */
    public synchronized boolean hasShip() {
        return _hasShip;
    }

    /**
     * @description
     *      The getter method for _isShieldActivated.
     *      shield.
     * @param  Null
     * @return _isShieldActivated
     */
    public synchronized boolean isShieldActivated(){
        return _isShieldActivated;
    }
}
