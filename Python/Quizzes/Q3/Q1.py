year=int(input("Write the year:"))
if year%4==0:
    if year%100==0:
        if year%400==0:
            print("{} is a leap year.".format(year))
        else:
            print("{} is a common year.".format(year))

    else:
        print("{} is a leap year.".format(year))
else:
    print("{} is a common year.".format(year))