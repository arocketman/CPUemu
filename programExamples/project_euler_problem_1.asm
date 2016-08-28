#Find the sum of all the multiples of 3 or 5 below 1000.
#D3 will hold the result.
MOVE 0,D3
#D4 will be our variable to go from 1 to 1000
MOVE 1,D4
# The loop starts here
MOVE D4,D5
MOD 3,D5
BEQ 4104
MOVE D4,D5
MOD 5,D5
BEQ 4104
ADD 1,D4
CMP 1000,D4
BNE 4024
JMP 4096
# This is for adding the found value to the partial sum.
ADD D4,D3
JMP 4072