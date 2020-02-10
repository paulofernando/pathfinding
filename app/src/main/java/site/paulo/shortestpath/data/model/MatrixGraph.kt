package site.paulo.shortestpath.data.model

class MatrixGraph(val rows: Int, val columns: Int) {

    val table = Array(rows) { arrayOfNulls<Node>(columns) }
    private val removedNodes = HashMap<String,Node>()

    init {
        populate()
        connectNodes()
    }

    fun getNode(row: Int, column: Int): Node? {
        return table[row][column]
    }

    fun getNode(point: Pair<Int,Int>): Node? {
        return table[point.first][point.second]
    }

    fun removeNode(nodePosition: Pair<Int,Int>) {
        val node = getNode(nodePosition) ?: return
        node.disconnectAll()
        removedNodes[node.name] = node
    }

    fun readdNode(nodePosition: Pair<Int,Int>) {
        val nodeName = "${nodePosition.first},${nodePosition.second}"
        val node = removedNodes[nodeName] ?: return
        removedNodes.remove(nodeName)
        node.reconnectAll()
    }

    private fun addNode(row: Int, col: Int) {
        if ((row >= rows) || (col >= columns)) return
        table[row][col] = Node("${row},${col}", Pair(row,col))
    }

    /**
     * Creates all the nodes of the matrix
     */
    private fun populate() {
        table.forEachIndexed { i, row ->
            row.forEachIndexed { j, _ -> addNode(i,j)}
        }
    }

    /**
     * Connects adjacent nodes with weight 1
     */
    private fun connectNodes() {
        table.forEachIndexed { i, row ->
            row.forEachIndexed { j, node ->
                if(node != null) {
                    if (i <= columns - 2) {
                        val rightNode = getNode(i + 1, j)
                        rightNode?.connect(node)
                    }

                    if (j <= rows - 2) {
                        val bottomNode = getNode(i, j + 1)
                        bottomNode?.connect(node)
                    }
                }
            }
        }
    }

    /**
     * Prints the matrix in System.out
     */
    fun print() {
        table.forEach {
            it.forEachIndexed { j, node ->
                if (node != null) {
                    print(" ${node.name} ")
                    if(j <= columns - 2) {
                        print("-")
                    }
                } else print("   ")
            }
            println()
        }
    }

}