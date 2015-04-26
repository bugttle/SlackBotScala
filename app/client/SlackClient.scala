package client

import play.api.Play.current
import play.api.Logger
import play.api.libs.json.Json
import play.api.libs.ws.WS

import scala.concurrent.ExecutionContext.Implicits.global

case class SlackClient(url:String) {
  // Send to Incoming api
  def sendIncomingRequest(channel: String, username: String, text: String, iconEmoji: String): Unit = {
    val json = Json.obj(
      "channel" -> channel,
      "username" -> username,
      "text" -> text,
      "icon_emoji" -> iconEmoji
    )
    WS.url(url)
      .post(Map("payload" -> Seq(Json.stringify(json))))
      .map { response => Logger.logger.debug("Response:" + response.body)}
  }
}
