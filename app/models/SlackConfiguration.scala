package models

class SlackConfiguration(val botName: String,
                         val iconEmoji: String,
                         val fallbackChannel: String,
                         val incommingUrl: String) {
}
