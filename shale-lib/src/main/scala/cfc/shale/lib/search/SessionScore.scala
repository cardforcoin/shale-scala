package cfc.shale.lib.search

case class SessionScore(
  weight: BigDecimal,
  requirement: SessionPredicate
)
