package cfc.shale

import java.util.UUID

import cfc.shale.core.NodeId

package object nodes {

  def generateNodeId(): NodeId = NodeId(UUID.randomUUID().toString)
}
