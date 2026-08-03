# collection framework

java gave us good support of every data structure

there is dynamic array - like if we keep adding element then size of array keep increasing

this is called arrayList

java also have Linkedlist

hashset
stack
queue
maps - hashmap , linked hashmap
bst - we have treeset and treemap also in java

### iterable

in java interface in which we can travel one by one

collection which want to be travel one by one it have to implement iterable interfdace

iterable have method iterator which gives method of hasNext

when calling arraylist -> it is calling itrator class
which is retuning arraylistierator object
this have hasnext and next methods



// concurrent modification exception

fail fast 
when itertor knwo that list have been changed then it gives this exception

why we cannot directly extend iterator class with array list why we need arraylistiterator

the answer is when we do it , it only make one object which implement mathod hasnext and next once then their position vairable have value of arraylist.size()

so when we need another loop of iteration it will start from the last index so it is not feasible ,so we implement arraylistietrators which is linked to itrator method

it gives us power of iterating in same list with unlimited position variables and without any errors.

### collcetion interface

in java we have collection interface and we also have collections class which is utility class


