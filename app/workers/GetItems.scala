package workers

import model.{ShopperDatabase, Item}

import scala.io.Source
import java.util.UUID

case class GetItems(filename: String) {

  private def putItem(item: (String, Int)): Unit = {
    ShopperDatabase.items.addItem(Item(item._1, item._2, UUID.randomUUID))
  }

  private def getItem(line: String): (String, Int) = {
    val splittedLine = line.split("\\t").toList
    (splittedLine.head, splittedLine.tail.head.toInt)
  }

  private def loadItems(items: List[String]): Unit = items match {
    case Nil => throw new NoSuchElementException("You've entered empty file")
    case x :: Nil => putItem(getItem(x))
    case x :: xs => {
      putItem(getItem(x))
      loadItems(xs)
    }
  }

  def parse: Unit = {
    val items = Source.fromFile(filename, "iso-8859-1").mkString.replace("\"", "").split("\\r?\\n").toList
    loadItems(items)
  }

}
