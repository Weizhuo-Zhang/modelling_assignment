public class Pilot extends Thread{
    private int pilotId;
    private WaitZone arrivalZone;
    private WaitZone departureZone;
    private Tugs tugs;
    private Berth berth;

    public Pilot(
            int pilotId,
            WaitZone arrivalZone,
            WaitZone departureZone,
            Tugs tugs,
            Berth berth) {
        this.pilotId = pilotId;
        this.arrivalZone = arrivalZone;
        this.departureZone = departureZone;
        this.tugs = tugs;
        this.berth = berth;
    }

    // TODO: to implement this run method
    @Override
    public void run() {
        super.run();
        System.out.println("pilot [" + this.pilotId + "].");
    }

    public int getPilotId() {
        return pilotId;
    }

    public void setPilotId(int pilotId) {
        this.pilotId = pilotId;
    }

    public WaitZone getArrivalZone() {
        return arrivalZone;
    }

    public void setArrivalZone(WaitZone arrivalZone) {
        this.arrivalZone = arrivalZone;
    }

    public WaitZone getDepartureZone() {
        return departureZone;
    }

    public void setDepartureZone(WaitZone departureZone) {
        this.departureZone = departureZone;
    }

    public Tugs getTugs() {
        return tugs;
    }

    public void setTugs(Tugs tugs) {
        this.tugs = tugs;
    }

    public Berth getBerth() {
        return berth;
    }

    public void setBerth(Berth berth) {
        this.berth = berth;
    }
}
