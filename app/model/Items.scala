package model

import java.util.UUID

import com.outworkers.phantom.dsl._
import play.api.libs.json._

import scala.concurrent.Future

case class Item(itemName: String, itemLvl: Int, id: UUID)

object Item {

  implicit val itemReads = Json.reads[Item]
  implicit val itemWrites = Json.writes[Item]

  def fromItem(i: Item): Item = Item(i.itemName, i.itemLvl, i.id)

}

abstract class Items
  extends CassandraTable[Items, Item]
    with RootConnector {

  object itemName extends StringColumn(this)
  object itemLvl extends IntColumn(this)
  object id extends UUIDColumn(this) with PartitionKey

  def addItem(item: Item): Future[ResultSet] = {
    insert.value(_.id, item.id)
      .value(_.itemName, item.itemName)
      .value(_.itemLvl, item.itemLvl)
      .future()
  }

  def getAllItems: Future[Seq[Item]] = select.fetch()

}
