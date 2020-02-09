package site.paulo.shortestpath.data.model

class MatrixGraph(val rows: Int, val columns: Int) {

    val table = Array(rows) { arrayOfNulls<Node>(columns) }
    private val removedNodes = ArrayList<Node>()

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

    fun disconnect(nodeA: Pair<Int,Int>, nodeB: Pair<Int,Int>) {
        val nA = getNode(nodeA)
        val nB = getNode(nodeB)
        if ((nA != null) && (nB != null)) nA.disconnect(nB)
    }

    fun removeNode(nodePosition: Pair<Int,Int>) {
        val node = getNode(nodePosition) ?: return
        node.edges.values.forEach {
            node.disconnect(it.getOpposite(node))
        }
        removedNodes.add(node)
    }

    /**
     * Creates all the nodes of the matrix
     */
    private fun populate() {
        table.forEachIndexed { i, row ->
            row.forEachIndexed { j, _ -> table[i][j] = Node("${i},${j}", Pair(i,j)) }
        }
    }

    /**
     * Connects adjacent nodes with weight 1
     */
    private fun connectNodes() {
        table.forEachIndexed { i, row ->
            row.forEachIndexed { j, node ->
                if(node != null) {
                    if (i <= rows - 2) {
                        val rightNode = getNode(i + 1, j)
                        rightNode?.connect(node)
                    }

                    if (j <= columns - 2) {
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