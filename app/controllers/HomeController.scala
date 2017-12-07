package controllers

import javax.inject._

import com.outworkers.phantom.connectors.KeySpace
import model._
import play.api._
import play.api.mvc._

import scala.concurrent._
import com.outworkers.phantom.dsl.context
import play.api.libs.json.{JsArray, JsValue, Json}
import workers.{Calculator, GetItems, GetWords, RevCalc}

import scala.collection.immutable
import scala.util._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  implicit val ex = ExecutionContext.Implicits.global
  implicit val session = ShopperDatabase.session
  implicit val keySpace = KeySpace(ShopperDatabase.keyspace.name)

  ShopperDatabase.create()

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def getAllSkills = Action.async { implicit request: Request[AnyContent] =>
    val skills = ShopperDatabase.craftSkills.getAllSkills
    skills.map(s => Ok(Json.toJson(s)))
  }

  def saveSkill = Action(parse.json[CraftSkill]) { implicit request =>
    val skill = request.body
    println(skill)
    ShopperDatabase.craftSkills.addCraftSkill(skill)
    Ok
  }

  def getAllWords = Action.async { implicit request =>
    val words = ShopperDatabase.words.getAllWords
    words.map(w => Ok(Json.toJson(w)))
  }

  def getAllItems = Action.async { implicit request =>
    val items = ShopperDatabase.items.getAllItems
    items.map(i => Ok(Json.toJson(i)))
  }

  def calcChances = Action(parse.json) { implicit request =>
    val items: JsValue = request.body
    val vals = (items \ "items").as[Map[String, String]]
    val serItems: Map[String, Future[Seq[Item]]] = vals.mapValues(x => ShopperDatabase.items.getByName(x))
    val x: Future[Iterable[Seq[Item]]] = Future.sequence(serItems.values)
    val calcVals = x.map(it => it.map { s => (s.head.itemName, s.head.itemLvl) -> "Common"}.toMap)
    calcVals.onComplete{
      case Success(s) => println(s)
    }
    val z: Future[Calculator] = calcVals.map(x => Calculator(x))
    z.onComplete{
      case Success(s) => println(s.fusChances)
    }
    Ok
  }

}
