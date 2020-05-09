package site.paulo.pathfinding.manager

interface Action {
    fun getType(): HistoryAction
}