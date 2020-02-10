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

    fun removeNode(nodePosition: Pair<Int,Int>) {
        val node = getNode(nodePosition) ?: return
        node.edges.values.forEach {
            node.disconnect(it.getOpposite(node))
        }
        removedNodes.add(node)
    }

    fun readdNode(nodePosition: Pair<Int,Int>) {
        addNode(nodePosition.first, nodePosition.second)
        connectToNeighboorNodes(nodePosition.first, nodePosition.second)
    }

    /**
     * Creates all the nodes of the matrix
     */
    private fun populate() {
        table.forEachIndexed { i, row ->
            row.forEachIndexed { j, _ -> addNode(i,j)}
        }
    }

    private fun addNode(row: Int, col: Int) {
        if ((row >= rows) || (col >= columns)) return
        table[row][col] = Node("${row},${col}", Pair(row,col))
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

    private fun connectToNeighboorNodes(row: Int, col: Int) {
        if ((row >= rows) || (col >= columns)) return
        val node = getNode(row, col) ?: return

        if (col > 0) {
            val leftNode = getNode(row, col - 1)
            leftNode?.connect(node)
        }

        if (row > 0) {
            val topNode = getNode(row - 1, col)
            topNode?.connect(node)
        }

        if (col <= columns - 2) {
            val rightNode = getNode(row, col + 1)
            rightNode?.connect(node)
        }

        if (row <= rows - 2) {
            val bottomNode = getNode(row + 1, col)
            bottomNode?.connect(node)
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