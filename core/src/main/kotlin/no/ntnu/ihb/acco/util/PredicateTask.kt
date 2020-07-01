package no.ntnu.ihb.acco.util

import no.ntnu.ihb.acco.core.Engine
import java.util.function.Predicate

interface PredicateTask : Predicate<Engine>, Runnable
