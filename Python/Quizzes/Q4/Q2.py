email = input("enter your e-mail:")
def is_valid(email):
    if "@" in email and "." in email:
        return True
    else:
        return False

if is_valid(email):
    print("Your e-mail is valid.")
else:
    print("Your e-mail is not valid.")