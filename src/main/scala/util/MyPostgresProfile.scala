package util

import com.github.tminglei.slickpg.PgSprayJsonSupport
import com.github.tminglei.slickpg.array.PgArrayJdbcTypes
import game.items.{ActiveEffect, PassiveEffect}
import slick.ast.BaseTypedType
import slick.jdbc.{JdbcType, PostgresProfile}
import spray.json._

trait MyPostgresProfile extends PostgresProfile
  with PgSprayJsonSupport
  with PgArrayJdbcTypes {
  override val pgjson = "jsonb"

  override val api: API = new API {}

  val plainAPI = new API with SprayJsonPlainImplicits

  ///
  trait API extends super.API with JsonImplicits {
    import game.JsonSupport._

    implicit val activeEffectsTypeMapper: JdbcType[List[ActiveEffect]] with BaseTypedType[List[ActiveEffect]] =
      MappedJdbcType.base[List[ActiveEffect], JsValue](_.toJson, _.convertTo[List[ActiveEffect]])

    implicit val passiveEffectsTypeMapper: JdbcType[List[PassiveEffect]] with BaseTypedType[List[PassiveEffect]] =
      MappedJdbcType.base[List[PassiveEffect], JsValue](_.toJson, _.convertTo[List[PassiveEffect]])
  }
}

object MyPostgresProfile extends MyPostgresProfile