package controllers

import javax.inject._

import com.outworkers.phantom.connectors.KeySpace
import model.{CraftSkill, ShopperDatabase}
import play.api._
import play.api.mvc._

import scala.concurrent._
import com.outworkers.phantom.dsl.context
import play.api.libs.json.Json

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

}
