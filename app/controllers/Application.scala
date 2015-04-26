package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import com.typesafe.config._

import models._
import client._

object Application extends Controller {
  val SlackConfig = ConfigFactory.load("slack.conf")

  /**
   * /index
   */
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  /**
   * /bot
   */
  def bindRequestForm(): Form[SlackSlashCommandsRequest] = {
    return Form(mapping(
      "token" -> text,
      "team_id" -> text,
      "channel_id" -> text,
      "channel_name" -> text,
      "user_id" -> text,
      "user_name" -> text,
      "text" -> text,
      "trigger_word" -> text
    )(SlackSlashCommandsRequest.apply)(SlackSlashCommandsRequest.unapply))
  }

  def bot = Action { implicit request =>
    val config = new SlackConfiguration(
      botName = SlackConfig.getString("slack.botName"),
      iconEmoji = SlackConfig.getString("slack.iconEmoji"),
      fallbackChannel = SlackConfig.getString("slack.fallbackChannel"),
      incommingUrl = SlackConfig.getString("slack.incommingUrl")
    )
    botQuestValidator(bindRequestForm().bindFromRequest, config, SlackConfig.getString("slack.outboundToken"))
  }

  def botQuestValidator(bindFromRequest: Form[SlackSlashCommandsRequest], config: SlackConfiguration, validToken: String): play.api.mvc.Result = {
    bindFromRequest.fold(
      errors => BadRequest(errors.toString()),
      request => {
        if (request.token != validToken) {
          BadRequest(""); // invalid outbound token
        } else {
          botCommandHandler(request, config)
        }
      }
    )
  }

  def botCommandHandler(request: SlackSlashCommandsRequest, config: SlackConfiguration): play.api.mvc.Result = {
    var channelName: String = null
    if (request.channelName == "privategroup") {
      channelName = config.fallbackChannel
    } else {
      channelName = "#" + request.channelName
    }

    val res = request.trigger_word match {
      case "lgtm" => doCommandLGTM(channelName, request.userName, config); true
      case "wtc" => doCommandWhatTheCommit(channelName, request.userName, config); true
      case _ => false
    }
    if (res) {
      Ok("Roger, " + request.userName)
    } else {
      Ok("Sorry, I don't know the command: [" + request.trigger_word + " " + request.text + "]")
    }
  }

  def doCommandLGTM(channelName: String, userName: String, config: SlackConfiguration) = {
    LGTMClient().fetchResponse((response: LGTMResponse) => {
      var text = s"[${userName}] ${response.getSlackMessage()}"
      SlackClient(config.incommingUrl).
        sendIncomingRequest(channel = channelName, username = config.botName, text = text, iconEmoji = config.iconEmoji)
    })
  }

  def doCommandWhatTheCommit(channelName: String, userName: String, config: SlackConfiguration) = {
    val response = WhatTheCommitClient().fetchResponse()
    var text = s"[${userName}] ${response.getSlackMessage()}"
    SlackClient(config.incommingUrl).
      sendIncomingRequest(channel = channelName, username = config.botName, text = text, iconEmoji = config.iconEmoji)
  }
}
