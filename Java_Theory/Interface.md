# interface

interface does not have any relation with object, it's syntax may seem like class

but it represent it self as contract

generally we dont define any methods in interface 
we just declare them
we define them in child class

so this is pure abstraction

like which child class implement the interface it has to define all the methods defined in interface that's why it is contract

# polymorphysm

static methods cannot be override

beacuse they belong to class and not to objects

private methods cant be overriden
final methods cannot be oveeriden

fields and variale they cannot be polymorphic

to override varibale just make getter methods

final class cannot be inherited 

# why java have only one public class per file why name of that class should be same as filename

jvm can know that which class have main method

jvm can acess file that's why it is public and name is same so jvm can load file directly without confusion

by that logic our project can have multiple files so each file can have one public class and main method then how can jvm know which method to execute

asnwer is jvm know it cause we will run that file only to start compiling the code


# autoboxing / unboxing


every primitive data type have the non primitve classes for it's packages 

so their class can also have objects like example int have package non primitive class called Integer then we can make object of it

then how it is different from the int data type

answer is int data type which is primitve data type stors in stack where as non primitive Integer class's object is being stored in heap memory

why wrapper classes exsits

cus there are so many things like collection framework they deal with classes and objects so they cannot work with primitve data type

and we want so many methods and classes for many operations

then why primitive exists

first reason is legacy code , every language have primitve and that continituy cannot be broke

main reason is primitve data type is fast. cause class and object need much space and take time to store


then what is autoboxing 

anything declared in primitive int and assign the non primitive class then it will automatically being converted in non primitive class

java use ValueOf method to convert the data type to object

and unoxing is vice versa we can assing th primitive data type to non primitve class

 where all this autboxing and unbocing appllied

 1. assignment
 2. method calls
 3. arithmatic operations


case of null :
object can store null but primitive data type cannot so in that case it gives error

case of comparison :
when comapring the two non primitive int class with same value 
Intger a = 200.
Integr b = 200.

a == b will give false

a and b are refernece address of object which are different so it will give false.

but int will give the true it is comparing the value.


## pojo classes

plain old java objects



## static nested class 

1. does not need an instance of outer class
2. class can be intiated like normal class
3. can access only static members of outer class
4. can acess non static members by having a reference of outer class
5. it is just like normal class & can do anything an outer class does


## nested class 

innner class's object being created with the refernece of outer class

inner class can access any method and variable of outer class

old java doesnt allow to make static  method or static var inside the inner class but java 16 solved this

## class without name

for some work that is too small or only one time thing - dont need to make new class can declare class in main function it self that is called class without name

we cannot make constructors in this
 