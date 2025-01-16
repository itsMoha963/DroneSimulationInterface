package utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The AutoRefresh class provides a utility for scheduling and managing periodic tasks.
 * It uses a {@link ScheduledExecutorService} to run tasks at fixed intervals.
 * This class includes methods to start, stop, and check the status of the scheduler.
 */
public class AutoRefresh {
    private static final Logger log = Logger.getLogger(AutoRefresh.class.getName());
    private final ScheduledExecutorService scheduler;
    private boolean isRunning;

    public AutoRefresh() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        isRunning = false;
    }

    /**
     * Starts the scheduler with the given task, initial delay, period, and time unit.
     * The task will run periodically according to the specified parameters.
     * If the scheduler is already running, this method logs a warning and does nothing.
     *
     * @param task The {@link Runnable} task to execute periodically.
     * @param initialDelay The initial delay before the task is first executed, in the specified time unit.
     * @param period The period between successive executions of the task, in the specified time unit.
     * @param unit The {@link TimeUnit} for the initial delay and period.
     */
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

    /**
     * Stops the scheduler from executing tasks.
     * If the scheduler is not running or is already shut down, this method logs the state and does nothing.
     */
    public synchronized void stop() {
        if (isRunning && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
            isRunning = false;
            log.log(Level.INFO, "Scheduler stopped");
        } else {
            log.log(Level.INFO, "Scheduler is not running. No Schedule to stop");
        }
    }

    /**
     * Checks whether the scheduler is currently running.
     *
     * @return {@code true} if the scheduler is running, {@code false} otherwise.
     */
    public boolean isRunning() {
        return isRunning;
    }
}
