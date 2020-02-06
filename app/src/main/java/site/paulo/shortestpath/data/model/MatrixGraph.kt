package site.paulo.shortestpath.data.model

class MatrixGraph(private val rows: Int, private val columns: Int) {

    private val table = Array(rows) { arrayOfNulls<Node>(columns) }

    init {
        populate()
        connectNodes()
    }

    fun getNode(row: Int, column: Int): Node? {
        return table[row][column]
    }

    /**
     * Creates all the nodes of the matrix
     */
    private fun populate() {
        table.forEachIndexed { i, row ->
            row.forEachIndexed { j, _ -> table[i][j] = Node("${i},${j}") }
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
                        rightNode?.connect(node, i)
                    }

                    if (j <= columns - 2) {
                        val bottomNode = getNode(i, j + 1)
                        bottomNode?.connect(node, i)
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