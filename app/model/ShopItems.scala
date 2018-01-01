package model

import slick.jdbc.PostgresProfile.api._


class ShopItems(tag: Tag) extends Table[(String, Int, Int)](tag, Some("shop"), "shop_items") {

  def itemName = column[String]("item_name")
  def itemLvl = column[Int]("item_lvl")
  def itemPrice = column[Int]("item_price")
  def * = (itemName, itemLvl, itemPrice)

}

case class ShopItem(itemName: String, itemLvl: Int, itemPrice: Int)
