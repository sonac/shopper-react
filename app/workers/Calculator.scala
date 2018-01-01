package workers

case class Calculator(inputs: Seq[(String, Int, String)]) {

  private val dummyItem = Map(("dummy", 1) -> "Common")

  private val qualityConst: Map[String, (Double, Double)] = Map("Good" -> (4, 1.25),
                         "Great" -> (3, 1.5),
                         "Flawless" -> (2, 1.75),
                         "Epic" -> (1.5, 2),
                         "Legendary" -> (1, 2.5),
                         "Mythical" -> (0.25, 3))

  private val qualities = Map("Common" -> 0,
                      "Good" -> 1,
                      "Great" -> 2,
                      "Flawless" -> 3,
                      "Epic" -> 4,
                      "Legendary" -> 5,
                      "Mythical" -> 6)

  val maxQ: Int = {
    inputs.map(inp => qualities(inp._3)).max + 1
  }

  val outputs: Seq[((String, Int), Int)] =
    for {
      inp <- inputs
      qual <- qualities(inp._3) + 1 to maxQ
    } yield ((inp._1, inp._2), qual)

  val maxLvlForQual: Map[String, Int] = inputs.groupBy(_._3).mapValues(_.map(_._2).max)

  val NQL: Map[String, Int] = {
    val ordQualities = for {
      (k,v) <- qualities
    } yield (v, k)
    var i = 1
    var tmpNQL = Map("Common" -> 1)
    while (i <= qualities.values.max) {
      val curQual = ordQualities(i)
      val prevQual = ordQualities(i-1)
      tmpNQL = tmpNQL ++ Map(curQual -> maxLvlForQual.get(curQual).getOrElse(tmpNQL(prevQual)))
      i+=1
    }
    tmpNQL
  }

  private def getQual(idx: Int): String = {
    qualities.map{case (k, v) => v -> k}.getOrElse(idx, "none")
  }

  val fusVals: Seq[((String, Int), Double)] = {
    for {
      outp <- outputs
      qual = getQual(outp._2)
    } yield (outp._1._1, outp._2) -> (qualityConst(qual)._1 * NQL(qual))/Math.pow(outp._1._2, qualityConst(qual)._2)
  }

  val fusChances: Map[(String, String), Double] = {
    val total = fusVals.foldLeft((0.0, 0.0)){case ((a, b), (k, v)) => (a, b + v)}._2
    fusVals.map(x => ((x._1._1, getQual(x._1._2)), x._2 / total)).groupBy(_._1).mapValues(_.map(_._2).sum)
  }

}
