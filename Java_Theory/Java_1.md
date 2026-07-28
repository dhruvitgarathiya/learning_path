# java intro

why java came?

c/c++ was famous in 1980/90 was fast , simple , close to hardware.


**there was problem of portability**

when running c++ code we have to run it in compiler 

compiler is software which convert code into 0's and 1's.

code compiled in machiene 1 will not be same in machiene 2. 

so machiene 2 have to compile the code another time

this is called protability problem

c / c++ was platform dependent language.

**platform**  

1. processor(ex. intel x86)
2. operating system(windows)

combination of this two is called platform.


' when you write any kind of code in any machiene that that machiene's os have differnet kind of executing or commanding functions.

so different os executes the code and gives different machiene codes.

' processor is brain of computer. it consist of transistors ( in trillions )

transistors have simple task if current is going then it's 0 else is 1 which we called on and off.

there are multiple pin in transistors like p1, p2...

to instract with different computer we have to intract with different transistors.

if processors are different we have to give different binary to different processor.

there is ISA involved in execution in processor which is called internal set architecture. which have control over how to do small operations like ADD, LOAD, STORE and JUMP etc..

different processor have different ISA(different language you can say which intract with processor , it is like grammer)

**c/c++ was platfrom dependent beacuse of this**

**another reason for birth of java was simplicity**

**java is more secure than c/c++**

this is the main reasons of java's birth.

if we write any java file like hello.java

java introduced the new concept of byte code.

### byte code

if there is indina guy gone to china , c/c++ said that if you want to talk to an chienes guy you have to learn mandrine.

java said if you're going to china take your friend with you who know all the language.

that translator friend is JVM(java virtual machiene)  

first when we execute the hello.java then compiler turns it into hello.class that is byte code 

this byte can run in any platform and jvm will concert that into machine code of that platform

but JVM should be platform dependent.

that's how java is portable . we also call it wora - write once run anywhere

at the end server is an computer . so portability was required for this.

some of the problems which c/c++ had which made the language and usage of language very complex like pointers, multiple inheritance, manual memory allocation - which java remove completely

### security

for using java in bakcend we use java servlets . which is used for taking api and data.people also used java in frontend in starting like applets.by using this we can make intial frontend.

so there are so many usecases of java.

so people started thefting data from it

so java introduced and new concept of security

so we have to run jvm in restricted and secure environment.

so this model is called sandbox model.

**why after java c/c++ haven't introduced the bytecode and jvm like things**

this experientments are done with c/c++ and made c#.

and c/c++ are not meant to be platform independent 
 



