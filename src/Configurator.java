import java.util.ResourceBundle;

public class Configurator {
    public final int MAP_WIDTH                    = 40;
    public final int MAP_HEIGHT                   = 40;
    private final float DENCITY_MIN               = 0.0f;
    private final float DENCITY_MAX               = 100.0f;
    private final float VISION_MIN                = 0.0f;
    private final float VISION_MAX                = 10.0f;
    private final float GOVERNMENT_LEGITIMACY_MIN = 0.0f;
    private final float GOVERNMENT_LEGITIMACY_MAX = 1.0f;
    private final int MAX_JAIL_TERM_MIN           = 0;
    private final int MAX_JAIL_TERM_MAX           = 50;
    private final String CONFIG_FILE_PATH         = "config.properties";

    private float copDensity;
    private float agentDensity;
    private float vision;
    private float governmentLegitimacy;
    private int maxJailTerm;
    private boolean movementSwitch;

    private static Configurator configurator =
            new Configurator();

    private Configurator() {
        ResourceBundle resource = ResourceBundle.getBundle(CONFIG_FILE_PATH);
        copDensity =
                Float.parseFloat(resource.getString("COP_DENSITY"));
        agentDensity =
                Float.parseFloat(resource.getString("AGENT_DENSITY"));
        vision =
                Float.parseFloat(resource.getString("VISION"));
        governmentLegitimacy =
                Float.parseFloat(resource.getString("GOVERNMENT_LEGITIMACY"));
        maxJailTerm =
                Integer.parseInt(resource.getString("MAX_JAIL_TERM"));
        movementSwitch =
                Boolean.getBoolean(resource.getString("MOVEMENT_SWITCH"));
    }

    public static Configurator getConfigurator() {
        return configurator;
    }

    public float getCopDensity() {
        return copDensity;
    }

    public float getAgentDensity() {
        return agentDensity;
    }

    public float getVision() {
        return vision;
    }

    public float getGovernmentLegitimacy() {
        return governmentLegitimacy;
    }

    public int getMaxJailTerm() {
        return maxJailTerm;
    }

    public boolean isMovement() {
        return movementSwitch;
    }
}
