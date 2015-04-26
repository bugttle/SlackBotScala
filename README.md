# Installation

# Slack Integrations
* Add "Incoming WebHooks" & "Outgoing WebHooks"
* Set configure file

```
$ cp conf/slack.conf-sample conf/slack.conf
```


# Deploy to Heroku

```
$ heroku login
$ heroku git:remote -add slack-bot
$ git push heroku master
```

# Local server for testing

```
$ sbt run
```
Access to `http://localhost:9000`
