 # jvm , jre & jdk
 
 each plafrom have their independet jvm to convert the byte code to machiene code
 
each platform have their own machiene code.


java ----> 
		     JDK
		    --------
		   |	JRE    |
		   |	-----  |
		   ||Jvm | |
		   |	-----  |
			-------
			
JVM is an virtual machiene like virtual computer in which byte code runs.

compiler and interpreter. both have task to taking an program and chaging it into another code.

compiler take an source code , in one time it reads whole file and convert that into machiene code.

interpreter take source code and after line by line reading and generating the machinene code.

jvm take bytecode . give it to interprete that convert the code line by line and into machiene code in early timing of java. beacuse we wanted to see program runnig as soon as possible. beause java already follows the long process of bytecode and portability.

after some time we have integrated interpreter with JIT compiler ( just in time ). compiler is still slow that's why we cannot solely rely on compiler.

which is frequant code we use in program - JIT compiles it immediatly and code which is not used frequently intpreter converts it line by line in background.


### JRE 

this is JVM + class libraries.

if in source code you want to console i/p and o/p or read files , in brief if you want to access internal library 

so this operations are in JRE.

### JDK

this consists of JRE + compiler + debugger + java docs etc.. 

it is complete packgae to run java program.

### jSE , JEE , JME 

java strandrad edition - core java - how java was made

java enterprise edition - for making real buisnees use with new library and features like transactional 
we also call it jakarta ee

java micro edition - lightweight edition of java for mobile phone application - this is not used anymore


## variable

name variable as descriptive as possible

name of variable - identifier

java is statically typed language

how we declare the varible in java
```java

datatype name - variable name = value ;

```

data type :

1. primitive
2. non-primitive

primitive :

1. interger - byte , short, int, long
2. real(floating)- float, double
3. char- char
4. bool- bool

every number in java is signed

in java application we mostly dont use float, less precision than double.

nowday's hardware are not optimizd with float so we use double.


**chars**:

other lagnguages stores the chars with help of ascii like convrting char to int then storing it in container as 8 bit binary

but it have limitation like it cannot stores the values beyond the english alphabet but java want to make itself an portable language 

so it introduced the cnocept of unicodes which are 16 bit binary represantation.

**bool**:

two val possible true or false


stored values are called literals.


'
**declaring and defining the values are differnt thing**

int x: <-- declaration

 x=4 <---- defination
 
 
-> keywords are the reserved words we cannot use as varible names.

**to store negative number in java**

it store it by doing 2nd compliment of the binary representation 

why 2nd cuase -0's 1st compliment exists but it should not so 2nd compliment give precise results.

negative number have 1 as most significant bit where positive have 0


**float being saved in 32 bits**

1 bit sign bit , 8 bit for exponent, 23 bits for mantissa


**type conversion**

1. implicit
2. explicit

rule for implicit : destination data type should be wider than the source data type.


explicit conversion - we have to manually convert it.there will be widening and narrowing conversions.

b = i; this is wrong

b = (byte)i; this is right

**truncating conversions** - 

when float and double try to convert it into int.

decimal part are being ignored.

**automatic type promotion**

byte a = 50;
byte b = 40;
byte c = 100;

int i = (a*b)/c;

 when we do a into b the intermediatory result become out of range in byte so java promote it to int , also when dividing by c it also become out of range. so it also convert it to int,

**rules** 

byte short and char value are promoted to int

if one operand is long the whole expression will become long

uf one operand is float entire expression will become float

if one oprand is double , entire exp is double