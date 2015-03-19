package cfc.shale

import java.util.UUID

package object nodes {

  def generateNodeId(): NodeId = NodeId(UUID.randomUUID().toString)
}
