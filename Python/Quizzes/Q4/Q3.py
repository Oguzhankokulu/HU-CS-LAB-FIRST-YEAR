import random

number = random.randint(1,10)
guess = 0
while guess!=number:
    print("Guess a number between 1 and 10")

    guess = int(input("Enter your guess:"))
    if guess not in range(1,11):
        print("Your number is not in the range of between 1 and 10.")
    else:
        if guess == number:
            print("You guessed the right number. Congratulations!")
        elif guess > number:
            print("Your guess is greater than the right number. Try Again!")
        else:
            print("Your guess is lesser than the right number. Try Again!")