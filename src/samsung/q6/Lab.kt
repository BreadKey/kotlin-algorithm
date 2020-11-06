package samsung.q6

import java.util.*
import kotlin.math.max

class Lab {
    companion object {
        const val SPACE = 0
        const val WALL = 1
        const val VIRUS = 2
    }

    fun infect(map: Array<IntArray>): Int {
        val width = map.first().size
        val height = map.size

        var infectedSpaceCount = -1

        while (infectedSpaceCount != 0) {
            infectedSpaceCount = 0

            map.forEachIndexed { y, row ->
                row.forEachIndexed { x, tile ->
                    if (tile == VIRUS) {
                        if (x > 0 && setVirusIfNotWall(map, x - 1, y)) infectedSpaceCount++
                        if (x < width - 1 && setVirusIfNotWall(map, x + 1, y)) infectedSpaceCount++
                        if (y > 0 && setVirusIfNotWall(map, x, y - 1)) infectedSpaceCount++
                        if (y < height - 1 && setVirusIfNotWall(map, x, y + 1)) infectedSpaceCount++
                    }
                }
            }
        }

        return map.map { row -> row.filter { it == SPACE }.size }.sum()
    }

    private fun Array<IntArray>.setTileAt(x: Int, y: Int, tile: Int) {
        this[y][x] = tile
    }

    private fun Array<IntArray>.getTileAt(x: Int, y: Int): Int = this[y][x]

    private fun setVirusIfNotWall(map: Array<IntArray>, x: Int, y: Int): Boolean {
        if (map.getTileAt(x, y) == SPACE) {
            map.setTileAt(x, y, VIRUS)
            return true
        }

        return false
    }

    fun calculateMaxSpaceCount(map: Array<IntArray>): Int {
        val spacePoints = LinkedList<Offset>()

        var maxSpaceCount = 0

        map.forEachIndexed { y, row ->
            row.forEachIndexed { x, tile ->
                if (tile == SPACE) {
                    spacePoints.add(Offset(x, y))
                }
            }
        }

        val wallPoints = Array<Offset>(3) { Offset(0, 0) }

        while (spacePoints.isNotEmpty()) {
            wallPoints[0] = spacePoints.poll()

            spacePoints.forEachIndexed { index, secondWall ->
                wallPoints[1] = secondWall
                spacePoints.drop(index + 1).forEach { thirdWall ->
                    wallPoints[2] = thirdWall

                    val testMap = Array(map.size) {
                        map[it].copyOf()
                    }

                    wallPoints.forEach { point ->
                        testMap.setTileAt(point.x, point.y, WALL)
                    }

                    val spaceCount = infect(testMap)

                    maxSpaceCount = max(spaceCount, maxSpaceCount)
                }
            }
        }

        return maxSpaceCount
    }
}

data class Offset(val x: Int, val y: Int)

fun main() {
    val lab = Lab()

    val mapHeight = readLine()!!.split(' ').first().toInt()

    val map = Array(mapHeight) {
        readLine()!!.split(' ').map { it.toInt() }.toIntArray()
    }

    println(lab.calculateMaxSpaceCount(map))
}