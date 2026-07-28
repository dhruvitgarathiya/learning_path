# generics

datatypes gives us clearity about the rule which operation we can perform on the variable

like interger , string

### upcasting and downcasting 

upcasting : imagin class animal 
class dog extend animal

can we write animal a = new dog() --> yes cause animal is parent class and dog is child class

this is upcasting basically

we can call dog's methods from animal class

so we specific class being converted into general class

downcasting : 
 object obj = "hello";
 string s = obj; 
 compiler gives error

 so we have to do manual casting

 this is manuial casting

 string s = (string) obj;

 this check in runtime if s object is type of string class 
 if it is then it will cast it to string 
 if it is not then it willthrows an error

 this is very danguers


jvm checks in runtime that if is castable or not

### generics

is there any data type in which i can store any data type value

yes there is -> object it is parent class of every class

but operations when we dolike + - then it gives errro cause it dont know which values are there and which operation i have to do 

then we have to do operations with downcasting 



limitation fo using object class as universal type

1. type information is losyt
2. wrong object could be inserted

3. casting becoms necessary when reading

4. many error shif to runtime

so soultion is **generics** 


```java
class Box<T>{ // type parameter
    private T value;

    box(T value){
        this.value = value
    }

    public T getValue(){
        return this.value;
    }
}

// in this main method

{
    Box<Ineger> b1 = new Box<Integer>(value:10);//type argument

    Box<String> b2 = new Box<String>("hello"); 
}
```

so we get the errors in compiltime , we cannot do any work that gives us the runtime error

### genric methods

this methods can work on any data type 
when we call them in runtime at that time we tell them that which data type you have to work on

```java
public static <T> T getresult(T x){
    return x;
}
```
here also we will have compile time errors

// generic methods pattern
<T> returnType methodName(T parameter){

}

* type inference -  java assume the type of variable from it's value

##

what if we want class should work on number related data tpye

like int , double , float not string , boolean 

so we want to bound our generics??

also another question that when we create genric method and we want to perform some action like getDouble(type specific methods) then we cannot peerfomr casue java does not know that which type we gonna work with .. so this is problem aslo

soluton 
```java
class Box<T extends Number>{
    public void printDouble(){
        system.out.println(value.doubleValue())
    }
}
```
so my t is bounded that it implement the number class

##

<T extends classes & Interface1 , Interface2>






# wildcards

generics breaks the parent child relationship

in java we have List interface , which is being implemented by the Arraylist in collection framework

List<Integer> list  = new ArrayList<>();

imagin we have animal class and dog class

dog is extend of animal so we can create dog object of animal class means we can store dog object in animal referened memory

but can we do same of animal list and dog list
like can we store dog's list in animal's list

the answer is no

if it happend than it will break type safety

generics are invariant 

if A is child of B generic<A> is not child of generic<B>

wildecard is list that can store anything

List<?> list = ..

wildcard gives us limited scope in general so we use it with bounce

static void fun(List<? extends Animal> values){

}

jo bhi list me ayega yato animal hoga ya uske niche ka ya andar ka class hoga usse upar kuch nahi hoga

List < ? super Animal> 

in this , upar ka kuch bhi bhej do niche ka ek bhi class allowed nahi hai


PECS rule- Producer extends consumenr super


### type erasers

does jvm knows generics?

compiler always changes the generics to object 

so runtime have no idea of generics

rule-

1. if no bound --> replace object
2. if bounded replace with bound
3. insert cast automatically

sooo 

we cannot do this operations beacuse of generics not being in runtime

1. you cannot check specific thing
2. cannot overload method based on generics
3. compiler generated bridge method
generic write an object method for another method to make birdge between them

why java does not support primitives -- 

beacuse primitive data type is not class so it dont have parent of object so it cannot be replace by object at runtime