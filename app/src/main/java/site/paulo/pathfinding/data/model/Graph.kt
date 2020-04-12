package site.paulo.pathfinding.data.model

import java.util.*

interface Graph<T> {
    fun getNodes() : LinkedList<T>
}