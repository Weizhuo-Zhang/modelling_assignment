import sys
import os
import subprocess


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


def generateConfigFile(cop_density, agent_density, vision, legitimacy, max_jail_term, repeat_number):
    '''
    Function generating a configuration file.
    '''
    # Convert parameters to string.
    cop_density_str = str(cop_density)
    agent_density_str = str(agent_density)
    vision_str = str(vision)
    # Times legitimacy with 100 and use int() to avoid period (.) in
    # file name
    legitimacy_str = str(int(legitimacy*100))
    max_jail_term_str = str(max_jail_term)

    # Define fle names.
    parameters_list = [cop_density_str, agent_density_str, vision_str,
                       legitimacy_str, max_jail_term_str,
                       str(repeat_number)]
    file_name_pattern = "_".join(parameters_list)
    file_name = "config" + file_name_pattern + ".properties"
    output_file_name = "out" + file_name_pattern

    # Create the file.
    with open(file_name, "w+") as f:
        # Write the configuration data.
        f.write(make_config_line(AGENT_DENSITY_LABEL,
                                 agent_density_str))
        f.write(make_config_line(COP_DENSITY_LABEL, cop_density_str))
        f.write(make_config_line(VISION_LABEL, vision_str))
        f.write(make_config_line(
            GOVERNMENT_LEGITIMACY_LABEL, GOVERNMENT_LEGITIMACY))
        f.write(make_config_line(MAX_JAIL_TERM_LABEL, MAX_JAIL_TERM))
        f.write(make_config_line(MOVEMENT_SWITCH_LABEL,
                                 MOVEMENT_SWITCH))
        f.write(make_config_line(ITERATION_TIMES_LABEL,
                                 ITERATION_TIMES))
        f.write(make_config_line(OUTPUT_FILE_NAME_LABEL,
                                 output_file_name))

    f.close()

    return file_name


def run_the_experiement(cop_density, agent_density, vision, legitimacy, max_jail_term, repeat_number=5):
    experiment_parameter_statement = ("cop_density: {}, agent_density: "
                                      "{}, vision: {}, legitimacy: {},"
                                      " max_jail_term: {}, number of"
                                      " repeat times: {}").format(
        cop_density, agent_density, vision, legitimacy, max_jail_term,
        repeat_number)

    print("Running experience with following parameters: {}".format(
        experiment_parameter_statement))

    # Create repeat_number of config files, the only dofference
    # between these files are the OUTPUT_FILE_NAME_LABEL
    config_files = [generateConfigFile(
        cop_density, agent_density, vision, legitimacy,
        max_jail_term, i) for i in range(repeat_number)]

    # Run repeat_number of processes together, wait for all of them
    # finish.
    processes = [subprocess.Popen("java Main " + config_file,
                                  shell=True, stdout=subprocess.PIPE)
                 for config_file in config_files]

    # Wait all processes finish.
    for process in processes:
        process.wait()

    remove_config_files_command = "rm " + " ".join(config_files)
    process = subprocess.Popen(
        remove_config_files_command, shell=True, stdout=subprocess.PIPE)
    process.wait()

    print("Experiment with following parameters: {} finished.\n".format(
        experiment_parameter_statement))


# Entry point of the file
if __name__ == "__main__":
    # Check if the user is using Python3 to run this script, if not,
    # throw an exception.
    if sys.version_info[0] < 3:
        raise Exception("Python 3 is required.")

    # Compile Java codes to classes.
    print("Compiling Java code...")
    process = subprocess.Popen(
        "javac Main.java", shell=True, stdout=subprocess.PIPE)
    process.wait()
    print("Java code compilation finished.\n")

    # Print the chart
    print("Start printing chart using Java...\n")
    process = subprocess.Popen("java Main PrintChart",
                               shell=True, stdout=subprocess.PIPE)

    run_the_experiement(0, 80, 6, 0.8, 40)
