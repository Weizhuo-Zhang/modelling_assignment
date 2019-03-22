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
    private String zoneType;

    public WaitZone (String zoneType){
        this.zoneType = zoneType;
    }

    // TODO: need to complete this function
    public void arrive(Ship ship) {
        if (this.zoneType.equals("arrival")){

        } else {
            System.err.println(
                    "This function can only call by a arrival wait zone.");
        }

    }

    // TODO: need to complete this function
    public void depart(){
        if (this.zoneType.equals("departure")){

        } else {
            System.err.println(
                    "This function can only call by a departure wait zone.");
        }

    }

    public String getZoneType() {
        return zoneType;
    }

    public void setZoneType(String zoneType) {
        this.zoneType = zoneType;
    }
}
