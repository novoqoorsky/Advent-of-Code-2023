fun main() {
	part1()
	part2()
}

private fun part1() {
	val races = listOf(Race(56, 499), Race(97, 2210), Race(77, 1097), Race(93, 1440))
	println(races.map { it.findNumberOfWaysToWin() }.reduce(Int::times))
}

private fun part2() {
	val race = Race(56977793, 499221010971440)
	println(race.findNumberOfWaysToWin())
}

data class Race(val time: Long, val distanceToBeat: Long) {

	fun findNumberOfWaysToWin(): Int {
		var ways = 0

		for (timeSpentCharging in 0..<time) {
			val timeSpentMoving = time - timeSpentCharging
			val distance = timeSpentMoving * timeSpentCharging

			if (distance > distanceToBeat) ways++
		}

		return ways
	}
}
