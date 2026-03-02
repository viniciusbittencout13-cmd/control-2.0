package com.controlplus

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.controlplus.ui.screen.DashboardScreen
import com.controlplus.ui.theme.ControlPlusTheme

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ControlPlusTheme {
                viewModel.state?.let { dashboard ->
                    DashboardScreen(
                        state = dashboard,
                        onQuickExpense = {
                            Toast.makeText(this, "Confirma salvar gasto manual de R$ 25,90", Toast.LENGTH_SHORT).show()
                            viewModel.addExpense(25.90, "Registro rápido")
                        }
                    )
                }
            }
        }
    }
}
