package retrosheet

/**
 * Represents the different types of records in a Retrosheet event file.
 */
sealed class RetrosheetRecord {
    /**
     * Represents the game ID record, which identifies the date, home team, and number of the game.
     * Format: id,<game_id>
     * Example: id,ATL198304080
     */
    data class Id(val gameId: String) : RetrosheetRecord()

    /**
     * Represents the version record, used for file management internal to Retrosheet.
     * Format: version,<version_number>
     * Example: version,1
     */
    data class Version(val version: String) : RetrosheetRecord()

    /**
     * Represents an info record, which contains a single piece of information about the game.
     * Format: info,<type>,<data>
     * Example: info,visteam,SDN
     */
    data class Info(val type: String, val data: String) : RetrosheetRecord()

    /**
     * Represents a start record, which identifies a starting player in the game.
     * Format: start,<player_id>,<player_name>,<team>,<batting_position>,<fielding_position>
     * Example: start,richg001,"Gene Richards",0,1,7
     */
    data class Start(
        val playerId: String,
        val playerName: String,
        val team: Int, // 0 for visiting team, 1 for home team
        val battingPosition: Int, // 1-9, or 0 for pitcher in DH games
        val fieldingPosition: Int // 1-9 for standard positions, 10 for DH
    ) : RetrosheetRecord()

    /**
     * Represents a substitution record, which identifies a player entering the game.
     * Format: sub,<player_id>,<player_name>,<team>,<batting_position>,<fielding_position>
     * Example: sub,kutcr001,"Randy Kutcher",1,5,8
     */
    data class Sub(
        val playerId: String,
        val playerName: String,
        val team: Int, // 0 for visiting team, 1 for home team
        val battingPosition: Int, // 1-9, or 0 for pitcher in DH games
        val fieldingPosition: Int // 1-9 for standard positions, 10 for DH, 11 for PH, 12 for PR
    ) : RetrosheetRecord()

    /**
     * Represents a play record, which describes an event in the game.
     * Format: play,<inning>,<team>,<player_id>,<count>,<pitches>,<event>
     * Example: play,5,1,ramir001,00,,S8.3-H;1-2
     */
    data class Play(
        val inning: Int,
        val team: Int, // 0 for visiting team, 1 for home team
        val playerId: String,
        val count: String, // Ball-strike count, may be "??" if unknown
        val pitches: String, // Pitch sequence, may be empty
        val event: String // Description of the play
    ) : RetrosheetRecord()

    /**
     * Represents a comment record, which provides additional information.
     * Format: com,<comment>
     * Example: com,"ML debut for Behenna"
     */
    data class Com(val comment: String) : RetrosheetRecord()

    /**
     * Represents a data record, which appears after all records from the game.
     * Format: data,<type>,<player_id>,<value>
     * Example: data,er,showe001,2
     */
    data class Data(val type: String, val playerId: String, val value: String) : RetrosheetRecord()

    /**
     * Represents a batter adjustment record, which marks a plate appearance where the batter bats
     * from the side that is not expected.
     * Format: badj,<player_id>,<hand>
     * Example: badj,bonib001,R
     */
    data class BatterAdjustment(val playerId: String, val hand: String) : RetrosheetRecord()

    /**
     * Represents a pitcher adjustment record, which covers the unusual case where a pitcher pitches
     * with the hand opposite the one listed in the roster file.
     * Format: padj,<player_id>,<hand>
     * Example: padj,harrg001,L
     */
    data class PitcherAdjustment(val playerId: String, val hand: String) : RetrosheetRecord()

    /**
     * Represents a lineup adjustment record, which is used when a team bats out of order.
     * Format: ladj,<team>,<batting_position>
     * Example: ladj,0,4
     */
    data class LineupAdjustment(val team: Int, val battingPosition: Int) : RetrosheetRecord()

    /**
     * Represents a runner adjustment record, which is used in games beginning in 2020 in which
     * an extra inning begins with a runner on 2nd.
     * Format: radj,<player_id>,<base>
     * Example: radj,turnj001,2
     */
    data class RunnerAdjustment(val playerId: String, val base: Int) : RetrosheetRecord()

    /**
     * Represents a pitcher responsibility adjustment record, which is used to account for the
     * varied patterns of charging runs to pitchers in innings with more than one pitcher.
     * Format: presadj,<player_id>,<base>
     * Example: presadj,cicoe001,2
     */
    data class PitcherResponsibilityAdjustment(val playerId: String, val base: Int) : RetrosheetRecord()
}

/**
 * Represents a complete game from a Retrosheet event file.
 */
data class RetrosheetGame(
    val id: RetrosheetRecord.Id,
    val version: RetrosheetRecord.Version?,
    val info: List<RetrosheetRecord.Info>,
    val startingLineup: List<RetrosheetRecord.Start>,
    val plays: List<RetrosheetRecord.Play>,
    val records: List<RetrosheetRecord>
) {
    val comments: List<RetrosheetRecord.Com>
        get() = this.records.filterIsInstance<RetrosheetRecord.Com>()

    val data: List<RetrosheetRecord.Data>
        get() = this.records.filterIsInstance<RetrosheetRecord.Data>()
}
