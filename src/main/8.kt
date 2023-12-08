import java.io.File

fun main() {
	val network = readNetwork()
	part1(network)
	part2(network)
}

private fun part1(network: Network) {
	println(findStepsToFirstZ(network.nodes.single { it.from == "AAA" }, network))
}

private fun part2(network: Network) {
	println(
		network.nodes
			.filter { it.from[2] == 'A' }
			.map { findStepsToFirstZ(it, network) }
			.leastCommonMultiple()
	)
}

private fun findStepsToFirstZ(current: Node, network: Network): Long {
	var currentNode = current
	var steps = 0L
	var instructionIndex = 0

	while (currentNode.from[2] != 'Z') {
		if (instructionIndex == network.instructions.length) {
			instructionIndex = 0
		}

		val instruction = network.instructions[instructionIndex]
		currentNode = network.nodes.single { it.from == if (instruction == 'R') currentNode.right else currentNode.left }

		steps++
		instructionIndex++
	}
	return steps
}

private fun leastCommonMultiple(a: Long, b: Long): Long {
	val larger = if (a > b) a else b
	val maxLcm = a * b
	var lcm = larger
	while (lcm <= maxLcm) {
		if (lcm % a == 0L && lcm % b == 0L) {
			return lcm
		}
		lcm += larger
	}
	return maxLcm
}

fun List<Long>.leastCommonMultiple(): Long {
	var result = this[0]
	for (i in 1 until this.size) {
		result = leastCommonMultiple(result, this[i])
	}
	return result
}

private fun readNetwork(): Network =
	File(ClassLoader.getSystemResource("input8.txt").file).useLines { lines ->
		val linesList = lines.toList()
		val instructions = linesList[0]

		val regex = Regex("([A-Z0-9]{3})")

		val nodes = linesList.subList(2, linesList.size).map { line ->
			regex.findAll(line).toList().let {
				Node(it[0].value, it[1].value, it[2].value)
			}
		}

		Network(instructions, nodes)
	}

data class Network(val instructions: String, val nodes: List<Node>)

data class Node(val from: String, val left: String, val right: String)
