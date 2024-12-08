import java.io.File

fun main() {
	val engine = readEngine()
	part1(engine)
	part2(engine)
}

private fun readEngine(): List<String> =
	File(ClassLoader.getSystemResource("input3.txt").file).useLines { lines ->
		lines.toList()
	}

private fun part1(engine: List<String>) {
	val regex = Regex("\\d+")
	var sum = 0

	engine.forEachIndexed { index, line ->
		regex.findAll(line).forEach { matchResult ->
			val match = matchResult.groups[0]!!
			val number = match.value.toInt()

			if (engine.isAdjacent(index, match.range)) {
				sum += number
			}
		}
	}

	println(sum)
}

private fun List<String>.isAdjacent(lineIndex: Int, numberSpan: IntRange): Boolean {
	if (lineIndex == 0) {
		return this[0].isAdjacent(numberSpan) || this[1].isAdjacent(numberSpan)
	}
	if (lineIndex == size - 1) {
		return this[size - 1].isAdjacent(numberSpan) || this[size - 2].isAdjacent(numberSpan)
	}
	return this[lineIndex].isAdjacent(numberSpan) || this[lineIndex - 1].isAdjacent(numberSpan) || this[lineIndex + 1].isAdjacent(numberSpan)
}

private fun String.isAdjacent(numberSpan: IntRange): Boolean {
	val lowerBound = (numberSpan.first - 1).takeIf { it >= 0 } ?: 0
	val upperBound = (numberSpan.last + 1).takeIf { it < length } ?: (length - 1)

	return substring(lowerBound, upperBound + 1).any { !it.isDigit() && it != '.' }
}

private fun part2(engine: List<String>) {
	val regex = Regex("\\*")
	var sum = 0

	engine.forEachIndexed { index, line ->
		regex.findAll(line).forEach { matchResult ->
			val gearIndex = matchResult.groups[0]!!.range.first
			engine.gearAdjacency(index, gearIndex)?.let {
				sum += it.first * it.second
			}
		}
	}

	println(sum)
}

private fun List<String>.gearAdjacency(lineIndex: Int, gearIndex: Int): Pair<Int, Int>? {
	val adjacent = mutableListOf<Int>()

	adjacent.addIfGearAdjacent(this[lineIndex], gearIndex)
	if (lineIndex != 0) {
		adjacent.addIfGearAdjacent(this[lineIndex - 1], gearIndex)
	}
	if (lineIndex != size - 1) {
		adjacent.addIfGearAdjacent(this[lineIndex + 1], gearIndex)
	}

	if (adjacent.size == 2) {
		return Pair(adjacent[0], adjacent[1])
	}

	return null
}

private fun MutableList<Int>.addIfGearAdjacent(line: String, gearIndex: Int) {
	Regex("\\d+").findAll(line).forEach {
		it.groups[0]!!.let { match ->
			if (match.range.isGearAdjacent(gearIndex)) {
				add(match.value.toInt())
			}
		}
	}
}

private fun IntRange.isGearAdjacent(gearIndex: Int): Boolean =
	last == gearIndex - 1 || first == gearIndex + 1 || contains(gearIndex)
