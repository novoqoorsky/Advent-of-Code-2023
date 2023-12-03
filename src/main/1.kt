import java.io.File

fun main() {
	val file = File(ClassLoader.getSystemResource("input1.txt").file)

	println(part1(file))
	println(part2(file))
}

private fun part1(file: File): Int =
	file.useLines { lines ->
		lines.sumOf {
			val digits = it.filter { c -> c.isDigit() }
			"${digits[0]}${digits[digits.length - 1]}".toInt()
		}
	}

private fun part2(file: File): Int =
	file.useLines { lines ->
		lines.sumOf {
			val first = it.findAnyOf(Digit.digitNames()).takeLettersOrDigit(it, isFirst = true)
			val last = it.findLastAnyOf(Digit.digitNames()).takeLettersOrDigit(it, isFirst = false)
			"$first$last".toInt()
		}
	}

private fun Pair<Int, String>?.takeLettersOrDigit(line: String, isFirst: Boolean): Int {
	val digitIndex = if (isFirst) line.indexOfFirst { it.isDigit() } else line.indexOfLast { it.isDigit() }
	val compareTo = if (isFirst) -1 else 1

	return if (this != null && (first.compareTo(digitIndex) == compareTo || digitIndex == -1)) {
		Digit.valueOf(second).number
	} else {
		line[digitIndex].digitToInt()
	}
}


enum class Digit(val number: Int) {

	one(1),
	two(2),
	three(3),
	four(4),
	five(5),
	six(6),
	seven(7),
	eight(8),
	nine(9),
	;

	companion object {

		fun digitNames(): List<String> = entries.toList().map { it.name }
	}
}
