package workers

import org.scalatest.FlatSpec

class CalculatorSpec extends FlatSpec {

  val sword = Map(("noj", 2) -> "Great")
  val knife = Map(("mech", 2) -> "Good")
  val club = Map(("bulava", 3) -> "Good")

  val calc = Calculator(sword ++ knife ++ club)

  "Maximum quality" should "be +1 than max quality of inputs" in {
    assert(calc.maxQ == 3)
  }

  "Length of outputs" should "be equal to 3" in {
    assert(calc.outputs.length == 5)
  }

  "NQL" should "contain 2 lvl" in {
    assert(calc.NQL("Epic") == 2)
  }

  "FusChances" should "be cool" in {
    assert(calc.fusChances.foldLeft((0.0, 0.0)){ case ((k, v), (a, b)) => (k, v + b)}._2 == 1.0)
  }

}
