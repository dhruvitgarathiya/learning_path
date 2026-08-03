# streams in java

data processing - applying the set of operations

to write code that is too much declarative

employees.stream().filter(x->x>50000).map(Employee::getName())).sorted().toList();


stream have purpose of making code for like sql (functional programming)


stream : tool for processing squecne of data to do list of operations


when there is array like numbers so when we apply .stream() on it then we make wrpper around it kind of and do operation on it

### stream pipeline architecture

source -> intermediate operations -> terminal operation

when you not write the terminal condtion - stream will not start

source of stream - startig of stream - can be collection , arrays, stream.of(), infinite stream

intermediate operations -> filter(), map(), sorted(), distainct(), limit(), skip()

intermdiate operation have retrun type of stream

terminal operatino-
foreach()
tolist()
collect()
count()


laxy loading - no terminal - no stream intiated

lazy evolution is used in short circuting
it is like break statement to make stream optimized

after applying terminal condition stream is dead we cannot use stream now

we can make two type of stream - stream() , parallelstream()

infinite stream -> iterate(seed, nextfn)
generate(supplier)


**Primtive stream**

1. IntStream
2. DoubleStream
3. LongStream

object stream -> primitive stream and vice versa possible

and int to long and to double and vicversa is possible

  list of terminal operations

    1. collecting result
    tolist()
    collect()

    2.reducing
    reduce()
    sum(),max(),min(),array(),count()

    seaching/matching
    findfirst()
    findarray()
    anymatch()
    allmatch()
    nonematch()

    4. iterations
    foreach()
    foreachorders()


**collector**

gives the interface to collect the object with .collect lambda func

basic collectors
tolist
toset
tomap


groupingby
gorup our output based on some key

speical case of groupingby - partitioningby()



