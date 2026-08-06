# exception handling

prbolem divided in two parts 
1. exception
2. error

exception - a recoverable abnormal condition that occurs during program execution

errors - this is not recoverable business error occured in program

exception disrupts the flow of program and terminate the program

how to handle?

this does not mean to remove the exception 

if we are not handling the exception we will lose control of program jvm will decide eveything

other benifites are debugging , user experience

default exception handling - in flow of program goto the top of the stack search if someone have handled the exception

this is called stack trace 


**when an exception occurs dont crash handle it & continue.**

try and catch block - wrtie all the risky code under it

try - risky code
catch - preventive code


finally - if exception happens or not this runs
it is uses for cleanup code
resouces close
or logging

finally and catch is optional 