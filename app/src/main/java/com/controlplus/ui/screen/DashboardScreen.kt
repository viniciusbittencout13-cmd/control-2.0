package com.controlplus.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.controlplus.data.AppProgress
import com.controlplus.data.DashboardState
import com.controlplus.data.HeatmapDay
import com.controlplus.data.GoalProgress
import com.controlplus.ui.theme.AlertRed
import com.controlplus.ui.theme.ElectricBlue
import com.controlplus.ui.theme.NeonGreen
import com.controlplus.ui.theme.PurpleGoal
import com.controlplus.ui.theme.WarningYellow

@Composable
fun DashboardScreen(
    state: DashboardState,
    onQuickExpense: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Control+", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("Estou no controle hoje?", color = MaterialTheme.colorScheme.secondary)
        }
        item {
            ControlRing(percent = state.dailyControlLevel)
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FinanceCard(
                    remaining = state.todaysBudgetRemaining,
                    progress = state.todaysBudgetUsedPercent,
                    modifier = Modifier.weight(1f)
                )
                TimeCard(
                    apps = state.appProgress,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item { GoalsCard(state.goalsProgress) }
        item { HeatmapCard(state.monthHeatmap) }
        item { CalendarCard(state.todayEvents.map { it.title }) }
        item {
            OutlinedButton(onClick = onQuickExpense, modifier = Modifier.fillMaxWidth()) {
                Text("Registrar gasto manual (confirmação simulada)")
            }
        }
        if (state.alerts.isNotEmpty()) {
            items(state.alerts) { alert ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = AlertRed.copy(alpha = 0.2f)),
                    border = BorderStroke(1.dp, AlertRed)
                ) {
                    Text(alert, modifier = Modifier.padding(12.dp))
                }
            }
        }
    }
}

@Composable
private fun ControlRing(percent: Int) {
    val ringColor = when {
        percent >= 75 -> NeonGreen
        percent >= 50 -> WarningYellow
        else -> AlertRed
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, ringColor),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(220.dp).shadow(16.dp)) {
                Canvas(modifier = Modifier.size(220.dp)) {
                    drawArc(
                        color = Color.DarkGray,
                        startAngle = 150f,
                        sweepAngle = 240f,
                        useCenter = false,
                        style = Stroke(width = 28f, cap = StrokeCap.Round)
                    )
                    drawArc(
                        color = ringColor,
                        startAngle = 150f,
                        sweepAngle = 240f * (percent / 100f),
                        useCenter = false,
                        style = Stroke(width = 30f, cap = StrokeCap.Round)
                    )
                    drawCircle(color = ringColor.copy(alpha = 0.1f), radius = size.minDimension / 3f)
                    drawLine(
                        color = ringColor.copy(alpha = 0.5f),
                        start = Offset(size.width / 2f, size.height / 2f),
                        end = Offset(size.width / 2f, 24f),
                        strokeWidth = 6f
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Nível de Controle", color = MaterialTheme.colorScheme.onSurface)
                    Text("$percent%", fontSize = 42.sp, fontWeight = FontWeight.Black, color = ringColor)
                }
            }
        }
    }
}

@Composable
private fun FinanceCard(remaining: Double, progress: Float, modifier: Modifier = Modifier) {
    NeonCard("Financeiro", NeonGreen, modifier) {
        Text("Você pode gastar hoje:")
        Text("R$ ${"%.2f".format(remaining)}", fontWeight = FontWeight.Bold, fontSize = 22.sp)
        LinearProgressIndicator(progress = { progress.coerceIn(0f, 1f) }, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun TimeCard(apps: List<AppProgress>, modifier: Modifier = Modifier) {
    NeonCard("Tempo", ElectricBlue, modifier) {
        apps.take(3).forEach {
            Text("${it.appName}: ${it.usedMinutes}/${it.limitMinutes} min", fontSize = 12.sp)
            LinearProgressIndicator(
                progress = { (it.usedMinutes.toFloat() / it.limitMinutes).coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth(),
                color = ElectricBlue
            )
        }
    }
}

@Composable
private fun GoalsCard(goals: List<GoalProgress>) {
    NeonCard("Metas", PurpleGoal, Modifier.fillMaxWidth()) {
        goals.forEach {
            Text(it.name)
            LinearProgressIndicator(
                progress = { (it.currentValue / it.targetValue).toFloat().coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth(),
                color = PurpleGoal
            )
        }
    }
}

@Composable
private fun HeatmapCard(days: List<HeatmapDay>) {
    NeonCard("Heatmap de Gastos", WarningYellow, Modifier.fillMaxWidth()) {
        val weeks = days.chunked(7)
        weeks.forEach { week ->
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                week.forEach { day ->
                    val color = when {
                        day.exceeded -> AlertRed
                        day.totalSpent > 0 -> WarningYellow.copy(alpha = 0.6f)
                        else -> Color.Gray.copy(alpha = 0.25f)
                    }
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(color, RoundedCornerShape(4.dp))
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarCard(events: List<String>) {
    NeonCard("Calendário Inteligente", ElectricBlue, Modifier.fillMaxWidth()) {
        events.forEach { name ->
            Text("• $name")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {}) { Text("Concluir") }
            Button(onClick = {}) { Text("Adiar") }
            Button(onClick = {}) { Text("Reprogramar") }
        }
    }
}

@Composable
private fun NeonCard(
    title: String,
    color: Color,
    modifier: Modifier = Modifier,
    content: @Composable Column.() -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, color.copy(alpha = 0.9f)),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = {
                Text(title, fontWeight = FontWeight.Bold, color = color)
                content()
            }
        )
    }
}
