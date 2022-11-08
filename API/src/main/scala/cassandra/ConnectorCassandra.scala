package cassandra

import com.datastax.oss.driver.api.core.{CqlIdentifier, CqlSession}
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.querybuilder.QueryBuilder.{bindMarker, selectFrom}
import com.datastax.oss.driver.api.querybuilder.select.Select
import com.datastax.oss.driver.api.core.cql.ResultSet

import java.net.InetAddress
import java.net.InetSocketAddress
import java.util.UUID


object ConnectorCassandra {
  //Create a connexion to localhost cassandra docker server
  def connect(): CqlSession = {
    val builder = CqlSession.builder()
    builder.addContactPoint(new InetSocketAddress("localhost",9042))
    builder.withLocalDatacenter("datacenter1")
    val session = builder.build()
    return session
  }

  //Create a query to get all the articles of a given userId
  def getArticlesByUser(userId: UUID, session : CqlSession): ResultSet ={
    val query = selectFrom("projetRSS","articlebyuser")
      .all()
      .whereColumn("iduser")
      .isEqualTo(bindMarker())

    val  prepareStatement = session.prepare(query.build())
    val resultSet = session.execute(prepareStatement.bind(userId))
    return resultSet
  }



  def main(args : Array[String]) : Unit =
    val session =  connect();

//    val createKeyspace = SchemaBuilder.createKeyspace("projetRSS").ifNotExists.withSimpleStrategy(1)
//    session.execute(createKeyspace.build)

//  val addrOne: InetAddress = InetAddress.getByName("locahost")
//  val addrSocOne: InetSocketAddress = new InetSocketAddress(addrOne, 9042)
//  val sessionOne: CqlSession = CqlSession.builder.addContactPoint(addrSocOne).withLocalDatacenter("datacenter1").withKeyspace("projetRSS").build
//  val result = sessionOne.execute("SELECT ALL FROM user")

//    val select = selectFrom("projetRSS","user").all()
//    val resultSet = session.execute(select.build())

    val resultSet = getArticlesByUser(UUID.fromString("b92c0997-d91b-4005-baa0-4fc1e17082c5"),session)
    resultSet.forEach(row => println(row.getString("name")))

    session.close()
}
