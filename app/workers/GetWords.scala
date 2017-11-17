package workers

import model.{ShopperDatabase, Word}

import scala.io.Source
import java.util.UUID

case class GetWords(filename: String) {

  def getWord(rawText: List[String]): Unit = rawText match {
    case x :: Nil => println(x + " this is the last word")
    case x :: xs => {
      if (x.startsWith("word") && xs(3).startsWith("definition")) {
        ShopperDatabase.words.addWord(Word(x.substring(5), xs(3).substring(11), UUID.randomUUID()))
        getWord(xs)
      }
      else getWord(xs)
    }
  }

  def parse: Unit = {
    val words = Source.fromFile(filename, "iso-8859-1").mkString.replace("\"", "").split("\\r?\\n|,|\\s+").toList
    getWord(words)
  }

}
