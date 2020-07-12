
case class RawToken(
  token: String
)

case class Token(
  exp: Long,
  opaque_user_id: String,
  channel_id: String,
  role: String,
  is_unlinked: Boolean,
  pubsub_perms: PubSubPerms
)

case class PubSubPerms(
  listen: List[String],
  send: List[String]
)
