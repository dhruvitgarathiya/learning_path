interface deep dive

interface defines what object can do without saying how it do it

class is bleuprint of object but interface is blueprint of behaviour

method we write in interface will be by default public

so when implementing it in object you cannot narrow down it you can only widen it


// variable inside interfaces

we can define variables 

//

we can define methods in interface body by default modifier 

 
 
* if an class is exteding other class also implementing the interface and both have same method with same name then class will given priority.


so now after java 8 we can have defualt, static, private methods and also static and final variables then how it is different from abstract class now


the biggest gap is intention

interface - contract , roles /  functionalities

ex. ruunable , walkable, playable


abstract class - families or similar class

ex. animal , dog , duck..

we represent abstract class as is-a relationship

interface as can-do relationship


syntax wise difference 

interface cannot have normal fields

abstract class can have normal fields

interface does not have constructors

abstract class can have 

interface multiple inheritance

interface bydefualt public methods

abstract class allow all


### functional interfaces

if interface have only one methods then it is

it unlocks the functional programming 

with lambda expressions

we already have in built interface like comprabale predicate

### marker interface


empty interface

*  interface clonable
* interface serializable
* interface randomaccess




interface also after compilation becoms an class

everything in java becomes an class after gettinn into compilation 

in compilation it will attach an tag of acc_interface in class so compiler knows that it have charctertics of interface



