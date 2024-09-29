b=int(input("Give a b number for the equation 'x^2 + bx + c = 0':"))
c=int(input("Give a c number for the equation 'x^2 + bx + c = 0':"))
delta= (b*b)-(4*c)
if delta>0:
    root1= -b + (delta**(1/2)) /2
    root2= -b - (delta**(1/2)) /2
    print("The roots for equation 'x^2 + {}x + {}' are {} and {}".format(b,c,root1,root2))
elif delta==0:
    root0= -b/2
    print("The root for equation 'x^2 + {}x + {}' is {}".format(b,c,root0))
else:
    print("There is no real root for this equation.")