### 1. What will be printed after interrupting the thread?
```java
public class Main {
    public static class SleepThread extends Thread {
        public void run() {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println("Thread was interrupted!");
            } finally {
                System.out.println("Thread will be finished here!!!");
            }
        }
    }
    public static void main(String[] args) {
        SleepThread thread = new SleepThread();
        thread.start();
        thread.interrupt();
    }
}
```

### Output:
```
Thread was interrupted!
Thread will be finished here!!!
```
### Reason:
Because it was sleeping when we interrupt the thread.

### 2. In Java, what would be the outcome if the ```run()``` method of a ```Runnable``` object is invoked directly, without initiating it inside a ```Thread``` object?

```java
public class DirectRunnable implements Runnable {
    public void run() {
        System.out.println("Running in: " + Thread.currentThread().getName());
    }
}

public class Main {
    public static void main(String[] args) {
        DirectRunnable runnable = new DirectRunnable();
        runnable.run();
    }
}
```

### Output:
```
Running in: main
```
### Answer:
The ```run``` method is executed normally like any other method in Java, and according to the output and the fact that we did not create any other thread, we can see that the command were executed on the same ```main``` thread.
( if we add a new line in ```main()``` method to see its thread name, we see that it's the same ```main``` )


### 3. Elaborate on the sequence of events that occur when the ```join()``` method of a thread (let's call it Thread_0) is invoked within the ```main()``` method of a Java program.

```java
public class JoinThread extends Thread {
    public void run() {
        System.out.println("Running in: " + Thread.currentThread().getName());
    }
}

public class Main {
    public static void main(String[] args) {
        JoinThread thread = new JoinThread();
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Back to: " + Thread.currentThread().getName());
    }
}
```

### Output:
```
Running in: Thread-0
Back to: main
```

### Answer:
* **Invoking ```join()```:** the ```main``` thread waits for ```Thread_0``` to complete its execution. The calling thread (in this case, the ```main``` thread) enters a waiting state. It remains in this waiting state until the referenced thread (Thread_0) terminates.

* **Thread Execution and Termination:** Inside the ```run()``` method of ```JoinThread```, the thread performs some processing (in this case, it simply prints the current thread that is ```Thread-0```). At the end of ```run()``` method, ```Thread-0``` terminates and the ```main``` thread (which was waiting) resumes execution.

* **Back to Main Thread:** After ```thread.join()``` returns (or ```Thread_0``` terminates), the ```main``` thread continues executing. It prints a message indicating that it’s back to the ```main``` thread.

Note that the ```join()``` method may throw an ```InterruptedException``` if the waiting thread (in this example ```main```) is interrupted.