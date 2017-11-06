package model

import java.util.UUID

import com.outworkers.phantom.dsl._
import play.api.libs.json.Json

import scala.concurrent.Future

case class User(userName: String, id: UUID = UUID.randomUUID)

object User {
  implicit val userReads = Json.reads[User]

  implicit val userWrites = Json.writes[User]

  def fromUser(u: User): User = User(u.userName, u.id)
}

abstract class Users
  extends CassandraTable[Users, User]
    with RootConnector {

  object id extends UUIDColumn(this) with PartitionKey
  object userName extends StringColumn(this)

  def addUser(user: User): Future[ResultSet] = {
    insert.value(_.id, user.id)
      .value(_.userName, user.userName)
      .future()
  }

  def getAllUsers: Future[Seq[User]] = select.fetch()

}