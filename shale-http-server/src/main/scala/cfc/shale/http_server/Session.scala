package cfc.shale.http_server

import cfc.shale.selenium.SeleniumSessionId
import cfc.shale.sessions.ShaleSessionId

case class Session(
  sessionId: ShaleSessionId,
  webdriverId: Option[SeleniumSessionId],
  tags: Set[String],
  reserved: Boolean,
  currentUrl: String,
  browserName: String,
  node: Node
)
