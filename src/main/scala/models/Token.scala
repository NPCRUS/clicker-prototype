package models

case class Token(
  exp: Long,
  opaque_user_id: String,
  user_id: String,
  channel_id: String,
  role: String,
  is_unlinked: Boolean,
  pubsub_perms: PubSubPerms
)

case class PubSubPerms(
  listen: Option[List[String]],
  send: Option[List[String]]
)
