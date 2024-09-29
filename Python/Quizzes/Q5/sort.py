import sys

def last_space_deleter(str_list, output_file):
    """This function deletes the last blank space in the output."""
    total_str = "".join([str(i) for i in str_list])                      #list comprehension is used in join method to create the output string
    output_file.write(total_str[:-1])                                    #for deleting the last space, list slicing is used

def list_func(input_file):
    """This function is used for converting the input string to an integer list."""
    input_list = [int(i) for i in input_file.read().split()]             #list comprehension is used for creating a list of given integers
    return input_list

def str_func(int_list):
    """This function is used for converting the integer list to string line."""
    str_line = " ".join([str(i) for i in int_list])                      #list comprehension and join method is used for converting integer list to string
    return str_line

def force_sort(given_list):
    """This function checks if the given list is sorted or not by returning True or False values."""
    i = 1
    while i < len(given_list):                                           #This loop compares integer with next index's integer one by one, if there is disordeliness it returns True if not it returns False
        if given_list[i] < given_list[i-1]:
            return True
        i += 1
    return False

def bubble_sort(input_list,output_list):
    """This function sorts the input_list according to the bubble sort method."""
    str_list = []                                                        #List created for string lines
    queue = 0                                                            #Variable for step number
    ending_index = len(input_list)-1
    for i in range(len(input_list)-1):                                   #To bubble sort, n-1 iteration for a list that contains n element
        while force_sort(input_list):                                    #This checks if the input_list is already sorted
            index = 0
            for j in range(ending_index):                                #number of iterations required for the remaining element and one by one sorting mechanism
                if input_list[index] < input_list[index + 1]:
                    index += 1
                    continue
                elif input_list[index] > input_list[index + 1]:
                    temp = input_list[index + 1]
                    input_list[index + 1] = input_list[index]
                    input_list[index] = temp
                    index += 1
                else:
                    index += 1
                    continue
            queue += 1
            ending_index -= 1
            str_line = str_func(input_list)                              #Turning integers to str
            str_list.append(f"Pass {queue}: {str_line}\n")               #Assembling all the steps
    last_space_deleter(str_list,output_list)                             #Deleting the last blank space of the output

def insertion_sort(input_list,output_list):
    """This function sorts the input_list according to the insertion method."""
    str_list = []                                                        #List created for string lines
    sorted_list = []                                                     #List created for insertion
    sorted_list.append(input_list[0])
    input_list.pop(0)
    queue = 0                                                            #Variable for step number
    copy_list = input_list.copy()                                        #This list is created for loops because input_list will be changed in the process
    while len(input_list) >= 0:
        for i in copy_list:
            sorted_list.append(i)                                        #Inserting the every number to sorted list, which numbers in the copy_list
            index = 0
            while force_sort(input_list) or force_sort(sorted_list):     #This checks if the list is sorted or not
                input_list.pop(0)                                        #Removing the inserted number from the input_list
                for number in sorted_list:                               #This loop checks the order of the sorted_list
                    while len(sorted_list)-1 >= index+1:                 #Checks if there is anything to check
                        if sorted_list[index+1] < sorted_list[index]:    #If the two number is NOT sorted, change their places
                            removed_1 = sorted_list.pop(index+1)
                            removed_2 = sorted_list.pop(index)
                            sorted_list.insert(index, removed_1)
                            sorted_list.insert(index+1, removed_2)
                            index = 0

                        elif sorted_list[index+1] > sorted_list[index]:  #If the two number is sorted
                            index += 1
                            continue

                        else:                                            #If the two number is equal
                            index += 1
                            continue
                queue += 1
                str_line_1 = str_func(input_list)                        #Converting the list of integers to str
                str_line_2 = str_func(sorted_list)
                if len(str_line_1) > 0:                                  #This condition is given for the blank space which comes between the two list
                    str_line = str_line_2 + " " + str_line_1             #Assembling the sorted_list and input_list string lines
                else:
                    str_line = str_line_2
                str_list.append(f"Pass {queue}: {str_line}\n")
                break
            else:
                try:                                                     #This tries to insert a list element to sorted_list if there is no element left it breaks the loop
                    sorted_list.append(input_list[0])
                    input_list.pop(0)
                except:
                    break
        break
    last_space_deleter(str_list, output_list)

def main():
    input_file = open(sys.argv[1],"r")
    bubble_output = open(sys.argv[2],"w")
    insertion_output = open(sys.argv[3],"w")

    input_list_1 = list_func(input_file)
    input_list_2 = input_list_1.copy()

    if not force_sort(input_list_1):                                     #If the input is already sorted
        bubble_output.write("Already sorted!")
        insertion_output.write("Already sorted!")
    else:
        bubble_sort(input_list_1,bubble_output)
        insertion_sort(input_list_2,insertion_output)

if __name__ == "__main__":
    main()