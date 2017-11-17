package model

import java.util.UUID

import com.outworkers.phantom.dsl._
import play.api.libs.json._

import scala.concurrent.Future

case class Word(word: String, article: String, id: UUID)

object Word {

  implicit val wordReads = Json.reads[Word]
  implicit val wordWrites = Json.writes[Word]

  def fromWrod(w: Word): Word = Word(w.word, w.article, w.id)

}

abstract class Words
  extends CassandraTable[Words, Word]
    with RootConnector {

  object word extends StringColumn(this)
  object article extends StringColumn(this)
  object id extends UUIDColumn(this) with PartitionKey

  def addWord(word: Word): Future[ResultSet] = {
    insert.value(_.id, word.id)
      .value(_.word, word.word)
      .value(_.article, word.article)
      .future()
  }

  def getAllWords: Future[Seq[Word]] = select.fetch()

}
