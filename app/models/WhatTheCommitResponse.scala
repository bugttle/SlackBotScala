package models

case class WhatTheCommitResponse(val url: String, val message: String) {
  def getSlackMessage(): String = {
    return "<" + url + "|" + message + "> #whatthecommit"
  }
}
