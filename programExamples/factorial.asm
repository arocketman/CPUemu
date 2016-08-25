#Factorial of a given number.
#Change the '5' with whatever number you want to get the factorial of.
MOVE 5,D0
MOVE D0,D1
SUB 1,D0
BEQ 4048
MULT D0,D1
JMP 4016
#Program finishes we keep jumping to the same position to simulate its ending.
JMP 4048