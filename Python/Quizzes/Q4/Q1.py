number = int(input("enter the number:"))
odd_sum = 0
even_list = []
for i in range(1,number+1):
    if i%2==0:
        even_list.append(i)
    else:
        odd_sum += i

average_sum = sum(even_list)

average = average_sum//len(even_list)

print("Sum of odd numbers is: {}".format(odd_sum))
print("Average of even numbers is: {}".format(average))