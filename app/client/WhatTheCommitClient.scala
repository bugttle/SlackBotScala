package client

import java.net.URI
import org.jsoup._

import models._

case class WhatTheCommitClient() {
  private val BaseURL = "http://whatthecommit.com"

  def fetchResponse(): WhatTheCommitResponse = {
    var doc = Jsoup.connect(BaseURL).get()
    val pNodes = doc.body().select("div#content > p")
    val message = pNodes.first().text()
    val permalinkPath = pNodes.select("p.permalink > a").attr("href")
    val uri = new URI(BaseURL + "/" + permalinkPath) // permalinkPath includes "/", but normalize manually.

    return WhatTheCommitResponse(uri.normalize().toURL().toString(), message)
  }
}
