# Problem 

amazon have it's collection products - million of entry

now if we want 10 most expensive thing list , we cannot do it without aggregate framework.


## in aggregate framework you have 

* read doc
* filter
* sort
* limit
* remove unnnecessary field



aggregate framework is like assembly section of car factory. 


### basic syntax 

```mongodb
db.products.aggregate([
{ Stage1 },
{ Stage2 },
{ Stage3 },
])
```

each stage recevies the output of the previous stage.

suppose collection

```mongodb
[
{"name": "laptop", "price": 70000},
{"name": "mouse", "price": 80000},
{"name": "Phone", "price": 90000},
]
```

### pipeline

```mongodb
db.products.aggregate([
{$match:{}},
{$sort:{price:-1}
{$limit:2}
])
```


flow = collection - match - sort - limit - result



## example of match :

swiggy collection - orders , needs only deliverd orders.

```mongodb
{ 
$match:{
status:"Deliverd" 
} 
}
```


**rule:filter as early as possible it reduces the document processing by huge margin for big db**


aggregation can calculate values while returning results.

## project example:

```mongodb
{
$project:{
name:1,
price:1,
priceWithGST:{
$multiply:["$price",1.18]
}
}
}
```


## sort example:

highest price first:
```mongodb
{$sort:{
price:-1
}
}
```

acending :
```mongodb
{
$sql:{
price:1
}
}
```

## limit 

```mongodb
db.prducts.aggregate([
{$sort:{sales:-1}},
{$limit:10}
]
)
```


## skips 

used for pagination

suppose each page 10 records page 1 skip

```mongodb
{$skips:0}
```



## combine everything

```mongodb
db.products.aggregate([
{
$match:{
category:"electronics",
rating:{$gt:4}
}  
},
{
$project:{
name:1,
price:1
}
},
{
$sort:{  
price:-1
}
},
{
$limit:5
}
])
```


differenct between find and aggregate is find process simple queries and used for faster lookup where as aggregate used for complex data processing. 	

## group 

```mongodb
db.orders.aggregate({
$group:{  
_id:"$category",
totalRevune: {
$sum:"$Price"  
}
}
})
```



## sql vs mongo

| sql | mongodb |
|-----|---------|
|group by | $group |
| sum | $sum |
| avg | $avg 
| min | $Min  |
| max | $max |
| count | $sum:1 or $count:stage|

### uderstading _id in group 

group document having the same category.called as id

input : 
electronics
electronics
electronics
fashion
electronics
fashion

output :
electronics
fashion


everything inside group then be calculated

## sum example

```mongodb
db.orders.aggregate([
{
$group:{
_id:nul,
totalsales:{
$sum:"$price"  
}
}
  
}
])
```




why id null because we want overall result not sperate groups.

## counting docs

```mongodb
db.employees.aggregate([
{
$group:{
_id:"$department",
count:{
$sum:1
}
}
}
])
```

*first gives the first document it receives. also last give last document it recevies*

## push

need all purchased products for each customer.

real example : amazon order history grouped by each customer

## addtoset

looks similar to push 
difference: this dont entertain duplicates where push does.

* put match before group whenever possible, to reduce number of documents being groupped.
* sort before using first and last
* grouping largwe collection can cause problem so filter first always.


## lookup

used for referencing , similar to join in sql.

```mongo
db.orders.aggregate([
{
$lookup:{
from:"products",
localField:"productId",
foreignField:"_id",
as:"productDetails"
}
}
])
```

from : which collection should mongodb search

localfield : which field exists in the current collection

foreignfield : which field should it compare again

as : where should mongodb place the matching documents

now product details will return an array even if only one document matches,that is the one of the biggest difference between matches.


## multiple lookup example


```mongo
db.orders.aggregate([
{
$lookup:{
from:"customer",
localField:"customerId",
foreignField:"_id",
as:"customer"
}
},

{
$lookup:{
from:"products",
localField:"productId",
foreignField:"_id",
as:"product"
}},

{$lookup:{
from:"payments",
localfield:"paymentId",
foreignField:"_id",
as:"payment"
}}
])
```


