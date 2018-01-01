package workers

import model._

import scala.io.Source
import java.util.UUID

import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration


case class GetItems(filename: String) {



  private def putItems(inp: Seq[ShopItem]): Unit = {

    val db = Database.forConfig("shopperDb")
    val items = TableQuery[ShopItems]
    items.schema.create
    val newItems: Seq[(String, Int, Int)] = inp.map(i => (i.itemName, i.itemLvl, i.itemPrice))
    val ins_action = items ++= newItems
    Await.result(
      db.run(ins_action),
      Duration.Inf
    )

  }

  private def getItems(line: List[String], acc: Seq[ShopItem])(ins: Seq[ShopItem] => Unit): Unit = line match {
    case Nil => ins(acc)
    case x :: xs => {
      val splittedLine = x.split("\\t").toList
      getItems(xs, acc :+ ShopItem(splittedLine.head, splittedLine.tail.head.toInt, splittedLine.tail.tail.head.toInt))(ins)
    }
  }

  def parse: Unit = {
    val items = Source.fromFile(filename, "iso-8859-1").mkString.replace("\"", "").split("\\r?\\n").toList
    getItems(items, Nil)(putItems)
  }

}
