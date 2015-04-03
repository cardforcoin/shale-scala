package cfc.shale.lib.search

import cfc.shale.core.ShaleSessionId
import cfc.shale.redis.{Node, Session}

case class SessionMatchTarget(
  sessionId: ShaleSessionId,
  session: Session,
  node: Node
) {

  def matches(predicate: SessionPredicate): Boolean = {
    import SessionPredicate._
    predicate match {
      case HasSessionTag(x) => session.tags contains x
      case HasNodeTag(x) => node.tags contains x
      case EqSessionId(x) => sessionId == x
      case EqWebDriverId(x) => session.webDriverId == Some(x)
      case HasWebDriverId => session.webDriverId.isDefined
      case EqNodeId(x) => session.nodeId == Some(x)
      case IsReserved => session.reserved.isDefined
      case EqBrowserName(x) => session.browserName == Some(x)
      case EqCurrentUrl(x) => session.currentUrl == Some(x)
      case Not(x) => !matches(x)
      case And(xs) => xs forall matches
      case Or(xs) => xs exists matches
    }
  }
}
