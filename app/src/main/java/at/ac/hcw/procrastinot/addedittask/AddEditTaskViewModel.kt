package at.ac.hcw.procrastinot.addedittask

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.ac.hcw.procrastinot.R
import at.ac.hcw.procrastinot.TodoDestinationsArgs
import at.ac.hcw.procrastinot.data.TaskPriority
import at.ac.hcw.procrastinot.data.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class AddEditTaskUiState(
    val title: String = "",
    val description: String = "",
    val isTaskCompleted: Boolean = false,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val isLoading: Boolean = false,
    val userMessage: Int? = null,
    val isTaskSaved: Boolean = false
)

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val taskId: String? = savedStateHandle[TodoDestinationsArgs.TASK_ID_ARG]

    private val _uiState = MutableStateFlow(AddEditTaskUiState())
    val uiState: StateFlow<AddEditTaskUiState> = _uiState.asStateFlow()

    init {
        Timber.d("init: taskId=%s", taskId)
        if (taskId != null) {
            loadTask(taskId)
        }
    }

    fun saveTask() {
        if (uiState.value.title.isEmpty() || uiState.value.description.isEmpty()) {
            _uiState.update { it.copy(userMessage = R.string.empty_task_message) }
            return
        }
        if (taskId == null) {
            createNewTask()
        } else {
            updateTask()
        }
    }

    fun snackbarMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }

    fun updateTitle(newTitle: String) {
        _uiState.update { it.copy(title = newTitle) }
    }

    fun updateDescription(newDescription: String) {
        _uiState.update { it.copy(description = newDescription) }
    }

    fun updatePriority(priority: TaskPriority) {
        Timber.d("updatePriority: %s", priority)
        _uiState.update { it.copy(priority = priority) }
    }

    private fun createNewTask() = viewModelScope.launch {
        taskRepository.createTask(
            uiState.value.title,
            uiState.value.description,
            uiState.value.priority,
        )
        _uiState.update { it.copy(isTaskSaved = true) }
    }

    private fun updateTask() {
        if (taskId == null) throw RuntimeException("updateTask() was called but task is new.")
        viewModelScope.launch {
            taskRepository.updateTask(
                taskId,
                title = uiState.value.title,
                description = uiState.value.description,
                priority = uiState.value.priority,
            )
            _uiState.update { it.copy(isTaskSaved = true) }
        }
    }

    private fun loadTask(taskId: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            taskRepository.getTask(taskId).let { task ->
                if (task != null) {
                    _uiState.update {
                        it.copy(
                            title = task.title,
                            description = task.description,
                            isTaskCompleted = task.isCompleted,
                            priority = task.priority,
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Timber.d("onCleared")
    }
}