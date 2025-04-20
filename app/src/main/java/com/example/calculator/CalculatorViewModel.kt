package com.example.calculator

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {
    var state by mutableStateOf(CalculatorState())
        private set

    fun onAction(action: CalculatorActions){
        when(action){
            is CalculatorActions.Number -> enterNumber(action.number)
            is CalculatorActions.Decimal -> enterDecimal()
            is CalculatorActions.Clear -> state = CalculatorState()
            is CalculatorActions.Operation -> enterOperation(action.operator)
            is CalculatorActions.Calculate -> performCalculation()
            is CalculatorActions.Delete -> performDeletion()
        }

    }

    private fun performCalculation() {
       val number1 = state.number1.toDoubleOrNull()
       val number2 = state.number2.toDoubleOrNull()
       if(number1 != null && number2 != null){
           val result = when(state.operator){
           is CalculatorOperation.Add -> number1 + number2
           is CalculatorOperation.Subtract -> number1 - number2
           is CalculatorOperation.Multiply -> number1 * number2
           is CalculatorOperation.Divide -> number1 / number2
           null -> return
       }
       state = state.copy(
           number1 = result.toString().take(15),
           number2 = "",
           operator = null
       )
   }

    }

    private fun performDeletion() {
        when{
            state.number2.isNotBlank()->
                state = state.copy(number2 = state.number2.dropLast(1))
            state.operator != null ->
                state = state.copy(operator = null)
            state.number1.isNotBlank()->
                state= state.copy(number1 = state.number1.dropLast(1))

        }

    }

    private fun enterOperation(operation: CalculatorOperation) {
        if (state.number1.isNotBlank()){
            state = state.copy(operator = operation)
        }
    }

    private fun enterDecimal() {
       if(state.operator == null && !state.number1.contains(".")
           && state.number1.isNotBlank()){
           state =state.copy(
               number1 = state.number1 + "."
           )
           return
       }
        if(!state.number2.contains(".")
            && state.number2.isNotBlank()){
            state =state.copy(
                number2 = state.number2 + "."
            )
            return
        }

    }

    private fun enterNumber(number: Int) {
        if(state.operator == null){
            if(state.number1.length >= MAX_NUM_LENGTH){
                return
            }
            state = state.copy(
                number1 = state.number1 + number
            )
            return
        }
        if(state.number2.length >= MAX_NUM_LENGTH){
            return
        }
        state = state.copy(
            number2 = state.number2 + number
        )
        return

    }

    companion object{
        private const val MAX_NUM_LENGTH =8
    }
}
