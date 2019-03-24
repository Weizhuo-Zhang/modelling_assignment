import jdk.swing.interop.LightweightContentWrapper;

import javax.swing.plaf.SeparatorUI;
import java.util.concurrent.Semaphore;

/**
 * The definition of Berth class.
 *
 * It is responsible for:
 * - define the variables, constructor and getter, setter methods for Berth.
 *
 * @author weizhuoz@student.unimelb.edu.au
 */

public class Berth {
    // The name for each Berth instance
    private String _name;
    private volatile boolean _isShieldActivated;
    private volatile boolean _hasShip;
    private Semaphore _semaphore;

    Berth (String name) {
        this._name = name;
        this._isShieldActivated = false;
        this._hasShip = false;
        this._semaphore = new Semaphore(1, true);
    }

    public void activate(){
        this._isShieldActivated = true;
        System.out.println("Shield is activated.");
    }

    public synchronized void deactivate(){
        this._isShieldActivated = false;
        System.out.println("Shield is deactivated.");
        notifyAll();
    }

    public void allocateBerth() {
        try {
            _semaphore.acquire();
            this._hasShip = true;
        } catch (InterruptedException e) { }
    }

    public synchronized void waitForBeris() {
        try {
            while (this.isShieldActivated()) {
                wait();
            }
        } catch (InterruptedException e) { }

    }

    public void releaseBerth() {
        this._hasShip = false;
        _semaphore.release();
    }

    public synchronized boolean hasShip() {
        return _hasShip;
    }

    public synchronized boolean isShieldActivated(){
        return _isShieldActivated;
    }
}
