# Functions in java

type of function 

no ip , no op
ip , no op
no ip , op
ip , op

ordering of parameter can be anything

compiler errors are the errros that compiler thorws in coding that he absoulty know will get confused while compiling.

like if we give same name to two function it will throw error cause he doesnt know that he have to call which function when calling it in main

casue in java if you declare the variable to hold the output of the function then it is okay , value can be lost in memory it will not thorw erro.

so compiler will not know having 2 functions which are you calling


### method chaining

Method chaining is a programming technique where you call multiple methods on the same object in a single, continuous line of code.Instead of writing separate statements for every action, you connect them together using dots

To make method chaining possible, each method must return an object (usually the current object instance using the keyword this) instead of returning void.Because the method returns the object, you can immediately append another dot and call the next method on it.

###  Constructor Chaining 

developers use Constructor Chaining via the this() keyword to make one constructor call another.Refactored "Clean" Implementation:javapublic class Member {
    private String email;
    private String phone;
    private boolean isPremium;

    // Constructor 1: Passes default fallbacks to Constructor 2
    public Member(String email) {
        this(email, "Not Provided"); 
    }

    // Constructor 2: Passes default fallbacks to Constructor 3
    public Member(String email, String phone) {
        this(email, phone, false); 
    }

    // Constructor 3: The "Master" constructor that handles the actual work
    public Member(String email, String phone, boolean isPremium) {
        this.email = email;
        this.phone = phone;
        this.isPremium = isPremium;
    }
}

How it works: When you call new Member("user1@email.com"), Constructor 1 catches it and instantly forwards it down the chain to Constructor 2, which then forwards it to Constructor 3. All your data assignment happens safely in exactly one place



