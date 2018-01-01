package workers

import org.scalatest.FlatSpec

class CalculatorSpec extends FlatSpec {

  val sword = Seq(("noj", 2, "Great"))
  val knife = Seq(("mech", 2,  "Good"))
  val club = Seq(("bulava", 3,  "Good"))

  val calc1 = Calculator(Seq(("Dummy Item 41",41, "Good"), ("Dummy Item 41",41, "Flawless")))

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

  "maxlvl for qual" should "contain correct data" in {
    assert(calc1.maxLvlForQual == Map("Good" -> 41, "Flawless" -> 41))
  }

  "NQL for calc1" should "be 41 for everything except common" in {
    assert(calc1.NQL.filter(x => x._1 != "Common").values.head == 41)
  }

  "fus cahnces " should "be almost 1" in {
    assert(calc1.fusChances.foldLeft((0.0, 0.0)){ case ((k, v), (a, b)) => (k, v + b)}._2.round == 1.0)
  }

}
