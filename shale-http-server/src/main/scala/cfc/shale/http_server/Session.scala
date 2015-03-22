package cfc.shale.http_server

import cfc.shale.core.ShaleSessionId
import cfc.shale.selenium.SeleniumSessionId

case class Session(
  sessionId: ShaleSessionId,
  webdriverId: Option[SeleniumSessionId],
  tags: Set[String],
  reserved: Boolean,
  currentUrl: String,
  browserName: String,
  node: Node
)
