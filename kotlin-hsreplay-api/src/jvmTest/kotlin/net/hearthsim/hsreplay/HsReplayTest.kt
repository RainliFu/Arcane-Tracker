package net.hearthsim.hsreplay

import kotlinx.coroutines.GlobalScope
import net.hearthsim.console.Console
import net.hearthsim.hsreplay.model.legacy.HSPlayer
import net.hearthsim.hsreplay.model.legacy.UploadRequest
import kotlinx.coroutines.*
import org.junit.Test
import java.io.File

class HsReplayTest {
    val console = object : Console {
        override fun debug(message: String) {
            println(message)
        }

        override fun error(message: String) {
            println(message)
        }

        override fun error(throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

    val preferences = object : Preferences {
        override fun putString(key: String, value: String?) {

        }

        override fun getString(key: String): String? {
            return when (key) {
                "HSREPLAY_TOKEN" -> "4d9dab2e-5602-4efc-9a1f-314a2a47e375"
                else -> null
            }
        }

        override fun putBoolean(key: String, value: Boolean?) {
        }

        override fun getBoolean(key: String): Boolean? {
            return null
        }

    }

    @Test
    fun testUpload() {
        val hsReplay = HsReplay(userAgent = "net.mbonnin.arcanetracker/4.13; Android 9;",
                console = console,
                preferences = preferences)

        val uploadRequest = UploadRequest(
                match_start = "2019-06-30T12:00:00Z",
                spectator_mode = false,
                game_type = 2, // ranked_standard
                format = 2, // standard
                build = 31353,
                friendly_player = "2",
                player1 = HSPlayer(
                        deck_id = null,
                        rank = 5,
                        deck = emptyList()
                ),
                player2 = HSPlayer(
                        deck_id = null,
                        rank = 5,
                        deck = emptyList()
                )
        )

        val dir = System.getProperty("user.dir")

        val text = File(dir, "src/jvmTest/files/power.log").readText()
        runBlocking {
            hsReplay.uploadGame(uploadRequest, text)
        }

    }
}