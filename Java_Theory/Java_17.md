# multithreading

## concurrency 

concurrency means dealing with multiple tasks during overlapping periods of time

- concurrency does not necessarily mean tasks execute at excalty the same time.

### concurrency vs prallaism

concurrency - multiple tasks are in progress during same preiod.

cpu may switch between tasks

parallaism - multiple tasks literally execute at the same time 
generally using multiple cpu core


multicore machiene can provide both


### process

process is running instance of a program

like if you create myJavaProgram - os create a process for that running java application

Process : -> genrally have their
	
	- memory
	- resources
	- files
	- threads
	

**process contain one or more threads**

### thread

thread is an independent path of execution inside a process

all these threads belong to same process

share many resources especially process's memory

- process  isolation is strong


### why do we need threads

if we have multiple operation and if we do them in one thread then it will have waiting time. 

with multiple threads other can make progress while other work.

useful for the i/o bound operation

### single  vs multi

single - one thread
multi - many thread one is main - java is multithreaded by nature

your java program is acutally an thread if you have not intlialized it but it is



- when you run the program java main operating system start java process

in this process jvm manages threads

threads use shared memory - it generates race conditions

process generally have different memory spaces but threads have shared memory inside process

one cpu core - you cannot literally execute two cpu instructions simulaneously on the same core

multiple cpu core - true prallel execution is possible 

# why we need multithreading

doing everything sequenctially can waste time

multithreading allows diffrent tasks to make progress independently or simultaneously

 
 responsiveness - dont let one task freeze everything
 
 resource-utilization
 

cpu bound - the task spends most of its time using the cpu multithreading can help through parallelism

i/o bound - task spends significant time waiting for external resources multithreading can improve resource utilization and responsiveness 


## java thread lifecycle

6 state
- new
- runnable
- blocked
- waiting
- time_waiting
- terminated

blocked - trying to acquire a monitor lock that another thread currently ownes

waiting - it waits indefinitly for another thread to perform some action

timed_waiting - specified amount of time waits for thread
- sleep does not make thread waiting it make it timed_waiting

terminated - finishes it's executiont

once thread is terminated it cannot be restarted

- java combines ready-to-run and running under the RUNNABLE.

during sleep thread is on timed_waiting but does nor release the monitor lock
in wait it releases the monitor lock

### creating thread

how java creates and starts concurrent threads

thread represents the execution mehcanism
runnable/callble represent the work to be executed

1. extending Threads
 ```java
 class MyThread extends Thread {
    @Override 
    public void run(){
        System.out.println("Task is runnig")
    }
 ```
	  
	  
what does run represent - work that the thread should perform.but you should not call run normally

but start() starts new thread run does not

start() tells jvm that start the thread execution

problem with extedning thread - java only allow single extension so we use runnable

runnable - my class has a task can be run - return void - cannot declare checked exception from run() - simple tasks

callble - return a result
can throw checked exceptions
task prodcing results

how do we execute the ExecuterService and Future 

```java
ExecutorService executor = Executors.newSingleThreadExector();

Future<Integer> future = executor.submit(()-> 10+20);

Integer result = future.get();

executor.shutdown();
```


instead of manually creating thread java provide an abstraction for managing threads - executorService

executorService --> threadpool --> t1,t2,t3

you submit the task to executor , it decides which worker thread excture them. 

### callable + future

future = a handle representing the result of an asynchronous computation

submit --> task starts / gets scheduled --> future<Integer>

when future.get -> if task already completed -:> gives val
if not wait -> task finishes -> gives val

future gives you way to retrived result later

runnable and callable are fucntional interface so it can be represented as lambda expression

modren java application we dont create a new raw thread for every task

we commanly use executor.submit(task)
executor.execute(task)

pool allow you to reuse thread

thread pool -----> 

worker 1-> thread 1

worker 2-> thread 2

worker 1 -> thread 3

# thread control

### start() vs run()

start() tells java Thread object should begin exectution as a seprate object

Main thread ---> Start() ---> jvm/os schedualing ---> new thread --> run()

run() is simply normal method call

### sleep()

