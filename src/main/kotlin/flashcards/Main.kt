package flashcards

val flashcard = Flashcard()

fun main(args: Array<String>) {
    flashcard.checkArguments(args)
    flashcard.flashcardLoop(args)
}