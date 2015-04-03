package cfc.shale

import cfc.shale.core.{NodeAddress, NodeId}
import cfc.shale.redis_client.commands.RedisCommand

package object lib {

  def addNode(address: NodeAddress): RedisCommand[NodeId] =
    for {
      nodeId <- RedisCommand(NodeId.random())
      _ <- RedisCommand.gatherUnordered(Seq(
        redis.nodeAddress(nodeId).set(Some(address)),
        redis.nodeIds.add(nodeId)
      ))
    } yield nodeId

  /**
   * Sets the entire node list by address. In other words, adds nodes for
   * addresses that don't already have nodes, and removes any nodes that don't.
   */
  def setAllNodeAddresses(theAddresses: Set[NodeAddress]): RedisCommand[Unit] =
    for (idToAddress <- redis.getNodeIdToAddressMap)
    yield {
      val addressToId = idToAddress.collect({ case (nodeId, Some(url)) => (url, nodeId) })
      val redisAddresses = addressToId.keySet
      RedisCommand.reduceUnordered[Unit, Unit](
        (
          for (address <- (theAddresses -- redisAddresses).toSeq)
          yield addNode(address).map(_ => ())
        ) ++ (
          for (url <- (redisAddresses -- theAddresses).toSeq)
          yield redis.removeNode(addressToId(url)).map(_ => ())
        )
      )
    }
}
