package cassandra

import com.datastax.oss.driver.api.core.{CqlIdentifier, CqlSession}
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder

import java.net.InetSocketAddress

object ConnectorCassandra {
  def connect(): CqlSession = {
    val builder = CqlSession.builder()
    builder.addContactPoint(new InetSocketAddress("localhost",9042))
    builder.withLocalDatacenter("datacenter")
    val session = builder.build()
    return session
  }



  def main(args : Array[String]) : Unit =
    val session =  connect();


    session.close()
}
