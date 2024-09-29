import sys

calculated_numbers = []

def list_func(input):
    # This function converts string lines to lists that contains each of number
    # as their elements and inserts all of the line lists in complete_list.
    complete_list = []
    for line in input:
        line_split = line.split()
        complete_list.extend(line_split)
    return complete_list

def fibonacci_naive(number,naive_output_file):
    #This function calculates the fibonacci value according to naive approach.
    try:
        assert type(int(number)) and int(number) > 0       #If the number is not an integer and is not positive, it raises an assertion error.
        number = int(number)
        if number == 1:
            naive_output_file.write("fib(1) = 1\n")
            return 1
        elif number == 2:
            naive_output_file.write("fib(2) = 1\n")
            return 1
        else:
            naive_output_file.write("fib({}) = fib({}) + fib({})\n".format(number, number - 1, number - 2))
            fibonacci = fibonacci_naive(number-1,naive_output_file) + fibonacci_naive(number-2,naive_output_file)
            return fibonacci
    except AssertionError:
        naive_output_file.write("ERROR: Fibonacci cannot be calculated for the non-positive numbers!\n"
                                "{}. Fibonacci number is: nan\n".format(number, number))


def fibonacci_eager(number,eager_output_file):
    # This function calculates the fibonacci value according to eager approach.
    global calculated_numbers
    try:
        assert type(int(number)) and int(number) > 0       #If the number is not an integer and is not positive, it raises an assertion error.
        number = int(number)
        if number <= len(calculated_numbers):        #If the number is already calculated.
            if number == 1 and len(calculated_numbers)<2:       #If the number 2 calculated before the 1, then len condition is true but there is no result for fib(1). So this condition adds 1 to the list if 1 isn't calculated.
                calculated_numbers.insert(number - 1, 1)
            eager_output_file.write("fib({}) = {}\n".format(number, calculated_numbers[number-1]))
            return calculated_numbers[number-1]     #Returns the result from the memory.
        else:
            if number == 1:
                eager_output_file.write("fib(1) = 1\n")
                calculated_numbers.insert(number-1,1)         #It inserts the calculation in the index which is equals to number-1.
                return 1
            elif number == 2:
                eager_output_file.write("fib(2) = 1\n")
                calculated_numbers.insert(number-1,1)         #It inserts the calculation in the index which is equals to number-1.
                return 1
            else:
                eager_output_file.write("fib({}) = fib({}) + fib({})\n".format(number,number-1,number-2))
                try:
                    fibonacci = calculated_numbers[number-2] + calculated_numbers[number-3]         #If the numbers are in the memory, get them from the global list.
                    eager_output_file.write("fib({}) = {}\n".format(number-1,calculated_numbers[number-2]))
                    eager_output_file.write("fib({}) = {}\n".format(number-2,calculated_numbers[number-3]))
                except:
                    fibonacci = fibonacci_eager(number-1,eager_output_file) + fibonacci_eager(number-2,eager_output_file)       #If the numbers didn't calculated by the function, calculate it for these numbers.
                calculated_numbers.insert(number-1,fibonacci)       #It inserts the calculation in the index which is equals to number-1.
                return fibonacci
    except AssertionError:
        eager_output_file.write("ERROR: Fibonacci cannot be calculated for the non-positive numbers!\n"
                                "{}. Fibonacci number is: nan\n".format(number,number))

def main():
    input_file = open(sys.argv[1], "r")
    naive_output_file = open(sys.argv[2], "w")
    eager_output_file = open(sys.argv[3], "w")
    input_list = list_func(input_file)
    for number in input_list:
        eager_output_file.write("--------------------------------\n"
                                "Calculating {}. Fibonacci number:\n".format(number))
        fibonacci_eager(number, eager_output_file)
        if int(number) > 0:     #To prevent this message to overlap with error massage.
            eager_output_file.write("{}. Fibonacci number is: {}\n".format(number,calculated_numbers[int(number)-1]))
    eager_output_file.write("--------------------------------\n"
                            "Structure for the eager solution:\n"
                            "{}\n"
                            "--------------------------------".format(calculated_numbers))
    for number in input_list:
        naive_output_file.write("--------------------------------\n"
                                "Calculating {}. Fibonacci number:\n".format(number))
        result = fibonacci_naive(number, naive_output_file)     #The calculation for that number is done from scratch and saved.
        if int(number) > 0:     #To prevent this message to overlap with error massage.
            naive_output_file.write("{}. Fibonacci number is: {}\n".format(number, result))
    naive_output_file.write("--------------------------------")

    input_file.close()
    eager_output_file.flush()
    naive_output_file.flush()
    eager_output_file.close()
    naive_output_file.close()

if __name__ == "__main__":
    main()