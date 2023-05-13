package flashcards

import java.io.File
import kotlin.random.Random

class CheckUserInput {

    fun addCard(mapOfFlashcards: MutableMap<String, String>, logList: MutableList<String>, errorMap: MutableMap<String, Int>) {

        println("The card:")
        logList.add("The card:")
        var newTerm = ""
        try {
            newTerm = readln()
            logList.add(newTerm)
            newTerm = checkTerm(newTerm, mapOfFlashcards)
        } catch (e: Exception) {

            println("The card \"$newTerm\" already exists.")
            println()
            logList.add("The card \"$newTerm\" already exists.")
            logList.add("\n")
            return
        }
        println("The definition of the card:")
        logList.add("The definition of the card:")
        var newDef = ""
        try {
            newDef = readln()
            logList.add(newDef)
            newDef = checkDef(newDef, newTerm, mapOfFlashcards)
        } catch (e: Exception) {
            println("The definition \"$newDef\" already exists.")
            println()
            logList.add("The definition \"$newDef\" already exists.")
            logList.add("\n")
            return
        }
        //will only invoke when no Exception was thrown
        println("The pair (\"$newTerm\":\"$newDef\") has been added:")
        println()
        logList.add("The pair (\"$newTerm\":\"$newDef\") has been added:")
        logList.add("\n")
        errorMap[newTerm] = 0
    }

    //help method for -> addCard()
    private fun checkTerm(newTerm: String, mapOfFlashcards: MutableMap<String, String>): String {
        if (mapOfFlashcards.containsKey(newTerm)) throw Exception()
        else {
            mapOfFlashcards[newTerm] = ""
            return newTerm
        }
    }

    //help method for -> addCard()
    private fun checkDef(userInputDef: String, newTerm: String, mapOfFlashcards: MutableMap<String, String>): String {
        if (mapOfFlashcards.containsValue(userInputDef)) throw Exception()
        else {
            mapOfFlashcards[newTerm] = userInputDef
            return userInputDef
        }
    }

    fun removeCard(mapOfFlashcards: MutableMap<String, String>, logList: MutableList<String>, errorMap: MutableMap<String, Int>) {
        println("Which card?")
        logList.add("Which card?")
        val removingCard = readln()
        logList.add(removingCard)
        try {
            checkForRemovingCard(removingCard, mapOfFlashcards)
        } catch (e: Exception) {
            println("Can't remove \"$removingCard\": there is no such card.")
            println()
            logList.add("Can't remove \"$removingCard\": there is no such card.")
            logList.add("\n")
            return
        }

        println("The card has been removed.")
        println()
        logList.add("The card has been removed.")
        logList.add("\n")
        errorMap.remove(removingCard)
    }

    //help method for -> removeCard()
    private fun checkForRemovingCard(removingCard: String, mapOfFlashcards: MutableMap<String, String>) {
        if (mapOfFlashcards.containsKey(removingCard)) {
            mapOfFlashcards.remove(removingCard)
        } else throw Exception()
    }

    fun importFlashcards(mapOfFlashcards: MutableMap<String, String>, errorMap: MutableMap<String, Int>, logList: MutableList<String>) {
        println("File name:")
        logList.add("File name:")
        try {
            val filename = readln()
            logList.add(filename)
            val fileLines = findTheFileAndImportFlashcards(filename, mapOfFlashcards, errorMap)
            println("$fileLines cards have been loaded.")
            println()
            logList.add("$fileLines cards have been loaded.")
            logList.add("\n")
        } catch (e: Exception) {
            println("File not found.")
            println()
            logList.add("File not found.")
            logList.add("\n")
        }
    }

