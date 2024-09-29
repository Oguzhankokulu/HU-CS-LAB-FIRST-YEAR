import sys

def list_func(input_file):
    # This function converts string lines to lists that contains each of number
    # as their elements and inserts all of the line lists in complete_list.
    complete_list = []
    for line in input_file:
        line_split = line.split()
        complete_list.append(line_split)
    return complete_list

def empty_line_checker(input_list):
    #This function checks if there is a sublist that all of its elements are blank spaces.
    counter = 0
    for sublist in range(len(input_list)):
        for i in range(len(input_list[sublist])):
            if input_list[sublist][i] == " ":
                counter += 1
            if counter == len(input_list[sublist]):
                return True
        counter = 0
    return False

def empty_line_remover(input_list):
    #This function removes the empty line.
    first_list = []
    for line in input_list:       #Makes the input_list a list that contains a seperate copies of sublists.
        row = line.copy()
        first_list.append(row)
    counter = 0
    numbers = ["1","2","3","4","5","6","7","8","9"]
    for sublist in range(len(first_list)):
        for i in range(len(first_list[sublist])):
            if first_list[sublist][i] in numbers:       #If the element is a number, continue query.
                break
            counter += 1
            if counter == len(first_list[sublist]):     #If all the elements in a sublist are blank space, remove that sublist from the list.
                input_list.pop(sublist)


def list_to_str_1(lines):
    # This function converts lists to string.
    line_str = ""
    for i in lines:
        line_str += i + " "
    return line_str

def list_to_str_2(list_input):
    # This function turns a list of sublists to a total string.
    counter = 0
    output_str = "\n"
    for line_list in list_input:
        if len(line_list) == 0:
            counter += 1
            continue
        line_str = list_to_str_1(line_list)[:-1]        # Converting the sublist to string line except the last space
        output_str += line_str + "\n"
    if counter == len(list_input):                      #This queries for the get exactly the same print outputs with samples. If the list is empty it prints 3 empty lines.
        output_str += "\n"
    if len(list_input) == 0:
        output_str += "\n"
    return output_str[:-1]                              # Extracting the last \n by using list slicing

def neighbor_finder(row,column,input_list,current_number):
    # This function finds neighbors' indexes that equal to the number that is in the center.
    try:        #Checks whether if the selected number is located in the border of the table or not.
        column_neighbor = [input_list[abs(row - 1)][column], input_list[row + 1][column]]
    except IndexError:
        try:
            column_neighbor = [input_list[abs(row - 1)][column], "none"]
        except IndexError:
            column_neighbor = ["none", "none"]

    try:        #Checks whether if the selected number is located in the border of the table or not.
        row_neighbor = [input_list[row][abs(column-1)],input_list[row][column+1]]
    except IndexError:
        try:
            row_neighbor = [input_list[row][abs(column - 1)], "none"]
        except IndexError:
            row_neighbor = ["none", "none"]
    equal_neighbors = []
    for i in column_neighbor:
        if i == current_number:             #If the neighbor has the same value as the selected number's.
            equal_neighbors.append(i)
    for i in row_neighbor:
        if i == current_number:             #If the neighbor has the same value as the selected number's.
            equal_neighbors.append(i)
    return equal_neighbors

def number_popper(row,column,input_list,current_number):
    #This function pops the equal neighbor numbers by putting spaces in numbers' places.
    try:        #Checks whether if the selected number is located in the border of the table or not.
        column_neighbor = [input_list[abs(row-1)][column], input_list[row+1][column]]
    except IndexError:
        try:
            column_neighbor = [input_list[abs(row - 1)][column], "none"]
        except IndexError:
            column_neighbor = ["none", "none"]
    try:        #Checks whether if the selected number is located in the border of the table or not.
        row_neighbor = [input_list[row][abs(column-1)],input_list[row][column+1]]
    except IndexError:
        try:
            row_neighbor = [input_list[row][abs(column - 1)], "none"]
        except IndexError:
            row_neighbor = ["none", "none"]
    input_list[row].pop(column)
    input_list[row].insert(column, " ")  # Inserting space in the selected number's place.
    if len(neighbor_finder(row,column,input_list,current_number)) > 0:                     #If there is 1 or more equal neighbor number.
        if current_number in column_neighbor[0]:        #If there is a neighbor above whose value is equal to the selected number.
            upper_row = abs(row - 1)
            number_popper(upper_row,column,input_list,current_number)       #Calls the function again for the neighbors (same logic applied in below.).
        if current_number in column_neighbor[1]:        #If there is a neighbor below whose value is equal to the selected number.
            lower_row = row + 1
            number_popper(lower_row, column, input_list,current_number)
        if current_number in row_neighbor[0]:           #If there is a neighbor on the left whose value is equal to the selected number.
            left_column = abs(column - 1)
            number_popper(row,left_column,input_list,current_number)
        if current_number in row_neighbor[1]:           #If there is a neighbor on the right whose value is equal to the selected number.
            right_column = column + 1
            number_popper(row, right_column, input_list,current_number)

