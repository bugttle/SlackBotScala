package models

import play.api.libs.json.JsValue

case class LGTMResponse(val json: JsValue) {

  def getSlackMessage(): String = {
    return (json \ "imageUrl").as[String]
  }
}
