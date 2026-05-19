package at.ac.hcw.procrastinot.data.source.local

import at.ac.hcw.procrastinot.data.TaskPriority

internal object SeedData {
    val initialTasks = listOf(
        LocalTask(
            id = "seed-1",
            title = "Prepare for presentation",
            description = "Review slides and practice delivery",
            isCompleted = false,
            priority = TaskPriority.HIGH,
        ),
        LocalTask(
            id = "seed-2",
            title = "Finish this assignment",
            description = "Complete all remaining tasks before the deadline",
            isCompleted = false,
            priority = TaskPriority.HIGH,
        ),
        LocalTask(
            id = "seed-3",
            title = "Call a friend",
            description = "Catch up and plan the weekend",
            isCompleted = false,
            priority = TaskPriority.MEDIUM,
        ),
        LocalTask(
            id = "seed-4",
            title = "Buy groceries",
            description = "Milk, eggs, bread, and fruit",
            isCompleted = false,
            priority = TaskPriority.LOW,
        ),
        LocalTask(
            id = "seed-5",
            title = "Relax and take a break",
            description = "Go for a walk or watch something",
            isCompleted = true,
            priority = TaskPriority.LOW,
        ),
    )
}
