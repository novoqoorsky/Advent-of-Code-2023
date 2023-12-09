import java.io.File

fun main() {
	val file = File(ClassLoader.getSystemResource("input9.txt").file).readLines()
	part1(file)
	part2(file)
}

private fun part1(lines: List<String>) {
	println(
		findDifferences(lines).sumOf { differences ->
			for (i in differences.size - 2 downTo 0) {
				differences[i].add(differences[i][differences[i].size - 1] + differences[i + 1][differences[i + 1].size - 1])
			}
			differences[0][differences[0].size - 1]
		}
	)
}

private fun findDifferences(lines: List<String>): List<List<MutableList<Int>>> =
	lines.map { line ->
		val values = line.split(" ").map { it.toInt() }.toMutableList()
		val differences = mutableListOf(values)
		var currentDifferences = values
		while (currentDifferences.any { it != 0 }) {
			currentDifferences = currentDifferences.zipWithNext().map { it.second - it.first }.toMutableList()
			differences.add(currentDifferences)
		}
		differences[differences.size - 1].add(0)
		differences
	}

private fun part2(lines: List<String>) {
	println(
		findDifferences(lines).sumOf { differences ->
			for (i in differences.size - 2 downTo 0) {
				differences[i].add(0, differences[i][0] - differences[i + 1][0])
			}
			differences[0][0]
		}
	)
}
