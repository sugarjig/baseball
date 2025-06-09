package retrosheet

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RetrosheetParserTest {

    private val parser = RetrosheetParser()

    @Test
    fun testParseIdRecord() {
        val record = parser.parseLine("id,BAL202403280")
        assertTrue(record is RetrosheetRecord.Id)
        assertEquals("BAL202403280", (record as RetrosheetRecord.Id).gameId)
    }

    @Test
    fun testParseVersionRecord() {
        val record = parser.parseLine("version,2")
        assertTrue(record is RetrosheetRecord.Version)
        assertEquals("2", (record as RetrosheetRecord.Version).version)
    }

    @Test
    fun testParseInfoRecord() {
        val record = parser.parseLine("info,visteam,ANA")
        assertTrue(record is RetrosheetRecord.Info)
        assertEquals("visteam", (record as RetrosheetRecord.Info).type)
        assertEquals("ANA", record.data)
    }

    @Test
    fun testParseStartRecord() {
        val record = parser.parseLine("start,renda001,\"Anthony Rendon\",0,1,5")
        assertTrue(record is RetrosheetRecord.Start)
        val start = record as RetrosheetRecord.Start
        assertEquals("renda001", start.playerId)
        assertEquals("Anthony Rendon", start.playerName)
        assertEquals(0, start.team)
        assertEquals(1, start.battingPosition)
        assertEquals(5, start.fieldingPosition)
    }

    @Test
    fun testParseSubRecord() {
        val record = parser.parseLine("sub,kutcr001,\"Randy Kutcher\",1,5,8")
        assertTrue(record is RetrosheetRecord.Sub)
        val sub = record as RetrosheetRecord.Sub
        assertEquals("kutcr001", sub.playerId)
        assertEquals("Randy Kutcher", sub.playerName)
        assertEquals(1, sub.team)
        assertEquals(5, sub.battingPosition)
        assertEquals(8, sub.fieldingPosition)
    }

    @Test
    fun testParsePlayRecord() {
        val record = parser.parseLine("play,1,0,renda001,32,CFBBBT,K")
        assertTrue(record is RetrosheetRecord.Play)
        val play = record as RetrosheetRecord.Play
        assertEquals(1, play.inning)
        assertEquals(0, play.team)
        assertEquals("renda001", play.playerId)
        assertEquals("32", play.count)
        assertEquals("CFBBBT", play.pitches)
        assertEquals("K", play.event)
    }

    @Test
    fun testParseComRecord() {
        val record = parser.parseLine("com,\"ML debut for Behenna\"")
        assertTrue(record is RetrosheetRecord.Com)
        assertEquals("ML debut for Behenna", (record as RetrosheetRecord.Com).comment)
    }

    @Test
    fun testParseDataRecord() {
        val record = parser.parseLine("data,er,showe001,2")
        assertTrue(record is RetrosheetRecord.Data)
        val data = record as RetrosheetRecord.Data
        assertEquals("er", data.type)
        assertEquals("showe001", data.playerId)
        assertEquals("2", data.value)
    }

    @Test
    fun testParseBatterAdjustmentRecord() {
        val record = parser.parseLine("badj,bonib001,R")
        assertTrue(record is RetrosheetRecord.BatterAdjustment)
        val badj = record as RetrosheetRecord.BatterAdjustment
        assertEquals("bonib001", badj.playerId)
        assertEquals("R", badj.hand)
    }

    @Test
    fun testParsePitcherAdjustmentRecord() {
        val record = parser.parseLine("padj,harrg001,L")
        assertTrue(record is RetrosheetRecord.PitcherAdjustment)
        val padj = record as RetrosheetRecord.PitcherAdjustment
        assertEquals("harrg001", padj.playerId)
        assertEquals("L", padj.hand)
    }

    @Test
    fun testParseLineupAdjustmentRecord() {
        val record = parser.parseLine("ladj,0,4")
        assertTrue(record is RetrosheetRecord.LineupAdjustment)
        val ladj = record as RetrosheetRecord.LineupAdjustment
        assertEquals(0, ladj.team)
        assertEquals(4, ladj.battingPosition)
    }

    @Test
    fun testParseRunnerAdjustmentRecord() {
        val record = parser.parseLine("radj,turnj001,2")
        assertTrue(record is RetrosheetRecord.RunnerAdjustment)
        val radj = record as RetrosheetRecord.RunnerAdjustment
        assertEquals("turnj001", radj.playerId)
        assertEquals(2, radj.base)
    }

    @Test
    fun testParsePitcherResponsibilityAdjustmentRecord() {
        val record = parser.parseLine("presadj,cicoe001,2")
        assertTrue(record is RetrosheetRecord.PitcherResponsibilityAdjustment)
        val presadj = record as RetrosheetRecord.PitcherResponsibilityAdjustment
        assertEquals("cicoe001", presadj.playerId)
        assertEquals(2, presadj.base)
    }

    @Test
    fun testParseInvalidRecord() {
        assertThrows(IllegalArgumentException::class.java) {
            parser.parseLine("unknown,field1,field2")
        }
    }

    @Test
    fun testParseReader() {
        val gameData = """
            id,BAL202403280
            version,2
            info,visteam,ANA
            info,hometeam,BAL
            start,renda001,"Anthony Rendon",0,1,5
            start,hicka001,"Aaron Hicks",0,2,9
            start,hendg002,"Gunnar Henderson",1,1,6
            start,rutsa001,"Adley Rutschman",1,2,2
            play,1,0,renda001,32,CFBBBT,K
            play,1,0,hicka001,12,.CBF*S,K.BX1(23)
            com,"Some comment"
            data,er,showe001,2
            id,BAL202403290
            version,2
            info,visteam,ANA
            info,hometeam,BAL
            start,renda001,"Anthony Rendon",0,1,5
            start,hicka001,"Aaron Hicks",0,2,9
            start,hendg002,"Gunnar Henderson",1,1,6
            start,rutsa001,"Adley Rutschman",1,2,2
            play,1,0,renda001,32,CFBBBT,K
            play,1,0,hicka001,12,.CBF*S,K.BX1(23)
            com,"Some comment"
            data,er,showe001,2
        """.trimIndent()

        val games = parser.parseReader(gameData.reader())
        assertEquals(2, games.size)

        val game = games[0]
        assertEquals("BAL202403280", game.id.gameId)
        assertEquals("2", game.version?.version)

        assertEquals(2, game.info.size)
        assertEquals("visteam", game.info[0].type)
        assertEquals("ANA", game.info[0].data)
        assertEquals("hometeam", game.info[1].type)
        assertEquals("BAL", game.info[1].data)

        assertEquals(4, game.startingLineup.size)
        assertEquals("Anthony Rendon", game.startingLineup[0].playerName)
        assertEquals("Aaron Hicks", game.startingLineup[1].playerName)
        assertEquals("Gunnar Henderson", game.startingLineup[2].playerName)
        assertEquals("Adley Rutschman", game.startingLineup[3].playerName)

        val plays = game.plays
        assertEquals(2, plays.size)
        assertEquals("K", plays[0].event)
        assertEquals("K.BX1(23)", plays[1].event)

        val comments = game.comments
        assertEquals(1, comments.size)
        assertEquals("Some comment", comments[0].comment)

        val data = game.data
        assertEquals(1, data.size)
        assertEquals("er", data[0].type)
        assertEquals("showe001", data[0].playerId)
        assertEquals("2", data[0].value)
    }
}
