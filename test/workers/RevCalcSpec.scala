package workers

import com.outworkers.phantom.connectors.KeySpace
import model.{Item, ShopperDatabase}
import org.eclipse.jetty.util.SocketAddressResolver.Async
import org.scalatest.{AsyncFlatSpec, FlatSpec}

import scala.util._
import scala.concurrent.{ExecutionContext, Future}

class RevCalcSpec extends AsyncFlatSpec {

  implicit val ex = ExecutionContext.Implicits.global
  implicit val session = ShopperDatabase.session
  implicit val keySpace = KeySpace(ShopperDatabase.keyspace.name)

  val myItem = ShopperDatabase.items.getByName("Traitorous Blade")

  val dummyItem = ShopperDatabase.items.getByName("Dummy Item 43")

  val revCalc = myItem.map(it => RevCalc(it.head, "Epic"))

  "Length of distinct lvls " should " be 54" in {
    revCalc.flatMap(s => s.dstnctLvls.map(x => assert(54 == x.length)))
  }

  "Dummy items " should " contain dummy item" in {
    revCalc.flatMap(s => s.dummyItems.flatMap(itms => {
      dummyItem.flatMap(x => assert(itms.contains(x.head)))
    }))
  }

  "All combos " should " contain dummy item" in {
    revCalc.flatMap(s => s.allCombs.flatMap(seq => assert(seq.contains(("Dummy Item 43", 43, "Common")))))
  }

  "All combos " should " have length 36" in {
    revCalc.flatMap(s => s.allCombs.flatMap(seq => assert(seq.length == 36)))
  }

  "All 2 combos " should " have length 279" in {
    revCalc.flatMap(s => s.all2Combs.flatMap(seq => assert(seq.length == 59)))
  }

  "All 3 combos " should " have length 4215" in {
    revCalc.flatMap(s => s.all3Combs.flatMap(seq => assert(seq.length == 4215)))
  }

  "All 4 combos " should " have length 41355" in {
    revCalc.flatMap(s => s.all4Combs.flatMap(seq => assert(seq.length == 41355)))
  }

  "All 5 combos " should " have length 296262" in {
    revCalc.flatMap(s => s.all5Combs.flatMap(seq => assert(seq.length == 296262)))
  }

  "Check that 2 fuses " should " return smth" in {
    revCalc.flatMap(s => s.all2Fuses.flatMap(seq => assert(seq.length == 59)))
  }

  "Top fuses " should " be not empty" in {
    revCalc.flatMap(s => s.top2Fuses.flatMap(seq => assert(seq.isEmpty)))
  }


}
