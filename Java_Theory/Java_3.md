## operators

arithmatic operators

relational operator give boolean results

bitwise operators
& , | , ^ , ~ , >> , << , >>> 


in left shift it will be being doubled by every left shift , but after an time it becoms the negative

8 bit - for 6th bit it gonna be doubled but after that 7th bit it becomes negative


n bit - for (n-2) bit it gonna be doubled but after that (n-1) bit it become negative

and after that it becomes 0.

after that type promotion happens.

bitwise only being performed in int and long

**short circuit - && and ||** 
 
 if i am gonna do && of A and B . if A is already f then i dont have to check b cause whole condition will be false.
 
 same for ||
 
 to avoid short circuting use & in java
 
 
 precedence table - BODMAS
 
 **switch** - limited use case -> should evaluate to byte, short , int , char, enums

and no duplicate cases allowed

after jdk7 strings can also be used as switch expression

jdk-14 have many enhancement for switch

**switch vs if-else-if ladder**

switch can test equality but ladder can test the boolean

switch is not efficient than ladder

for optimizing the switch statement java internally make jump table 

but not always cause jump table are not always efficient.

jump table also have 2 ways 
when i's values are dense - table switch

when i's value is not dense- lookup switch it uses the binary search


we can also nest the switch statements.

## Arrays

int[] rolleNums;
 
collection of a particular data type

it will be stored like varibal like 32 bit for each entry.

exception give by compiler
indexoutofbound exception - when you try to excess the index in array which is not part of array

multidimension array - array's element is array itself





all the primitive data type stores in memory as stack memory.

non primitive data type being stored as head memory

while creating array the reference varibale of array is being created in stack memory but actual array is being created in heap memory,

strings are not character array it is more than that , non primitive data type and pattern like array of arrays.
