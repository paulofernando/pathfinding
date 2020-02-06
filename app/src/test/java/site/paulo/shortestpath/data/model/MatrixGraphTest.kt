package site.paulo.shortestpath.data.model

import io.mockk.clearAllMocks
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MatrixGraphTest {

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `test matrix creation`() {
        val matrix = MatrixGraph(3,3)

        assert(matrix.getNode(0,0)?.name.equals("0,0"))
        assert(matrix.getNode(0,1)?.name.equals("0,1"))
        assert(matrix.getNode(0,2)?.name.equals("0,2"))
        assert(matrix.getNode(1,0)?.name.equals("1,0"))
        assert(matrix.getNode(1,1)?.name.equals("1,1"))
        assert(matrix.getNode(1,2)?.name.equals("1,2"))
        assert(matrix.getNode(2,0)?.name.equals("2,0"))
        assert(matrix.getNode(2,1)?.name.equals("2,1"))
        assert(matrix.getNode(2,2)?.name.equals("2,2"))
    }

    @Test
    fun `test matrix connections`() {
        val matrix = MatrixGraph(3,3)

        assert(matrix.getNode(0,0)?.edges?.size == 2)
        assert(matrix.getNode(0,1)?.edges?.size == 3)
        assert(matrix.getNode(0,2)?.edges?.size == 2)
        assert(matrix.getNode(1,0)?.edges?.size == 3)
        assert(matrix.getNode(1,1)?.edges?.size == 4)
        assert(matrix.getNode(1,2)?.edges?.size == 3)
        assert(matrix.getNode(2,0)?.edges?.size == 2)
        assert(matrix.getNode(2,1)?.edges?.size == 3)
        assert(matrix.getNode(2,2)?.edges?.size == 2)
    }

}