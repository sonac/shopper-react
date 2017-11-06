package model

import com.outworkers.phantom.connectors.KeySpace
import org.scalatest.FlatSpec
import model.{CraftSkill, CraftSkills}
import model.ShopperDatabase

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}
import com.outworkers.phantom.dsl.context


class DatabaseSpec extends FlatSpec {

  "After insert there" should "inserted item" in {
    implicit val ex = ExecutionContext.Implicits.global
    implicit val session = ShopperDatabase.session
    implicit val keySpace = KeySpace(ShopperDatabase.keyspace.name)

    val skill = CraftSkill("TestSkill")
    ShopperDatabase.create()
    ShopperDatabase.craftSkills.addCraftSkill(skill)
    var res = Seq(skill)
    ShopperDatabase.craftSkills.getAllSkills.onComplete {
      case Success(value) => {
        res = value
      }
      case Failure(e) => {
        res = Seq()
        e.printStackTrace
      }
    }
    assert(res.contains(skill))
  }

}
