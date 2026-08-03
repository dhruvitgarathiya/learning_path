# lambda function

eerything in java revolve around objcet

to perfrom any function we have to put it in class and make object

but if we want to use behaviour without making class

we use funcational interface - lambda functions

\\ we can only pass argumnt in function 
no functions can be passed in function

behaviour parsing 

we are making clear differentiation between the student class and comparator interface of it in which it compares the marks of student

we can make anoymus class under the method


funcational interface

1. only one abstract method
2. static method
3. default method

lambda expressinns--

(parameters) --> expressions


types-

multiple parameters
single parameters
no parameters
multiline lambda


we get compiler time advantage

we rearly make our own functional interface

java gives us plenty of functional interface

function lambda interface-to transform
take i/p -> o/p
interface(<r,t>){
    r apply(T t);
}

conusmer labmda interface - when we want to accept something and dont want to return like println

supplier lambda interface - it takes no input gives output
example get methods

predicate lambda interface - to perform test and in output get result in true and false

### primitiv functional interface

function<Integer,Integer> s = (x -> x*x);

we know generic dont work with primitive data type

so the steps involved in this would be unboxing, operation, autoboxing

we are doing one time in this so it is not costly but when we do it in loop it is costly 

so lambda have to support primitive

* int
* long
* double

this three are being supported

function
* infunction(int->R)
* longfunction(int->R)
* doublefunction(int->R)

here in apply method it will not take Integer but primitive int as input and R as output

we can same this kind of interface for consumer , supplier and predicate

primitive oeprator family --

IntunaryOperator(int->int)
LongUnaryOperator(long->long)
DoubleUnaryOperator(double->double)

### method reference

we can furthur optimize lambda

(x) -> system.out.println(x)

list.forEach();
             ^
             |
            system.out::println
             |
             classname::Methodname

by this we tell java that in list's foreach function refer the method od system.out class println  and do the logic what it indicates  

can we exchange evry lambda with method reference

no we only can when we are using in built func

type of method reference
* static method reference
math::abs

* instance method refernce
x-> so(x)
system.out::println()

constructor referecne
supplier<T>: () -> new arrayList<INteger>()
(void -> T)

s = Arraylist::new

if sometime method ref is not making code readable , then use lambda

### functional compositions

int result = ((x+2)*3)
     add 2 
     multiply 3

mathematically
    f(x) = x+2
    g(x) = x*3

g(f(x))

function<T,R>
    R apply(T t);

**and Then()
compose()**

f.compose(g) --> f(g(x));
f.andThen(g) --> g(f(x));

compose work right to left
andThen work left to right

function<T,R> f
function<V,T> g

compose -> first perform g then f, takes v as input and gives R as output


### predicate chaining methods

and()
or()
ngate()- not like function