    //help method for -> importFlashcard()
    fun findTheFileAndImportFlashcards(filename: String, mapOfFlashcards: MutableMap<String, String>, errorMap: MutableMap<String, Int>): Int {
        val searchingFile = File(filename)
        val userDir = File(System.getProperty("user.dir"))
        userDir.walkBottomUp().forEach {
            //substring for just the file not the absolute path
            val substringForFile = it.toString().substringAfterLast("/")
            if (substringForFile == searchingFile.toString()) {
                for (i in it.readLines()) {
                    val listTermAndDefAndError = i.split(":")
                    mapOfFlashcards [listTermAndDefAndError[0]] = listTermAndDefAndError[1]
                    errorMap [listTermAndDefAndError[0]] = listTermAndDefAndError[2].toInt()
                }
                return it.readLines().size
            }
        }
        throw Exception()
    }

    fun exportFlashcards(mapOfFlashcards: MutableMap<String, String>, errorMap: MutableMap<String, Int>, logList: MutableList<String>) {
        println("File name:")
        logList.add("File name:")
        try {
            val filename = readln()
            logList.add(filename)
            val howManyFlashcardsExport = createNewFileAndExport(filename, mapOfFlashcards, errorMap)
            println("$howManyFlashcardsExport cards have been saved.")
            println()
            logList.add("$howManyFlashcardsExport cards have been saved.")
            logList.add("\n")
        } catch (e: Exception) {
            println("File not found.")
            println()
            logList.add("File not found.")
            logList.add("\n")
        }
    }

    //help method for -> exportFlashcard()
    fun createNewFileAndExport(filename: String, mapOfFlashcards: MutableMap<String, String>, errorMap: MutableMap<String, Int>): Int {
        val searchingFile = File(filename)
        searchingFile.createNewFile()
        searchingFile.writeText("")
        for ((k, v) in mapOfFlashcards) {
            searchingFile.appendText("$k:$v:${errorMap[k]}\n")
        }
        return searchingFile.readLines().size
    }

    fun askFlashcards(mapOfFlashcards: MutableMap<String, String>, errorMap: MutableMap<String, Int>, logList: MutableList<String>) {
        println("How many times to ask?")
        logList.add("How many times to ask?")
        val rounds = readln().toInt()
        logList.add(rounds.toString())
        compareSolutions(mapOfFlashcards, rounds, errorMap, logList)
        println()
        logList.add("\n")
    }

