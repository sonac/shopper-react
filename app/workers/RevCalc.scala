package workers

import com.outworkers.phantom.connectors.KeySpace
import model._
import java.util.UUID
import scala.util._

import scala.concurrent.{ExecutionContext, Future}

case class RevCalc(desItem: (Item, String)) {

  implicit val ex = ExecutionContext.Implicits.global
  implicit val session = ShopperDatabase.session
  implicit val keySpace = KeySpace(ShopperDatabase.keyspace.name)

  val qualCoefs = Map("Common" -> 1,
    "Good" -> 2,
    "Great" -> 3,
    "Flawless" -> 4,
    "Epic" -> 5,
    "Legendary" -> 6
  )

  val rankedQuals: Map[Int, String] = qualCoefs.map{case (k, v) => (v, k)}

  val validQuals: Iterable[String] = {
    val mLvl = qualCoefs(desItem._2) - 1
    rankedQuals.filter{ case (k, v) => k >= mLvl}.values
  }

  val dstnctLvls: Future[Seq[Int]] = ShopperDatabase.items.getAllItems.map(x => x.map(it => it.itemLvl).distinct)

  val dummyItems: Future[Seq[Item]] = {
    val lvls: Future[Seq[Int]] = dstnctLvls.map(x => x.filter(l => desItem._1.itemLvl - 5 < l && desItem._1.itemLvl + 5 > l))
    lvls.flatMap { x =>
      val y: Seq[Future[Item]] = x.map { l =>
        ShopperDatabase.items.getByName("Dummy Item " + l.toString).map(_.head)
      }
      val y1: Future[Seq[Item]] = Future.sequence(y)
      y1
    }

  }

  val allCombs: Future[Seq[((String, Int), String)]] = {
    val quals: Seq[String] = rankedQuals.filter(p => p._1 < 5).values.toSeq
    val combs: Future[Seq[((String, Int), String)]] = {
      dummyItems.map(seq => seq.flatMap(it => quals.map(q => (it.itemName, it.itemLvl) -> q)))
    }
    combs
  }

  lazy val all2Combs: Future[Seq[Seq[((String, Int), String)]]] = {
    allCombs.map(seq => seq.combinations(2).toSeq.filter(comb => {
      comb.exists(p => validQuals.toSeq.contains(p._2)) && comb.exists(p => p._1._2 == desItem._1.itemLvl)
    }))
  }

  lazy val all3Combs: Future[Seq[Seq[((String, Int), String)]]] = {
    allCombs.map(seq => seq.combinations(3).toSeq.filter(comb => comb.exists(p => validQuals.toSeq.contains(p._2))))
  }

  lazy val all4Combs: Future[Seq[Seq[((String, Int), String)]]] = {
    allCombs.map(seq => seq.combinations(4).toSeq.filter(comb => comb.exists(p => validQuals.toSeq.contains(p._2))))
  }

  lazy val all5Combs: Future[Seq[Seq[((String, Int), String)]]] = {
    allCombs.map(seq => seq.combinations(5).toSeq.filter(comb => comb.exists(p => validQuals.toSeq.contains(p._2))))
  }

  lazy val all2Fuses: Future[Seq[Seq[((String, String), Double)]]] = {
    all2Combs.map(seq => seq.map(s => Calculator(s.toMap).fusChances))
  }

}
