package model

import com.outworkers.phantom.connectors.{CassandraConnection, ContactPoints}
import com.outworkers.phantom.dsl._

object Defaults {
  val connector = ContactPoints(Seq("localhost")).keySpace("shopper")
}

class ShopperDatabase(val keyspace: CassandraConnection) extends Database[ShopperDatabase](keyspace) {
  object craftSkills extends CraftSkills with Connector
  object words extends Words with Connector
  object items extends Items with Connector
}

object ShopperDatabase extends ShopperDatabase(Defaults.connector)