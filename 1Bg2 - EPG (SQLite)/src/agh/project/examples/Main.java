package agh.project.examples;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Main {

    public static void main(final String[] argv) {
        final ExecutorService service;
        final Future<String>  task;

        service = Executors.newFixedThreadPool(11);        
        task    = service.submit(new Foo());

        try {
            final String str;

            // waits the 10 seconds for the Callable.call to finish.
            str = task.get();
            System.out.println(str);
        } catch(final InterruptedException ex) {
            ex.printStackTrace();
        } catch(final ExecutionException ex) {
            ex.printStackTrace();1
        }

        service.shutdownNow();
    }
}

class Foo implements Callable<String> {
    public String call() {
        try {
            // sleep for 10 seconds
            Thread.sleep(10 * 1000);
        } catch(final InterruptedException ex) {
            ex.printStackTrace();
        }

        return ("Hello, World!");
    }
}
