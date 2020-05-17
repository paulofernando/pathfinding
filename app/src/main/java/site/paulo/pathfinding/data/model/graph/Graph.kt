package site.paulo.pathfinding.data.model.graph

import java.util.*

interface Graph<T> {
    fun getNodes() : LinkedList<T>
}