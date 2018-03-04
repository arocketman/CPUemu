# CPUemu
CPUemu is a CPU emulator based on the MC68000. The emulator is capable of executing simple programs and show their results. 

* [Project structure](#project-structure)  
* [Instruction's format](#instructions-format)
* [Working instructions](#working-instructions)
* [Program examples](#program-examples)
* [Contributing](#contributing)

# Project structure

These are the main classes of the system with a brief description:

 - CPU - Contains all the CPU registers and methods to manipulate them.
 - Memory - A simulated RAM with a fixed size and accessor methods.
 - Sim - The emulated system. Contains instances of CPU and Memory.
 - InstructionSet - Contains all the possible instructions the CPU can execute.
 - Compiler - A simple compiler that parses instructions and writes them to memory.

There are other utility classes : Utils and Costants.

# Instruction's format

As of version 0.1 the instruction format is as follows:

**8** bytes divided in [4 bytes for the instruction , 2 bytes for source operand , 2 bytes for desination operand]
There are two kinds of instructions:

 - **Register-register** operations (e.g: MOVE D0,D1)
 - **Immediate-register** operations (e.g: MOVE 12,D1)

Register-register operations are transformed as follows:

MOVE -> MOVR

ADD -> ADDR

Immediate-register operations are transformed as follows:

MOVE -> MOVI

ADD -> ADDI

This is an example of how the instruction is saved onto the RAM (MOVE 5,D0): 

![RAM view](http://i.imgur.com/RHJT5GR.png)

# Working instructions:

Arithmetic instructions: 

* Move between Data registers       (e.g : MOVE D0,D1)
* Move between Number and data register (e.g : MOVE 4,D2)
* Addition between data registers   (e.g : ADD  D1,D2)
* Addition between Number and data registers (e.g : ADD 4,D2)
* Subtraction between data registers (e.g : SUB D1,D2)
* Subtraction between Number and data register (e.g : SUB 5,D2)
* Multiplication between data registers (e.g : MUL D1,D2)
* Multiplication between Number and data register (e.g : MUL,D2)
* Modulo between data registers (e.g : MOD D1,D2)
* Modulo between Number and data register (e.g : MOD 5,D2)

Jump instructions: 

* Branch equal (BEQ 4000)
* Branch not equal (BNE 4000)
* Inconditional Jump (JMP 4000)

Other instructions: 

* Compare instructions with a register (CMP D0,D1)
* Compare instruction with an immediate operand (CMP 1000,D1)

# Program examples

Program examples can be found in the programExamples folder. To load them you must reset the CPU and memory first then file->load the file.

![View of a loaded program](http://i.imgur.com/bAhXZgA.png)

# Contributing

Feel free to contribute and to send pull requests, there are still plenty of instructions to be added and features that could be improved.