always index the forign field
 
dont join huge collection unnecessary

|sql | mongodb |
|----|---------|
|join|lookup|
|innner join| lookup+unwind+filtering
|left join| basic lookup 


## unwind 

flattening arrays

like there is product array in document in which laptop , mouse and keyboard are availble as entry.

if manager asks how many laptops were sold. then we cannot direclty gruop by items.product? not properly because items is an array

mongodb first need each array element to become its own document

that's exactly what unwind does

example 
items
[
laptops,
mouses,
keyboards
]

unwind

rahul laptop
rahul mouse
rahul keyboard

# preventNullAndEmptyArrays
when array is empty then unwind dont return it, so use this as true to get empty array as well

# includeArrayIndex
sometimes you also want the position of each element

very useful when order of elements matters, such as playlist tracker or survey qustions.

best apply match before the unwind whenever possible to reduce the number of docs

## size 
count array element 

how many products are in each order?

## arrayElmeAt

need the specific element at position

## slice

as example needs only two items like $slice:["items",2] needs last two $slice"["items",-2]

$slice:["$items",1,2] start from index 1 and return 2 element

## filter

filter any array or doc

## map

transform every array element

{
$projects:{
products:{  
$map:{
input:"$items",
as:"item",
in:{
$toUpper:"$$item.product"
}
}
}}}

## reduces

{
$project:{
total:{
$reduce:{
input:"$items",
intialValue:0,
in:{
$add:[
"$$value",
"$$this.price"
]
}
}  
}
}
}

## in

if specific values match in array

## concateArrays

merge arrays like combined into one array

## first and last

this works direclty on array also

## elematch

more tha one condtion can apply to find matching elements using this. like price > 5000 and quantity >= 2

## cond 

this is like mongpdb if else

## switch
 same as sql switch
 
## ifnull

if field is null should display something.
like colesec

## let

creating local variables

## mergeObject

useful after lookup

## replaceRoot

```mongo
{
"_id":1,

customer:{
"name":"rahul",
"age":24
}
}

```

need only rahul and age

then 

```mongo
{
$replaceRoot:{
newRoot:"customer"
}
}
```

old root disappers.
customer becomes the new root.

after lookup this is important

# type conversations

### toInt
### toDouble
### toString
### toDate
### toBool




# aggregate with arithmatic operators

## add
add numbers

## substract

substracting vals

## multiply

## divide

## mod - return the reminder

## abs- to get absolute value

## ceil - always round upwards

## floor - round downwards

## round - round normally

## trunc - removes decimal completely

## dateAdd - Increments a Date() object by a specified number of time units.

The $dateAdd expression has the following syntax:

```mongo
{
   $dateAdd: {
      startDate: <Expression>,
      unit: <Expression>,
      amount: <Expression>,
      timezone: <tzExpression>
   }
}
```

## dateDiff - give the different in any time unit between dates.

# variables:

system generated varible -

 which mongodb provide us
example NOW, CLUSTER_TIME

user defined variables - 

we make this to store value in pipeline to make it efficient.


# Data modeling

mongo doesn't enforce foreign keys in relational database

two type of relationships are:

1. embedded documents(denormalization)-all the data in one collection
2.referenced documents(normalization)-normalized table


# validation

mongo usejson schema format for validation.

allow you to apply constraints

```mnogo
db.createCollection("users1",{
validator: {
$jsonSchema:{
bsonType: "object",
required: ["name","phone"]
}
}
})
```

validation level:

strict: not be inserted or updated

moderate: new doc and modified fields in existing docs are validated 

validation actions:

error: reject the insertation and updation

warn: just warning allows the insert or update operation.

to modify the exisiting collection :- db.runCommand(collMod)

### ttl indexing 

delete the doc automatically after a specified period.
