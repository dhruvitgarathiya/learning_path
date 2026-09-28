# First class function

## function statement:

```js
function a(){
    console.log("a called");
}
```

## function expression

```js
var b = function(){
    console.log("b called");
}
```

what is difference between them 

Hositing

when we call both on top of the file , a will be called and b will be throwing an error

when js store var and function in memory phase , it stores the var b as undefinend and func a fully

so when calling the b on top of the file it will throw an undefined error


## anonymus func

does not have their own identity

```js
function(){

}
```
if you alone run this code it will throw an error
so it is used in function expression

## named function expression

```js
var  c = function xyz(){
    console.log("B called");
}
```
## difference between parameter and argument

var b = function(parameter1 , parameter2){
    cosole.log("b");
}

b(arguemnts1 , argument2);

## first class function

 passing another function inside an function , and we can return anyomus function from function

 ability of doing it called first class function

 this is programming concept not js concept

## arrow function




  


