def printer(input_list):
    total_str = ""
    for str in input_list:
        total_str = total_str + str + "\n"
    return total_str[:-1]

n = 8
myDict = {x:"*"*x for x in range(1,(n+1))}
values_list = myDict.values()
print(printer(values_list))

