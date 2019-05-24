import sys

# Global constants for the configuration labels.
AGENT_DENSITY_LABEL = "AGENT_DENSITY"
COP_DENSITY_LABEL = "COP_DENSITY"
VISION_LABEL = "VISION"
GOVERNMENT_LEGITIMACY_LABEL = "GOVERNMENT_LEGITIMACY"
MAX_JAIL_TERM_LABEL = "MAX_JAIL_TERM"
MOVEMENT_SWITCH_LABEL = "MOVEMENT_SWITCH"
ITERATION_TIMES_LABEL = "ITERATION_TIMES"
OUTPUT_FILE_NAME_LABEL = "OUTPUT_FILE_NAME"


# Global constants for the configuration.
GOVERNMENT_LEGITIMACY = 0.82
MAX_JAIL_TERM = 30
MOVEMENT_SWITCH = True
ITERATION_TIMES = 1000


def make_config_line(label, value):
    return str(label) + "=" + str(value) + "\n"


def generateConfigFile(cop_density, agent_density, vision):
    '''
    Function generating a configuration file.
    '''
    # Convert parameters to string.
    cop_density_str = str(cop_density)
    agent_density_str = str(agent_density)
    vision_str = str(vision)

    # Define fle names.
    file_name_pattern = "_" + cop_density_str + \
        "_" + agent_density_str + "_" + vision_str
    file_name = "config" + file_name_pattern + ".properties"
    output_file_name = "out" + file_name_pattern

    # Create the file.
    with open(file_name, "w+") as f:
        # Write the configuration data.
        f.write(make_config_line(AGENT_DENSITY_LABEL, agent_density_str))
        f.write(make_config_line(COP_DENSITY_LABEL, cop_density_str))
        f.write(make_config_line(VISION_LABEL, vision_str))
        f.write(make_config_line(
            GOVERNMENT_LEGITIMACY_LABEL, GOVERNMENT_LEGITIMACY))
        f.write(make_config_line(MAX_JAIL_TERM_LABEL, MAX_JAIL_TERM))
        f.write(make_config_line(MOVEMENT_SWITCH_LABEL, MOVEMENT_SWITCH))
        f.write(make_config_line(ITERATION_TIMES_LABEL, ITERATION_TIMES))
        f.write(make_config_line(OUTPUT_FILE_NAME_LABEL, output_file_name))


# Entry point of the file
if __name__ == "__main__":
    # Check if the user is using Python3 to run this script, if not,
    # throw an exception.
    if sys.version_info[0] < 3:
        raise Exception("Python 3 is required.")

    generateConfigFile(0, 80, 6)
