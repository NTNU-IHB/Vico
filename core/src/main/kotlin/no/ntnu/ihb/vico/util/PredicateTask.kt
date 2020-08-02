package no.ntnu.ihb.vico.util

import no.ntnu.ihb.vico.core.Engine
import java.util.function.Predicate

interface PredicateTask : Predicate<Engine>, Runnable
