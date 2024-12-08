import java.io.File
import kotlin.math.pow

fun main() {
	val cards = readCards()
	part1(cards)
	part2(cards)
}

private fun part1(cards: List<Card>) {
	println(
		cards.sumOf {
			it.myNumbers.count(it.winningNumbers::contains).let { count ->
				if (count == 1) 1 else 2.0.pow(count - 1).toInt()
			}
		}
	)
}

private fun part2(cards: List<Card>) {
	val copiesCount = cards.associateWith { 1 }.toMutableMap()

	cards.forEachIndexed { index, card ->
		val matches = card.myNumbers.count(card.winningNumbers::contains)

		if (matches != 0) {
			repeat(copiesCount[card]!!) {
				cards.subList(index + 1, index + 1 + matches).forEach { copiesCount[it] = copiesCount[it]!! + 1 }
			}
		}
	}

	println(copiesCount.values.sum())
}

private fun readCards(): List<Card> =
	File(ClassLoader.getSystemResource("input4.txt").file).useLines { lines ->
		lines.map {
			Card(getWinningNumbers(it), getMyNumbers(it))
		}.toList()
	}

private fun getWinningNumbers(card: String): List<Int> =
	getNumbers(card.substring(card.indexOfFirst { it == ':' } + 1, card.indexOfFirst { it == '|' }))

private fun getMyNumbers(card: String): List<Int> =
	getNumbers(card.substring(card.indexOfFirst { it == '|' } + 1, card.length))

private fun getNumbers(substring: String): List<Int> =
	Regex("(\\d+)").findAll(substring).map { it.groups[0]!!.value.toInt() }.toList()

data class Card(val winningNumbers: List<Int>, val myNumbers: List<Int>)
