import fileinput
import sys

def convert_to_list(input_file):
    #Converts lines to lists
    input_list = []
    for line in input_file:
        input_list.append(line.split())
    return input_list

def last_space_deleter(output_file):
    #To remove the last '\n', the last line is gotten and removing the last '\n' by slicing.
    output_file = open(sys.argv[2],"r")
    all_lines = output_file.readlines()
    last_line_in_out = all_lines[-1]
    correct_last_line = last_line_in_out[:-1]
    all_lines[-1] = correct_last_line
    output_file.close()

    output_file = open(sys.argv[2],"w")
    output_file.writelines(all_lines)
    output_file.flush()
    output_file.close()

def line_corrector(line,output_file):
    #Writes the given equation and adjusts it while writing.
    correct_line = ""
    for str in line:
        correct_line += str + " "
    if len(correct_line)>0:
        correct_line = correct_line[:-1]
        output_file.write(correct_line+"\n")

def arg_checker():
    #This function checks the number of the given arguments and if the input file is workable.
    try:
        if len(sys.argv)>3 or len(sys.argv)<3:
            # If there is more or less argument then it raises an error.
            class ArgError(Exception):
                pass
            raise ArgError

        try:
            input_file = open(sys.argv[1],"r")
            input_file.close()

        except FileNotFoundError:
            #If there is no such a file namely as the input or can't get the input file due to permission issues.
            print(
                "ERROR: There is either no such a file namely %s or this program does not have permission to read it!\n"%sys.argv[1] +
                "Program is going to terminate!")
            exit()


    except ArgError:
        print("ERROR: This program needs two command line arguments to run, where first one is the input file and the second one is the output file!\n"
            "Sample run command is as follows: python3 calculator.py input.txt output.txt\n"
            "Program is going to terminate!")
        exit()

def calculator(input_list,output_file):
    for line in input_list:
        if len(line)>0:                                         #If the line is not empty.
            line_corrector(line,output_file)                    #Writes the equation in the output.
            try:
                if len(line) != 3:                              #If there is more than 3 elements give Erroneous Error.
                    class ErroneousError(Exception):
                        pass
                    raise ErroneousError
                else:
                    try:
                        first_operand = float(line[0])          #Checks whether if the first operand is number or not.
                        try:
                            second_operand = float(line[2])     #Checks whether if the first operand is number or not.

                            operators = ["+", "-", "*", "/"]
                            if line[1] not in operators:        #Checks whether if the operator is valid or not.
                                class OperatorError(Exception):
                                    pass
                                raise OperatorError

                            if line[1] == "+":                  #Makes the calculations and writes the results in the output.
                                result = first_operand + second_operand
                                output_file.write("=%.2f\n" % result)
                            elif line[1] == "-":
                                result = first_operand - second_operand
                                output_file.write("=%.2f\n" % result)
                            elif line[1] == "*":
                                result = first_operand * second_operand
                                output_file.write("=%.2f\n" % result)
                            elif line[1] == "/":
                                result = first_operand / second_operand
                                output_file.write("=%.2f\n" % result)


                        except ValueError:
                            output_file.write("ERROR: Second operand is not a number!\n")

                        except OperatorError:
                            output_file.write("ERROR: There is no such an operator!\n")

                    except ValueError:
                        output_file.write("ERROR: First operand is not a number!\n")

            except ErroneousError:
                output_file.write("ERROR: Line format is erroneous!\n")

    output_file.flush()
    output_file.close()
    last_space_deleter(output_file)

def main():
    arg_checker()
    input_file = open(sys.argv[1],"r")
    output_file = open(sys.argv[2],"w")
    calculator(convert_to_list(input_file),output_file)
    input_file.close()

if __name__ == "__main__":
    main()