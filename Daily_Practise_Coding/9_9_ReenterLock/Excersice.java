/*
* synchronized is simple but rigid -- it's all or nothing you can'y
* try for a lock and give up if you don't get it, you can't set a timeout ,
* and the lock / unlock happens automatically tied to the block;s braces.
*
* ReentrantLock (from java.util.concurrent.locks) is a more flexible,
*  manual version of the same idea —
* same core concept (only one thread at a time), but with extra control.
*
* Think of synchronized as an automatic door that locks/unlocks itself as you walk through a doorway
*
*ReentrantLock is a manual door with a handle — you explicitly lock it and explicitly unlock it,
* which is more work but lets you do things an automatic door can't
*
*like "try the handle, and if it's stuck, just walk away instead of standing there forever")
*
* basic syntax
*
* ReenterantLock lock = new ReentranatLock();
*
* lock.lock();
* try{
*  //criticle section - same as inside synchronized
* }finally{
* lock.unlock(); // must be manual and must be in finally
* }
*
* rule: unlike synchronized which auto relases even if an exception is thrown reentranlock required you to call
* .unlock() yourself. if you forget it or dont put it in a finally block
* and an exception is thrown inside the criticle section the lock is never released
* and every other thread waiting for it hangs forever thuis is single biggest risk of using reentrantlock over synchornized it trades safety by default for flexibility
*
*
* the big advantage: tryLock()
*
* if(lock.tryLock()){
* try{
* // got the lock do work
* }
* finally{
* lock.unlock();
* }
* }else{
* System.out.println("couldnt get the lock , moving on)
*
* }
*
* directly solved the deadlock problem
*
* hold and wait condition requires for deadlock tryLock() lets a thread give up instead of waiting forever breaking that conditoon entirly a thread can backoff relelase what it is holding and retry later no permanent freeze
*
*
* */

class Excersice{
    int id;
    double balance;
    ReentrantLock lock = new ReentrantLock();

    Account(int id, double balance){
        this.id = id;
        this.balance = balance;
    }

    public class BankTransferTryLock{
        public static void transfer(Account from , Account to, doiuble amount){
            while(true){
                boolean gotFrom = false;
                boolean gotTo  = false;
                try{
                    gotFrom = from.lock.tryLock(1,TimeUnit.SECONDS);
                    if(gotFrom){
                        gotTo = to.lock.tryLock(1,TimeUnit.SECONDS;
                        if(gotTo){
                            //got both lock doing the transfer
                            from.balance -= amount;
                            to.balance += amount;
                            System.out.println("transfered "+ amount + " from account" + from.id + " to account "+ to.id);
                            break;
                        }
                    }
                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                }finally{
                    if(gotTo){
                        to.lock.unLock();
                    }
                    if(gotFrom){
                        from.lock.unlock();
                    }
                }

            }
        }

        Systme.out.println("could not acquire locks. retrying ");
        try{
            Thread.sleep(10);
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}
