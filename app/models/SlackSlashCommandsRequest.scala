package models

case class SlackSlashCommandsRequest(val token: String,
                                     val teamID: String,
                                     val channelID: String,
                                     val channelName: String,
                                     val userID: String,
                                     val userName: String,
                                     val text: String,
                                     val trigger_word: String) {
}
