## input / output

types of ip and op:

1. console i/o
2. file
3. network
4. memory

**console i/o**

by system.out.println("helo");

here by our knowldge of oops we can say that println is some object's method which are we calling

here out is reference variable of that object 

and System's s is capital so it is clss

we are using the object directly from class which means it is an static class

so by this we can have broad idea of this system 

System.err -> can use for showing error

### input

inputStream type's variable in

we can call it by System.in


Inputstream (abstract)-> read

fileinputstream 
byteinputstream
bufferinputstream
datainputstream

outputstream(abstract) -> write

fileoutputstream
bytearryoutputstream
bufferputputstream
printstream

 read() method reads one byte at a time
 
 
and input stream take byte stream one by one and covnert it to char stream

so to solve we use java buffer's function buffer stream

buffer stream will give byte stream all togather and input stream than change this to char stream

it have limitations 

like it take 2 step to take numbers as input

scanner class is slow than buffer read

## immutable class

rules 

mark class as final

mark variable as private and final

no setters

but we can make shallow copy of the object and than pass it to class as variable , then we can set them despite being immutable class

so now to remove this drawback also we use defencive copy of that object in our class

so it have fixed value assoicated with class , so we can not change the real object



## object class

this is parent class of all the class we use

every class inherit from it.


