package art.timestop.scheduler.controller;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Component
@RequestMapping("/task")
public class TaskController {
    
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    private ScheduledFuture<?> scheduledFuture;

    long fixedRateMillis = 5000; // Every 5 seconds  
    
    private final Runnable taskRunnable = () -> {
        System.out.println("Task Running: " + LocalDateTime.now());
    };

    @GetMapping("/start")
    public String startTask() {
        if (scheduledFuture != null && !scheduledFuture.isDone()) {
            return "task is already running.";
        }
        
        scheduledFuture = taskScheduler.scheduleAtFixedRate(taskRunnable, Instant.now(), Duration.ofMillis(fixedRateMillis));
        return "Task Started...";
    }
    
    @GetMapping("/stop")
    public String stopTask() {
        if (scheduledFuture == null || scheduledFuture.isDone()) {
            return "Task is not running or already stopped.";
        }
        boolean cancelled = scheduledFuture.cancel(true);
        if (cancelled) {
            return "task stopped.";
        } else {
            return "Failed to stop task or already completed).";
        }
    }
}
