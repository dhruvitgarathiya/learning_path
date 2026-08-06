# optional classes

java have null pointer exception problem

string name  = getNmae();
int len = name.length() <-- this can give nullpointerexception

we can put check before comparioson

but like this we have to put too many null checks and our code become ugly

to identify which kind of null it is java gives us optional class object

it is wrapper class

when we are not sure that this object can have null values or not
 we return them an optional class

so in string name = getName();
we are modifiyin it with
Optional<String> name = getName(;

it gives us too many methods like .ifPresent();

this is very good practise


to store null Optional.empty()

to store value Optional.of("Adity");

if we want to declare the optional in way that it can take val and null -optional.ofNullable()

getting value from optional 
- get()
- isPresent() (different from ifPresent())
- ifPresent() - recommended to use it.
name.ifPresent(System.out::println);
- orElse() - fetches an value
- orElseGet()
- orElseThrow()
- ifPresentOrElse()

## optional transformations

we can apply transfromations in optional like stream

methods like map(), flatmap(), filter()

optional is like an ministream


map(T->R) transfromation 

streams works on -> [u1,u2,u3..]
optional works on -> user

so it is working like mini stream

some people use it like data type - it is allowed but it is not good practise



example usage of stream 

users.stream()
     .map(u -> u.getEmail())
     .filter(x->x.isPresent())
     .map(x->x.get())
     .toList();


# parellal stream

// squeantial stream
list.stream()
    .map(x->x*2) // stateless
    .sort(x) //statefull
    .forEach(System.out::println);

//parellel stream
uses multithreading internally
we always passes the pipeline vertically in the stream so we do all the operation sequencally

and the splitoperator passes the input one by one in pipeline for processing

but instead of this we can passes many inputs and do one or more operation witht the help of parellel stream

list.parallelStream()
    .map(x->x.get())
    .foreach(system.out::println)

parellel stream dont give us output in ordered manner cause all the input cannot complete the operation in similar time


parellel stream ->
        - spilit the data into chunks
        - assign chunk to thread
        - process independently
        - combine result in for join pool

this all operation and step are handled by java internally

### spilt iterator

normal iterator have limited scope and function and parallel stream cannot get the benifit of it

spiliterotr -> traverse the element 
decompose the source into parts
describe the source

methods of splititertor ( we not gonna need it cause java does it internally)

tryadvance - responsible for traversal
trysplit - try to split the dataset

parellal stream is not magic there are so many ifs and buts 
java have multiple stateful operations so they need all the element

so spilitting them and combining them and also take time and they cannot be benifited from parallism

so java try not to use parallism - and sometime it becoms overhead

so most of the time we use stram only cause we are always having mix of operation

use case -

* dataset is huge (millions)
* cpu intensive work
* stateless operations
* optimized data structor for parallel streams( arrays or arraylist)

when not to use
* data is small
* statfull operations

### shared mutable resources

List<integer> list;
list<integer> result;

list.stream()
    ....
    ....
    foreach(x->result.add(x));

when two threads are figthinh to be added at same positon in result this is called race condition and data can be lost


### can optional can use primitve 

optional have primitveoptionalclasses

- optionalint
- optionalDouble
- optionallong

num.getAsInt() - useful method