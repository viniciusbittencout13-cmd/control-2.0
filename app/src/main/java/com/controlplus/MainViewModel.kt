package com.controlplus

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.controlplus.data.DashboardState
import com.controlplus.data.MvpRepository

class MainViewModel : ViewModel() {
    private val repository = MvpRepository()

    var state by mutableStateOf<DashboardState?>(null)
        private set

    init {
        refresh()
    }

    fun addExpense(amount: Double, note: String) {
        repository.addManualExpense(categoryId = "2", amount = amount, note = note)
        refresh()
    }

    private fun refresh() {
        state = repository.calculateState()
    }
}
