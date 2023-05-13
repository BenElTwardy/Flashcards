package flashcards


class Flashcard {

    private var whileLoopBool: Boolean = true
    private val mapOfFlashcards = mutableMapOf<String, String>()
    private val checkUserInput = CheckUserInput()
    private val logList = mutableListOf<String>()
    private val errorMap = mutableMapOf<String, Int>()
    private var exportFileIndex = 0
    //if the bool is true, a exportFile will be generated at the end of the program
    private var exportBool = false

    fun flashcardLoop(args: Array<String>) {
        while (whileLoopBool) {
            println("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):")
            logList.add("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):")

            val userInput = readln()
            logList.add(userInput)

            when (userInput) {
                "add" -> checkUserInput.addCard(mapOfFlashcards, logList, errorMap)
                "remove" -> checkUserInput.removeCard(mapOfFlashcards, logList, errorMap)
                "import" -> checkUserInput.importFlashcards(mapOfFlashcards, errorMap, logList)
                "export" -> checkUserInput.exportFlashcards(mapOfFlashcards, errorMap, logList)
                "ask" -> checkUserInput.askFlashcards(mapOfFlashcards, errorMap, logList)
                "log" -> checkUserInput.saveLogFile(logList)
                "hardest card" -> checkUserInput.findHardestCard(errorMap, logList)
                "reset stats" -> checkUserInput.resetTheErrors(errorMap, logList)
                "exit" -> {
                    println("Bye bye!")
                    if (exportBool) {
                        checkUserInput.createNewFileAndExport(args[exportFileIndex], mapOfFlashcards, errorMap)
                    }
                    whileLoopBool = false
                }
            }
        }
    }

    fun checkArguments(args: Array<String>) {
        if (args.isNotEmpty()) {
            for (i in args.indices) {
                if (args[i] == "-import") {
                    try {
                        checkUserInput.findTheFileAndImportFlashcards(args[i+1], mapOfFlashcards, errorMap)
                    }
                    catch (e: Exception){
                        println("File couldn't be imported.")
                    }
                }
                else if (args[i] == "-export") {
                    try {
                        exportFileIndex = i+1
                        exportBool = true
                    }
                    catch (exception: Exception) {
                        println("Couldn't export the file.")
                    }
                }
            }
        }
        else return
    }
}