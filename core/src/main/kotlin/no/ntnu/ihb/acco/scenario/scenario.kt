package no.ntnu.ihb.acco.scenario


class Scenario {

    internal val timedActions: MutableMap<String, PropertyAction> = mutableMapOf()
    internal val predicateActions: MutableMap<String, PropertyAction> = mutableMapOf()

    var endTime: Number? = null

    fun invokeAt(timePoint: Double, action: TimeContext.() -> Unit) {
        action.invoke(TimeContext(timedActions))
    }

    fun invokeWhen(expression: String, action: WhenContext.() -> Unit) {
        action.invoke(WhenContext(predicateActions))
    }

}

class PropertyAction(
    val component: String,
    val value: Any,
    val actionType: ActionType
) {

    enum class ActionType {
        ADD, SUB, DIV, MUL, ASSIGN
    }

}

infix fun String.add(value: Any): PropertyAction {
    return PropertyAction(this, value, PropertyAction.ActionType.ADD)
}

infix fun String.sub(value: Any): PropertyAction {
    return PropertyAction(this, value, PropertyAction.ActionType.SUB)
}

infix fun String.div(value: Any): PropertyAction {
    return PropertyAction(this, value, PropertyAction.ActionType.DIV)
}

infix fun String.mul(value: Any): PropertyAction {
    return PropertyAction(this, value, PropertyAction.ActionType.MUL)
}

infix fun String.assign(value: Any): PropertyAction {
    return PropertyAction(this, value, PropertyAction.ActionType.ASSIGN)
}

fun scenario(init: Scenario.() -> Unit): Scenario {
    return Scenario().apply(init)
}

class TimeContext(
    private val actions: MutableMap<String, PropertyAction>
) {

    operator fun PropertyAction.unaryPlus() {
        actions[this.component] = this
    }

}

class WhenContext(
    private val actions: MutableMap<String, PropertyAction>
) {

    operator fun PropertyAction.unaryPlus() {
        actions[this.component] = this
    }

}
