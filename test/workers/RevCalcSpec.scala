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

  val revCalc = myItem.map(it => RevCalc(it.head, "Common"))

  "Length of distinct lvls " should " be 54" in {
    revCalc.flatMap(s => s.dstnctLvls.map(x => assert(54 == x.length)))
  }

  "Dummy items " should " contain dummy item" in {
    revCalc.flatMap(s => s.dummyItems.flatMap(itms => {
      dummyItem.flatMap(x => assert(itms.contains(x.head)))
    }))
  }

  "All combos " should " be cool" in {
    revCalc.flatMap(s => s.allCombs.flatMap(seq => assert(seq.contains(("Dummy Item 43", 43) -> "Common"))))
  }


}