pasue for specified time

does not release locks

eventually becomes eligible to run again

### join()

when one thread needs to wait for another thread to finish.

ex task a - donwload data
task b - process data

download thread -> finish -> main/worker thread -> process data

join with timeout - you can make join(2000) so it will put it into timed_waiting

### interrupts

a cooperative request asking a thread to stop what it is doing or respond to interruption.

interrupt with sleep

the worker was sleeping for 10 sec after apporximatly 1 sec

worker.interrup() -> sleep() is interrputed -> interrupException -> catch block

wheb sleep() responds to interruption by throwing exception interruption status is generally cleared

interrupt() -> thread.interrupt();
request interruption of another thread

isInterrupted() -> checkes the target thread's interrupt status
it does not clear the status

Thread.interrupted()
->
check the current thread's interrupt status and clears it


**daemon thread - background on behalf of the application**

include tasks such as monitoring , housekeeping, background maintainance

jvm does not keep running just beacose daemon threads are alive

if main threads are finished jvm can terminate main thread and worker threads are finished

### java thread priority

1 -> 10;

Thread.MIN_PRIORITY //1
Thread.NORM_PRIORITY //5
Thread.MAX_PRIORITY //10

default - norm

thread priority - scheduling hint , not strict execution order gurantee

the actual scheduling depend on 
- jvm implementation
- operation s
- cpu
- schedular
- system load

if synchronization / waiting mechanisim - correctness depends upon ordering : dont use priority
use appropriate coordination mechanisms such as join() locks countDownlatch future etc.

### synchornization

multiple threads accessing shared mutable data can produce incorrect results

if two thread try to execute at same time result can effectivly be lost

it is race condition

citicle section - piece of code that accesses shared state and must not be executred concurrently by conflicing threads

synchronized = only one thread at a time can executre a synchornizd region protected by the same monitor lock

you can synchornized an intance method
, you can lock behaviour too ---> lock this object


intrinstic lock / monitor -

java objects associated with a monitor which is used by synchronized 

like when thread reached to criticle section it tries to acqire the monitor if another thread already acqired the monitor than it have to wait

this lock is associated with objects two threads can execute two different lock at same time

synchronize the needed part by synchrnoized block 


we can create dedicated object for the synchornization 
```java
private final Object lock = new Object();

synchornized(lock){
}
```
 
 
 this is preferable because external code can't easily synchronize on your internal lock object
 
 
 instance synchronization -- uses this , every object create different locks
 
 if we synchronize static methods
 
 it doesnt give have this object
 
 it locks the class object's monitor
 
 synchroniation provide two guarantees:  
 1. mutual exculsion - only one thread can hold the same monitor at a time
 2. memory visiblity 
 

## inter thread communication

why -

prudcer creates data  - consumer processes it

there is buffer between the consumer consumption and producer providing , cause there is no communication

so consumer have to have power to tell producer that there nothing to consume

wait() -> 

current thread wait untill another thread singles it

thread must own object's monitor before calling wait() on that object

```java
synchronized(lock){  
lock.wait();
}
```

when wait is called:

1. releases the monitor lock
2. enter the waiting state
3. wait to be notified / inturrpted
4. evntually tries to reacqire the same lock
5. only after reacquring it can continue

difference with sleep -> sleep , time_waiting, does not release monitor

wait() -> waiting realses monitor

notfiy() -

wakes one thread that is waiting on same object's monitor.

notify does not immediatly give the lock the awakened thread. the notifying thread still owns the monitor untill it leaves the synchronized region

nofityall() -wakes all the thread , they compete to acquire lock , only one can acquire

always use while instead of if in locks cause it rechecks wheter the condition is actually true


spurious wakeups-

a thread waiting with can sometimes return from wait() without receiving the notification you expect

so we use while if thread wake up condition fails wait again

### java.utills.concurrent.locks

synchronization gives you simple automatic locking . Lock gives you more control over lcoking behaviour

**ReentrantLock**

lock()-> try-> criticle section -> finally -> unlock()

ReentrantLock means: same thread can acquire the same lock multiple times without deadlocking itself

