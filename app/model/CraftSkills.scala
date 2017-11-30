package model

import java.util.UUID

import com.outworkers.phantom.dsl._
import play.api.libs.json._

import scala.concurrent.Future

case class CraftSkill(skillName: String, id: UUID = UUID.randomUUID)


object CraftSkill {

  implicit val craftSkillReads = Json.reads[CraftSkill]
  implicit val craftSkillWrites = Json.writes[CraftSkill]

  def fromSkill(e: CraftSkill): CraftSkill = CraftSkill(e.skillName, e.id)

}

abstract class CraftSkills
  extends CassandraTable[CraftSkills, CraftSkill]
    with RootConnector {

  object id extends UUIDColumn(this) with PartitionKey
  object skillName extends StringColumn(this)

  def addCraftSkill(skill: CraftSkill): Future[ResultSet] = {
    insert.value(_.id, skill.id)
      .value(_.skillName, skill.skillName)
      .future()
  }

  def getAllSkills: Future[Seq[CraftSkill]] = select.fetch()

}