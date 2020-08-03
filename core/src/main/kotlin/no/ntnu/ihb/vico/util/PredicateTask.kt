package no.ntnu.ihb.vico.util

import no.ntnu.ihb.vico.core.Engine

fun interface Predicate<T> {

    fun test(it: T): Boolean

    fun negate(): Predicate<T> {
        return Predicate {
            !test(it)
        }
    }

    fun and(other: Predicate<T>): Predicate<T> {
        return Predicate {
            test(it) && other.test(it)
        }
    }

    fun or(other: Predicate<T>): Predicate<T> {
        return Predicate {
            test(it) || other.test(it)
        }
    }

}

interface PredicateTask : Predicate<Engine>, Runnable
