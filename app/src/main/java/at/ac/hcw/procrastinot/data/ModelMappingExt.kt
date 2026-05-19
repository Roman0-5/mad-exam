package at.ac.hcw.procrastinot.data

import at.ac.hcw.procrastinot.data.source.local.LocalTask
import at.ac.hcw.procrastinot.data.source.network.NetworkTask
import at.ac.hcw.procrastinot.data.source.network.TaskStatus

// External to local
fun Task.toLocal() = LocalTask(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted,
    priority = priority,
)

fun List<Task>.toLocal() = map(Task::toLocal)

// Local to External
fun LocalTask.toExternal() = Task(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted,
    priority = priority,
)

@JvmName("localToExternal")
fun List<LocalTask>.toExternal() = map(LocalTask::toExternal)

// Network to Local
fun NetworkTask.toLocal() = LocalTask(
    id = id,
    title = title,
    description = shortDescription,
    isCompleted = (status == TaskStatus.COMPLETE),
    priority = TaskPriority.MEDIUM,
)

@JvmName("networkToLocal")
fun List<NetworkTask>.toLocal() = map(NetworkTask::toLocal)

// Local to Network
fun LocalTask.toNetwork() = NetworkTask(
    id = id,
    title = title,
    shortDescription = description,
    status = if (isCompleted) { TaskStatus.COMPLETE } else { TaskStatus.ACTIVE }
)

fun List<LocalTask>.toNetwork() = map(LocalTask::toNetwork)

// External to Network
fun Task.toNetwork() = toLocal().toNetwork()

@JvmName("externalToNetwork")
fun List<Task>.toNetwork() = map(Task::toNetwork)

// Network to External
fun NetworkTask.toExternal() = toLocal().toExternal()

@JvmName("networkToExternal")
fun List<NetworkTask>.toExternal() = map(NetworkTask::toExternal)