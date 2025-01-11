package utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutoRefresh {
    private static final Logger log = Logger.getLogger(AutoRefresh.class.getName());
    private final ScheduledExecutorService scheduler;
    private boolean isRunning;

    public AutoRefresh() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        isRunning = false;
    }

    /**
     * Start the scheduler with the given task, initial delay, period and time unit
    @param task;
    @param initialDelay;
    @param period;
    @param unit;
    **/
    public synchronized void start(Runnable task, long initialDelay, long period, TimeUnit unit) {
        if (isRunning) {
            log.log(Level.WARNING, "Scheduler is already running");
            return;
        }
        scheduler.scheduleAtFixedRate(() -> {
            try {
                task.run();
            }catch (Exception e){
                log.log(Level.SEVERE, "Error during scheduled task execution", e);
            }
        }, initialDelay, period, unit);
        isRunning = true;
        log.log(Level.INFO, "Scheduler started");
    }

    public synchronized void stop() {
        if (isRunning && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
            isRunning = false;
            log.log(Level.INFO, "Scheduler stopped");
        } else {
            log.log(Level.WARNING, "Scheduler is not running");
        }
    }

    /**
     *
     * @return true if the scheduler is running, false otherwise
     */
    public boolean isRunning() {
        return isRunning;
    }
}