internally lock maintains count conceptually - only when count reaches to 0 it releases the lock

why to use it-

try to acquire lock but dont wait forever , synchronized dont have equvilat of the try catch 

**trylock()**

generally method have to wait untill another method release the lock

but in trylock - can i get the lock right now?  
  
  so threads dont have to wait indefinatly
  
**timed trylock()**

specify time

**lockinterruptibly()**  

wait for the lock but allow waiting thread to be interrupted

- fair locking try to give to threads whcih are watiing for long time

fairness can reduce throuuput

**readwritelock** 
readers can safely read the thread untill somone tries to write it

**reenterreadwritelock**
gives both read block and write lock

conditions - await() single() singleall()

## atomic variable

thread safe operations on single variable without using traditional locks like synchronize and reentratlock

instead of locking a variable before changing it an atomic variable performs the update using CAS(compare and swap)

AtomicInteger

AtomicLong

AtomicReference - atomically update an object reference

imp --- compareAndSet 

cas is useful cause there is no traditional monitor lock acquisition

atomic operation generally avoid blocking locks but a thread can still repeatedly fail cas

atomic reference -> makes reference update atomic but object still needs it own thread safety strategy

also atomic boolean

### executer framework

provide thread pool kind of structure

thread is a worker

excuterService as a manager of worker

newFixedThreadPool - creates a pool with fixed number of worker threads

newCachedThreadPool-create new threads when necessary and reuse previously creatd idle threads

this is not bounded mechanism , if task arrives real quickly many threads may be created

newSingleThreadExecutor- only one worker execute the task, this not execute concurrently with each other

useful when - multiple task but only one should modify a particular piece of state at a time

newScheduledThreadPool- used for delayed or periodic execution

execute() and submit() both can submit task but execute dont return anything means just run the task i dont want anything in return

submit() returns a future 

submit -> fire task -> future -> retrieve result/ status/ execption

future represents the result of an async computation

future.get -- wait for result

future.get(timeout, unit) -- wait for only specified amount of time if task doesn't finish TimeoutException

isdone - check task is done or not

cancle - attempt to cancle task

**execption with future**

task -> runtimeexecption -> future -> future.get() -> executionException -> getCause() -> RuntimeException

* **completableFuture**

future has important limitation

```java
CompletableFuture
.supplyAsync(()-> 10)
.thenApply(x -> x*2)
.thenAccept(System.out::println);
```


this is asynchronus composition

completebleFuture lets you build the pipeline around the result


supplyAsync() - used when the task returns a value

runAsync() - used when there is not result

thenApply() - transfrom the result like map

thenAccept() - consume the resultv

thenCompose() - used when one async operation depends on another async operation\

like FutureA -> operation return FutureB 

instead of getting Future(FutureB) thenCompose() flatten it to FutureB


thenCombine() - combine two independent async result


shutdown() - stop accepotng new tasks but allow already submitted tasks to finish

shutdown() -> no new tasks -> extisting tasks continue -> all finish -> executor terminates

shutdownNow() -> more aggresive -> prevent new task -> interrupt running task -> return the task that were waiting in the queue

it dont gureente that running tasks immediately stop

awaitTermination() -> sometimes you want to wait for the executor to actually terminate

executorserivce -- interface representing the executor

ExecutorService executor;

where Executors are the utility / factory classs:

Executors.newFixedThreadPool(5);

# concurrent collection in java
 
concurrenr collections 

	- ConcurrentHashMap
	
	- CopyOnwriteArrayList
	
	- BlockingQueue
		- ArrayBlockingQueue
		- LinkedBlockingQUeue


Dont choose a concurrent collection just beacuse "multiple threads are involed" choose one based on the access pattern

if you say we use synchronized(list) then we will have responsibility of synchronizing every relevent access

concurrent collection build the necessary concurrency mechanishms into the collection itself


**ConcurrentHashMap**

ConcurrentHashMap< String , Integer > map = new ConcurrentHashMap<>();

desgined for the high concurrent access

this dont allow null

