import java.util.concurrent.Semaphore;

public class Tugs {
    private volatile int _numOfTugs;
    private Semaphore _operationSemaphore;
    private Semaphore _insufficientSemaphore;
    private volatile int _availableTugs;
    private Params _params;
    private Berth _berth;

    Tugs(int numOfTugs, Params params, Berth berth) {
        this._numOfTugs = numOfTugs;
        this._operationSemaphore = new Semaphore(1, true);
        this._insufficientSemaphore = new Semaphore(0);
        this._params = params;
        this._berth = berth;
    }

    public synchronized void allocateTugs(String pilotName, int numOfAcquiring){
        try {
            while ((this._berth.hasShip() &&
                this._params.DOCKING_TUGS == numOfAcquiring &&
                (this._params.DOCKING_TUGS + this._params.UNDOCKING_TUGS)
                    > _numOfTugs) ||
                _numOfTugs < numOfAcquiring) {
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