def row_slider(input_list):
    #This function slides the numbers into the spaces that lies under them.
    for row in range(len(input_list)):
        try:
            if " " in input_list[row+1]:        #If there is space in the next row
                for column in range(len(input_list[row])):
                    try:
                        lower_row_number = input_list[row+1][column]        #If it's the last row, it raises IndexError.
                        if lower_row_number == " ":
                            upper_number = input_list[row][column]
                            if upper_number != " ":         #If there is a number to slide, proceed.
                                input_list[row+1][column] = upper_number
                                input_list[row][column] = " "           #Sliding is done.
                                row_slider(input_list)                  #Do it again with the new version of the list.

                    except:
                        pass
        except IndexError:
            pass

def column_slider(input_list):
    #This function deletes the columns that do NOT contain any number.
    def column_checker(row,column,input_list):
        #This function checks whether if there is a number in the column or not. If there is
        try:        #If it raises an IndexError that means there is no next space left in the column to check, therefore all values that checked were spaces.
            if input_list[row][column] == " ":
                    if column_checker(row + 1, column, input_list) == True:
                        return True
        except IndexError:
            return True

    def empty_column_left(input_list):
        #This function checks whether if there is a empty column left.
            for column in range(len(input_list[0])):
                if input_list[0][column] == " ":
                    if column_checker(0,column,input_list):
                        return True

    while empty_column_left(input_list):
        control = False
        for column in range(len(input_list[0])):
            if column_checker(0,column,input_list):
                for i in range(len(input_list)):      #Deletes the empty column.
                    input_list[i].pop(column)
                control = True
            if control:         #If a column is deleted, then start again the iteration from the first element.
                break



def score_calculator(first_input_list,current_input_list):
    #This function calculates the current score by substituting the current numbers summation from the total numbers' summation
    total_sum = 0
    current_sum = 0
    for row in range(len(first_input_list)):
        for column in range(len(first_input_list[row])):
            total_sum += int(first_input_list[row][column])         #Summation of all numbers in the beginning of the game.

    for row in range(len(current_input_list)):
        for column in range(len(current_input_list[row])):
            if current_input_list[row][column] != " ":
                current_sum += int(current_input_list[row][column]) #Summation of all numbers that are left in the current state of the game.

    score = total_sum - current_sum         #The difference equals to score.
    return score

def correct_size_checker(rc_input,input_list):
    #This function checks whether if the given inputs are valid or not. If the inputs are NOT valid it returns True.
    if len(rc_input) != 2:  # Checks whether if the given inputs are valid or not.
        print("\nPlease enter a correct size!")
        return True
    input_row = int(rc_input[0]) - 1
    input_column = int(rc_input[1]) - 1
    try:
        if input_list[input_row][input_column] == " ":  #If the input is points the blank space in the table.
            print("\nPlease enter a correct size!")
            return True
    except IndexError:                                  #If the indexes are NOT valid.
        print("\nPlease enter a correct size!")
        return True

def game_func(first_input_list,input_list):
    #This function is the general function for this game. It takes inputs and prints the table after calculations.
    total_row = len(input_list)
    total_column = len(input_list[-1])
    rc_input = input("\nPlease enter a row and a column number:")     #Taking input as a string then converting it to integer by splitting it.
    rc_input = rc_input.split()
    while correct_size_checker(rc_input,input_list):
        rc_input = input("\nPlease enter a row and a column number:")  # Taking input as a string then converting it to integer by splitting it.
        rc_input = rc_input.split()
    input_row = int(rc_input[0]) - 1
    input_column = int(rc_input[1]) - 1
    if input_row <= total_row and input_column <= total_column:         #Checks whether if the input values valid or not.
        current_number = input_list[input_row][input_column]
        if len(neighbor_finder(input_row, input_column, input_list, current_number)) > 0:       #Checks whether if the selected number has neighbors whose value is equal to the selected number.
            number_popper(input_row,input_column,input_list,current_number)
            row_slider(input_list)
            column_slider(input_list)
            score = score_calculator(first_input_list,input_list)
            while empty_line_checker(input_list):
                empty_line_remover(input_list)
            print(list_to_str_2(input_list))
            print("\nYour score is: {}".format(score))
        else:
            score = score_calculator(first_input_list, input_list)
            print("\nNo movement happened try again")
            print(list_to_str_2(input_list))
            print("\nYour score is: {}".format(score))
    else:
        print("\nPlease enter a correct size!")

def game_over_func(input_list):
    #This function returns true if there is a move to make, if not it returns false.
    for row in range(len(input_list)):
        for column in range(len(input_list[row])):
            if input_list[row][column] == " ":      #Blank spaces doesn't count as a valid element.
                continue
            if len(neighbor_finder(row, column, input_list, input_list[row][column])) > 0:      #If the element has neighbors whose value equals to that element.
                return True
    return False

def main():
    input_file = open(sys.argv[1], "r")
    first_input_list = list_func(input_file)
    input_list = []
    for line in first_input_list:       #Makes the input_list a list that contains a seperate copies of sublists.
        row = line.copy()
        input_list.append(row)
    score = 0
    print(list_to_str_2(input_list)[1:])    #Starting point of the game.
    print("\nYour score is: {}".format(score))
    while game_over_func(input_list):
        game_func(first_input_list,input_list)
    print("\nGame over")
    input_file.close()

if __name__ == "__main__":
    main()