- you can iterate while other threads modify it

	```java
	for(String key : map.KeySet()){  
	System.out.println(Key);  
	}
	```
 
 this does not behave like fail fast iterator. 
 
 the iterator can reflect some concurrent modifications, but it does not represent a forzen snapshot
 
 **CopyOnWriteList**
 
 when list is modified new internal array is created
 
 when you modify old array continue to be safely used by readers while the new array becomes the current array
 
 if list changes very rarely but it read constantly , copyOnWriteArrayList bee excellent
 
 it optimize reads not writ
 
 ### blockingqueue
 
 BlockingQueue< E >
 
 thread safe queue where operations can block untill they can proceed
 

 
 put vs offer - in put if queue is full wait , untill space becomes availble 
 
 in offer it will return false instead of waiting indefinatly
 
 take vs poll - 
 
 same idea on the consumer side
 
 take - if list empty - wait
 
 poll- if empty - return null
 
 ArrayBlockingQueue - fixed capacity 
 
 why this matters - server receiving requests faster than workers can process them
 eventually out of memory error
 
 LinkedBlockQueue - optionally bounded
 
 very large practical maxium capacity rather than a small appication defined bound
 
 blockingqueue solves bigger problem - this packages the pattern of the synchronization into reuseable abstraction
 
 like instead synchronized() -> wait() -> notify() you can simply use put and take
 
 # synchronizers - coordination utilities
 
 it is different from synchronized
 
 it means utility that helps multiple threads coordinate with each other
 
 
 like traffic control system
 
 **countDownLatch**
  == 
  
 allowes one or more threads to wait untill counter reaches zero
 
 CountDownLatch latch = new CountDownLatch(3);
 
 3 means three countdown events must happen before waiting threads can continue.
 
```java

CountDownLatch latch = new CountDownLatch(3);

Thread t1 = new Thread(() -> {  
	loadDatabase();
	latch.countDown();
});

Thread t2 = new Thread(() -> {  
	loadCache();
	latch.countDown();
});

Thread t3 = new Thread(() -> {  
	loadConfiguration();
	latch.countDown();
});

t1.start();
t2.start();
t3.start();

latch.await();

System.out.println("Application ready");

```
 
 
 ### await():
 
 latch.await() -> menas block this thread untill the latch reaches 0
 
 
### countdown():

decrease the count by one

Countdown() does not wait only await() waits

once 3 -> 2 -> 1 -> 0 

you cannot reset back it to 0

this is one time synchronized event

if you want same coordination point repeatedly look at CyclicBarrier


### cyclebarrier

we have 4 threads performaing four parts of the same computations

nobody should start phase 2 untill everyone has completed phase 1 

that's cuclebarrier 

CycleBarrier barrier = new CyclicBarrier(4);

each thread does: barrier.await();

after everyone crosses the barrier it can be used again


### semaphore

if application have 100 threads

but only 5 db connections

so we cannot allow all 100 threads to use the database simultaneosuly

we need maxium of 5 concurrent users

that's semaphore

Semaphore semaphore = new Semaphore(5);

5 presents 5 permits


### tryAcquire():

sometimes we dont want to wait - try to acquire a permit immediatly if none is availble continue without blocking 


## Exchanger

Exchanger< T > allows two threads to exchange objects

two threads can exchange buffer instead of copying individual elements

it can wait for partner also for it will to arrive

it can have timeout also

### completableFuture Async Programming

build a pipeline of asynchronus operations and react to their results without manually blocking between every step.

suppos you need - get user -> get orders -> calculate total -> send email

each operation waits for previos one 

with completeableFuture you can exprss the dependecny

async operation -> future result -> transform result -> another async operation -> transform again

it is both future and completion stage

runAsync() -> when the asynchronus task doesn't return a result
similiar to runnabel

supplyAsync() -> return a result
similar to callable

without specifying an executor , java uses the default executor which for the standard async methods is generally the forkjonipool.commandPool()  

```java
ExecutorService executor = Executors.newFixedThreadPool(10);

CompletableFuture< User > future = CompletableFuture.supplyAsync(() -> loadUser.executor();
```

**thenApply()**

