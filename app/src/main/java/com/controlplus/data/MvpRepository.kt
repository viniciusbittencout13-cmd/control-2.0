package com.controlplus.data

import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.math.max

data class DashboardState(
    val dailyControlLevel: Int,
    val todaysBudgetRemaining: Double,
    val todaysBudgetUsedPercent: Float,
    val topAppsToday: List<AppUsageLog>,
    val appProgress: List<AppProgress>,
    val goalsProgress: List<GoalProgress>,
    val monthHeatmap: List<HeatmapDay>,
    val todayEvents: List<CalendarEvent>,
    val alerts: List<String>
)

data class AppProgress(val appName: String, val usedMinutes: Int, val limitMinutes: Int)

data class GoalProgress(val name: String, val currentValue: Double, val targetValue: Double)

data class HeatmapDay(val dayOfMonth: Int, val totalSpent: Double, val exceeded: Boolean)

class MvpRepository {
    private val settings = UserSettings(monthlyIncome = 12500.0, financeMode = FinanceMode.RIGID)

    private val categories = listOf(
        Category("1", "Moradia", 3200.0, true),
        Category("2", "Alimentação", 1700.0, false),
        Category("3", "Transporte", 800.0, false),
        Category("4", "Lazer", 600.0, false)
    )

    private val expenses = mutableListOf(
        Expense("1", "2", 53.9, LocalDate.now(), ExpenseSource.MANUAL, "Almoço"),
        Expense("2", "3", 28.0, LocalDate.now(), ExpenseSource.MANUAL, "Corrida app"),
        Expense("3", "4", 35.0, LocalDate.now().minusDays(1), ExpenseSource.BANK_NOTIFICATION, "Streaming")
    )

    private val goals = listOf(
        Goal("1", "Reserva de Emergência", 10000.0, LocalDate.now().plusMonths(9), ContributionRule.FixedMonthly(800.0)),
        Goal("2", "Viagem", 5000.0, LocalDate.now().plusMonths(6), ContributionRule.PercentIncome(8.0))
    )

    private val contributions = listOf(
        GoalContribution("1", "1", LocalDate.now().minusDays(22), 800.0),
        GoalContribution("2", "1", LocalDate.now().minusDays(1), 800.0),
        GoalContribution("3", "2", LocalDate.now().minusDays(4), 550.0)
    )

    private val appLimits = listOf(
        AppLimit("Instagram", dailyLimitMinutes = 45, weeklyLimitMinutes = 210),
        AppLimit("YouTube", dailyLimitMinutes = 60, weeklyLimitMinutes = 360),
        AppLimit("WhatsApp", dailyLimitMinutes = 90, weeklyLimitMinutes = 630)
    )

    private val usageLogs = listOf(
        AppUsageLog("Instagram", LocalDate.now(), 52, false),
        AppUsageLog("YouTube", LocalDate.now(), 28, false),
        AppUsageLog("WhatsApp", LocalDate.now(), 67, true)
    )

    private val events = listOf(
        CalendarEvent("1", "Revisar orçamento", LocalDateTime.now().plusHours(2), 30),
        CalendarEvent("2", "Bloco foco projeto", LocalDateTime.now().plusHours(4), 90)
    )

    fun addManualExpense(categoryId: String, amount: Double, note: String) {
        expenses += Expense(
            id = (expenses.size + 1).toString(),
            categoryId = categoryId,
            amount = amount,
            date = LocalDate.now(),
            source = ExpenseSource.MANUAL,
            note = note
        )
    }

    fun calculateState(today: LocalDate = LocalDate.now()): DashboardState {
        val totalMonthlyBudget = categories.sumOf { it.monthlyBudget }
        val spentSoFar = expenses.filter { it.date.month == today.month }.sumOf { it.amount }
        val daysInMonth = today.lengthOfMonth()
        val daysRemaining = max(daysInMonth - today.dayOfMonth + 1, 1)
        val dailyBudget = (totalMonthlyBudget - spentSoFar) / daysRemaining
        val spentToday = expenses.filter { it.date == today }.sumOf { it.amount }
        val budgetUsedPercent = (spentToday / max(dailyBudget, 1.0)).toFloat().coerceIn(0f, 1.5f)

        val appProgress = appLimits.map { limit ->
            val used = usageLogs.filter { it.appName == limit.appName && it.date == today }.sumOf { it.usedMinutes }
            AppProgress(limit.appName, used, limit.dailyLimitMinutes)
        }

        val goalProgress = goals.map { goal ->
            val current = contributions.filter { it.goalId == goal.id }.sumOf { it.amount }
            GoalProgress(goal.name, current, goal.targetValue)
        }

        val financeScore = if (spentToday <= dailyBudget) 100 else 40
        val timeScore = appProgress.map {
            val ratio = it.usedMinutes.toFloat() / max(it.limitMinutes, 1)
            when {
                ratio <= 0.8f -> 100
                ratio <= 1f -> 70
                else -> 35
            }
        }.average().toInt()
        val taskScore = if (events.any { it.start.toLocalDate() == today }) 78 else 100
        val controlLevel = ((financeScore * 0.4) + (timeScore * 0.35) + (taskScore * 0.25)).toInt()

        val heatmap = (1..daysInMonth).map { day ->
            val date = today.withDayOfMonth(day)
            val daySpent = expenses.filter { it.date == date }.sumOf { it.amount }
            val exceeded = daySpent > dailyBudget && day <= today.dayOfMonth
            HeatmapDay(day, daySpent, exceeded)
        }

        val alerts = mutableListOf<String>()
        appProgress.filter { it.usedMinutes >= it.limitMinutes }.forEach {
            alerts += "Limite diário excedido em ${it.appName}. Ative bloqueio temporário?"
        }
        if (spentToday > dailyBudget) {
            alerts += "Gasto de hoje acima do limite diário. Revise seu cartão Financeiro."
        }

        return DashboardState(
            dailyControlLevel = controlLevel,
            todaysBudgetRemaining = dailyBudget - spentToday,
            todaysBudgetUsedPercent = budgetUsedPercent,
            topAppsToday = usageLogs.sortedByDescending { it.usedMinutes }.take(3),
            appProgress = appProgress,
            goalsProgress = goalProgress,
            monthHeatmap = heatmap,
            todayEvents = events,
            alerts = alerts
        )
    }
}
