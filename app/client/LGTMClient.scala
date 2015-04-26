package client

import play.api.Play.current
import play.api.libs.ws.{WS, WSResponse}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import models._

case class LGTMClient() {
  private val LGTMURL = "http://www.lgtm.in/g"

  def fetchResponse(callback: (LGTMResponse) => Unit): Unit = {
    // Get from lgtm.in
    val future: Future[WSResponse] = WS.url(LGTMURL)
      .withHeaders("Content-Type" -> "application/json", "Accept" -> "application/json")
      .withRequestTimeout(1000) // milliseconds
      .get()

    // Response for success
    future.onSuccess {
      case response => {
        callback(LGTMResponse(response.json))
      }
    }

    // Response for failure
    future.onFailure {
      case response => callback(null)
    }
  }
}