CompletableFuture< Integer > future = Completable.supplyAsync(() -> 10);

future.thenApply(x -> x*2);

thenApply() does not necessarily mean new thread

that's not guarantee 

if we want async execution through executor -> thenApplyAsync()

thenApply() -> continuation

thenApplyAsync() -> continuation schedualed asynchronusly

**thenCompose()**

get user -> get orders for that user

CompletableFuture< User > userfuture = getUserAsync();

CompletableFuture< List< orders >  > = getOrdersAsync(User user);

Future< User > -> getOrdersAsync() -> Future< List< order > >

this is excatly what thenCompose() designed for

CompletableFuture< List < order > > ordersFuture = userFuture.thenCompose(user -> getOrdersAsync(user) 
);

**thenApply() transfrom a value thenCompose chains an asynchronus operation that itself returns a future**


thencombine() -> wait for both future and combine their results.

```java
CompletableFuture< usersummery > summery = userFuture.thenCombine(
accountFuture, (user, account) -> new userSummery(user, account)
);
```

thenAcceptBoth() -

thenCombine -> combine -> new result

thenAcceptBoth() -> no result

**allof** -> combining many future

**anyof** -> give me whichever finishes first

### exception handling

**exceptionally**

any task -> exception -> exceptionally() -> fallback = 0

handle() runs whether the previous stage succeeds or fails

whenComplete



### deadlock

A deadlock happens when two or more threads are blocked forever, each waiting on a resource held by another thread in the group — nobody can proceed.

Best Practices Checklist
Practice	Why
Always release locks in finally	Prevents leaked locks on exceptions
Fixed global lock ordering	Eliminates circular wait
Prefer tryLock(timeout) over synchronized for multi-lock scenarios	Avoids indefinite blocking
Keep critical sections small	Reduces contention window
Avoid calling unknown/overridable methods while holding a lock	Prevents accidental nested locking
Prefer immutability / concurrent collections	Removes need for locks entirely
Monitor with ThreadMXBean or jstack in production	Early detection before it becomes a full outage
One lock per resource, never lock the same resource with two different lock objects	Avoids false sense of protection


### Livelock in Java Multithreading 

Livelock is like deadlock's hyperactive cousin: threads aren't blocked — they're actively running, but they keep responding to each other and never make progress. Think of two people in a hallway both stepping aside to let the other pass, over and over.

### starvation

thread is perpetually denied the resource it needs

Best Practices Checklist
Practice	Prevents
Add randomized jitter/backoff to retry loops	Livelock
Break symmetry with tiebreakers (ID, timestamp) in contested retry logic	Livelock
Use fair ReentrantLock/Semaphore only where starvation risk is real	Starvation
Avoid Thread.setPriority() for correctness-critical logic	Starvation
Keep critical sections minimal	Both livelock recovery and starvation
Cap retry attempts, escalate/fail instead of infinite loop	Livelock
Prefer java.util.concurrent executors over manual thread priority tuning	Starvation
Monitor CPU-active-but-no-progress threads (thread dumps over time, not single snapshot)


### thread safety and immutability


Thread safety = a class/object behaves correctly when accessed by multiple threads concurrently, regardless of interleaving.
Immutability = an object's state can never change after construction. Immutable objects are automatically thread-safe — with no locks needed — because there's no mutable state to corrupt.


best practise use immutable objects and atomic references , that is vaibale options

### Visibility Issues in Java Multithreading

Visibility is a different problem from atomicity. Even if an operation is "safe" in the sense of no data corruption, one thread's writes may never become visible to another thread — because of CPU caching, compiler/JIT reordering, and the Java Memory Model (JMM) not guaranteeing cross-thread visibility without explicit synchronization.


nside lock (keep SHORT)	Outside lock (move OUT)
Read/write shared mutable field	Validation logic
Simple state check-and-update	Computation/business logic (no shared state)
Quick copy/snapshot of shared collection	I/O — file, DB, network, logging
CAS-style compare-then-set	Sleep/wait for external event
	Heavy math/loop over large data
	Calling unknown/overridable method (callback) — cave-danger, can even cause deadlock!