    //helping method to -> askFlashcards()
    private fun compareSolutions(mapOfFlashcards: MutableMap<String, String>, rounds: Int, errorMap: MutableMap<String, Int>, logList: MutableList<String>) {
        for (i in 0 until rounds) {
            val random = Random.nextInt(0, mapOfFlashcards.size)
            val key = findKeyToValue(mapOfFlashcards, random)
            println("Print the definition of \"${key}\":")
            logList.add("Print the definition of \"${key}\":")
            var userInput = ""
            try {
                userInput = readln()
                logList.add(userInput)
                checkResult(userInput, mapOfFlashcards, key, logList)
            } catch (e: WrongAnswerException) {
                println("Wrong. The right answer is \"${mapOfFlashcards[key]}\".")
                logList.add("Wrong. The right answer is \"${mapOfFlashcards[key]}\".")
                errorMap[key] = errorMap.getValue(key)+1
            } catch (e: Exception) {
                println(
                    "Wrong. The right answer is \"${mapOfFlashcards[key]}\", but your definition is correct for \"${
                        findKeyToValue(mapOfFlashcards, userInput)
                    }\".")
                logList.add("Wrong. The right answer is \"${mapOfFlashcards[key]}\", but your definition is correct for \"${
                    findKeyToValue(mapOfFlashcards, userInput)
                }\".")
                errorMap[key] = errorMap.getValue(key)+1
            }
        }
    }

    //help method to  -> compareSolutions() - check the Result of the userInput
    private fun checkResult(userInputDef: String, mapOfFlashcards: MutableMap<String, String>, key: String, logList: MutableList<String>) {
        if (mapOfFlashcards[key] != userInputDef) {
            for ((k, _) in mapOfFlashcards) {
                if (mapOfFlashcards[k] == userInputDef) {
                    throw Exception()
                }
            }
            throw WrongAnswerException()
        } else {
            println("Correct!")
            logList.add("Correct!")
        }
    }

    //helping function to get the key - overload
    private fun findKeyToValue(mapOfFlashcards: MutableMap<String, String>, random: Int): String {
        var counter = 0
        var secureValue = ""
        for ((_, v) in mapOfFlashcards) {
            if (counter == random) {
                secureValue = v
            }
            counter++
        }
        for ((key) in mapOfFlashcards) {
            if (mapOfFlashcards[key] == secureValue) return key
        }
        return "This will hopefully never occur."
    }

    //helping function to get the key - overload
    private fun findKeyToValue(mapOfFlashcards: MutableMap<String, String>, value: String): String {
        for ((k, _) in mapOfFlashcards) {
            if (mapOfFlashcards[k] == value) return k
        }
        return "This will hopefully never occur."
    }

    fun saveLogFile(logList: MutableList<String>) {
        println("File name:")
        logList.add("File name:")

        val filename = readln()
        logList.add(filename)
        val file = File(filename)
        file.createNewFile()

        println("The log has been saved.")
        println()
        logList.add("The log has been saved.")
        logList.add("\n")

        for (i in logList.indices) {
            if (logList[i] == "\n") file.appendText(logList[i])
            else file.appendText("${logList[i]}\n")
        }

        logList.clear()
    }

    fun findHardestCard(errorMap: MutableMap<String, Int>, logList: MutableList<String>) {
        var highestValue = 0
        val listOfTermsWithTheMostErrors = mutableListOf<String>()

        for ((_, v) in errorMap) {
            if (v > highestValue) {
                highestValue = v
            }
        }
        for ((k, v) in errorMap) {
            if (v == highestValue && highestValue != 0) {
                listOfTermsWithTheMostErrors.add(k)
            }
        }

        when (listOfTermsWithTheMostErrors.size) {
            0 -> {
                println("There are no cards with errors.")
                println()
                logList.add("There are no cards with errors.")
                logList.add("\n")
            }
            1 -> {
                if (errorMap[listOfTermsWithTheMostErrors[0]] == 1) {
                    println(
                        "The hardest card is \"${listOfTermsWithTheMostErrors[0]}\"." +
                                " You have ${errorMap[listOfTermsWithTheMostErrors[0]]} error answering it.")
                    println()
                    logList.add("The hardest card is \"${listOfTermsWithTheMostErrors[0]}\"." +
                            " You have ${errorMap[listOfTermsWithTheMostErrors[0]]} error answering it.")
                    logList.add("\n")
                }
                else {
                    println(
                        "The hardest card is \"${listOfTermsWithTheMostErrors[0]}\"." +
                                " You have ${errorMap[listOfTermsWithTheMostErrors[0]]} errors answering it.")
                    println()
                    logList.add("The hardest card is \"${listOfTermsWithTheMostErrors[0]}\"." +
                            " You have ${errorMap[listOfTermsWithTheMostErrors[0]]} errors answering it.")
                    logList.add("\n")
                }
            }
            else -> {
                var errorString = "The hardest cards are "
                logList.add("The hardest cards are ")
                repeat(listOfTermsWithTheMostErrors.size) {
                    if (it == listOfTermsWithTheMostErrors.size-1) {
                        errorString += ("\"${listOfTermsWithTheMostErrors[it]}\".")
                    }
                    else errorString += ("\"${listOfTermsWithTheMostErrors[it]}\", ")
                }

                errorString += if (errorMap[listOfTermsWithTheMostErrors[0]] == 1) {
                    (" You have ${errorMap[listOfTermsWithTheMostErrors[0]]} error answering them.")
                } else (" You have ${errorMap[listOfTermsWithTheMostErrors[0]]} errors answering them.")

                println(errorString)
                println()
                logList.add(errorString)
                logList.add("\n")
            }
        }
    }

    fun resetTheErrors(errorMap: MutableMap<String, Int>, logList: MutableList<String>) {
        for ((k,_) in errorMap) {
            errorMap[k] = 0
        }
        println("Card statistics have been reset.")
        println()
        logList.add("Card statistics have been reset.")
        logList.add("\n")
    }
}