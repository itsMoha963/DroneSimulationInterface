package utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;

/**
 * The {@code AutoRefresh} class provides a simple way to periodically execute a task
 * It allows starting and stopping a task dynamically, to make sure only one task runs at a time.
 */
public class AutoRefresh {
    private static final Logger log = Logger.getLogger(AutoRefresh.class.getName());
    private ScheduledExecutorService scheduler;

    /**
     * Runs the given {@code task} after the initial delay and then in intervals.
     * @param task          Task to be executed.
     * @param initialDelay  Initial Delay after which the Task is executed.
     * @param period        Period after which the Task is run again.
     * @param unit          Time unit for the delay and interval.
     */
    public synchronized void start(Runnable task, long initialDelay, long period, TimeUnit unit) {
        stop();

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(task, initialDelay, period, unit);
        log.log(Level.INFO, "Scheduler started");
    }

    /**
     * Stops the currently running task if there is any
     */
    public synchronized void stop() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
            log.log(Level.INFO, "Scheduler stopped");
        }
    }
}