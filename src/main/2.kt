import java.io.File

fun main() {
	val games = readGames()
	part1(games)
	part2(games)
}

private fun readGames(): List<Game> {
	val regex = Regex("(\\d+) (\\w+)")
	var gameNumber = 0
	return File(ClassLoader.getSystemResource("input2.txt").file).useLines { lines ->
		lines.map {
			val cubes = regex.findAll(it)
				.map { matches -> matches.groupValues }
				.groupBy { values -> values[2] }
				.mapValues { entry -> entry.value.map { value -> value[1].toInt() } }
			Game(++gameNumber, greenCounts = cubes["green"]!!, blueCounts = cubes["blue"]!!, redCounts = cubes["red"]!!)
		}.toList()
	}
}

private fun part1(games: List<Game>) {
	println(
		games
			.filter { it.redCounts.all { counts -> counts <= 12 } && it.greenCounts.all { counts -> counts <= 13 } && it.blueCounts.all { counts -> counts <= 14 } }
			.sumOf { it.number }
	)
}

private fun part2(games: List<Game>) {
	println(
		games.sumOf {
			it.greenCounts.max() * it.blueCounts.max() * it.redCounts.max()
		}
	)
}

data class Game(val number: Int, val greenCounts: List<Int>, val blueCounts: List<Int>, val redCounts: List<Int>)
