# string

string is char array 
 then why dont we use char array

 cuase string gives us the abstraction with non primitive data type string class 



this class is part of java.lang

when we create nre object we use new keyword but when we delcare string we decalre them literally

but why??



string is immutable class 
it is used for passwords, urls , hashs

there are two ways to declare string 

1. litral ==> string s="hello" all listeral goes to string pool
2. string pool ==> java have crated an seprate space in the heap memory for strings that is called string pool

and java try to reuse string as much as possible

if we declare
String s1 = "hell0"
String s2 = "hell0"

java will use this same hello as object but variable refernce will be created 2 - s1 and s2

but if we declare an string by with new keyowrd than 2 hellos will not be same

* only compile time constants goes to string pool automatically

* run time constant goes to heap



like example 

string s1 = "ja" + "va";
string s2 = "java"

s1 == s2 will be true. cause it is being decided to compile time

String s1 = "ja"
string s2 = s1 + "va"

this is false
cause this being decided to runtime

// problem of immutability

String s = "";

for(int i=0;i<5;i++){
    s += i;
    System.out.println(s);
}

every time loop runs there is new object being created.

first declared string in this case empty string is being created in sting pool and all other obj are in heap

here meomry is being wasted 


## string class

public final class string{
    private final byte[] values;
    private final byte coder;
    private int hash;

    byte[] values  --> every char's unicode values is being stored

    what is beinfit of stoering it as byte instead of char array

    when storing the char array every char takes 2 byets to store but all this char are unicode val-> we use unicode to store any char of any lang in world so java is univoersal lang

    but most of time java stores english chars so java thought why not i store char as 1 byte ascii val to save space 


    byte coder only can have 2 val 0 and 1 

    if our char val is in ascii limit then 0 if not then 1

    if val is 1 then we have to read 2 indexes in ascii val


    hash --> string's hash values


}


string optimization --> 
1. string pool
2. char()-> byte[]
3. caching the hash values


## stringBuilder / stringBuffer

StringBuffer sb = new StringBuilder("hello")

System.out.println(s8)

we can create strings


stringbuilder - not thread safe

stringbuffer - thread safe

-->
```java
class stringbuilder extends abstractsstringbuilder{
    byte [] values; // coder
    int count;
}

stringbuilder sb = new stringbuilder("java)
```

when creating an java array in memory it also create some empty spaces in memory 

so when we append the string we dont have to create new object

when that array becoms full , capacity of array becoms double

so we dont have to create new object we work on same object

there are many methods we can implement after making object from stringbuilder

stringbuilder to convert into normal string 
use toString;

stringbuilder dont override eqals() so it will bydefault compare reference

stringbuffer ---- > when we want mutable string and thread safe string then we use

we 2 different thread want to append same string then race condition happens

string buffer synchronize them

strinbuilder is faster than strinbuffer cause it dont care about thrad safety

so in real life we mostly use strinbuilder

we ensure race safty by manually coding lock in code