package com.controlplus.data

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

data class UserSettings(
    val monthlyIncome: Double,
    val financeMode: FinanceMode,
    val darkModeEnabled: Boolean = true
)

enum class FinanceMode { RIGID, FLEXIBLE }

data class Category(
    val id: String,
    val name: String,
    val monthlyBudget: Double,
    val fixed: Boolean
)

data class Expense(
    val id: String,
    val categoryId: String,
    val amount: Double,
    val date: LocalDate,
    val source: ExpenseSource,
    val note: String = ""
)

enum class ExpenseSource { MANUAL, BANK_NOTIFICATION }

data class Goal(
    val id: String,
    val name: String,
    val targetValue: Double,
    val deadline: LocalDate?,
    val contributionRule: ContributionRule
)

data class GoalContribution(
    val id: String,
    val goalId: String,
    val date: LocalDate,
    val amount: Double
)

sealed class ContributionRule {
    data class FixedMonthly(val amount: Double) : ContributionRule()
    data class PercentIncome(val percent: Double) : ContributionRule()
}

data class AppLimit(
    val appName: String,
    val dailyLimitMinutes: Int,
    val weeklyLimitMinutes: Int,
    val blockedHours: IntRange = 0..0,
    val activeDays: Set<DayOfWeek> = DayOfWeek.entries.toSet()
)

data class AppUsageLog(
    val appName: String,
    val date: LocalDate,
    val usedMinutes: Int,
    val productive: Boolean
)

data class CalendarEvent(
    val id: String,
    val title: String,
    val start: LocalDateTime,
    val durationMinutes: Int,
    val completed: Boolean = false
)

data class Reminder(
    val id: String,
    val eventId: String,
    val minutesBefore: Int,
    val enabled: Boolean
)
