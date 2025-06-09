package retrosheet

import java.io.Reader

/**
 * Parser for Retrosheet event files.
 */
class RetrosheetParser {
    /**
     * Parses a Retrosheet event file from a Reader and returns a list of games.
     *
     * @param reader The reader to parse from
     * @return A list of RetrosheetGame objects
     */
    fun parseReader(reader: Reader): List<RetrosheetGame> {
        val games = mutableListOf<RetrosheetGame>()

        var currentGame: MutableList<RetrosheetRecord>? = null

        reader.forEachLine { line ->
            val record = parseLine(line)

            if (record is RetrosheetRecord.Id) {
                // If we encounter a new game ID, start a new game
                currentGame?.let { records ->
                    games.add(buildGame(records))
                }
                currentGame = mutableListOf(record)
            } else currentGame?.add(record)
        }

        // Add the last game if there is one
        currentGame?.let { records ->
            games.add(buildGame(records))
        }

        return games
    }

    /**
     * Builds a RetrosheetGame object from a list of records.
     *
     * @param records The list of records for the game
     * @return A RetrosheetGame object
     */
    private fun buildGame(records: List<RetrosheetRecord>): RetrosheetGame {
        return RetrosheetGame(
            id = records.filterIsInstance<RetrosheetRecord.Id>().first(),
            version = records.filterIsInstance<RetrosheetRecord.Version>().firstOrNull(),
            info = records.filterIsInstance<RetrosheetRecord.Info>(),
            startingLineup = records.filterIsInstance<RetrosheetRecord.Start>(),
            plays = records.filterIsInstance<RetrosheetRecord.Play>(),
            records = records
        )
    }

    /**
     * Parses a line from a Retrosheet event file and returns the appropriate RetrosheetRecord.
     *
     * @param line The line to parse
     * @return A RetrosheetRecord object
     * @throws IllegalArgumentException if the line is not a valid Retrosheet record
     */
    fun parseLine(line: String): RetrosheetRecord {
        val parts = splitCsvLine(line)
        require(parts.isNotEmpty()) { "Empty line" }
        
        return when (parts[0]) {
            "id" -> {
                require(parts.size == 2) { "Invalid id record: $line" }
                RetrosheetRecord.Id(parts[1])
            }
            "version" -> {
                require(parts.size == 2) { "Invalid version record: $line" }
                RetrosheetRecord.Version(parts[1])
            }
            "info" -> {
                require(parts.size == 3) { "Invalid info record: $line" }
                RetrosheetRecord.Info(parts[1], parts[2])
            }
            "start" -> {
                require(parts.size == 6) { "Invalid start record: $line" }
                RetrosheetRecord.Start(
                    playerId = parts[1],
                    playerName = parts[2].trim('"'),
                    team = parts[3].toInt(),
                    battingPosition = parts[4].toInt(),
                    fieldingPosition = parts[5].toInt()
                )
            }
            "sub" -> {
                require(parts.size == 6) { "Invalid sub record: $line" }
                RetrosheetRecord.Sub(
                    playerId = parts[1],
                    playerName = parts[2].trim('"'),
                    team = parts[3].toInt(),
                    battingPosition = parts[4].toInt(),
                    fieldingPosition = parts[5].toInt()
                )
            }
            "play" -> {
                require(parts.size == 7) { "Invalid play record: $line" }
                RetrosheetRecord.Play(
                    inning = parts[1].toInt(),
                    team = parts[2].toInt(),
                    playerId = parts[3],
                    count = parts[4],
                    pitches = parts[5],
                    event = parts[6]
                )
            }
            "com" -> {
                require(parts.size == 2) { "Invalid com record: $line" }
                RetrosheetRecord.Com(parts[1].trim('"'))
            }
            "data" -> {
                require(parts.size == 4) { "Invalid data record: $line" }
                RetrosheetRecord.Data(parts[1], parts[2], parts[3])
            }
            "badj" -> {
                require(parts.size == 3) { "Invalid badj record: $line" }
                RetrosheetRecord.BatterAdjustment(parts[1], parts[2])
            }
            "padj" -> {
                require(parts.size == 3) { "Invalid padj record: $line" }
                RetrosheetRecord.PitcherAdjustment(parts[1], parts[2])
            }
            "ladj" -> {
                require(parts.size == 3) { "Invalid ladj record: $line" }
                RetrosheetRecord.LineupAdjustment(parts[1].toInt(), parts[2].toInt())
            }
            "radj" -> {
                require(parts.size == 3) { "Invalid radj record: $line" }
                RetrosheetRecord.RunnerAdjustment(parts[1], parts[2].toInt())
            }
            "presadj" -> {
                require(parts.size == 3) { "Invalid presadj record: $line" }
                RetrosheetRecord.PitcherResponsibilityAdjustment(parts[1], parts[2].toInt())
            }
            else -> throw IllegalArgumentException("Unknown record type: ${parts[0]}")
        }
    }

    private fun splitCsvLine(line: String): List<String> {
        val result = mutableListOf<String>()
        var current = StringBuilder()
        var inQuotes = false
        
        for (char in line) {
            when {
                char == ',' && !inQuotes -> {
                    result.add(current.toString())
                    current = StringBuilder()
                }
                char == '"' -> {
                    inQuotes = !inQuotes
                    current.append(char)
                }
                else -> {
                    current.append(char)
                }
            }
        }
        
        // Add the last field
        result.add(current.toString())
        
        return result
    }
}
