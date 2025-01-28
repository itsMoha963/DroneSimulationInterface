package utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutoRefresh {
    private static final Logger log = Logger.getLogger(AutoRefresh.class.getName());
    private ScheduledExecutorService scheduler;

    /**
     * Runs the given {@code task} after the initial delay and then each period
     * @param task
     * @param initialDelay
     * @param period
     * @param unit
     */
    public synchronized void start(Runnable task, long initialDelay, long period, TimeUnit unit) {
        stop();

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(task, initialDelay, period, unit);
        log.log(Level.INFO, "Scheduler started");
    }

    /**
     * Stops the current task if there is any
     */
    public synchronized void stop() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
            log.log(Level.INFO, "Scheduler stopped");
        }
    }
}