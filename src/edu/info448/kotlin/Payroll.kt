package edu.info448.kotlin
import kotlin.reflect.KProperty

const val MINIMUM_WAGE = 15.0

/* See assignment instructions on Canvas */
open class Employee(val name: String, open val hourlyWage: Double) {
    private var hoursWorked: Int = 0
        set(value) {
            if (value in 0..168) {
                field = value
            }
        }

    init {
        println("Hiring $name")
    }

    fun calculatePay(): Double = hourlyWage * hoursWorked

    fun showInvoice() {
        println("Pay $name ${calculatePay()} for $hoursWorked hours work")
    }

    open fun work(job:() -> Int) {
        hoursWorked += job()
    }
}

class UnionEmployee(name: String) : Employee(name, hourlyWage = MINIMUM_WAGE) {
    override val hourlyWage by UnionContract()
    var onStrike = false

    fun picket() {
        println("On strike! Fair wages for all!")
    }

    override fun work(job:() -> Int) {
        if (onStrike) picket() else super.work(job)
    }
}

class UnionContract {
    companion object {
        private var negotiatedHourlyWage = 0.0
        fun negotiate(newWage: Double) {
            negotiatedHourlyWage = newWage
        }
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Double {
        return negotiatedHourlyWage
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Double) {
        negotiatedHourlyWage = value
    }
}

class Job(val description: String, val hoursRequired: Int): () -> Int {
    override operator fun invoke(): Int {
        println("Doing job: $description")
        return hoursRequired
    }

}

//A main() method in Kotlin
fun main(args: Array<String>) {
    val unionEmployees = arrayOf(UnionEmployee("Homer"), UnionEmployee("Simpson"))
    for (employee in unionEmployees) {
        employee.onStrike = true
        employee.work { 40 }
    }

    UnionContract.negotiate(22.0)
    val job = Job("Go to space", 100)

    for (employee in unionEmployees){
        employee.onStrike = false
        employee.work(job)
        employee.showInvoice()
    }
}
