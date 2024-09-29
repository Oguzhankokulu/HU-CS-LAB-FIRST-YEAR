import sys

def list_func(input):
    # This function converts string lines to lists that contains each of number
    # as their elements and inserts all of the line lists in complete_list.
    complete_list = []
    for line in input:
        line_split = line.split()
        complete_list.append(line_split)
    return complete_list

def check_list(list_input):
    # This function assemble the elements of sub_lists' in a list.
    sum_list = []
    for i in list_input:
        sum_list.extend(i)
    return sum_list

def list_to_str_1(lines):
    # This function converts lists to string.
    line_str = ""
    for i in lines:
        line_str += i + " "
    return line_str

def list_to_str_2(list_input):
    # This function turns a list of sublists to a total string.
    output_str = ""
    for line_list in list_input:
        line_str = list_to_str_1(line_list)[:-1]        # Converting the sublist to string line except the last space
        output_str += line_str + "\n"
    return output_str[:-1]                              # Extracting the last \n by using list slicing

def list_inter(list1,list2,list3):
    # This function returns the intersection of the 3 input list.
    inter_list = []
    for number in list1:
        if number in list2 and number in list3:
            inter_list.append(number)
    return inter_list

def row_query_func(row_list):
    # This function queries the possible numbers for a row and it returns the possible numbers
    possible_numbers = ["1", "2", "3", "4", "5", "6", "7", "8", "9"]
    for number in row_list:
        if number in possible_numbers:
            possible_numbers.remove(number)
    return possible_numbers

def colmn_query_func(list_input, column):
    # This function queries the possible numbers for a column and it returns the possible numbers
    possible_numbers = ["1", "2", "3", "4", "5", "6", "7", "8", "9"]
    for list in list_input:
        if list[column-1] in possible_numbers:
            possible_numbers.remove(list[column - 1])
    return possible_numbers

def box_row_finder(row_input):
    # This function finds the row indexes which are belong to the box of the given square
    row_index = []
    if row_input%3==0:
        row_index.extend(range(row_input - 3,row_input))
    elif row_input%3==1:
        row_index.extend(range(row_input - 1, row_input + 2))
    else:
        row_index.extend(range(row_input - 2, row_input + 1))
    return row_index

def box_colmn_finder(colmn_input):
    # This function finds the column indexes which are belong to the box of the given square
    colmn_index = []
    if colmn_input <= 3:
        colmn_index.extend(range(0,3))
    elif colmn_input <= 6:
        colmn_index.extend(range(3, 6))
    else:
        colmn_index.extend(range(6, 9))
    return colmn_index

def box_finder(list_input, row, column):
    # This function determines the 3x3 box for given row and column in a list.
    box_row_index = box_row_finder(row)
    box_column_index = box_colmn_finder(column)
    upper_list_row = list_input[box_row_index[0]:box_row_index[2]+1]
    box_list = []
    for box_row in upper_list_row:
        box_list.append(box_row[box_column_index[0]:box_column_index[2]+1])
    return box_list

def box_query_func(list_input, row, column):
    # This function queries the possible numbers for a box and it returns the possible numbers
    possible_numbers = ["1", "2", "3", "4", "5", "6", "7", "8", "9"]
    box_list = box_finder(list_input, row, column)
    for numbers in box_list:
        for number in numbers:
            if number in possible_numbers:
                possible_numbers.remove(number)
    return possible_numbers

def sudoku(list_input,output_file):
    # This is the main function which solves the sudoku and writes the outputs.
    step = 1        # Step counter for the step number in the output.
    while "0" in check_list(list_input):        # If there is unsolved square, runs the loop
        control = False
        for line in list_input:
            row = list_input.index(line) + 1
            num_index = -1
            if "0" in line:
                for number in line:
                    num_index += 1
                    if number == "0":
                        column = num_index + 1
                        possbl_r_num = row_query_func(line)
                        possbl_c_num = colmn_query_func(list_input, column)
                        possbl_b_num = box_query_func(list_input, row, column)
                        inter_list = list_inter(possbl_r_num, possbl_c_num, possbl_b_num) # The possible numbers for the 3 condition of the current square
                        if len(inter_list) == 1:         # If there is only one possible number for the current square, insert the possible number to the current square and write it in the output.txt
                            control = True
                            line.pop(num_index)
                            line.insert(num_index, inter_list[0])
                            output_file.write(
                                "------------------\n"
                                "Step {0} - {1} @ R{2}C{3}\n"
                                "------------------\n"
                                "{4}\n".format(step, inter_list[0], row, column, list_to_str_2(list_input)))
                            step += 1
                            break
            if control:         # If there is a switched number in the sudoku, query the function from the start again.
                break
    output_file.write("------------------")         # Last line

def main():
    input_file = open(sys.argv[1],"r")
    output_file = open(sys.argv[2], "w")

    complete_list = list_func(input_file)
    sudoku(complete_list,output_file)
    input_file.close()
    output_file.flush()
    output_file.close()

if __name__ == "__main__":
    main()