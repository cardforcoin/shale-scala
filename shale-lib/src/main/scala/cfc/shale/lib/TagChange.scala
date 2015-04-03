package cfc.shale.lib

case class TagChange(
  resource: TagChange.Resource,
  action: TagChange.Action,
  tag: String
)

object TagChange {

  sealed trait Resource
  case object Session extends Resource
  case object Node extends Resource

  sealed trait Action
  case object Add extends Action
  case object Remove extends Action
}
