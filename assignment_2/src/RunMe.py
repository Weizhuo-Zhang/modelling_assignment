"""RunMe.py

This module contains code used for running the experiments.


Usage:
As this is a Python3 script, please run it using Python3.

If you have only Python3 installed and it's used by calling `python`, then please run this script
using `python RunMe.py`.

If your Python3 is called using `python3`, then please run this script using `python3 RunMe.py`.


Authors:
    Weizhuo Zhang (1018329) - weizhuoz@student.unimelb.edu.au
    Heming Li (804996) - hemingl1@student.unimelb.edu.au
    An Luo (657605) - aluo1@student.unimelb.edu.au

This script's documentation follows the Google Python Style Guid, which can be found here:
http://google.github.io/styleguide/pyguide.html

"""

import os
import subprocess
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
MAX_JAIL_TERM = 30
MOVEMENT_SWITCH = True
ITERATION_TIMES = 1000


def make_config_line(label, value):
    """make_config_line

    Concatenate the label and value provided as a configuration file line, and add a new line
    character at the end.

    Args:
        label: Label of the configuration line.
        value: Value of the configuration line.

    Returns:
        str: Concatenated string with the format `label=value\n`
    """
    return str(label) + "=" + str(value) + "\n"


def generateConfigFile(cop_density, agent_density, vision, legitimacy, max_jail_term, repeat_number):
    """generateConfigFile

    Function generating a configuration file.

    Args:
        cop_density: Initial cop density.
        agent_density: Initial agent density.
        vision: Vision of people.
        legitimacy: Government legitimacy.
        max_jail_term: Max jail term.
        repeat_number: Number that we are going to repeat the experiment.

    Returns:
        str: generated configuration file's name.
    """
    # Convert parameters to string.
    cop_density_str = str(cop_density)
    agent_density_str = str(agent_density)
    vision_str = str(vision)

    # Times legitimacy with 100 and use int() to avoid period (.) in file name.
    legitimacy_str_in_file_name = (str(int(legitimacy * 100)))
    legitimacy_str = str(legitimacy)
    max_jail_term_str = str(max_jail_term)

    # Define fle names.
    parameters_list = [cop_density_str, agent_density_str, vision_str,
                       legitimacy_str_in_file_name, max_jail_term_str,
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
            GOVERNMENT_LEGITIMACY_LABEL, legitimacy_str))
        f.write(make_config_line(MAX_JAIL_TERM_LABEL, max_jail_term_str))
        f.write(make_config_line(MOVEMENT_SWITCH_LABEL,
                                 MOVEMENT_SWITCH))
        f.write(make_config_line(ITERATION_TIMES_LABEL,
                                 ITERATION_TIMES))
        f.write(make_config_line(OUTPUT_FILE_NAME_LABEL,
                                 output_file_name))

    f.close()

    return file_name


def run_the_experiement(cop_density, agent_density, vision, legitimacy, max_jail_term, repeat_number=5):
    """run_the_experiement

    Function running the experiment with provided configuration values. Expreiments will be 
    conducted n times, where n = repeat_number.

    Args:
        cop_density: Initial cop density.
        agent_density: Initial agent density.
        vision: Vision of people.
        legitimacy: Government legitimacy.
        max_jail_term: Max jail term.
        repeat_number: Number that we are going to repeat the experiment.
    """
    experiment_parameter_statement = ("cop_density: {}\n"
                                      "agent_density: {}\n"
                                      "vision: {}\n"
                                      "legitimacy: {}\n"
                                      "max_jail_term: {}\n"
                                      "number of repeat times: {}"
                                      ).format(
        cop_density, agent_density, vision, legitimacy, max_jail_term,
        repeat_number)

    print("--------------------------"
          " Start of the experiment "
          "--------------------------")
    print(experiment_parameter_statement)

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

    # Remove generated configuration files after all experiments finished.
    remove_config_files_command = "rm " + " ".join(config_files)
    process = subprocess.Popen(
        remove_config_files_command, shell=True, stdout=subprocess.PIPE)
    process.wait()

    print("---------------------------"
          " End of the experiment "
          "---------------------------\n")


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

    # Experiment One
    run_the_experiement(5, 85, 6, 0.8, 40)
    run_the_experiement(5.5, 85, 6, 0.8, 40)
    run_the_experiement(6.0, 85, 6, 0.8, 40)
    run_the_experiement(10, 85, 6, 0.8, 40)
    run_the_experiement(5, 85, 3, 0.8, 40)
    run_the_experiement(5, 85, 9, 0.8, 40)

    # Experiment Two
    run_the_experiement(10, 80, 7, 0.2, 40)
    run_the_experiement(10, 80, 7, 0.5, 40)
    run_the_experiement(10, 80, 7, 0.8, 40)
    run_the_experiement(10, 80, 7, 0.2, 20)
    run_the_experiement(4, 70, 7, 0.82, 16)
    run_the_experiement(4, 70, 7, 0.82, 30)
