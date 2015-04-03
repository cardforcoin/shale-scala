package cfc.shale.lib.search

import cfc.shale.core.{ShaleSessionId, NodeId}
import cfc.shale.selenium.SeleniumSessionId

sealed trait SessionPredicate

object SessionPredicate {
  case class HasSessionTag(tag: String) extends SessionPredicate
  case class HasNodeTag(tag: String) extends SessionPredicate
  case object IsReserved extends SessionPredicate
  case class EqSessionId(sessionId: ShaleSessionId) extends SessionPredicate
  case class EqWebDriverId(webDriverId: SeleniumSessionId) extends SessionPredicate
  case object HasWebDriverId extends SessionPredicate
  case class EqNodeId(nodeId: NodeId) extends SessionPredicate
  case class EqBrowserName(browserName: String) extends SessionPredicate
  case class EqCurrentUrl(currentUrl: String) extends SessionPredicate
  case class Not(requirement: SessionPredicate) extends SessionPredicate
  case class And(requirements: Seq[SessionPredicate]) extends SessionPredicate
  case class Or(requirements: Seq[SessionPredicate]) extends SessionPredicate
}
