import java.io.File
import java.math.BigDecimal

fun main() {
	val almanac = readAlmanac()
	part1(almanac)
	part2(almanac)
}

private fun part1(almanac: Almanac) {
	val locations = almanac.seeds.map { getLocation(it, almanac.maps) }
	println(locations.min())
}

private fun part2(almanac: Almanac) {
	var minLocation = BigDecimal.valueOf(Double.MAX_VALUE)

	almanac.seeds.chunked(2).forEach {
		var seed = it[0]
		while (seed < it[0] + it[1]) {
			val location = getLocation(seed, almanac.maps)
			if (location < minLocation) {
				minLocation = location
			}
			seed++
		}
	}

	println(minLocation)
}

private fun getLocation(seed: BigDecimal, maps: List<AlmanacMap>): BigDecimal {
	var currentLocation = seed

	maps.forEach {
		it.entries.firstOrNull { entry -> currentLocation in entry.source..<entry.source + entry.range }?.let { entry ->
			currentLocation = entry.destination + (currentLocation - entry.source)
		}
	}

	return currentLocation
}

private fun readAlmanac(): Almanac {
	val regex = Regex("(\\d+)")
	return File(ClassLoader.getSystemResource("input5.txt").file).useLines {
		val lines = it.toList()
		val seeds = regex.findAll(lines[0]).map { matches -> matches.groups[0]!!.value.toBigDecimal() }.toList()
		val maps = mutableListOf<AlmanacMap>()

		var index = 3
		while (index < lines.size) {
			val nextEmptyLineIndex = lines.subList(index, lines.size).indexOfFirst { line -> line.isBlank() }
				.takeIf { blankLine -> blankLine != -1 }
				?.let { blankLine -> blankLine + index }
				?: lines.size

			val mapEntries = lines.subList(index, nextEmptyLineIndex).map { line ->
				val mappings = regex.findAll(line).toList()
				AlmanacMapEntry(
					mappings[0].groups[0]!!.value.toBigDecimal(),
					mappings[1].groups[0]!!.value.toBigDecimal(),
					mappings[2].groups[0]!!.value.toBigDecimal(),
				)
			}

			maps.add(AlmanacMap(mapEntries))

			index = nextEmptyLineIndex + 2
		}

		Almanac(seeds, maps)
	}
}

data class Almanac(
	val seeds: List<BigDecimal>,
	val maps: List<AlmanacMap>,
)

data class AlmanacMap(
	val entries: List<AlmanacMapEntry>,
)

data class AlmanacMapEntry(
	val destination: BigDecimal,
	val source: BigDecimal,
	val range: BigDecimal,
)
