# object oriented programming

this is a way of writing code. java is object oriented programming language

to solve real world problem , you have to mimic real world

we can attach the charcterisitc and interincsitc properties to an single entity

this is called object oriented programming

let there a human , he is student , son and employee 

his charcterisitc depends on role he is playing

but programming we can not represent whole hireachy

we use the only variables we concerned.

every object is created in heap memory

object's location (which is stored as variable) is stored in stack memory

so it is called reference varibal which refers to an object's address

mew keyword's memory allocation is done in runtime

so it is called as dynamic memory allocation

so object is being made in runtime

instance baribale (varible of object) holds a default values

int - 0
float - 0.0
boolean - flase
string - null

local variable dont have defualt value

cause instance varibale are stored in heap have  broader scope

and local varibale are stored in stack and have limited scope till function 

so java try to optimize itself


**rules of constructor**

same name as class


no return type not even void

automatically called doing object creation

used to intializing an object

it can be overloaded



so when we write the 

student s1 = new **Student()**;


so the highlighted part we wrote for calling the constructor

if you haven't made constructor then java make constructor by itself.

parameterized constructor:

every value of parameter in object creation to be filled mandatory.

**this keyword**

this stores current object's reference.




Constructor chaining -

we can call the other constructor by using one constructor using this keyword.

we cannot call constrcutor manually


some time while creating object at run time in heap , our heap memory is full so we can't get space in heap


at that time we get the runtime exception of not enough space in heap


**memory thing in objects**

our refence varibale of object is stored in stack which takes 4 to 8 bytes generally
as other varibles


but our object is stored in heap as it consist of all the varibales like name , age... are total n then we assume object should take 4n or (4(some n) + 8(some n))

but this is not how it is being stored


three things which are being considered while stroing an object

1. header size
2. excat fields
3. padding

header stores metadata of objects
like markword - info about logs, synchronization, garbadge colleciton
this take 8 bytes

and like markwords it also stores the class pointers which takes 4/8 byts

padding - is optional ,
we beileve in java that if any thing is being stored in java should be stored in multiple of 8 bytes cause when cpu takes data to process it takes in the chunck of the 8 bytes

so this same applies to the object memory allocation

so padding try to make the object storing to make it 8 bytes multiple


### call by value

java only have concept of call by value 
there no thing such as call by refernce in java

but we have something like that works like call by reference but it is actually call by values


when dealing with objects when we try to change the value of object by passing it to different mathod with different varibale name

but at the end it is an referntial varibale pointing to same object . so it will copy the address from the class object and apply the method and give the result.

**static keywords**

if some values are gonna be static for the rest of the entries than it will be declared as static keyowrds

they are not stored in head 


methods and variables can be static

one static method can only call other static method


static method can only store static variables


static method does not have access to this keyword

 parameter can not be static cause it is used and dumped in local scope 
 
 
 root level class cannot be static but nested class can be
 
 
 final keyword to define and finlize the value of variable at once like PI.
 
 it can be applied to variable, method, class, parameter
 
 
**why main is static in java?**
 
 if main was not static java have to make object of it to intlize.
 
 so main is static , we make to make every method in main static
 
 we can make static final togather
 
 
 ## encapsulation
 
 
 both data and behaviour should be encapsulated on object
 
 we should not provide unrestricated access of data
 
 we should not give permission to anyone to make object of our class
 
 access modifier
 
1. public
2. dufualt
3. protected
4. private



make data private and give their access to anyone by bahavbiour methos

remember the example of the balance


### high level abstraction

seprating what from how

when object is tighly coupled with concert classes.v  

 
 
