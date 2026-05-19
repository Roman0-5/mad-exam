package at.ac.hcw.procrastinot.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import at.ac.hcw.procrastinot.R
import at.ac.hcw.procrastinot.util.LoadingContent
import at.ac.hcw.procrastinot.util.StatisticsTopAppBar

@Composable
fun StatisticsScreen(
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StatisticsViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { StatisticsTopAppBar(openDrawer) },
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        StatisticsContent(
            loading = uiState.isLoading,
            empty = uiState.isEmpty,
            activeTasksPercent = uiState.activeTasksPercent,
            completedTasksPercent = uiState.completedTasksPercent,
            onRefresh = { viewModel.refresh() },
            modifier = modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun StatisticsContent(
    loading: Boolean,
    empty: Boolean,
    activeTasksPercent: Float,
    completedTasksPercent: Float,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    LoadingContent(
        loading = loading,
        empty = empty,
        onRefresh = onRefresh,
        modifier = modifier,
        emptyContent = {
            Text(
                text = stringResource(id = R.string.statistics_no_tasks),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = dimensionResource(id = R.dimen.horizontal_margin))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = dimensionResource(id = R.dimen.horizontal_margin)),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (!loading) {
                StatCard(
                    label = stringResource(R.string.statistics_active_tasks_label),
                    value = "%.1f%%".format(activeTasksPercent),
                    containerColor = Color(0xFFFF9800),
                )
                StatCard(
                    label = stringResource(R.string.statistics_completed_tasks_label),
                    value = "%.1f%%".format(completedTasksPercent),
                    containerColor = Color(0xFF4CAF50),
                )
            }
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
            )
        }
    }
}

@Preview
@Composable
private fun StatisticsContentPreview() {
    Surface {
        StatisticsContent(
            loading = false,
            empty = false,
            activeTasksPercent = 88f,
            completedTasksPercent = 12f,
            onRefresh = { }
        )
    }
}

@Preview
@Composable
private fun StatisticsContentEmptyPreview() {
    Surface {
        StatisticsContent(
            loading = false,
            empty = true,
            activeTasksPercent = 0f,
            completedTasksPercent = 0f,
            onRefresh = { }
        )
    }
}
