package client

import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._

@RunWith(classOf[JUnitRunner])
class WhatTheCommitClientSpec extends Specification {

  "Application" should {

    "fetch commit message" in {
      val response = WhatTheCommitClient().fetchResponse()
      System.out.println("Response:" + response)
      response.getSlackMessage() must be matching("^<.* http://whatthecommit.com/.+?|.+?> #whatthecommit$")
    }

  }
}
