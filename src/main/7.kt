import java.io.File

// cards for part 2
var camelCards = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')

fun main() {
	val hands = readHands()
	println(
		hands.sortedWith(Hand::compareTo).mapIndexed { index, hand -> hand.bid * (index + 1) }.sum()
	)
}

private fun readHands(): List<Hand> =
	File(ClassLoader.getSystemResource("input7.txt").file).useLines { lines ->
		lines.map { line ->
			line.split(" ").let { Hand(it[0], it[1].toInt()) }
		}.toList()
	}

private fun Char.isStrongerThan(other: Char): Boolean = camelCards.indexOf(this) < camelCards.indexOf(other)

data class Hand(
	val cards: String,
	val bid: Int,
) : Comparable<Hand> {

	override fun compareTo(other: Hand): Int {
		this.jokerCamelCardType().compareTo(other.jokerCamelCardType()).takeIf { it != 0 }?.let { return it }

		for (i in 0..<5) {
			if (cards[i] == other.cards[i]) continue
			return if (cards[i].isStrongerThan(other.cards[i])) 1 else -1
		}

		return 0
	}

	// part 1
	private fun camelCardType(): List<Int> = cards.toCharArray().groupBy { it }.values.map { it.size }.sortedDescending()

	// part 2
	private fun jokerCamelCardType(): List<Int> {
		val counts = cards.toCharArray().filter { it != 'J' }.groupBy { it }.values.map { it.size }.sortedDescending().toMutableList()

		if (counts.isEmpty()) {
			return listOf(5)
		}

		counts[0] += cards.count { it == 'J' }
		return counts
	}

	private fun List<Int>.compareTo(other: List<Int>): Int {
		for (i in 0..<if (this.size < other.size) this.size else other.size) {
			if (this[i] == other[i]) continue
			return if (this[i] > other[i]) 1 else -1
		}
		return 0
	}
}
