package workers

import com.outworkers.phantom.connectors.KeySpace
import model._
import java.util.UUID
import scala.util._

import scala.concurrent.{ExecutionContext, Future}

case class RevCalc(desItem: (Item, String)) {

  type FusRes = Map[Seq[((String, Int, String))], Map[(String, String), Double]]

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

  val allCombs: Future[Seq[(String, Int, String)]] = {
    val quals: Seq[String] = rankedQuals.filter(p => p._1 < 5).values.toSeq
    val combs: Future[Seq[(String, Int, String)]] = {
      dummyItems.map(seq => seq.flatMap(it => quals.map(q => (it.itemName, it.itemLvl,  q))))
    }
    combs
  }

  lazy val all2Combs: Future[Stream[Seq[(String, Int, String)]]] = {
    allCombs.map(seq => seq.combinations(2).toStream.filter(comb => {
      comb.exists(p => validQuals.toSeq.contains(p._3)) && comb.exists(p => p._2 == desItem._1.itemLvl)
    }))
  }

  lazy val all3Combs: Future[Seq[Seq[(String, Int, String)]]] = {
    allCombs.map(seq => seq.combinations(3).toSeq.filter(comb => comb.exists(p => validQuals.toSeq.contains(p._3))))
  }

  lazy val all4Combs: Future[Seq[Seq[(String, Int, String)]]] = {
    allCombs.map(seq => seq.combinations(4).toSeq.filter(comb => comb.exists(p => validQuals.toSeq.contains(p._3))))
  }

  lazy val all5Combs: Future[Seq[Seq[(String, Int, String)]]] = {
    allCombs.map(seq => seq.combinations(5).toSeq.filter(comb => comb.exists(p => validQuals.toSeq.contains(p._3))))
  }

  lazy val all2Fuses: Future[Seq[Map[(String, String), Double]]] = {
    all2Combs.map(seq => seq.map(s => Calculator(s).fusChances
      .filter(p => p._1 == ("Dummy Item " + desItem._1.itemLvl.toString, desItem._2))))
  }

  def rankedFuses(combs: Stream[Seq[((String, Int, String))]], topRes: FusRes): FusRes = {
    if (combs.isEmpty) topRes
    else {
      val res: Map[(String, String), Double] = Calculator(combs.head).fusChances
      if (!res.exists(p => p._1 == ("Dummy Item " + desItem._1.itemLvl.toString, desItem._2))) rankedFuses(combs.tail, topRes)
      else {
        val minChance: Double = if (topRes.nonEmpty) topRes.values.map(p => p.values.min).head else 0
        val curChance: Double = res.filter(p => p._1 == ("Dummy Item " + desItem._1.itemLvl.toString, desItem._2)).head._2
        if (minChance >= curChance) rankedFuses(combs.tail, topRes)
        else if (topRes.keys.toSeq.length < 10) {
          rankedFuses(combs.tail, topRes + (combs.head -> res))
        }
        else {
          val restRes = topRes.filter(p => p._2.exists(r => r._2 != minChance))
          rankedFuses(combs.tail, restRes + (combs.head -> res))
        }
      }
    }
  }

  val top2Fuses: Future[FusRes] = all2Combs.map(x => rankedFuses(x, Map()))

}
