object class

java.lang (system) package contain this class

there are command behaviours which we need in all class

object can instanticate any class cause it is parent of all class this is beacuse of polymorphism

core methods:  

tostring()
equal()
hashcode()
getclass()

clone()

finalize()


### tostring()

convert anything to string

### equals()

compares to references

### hashcode()

return an integer of an object

integer- hexadecimal number

rule : if two objects are equal there hash must be equal

so if equals is true then hashcode must be same but reverse might not be true

### getclass()

return runtime class of an object

java have class name Class 

this method return Class type object

### clone

create copy of an object

protected type method

throws exception - class not supported

not every object should be cloneable

database threads

every class which want to be clone should implement clonable interface

clonable interface is marker interface
which means interface is empty


this function does shallow copy


* arrays are not primitive data type

so it also have parent class object so we can implement array methods from objects

* but int char float are primitive so it dont have object class as parent

## enum

we can create simple  variable for the purpose of solving the problem which can be solved by the enum


but that approach have it's problems:

1. type safety
2. poor readability
3. no grouping of related entities


enumerative type

predefined constants


* each enum is class
* extends enum class
* each constants are static and final object of type of that enum class


(((static variable a re not stored in stack but they reside in a seprate memory area called metaspace.)))

we can make variables of the enums cuase it's class at the end

  
