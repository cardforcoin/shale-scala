package cfc.shale.http_server

import cfc.shale.core.NodeId

case class Node(
  nodeId: NodeId,
  url: Option[String],
  tags: Set[String]
)
