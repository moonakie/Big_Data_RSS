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
import java.util.stream.Collectors

object ConnectorCassandra {
  //Create a connexion to localhost cassandra docker server
  def connect(): CqlSession = {
    val builder = CqlSession.builder()
    builder.addContactPoint(new InetSocketAddress("localhost",9042))
    builder.withLocalDatacenter("datacenter1")
    val session = builder.build()
    return session
  }

  //Get all the articles from a given userId
  def getArticlesIdByUserId(userId: UUID, session : CqlSession): ResultSet ={
    val query = selectFrom("projetRSS","articlebyuser")
      .all()
      .whereColumn("iduser")
      .isEqualTo(bindMarker())
      .limit(10)

    val  prepareStatement = session.prepare(query.build())
    val resultSet = session.execute(prepareStatement.bind(userId))
    return resultSet
  }

  //Get an article from a given id
  def getArticleById(articleId: UUID, session: CqlSession): String = {
    val query = selectFrom("projetRSS", "article")
      .all()
      .whereColumn("id")
      .isEqualTo(bindMarker())

    val prepareStatement = session.prepare(query.build())
    val resultSet = session.execute(prepareStatement.bind(articleId))
    val row = resultSet.one()
    val article_id = row.getUuid("id")
    val title = row.getString("title")
    val pubDate = row.getString("pubDate")
    val link = row.getString("link")
    val description = row.getString("description")
    s"""{"article_id": "$article_id", "title": "$title", "pubDate": "$pubDate", "link": "$link", "description": "$description"}"""

  }

  def getArticleSummaryById(articleId: UUID, session: CqlSession): String = {
    val query = selectFrom("projetRSS", "article")
      .all()
      .whereColumn("id")
      .isEqualTo(bindMarker())

    val prepareStatement = session.prepare(query.build())
    val resultSet = session.execute(prepareStatement.bind(articleId))
    val row = resultSet.one()
    val article_id = row.getUuid("id")
    val title = row.getString("title")
    val pubDate = row.getString("pubDate")
    val link = row.getString("link")
    s"""{"article_id": "$article_id", "title": "$title", "pubDate": "$pubDate"}"""

  }

  //Get the 10 last articles
  def get10Article(userId: UUID, session: CqlSession): String = {
    val resultSet = getArticlesIdByUserId(userId, session)
    resultSet.map{row =>
      getArticleById(row.getUuid("idarticle"),session)
    }.all().stream().collect(Collectors.joining(", ", "{[", "]}"))

  }

  def saveArticle(map : Map[String, String], session: CqlSession): Unit = {
    //not implement yet, kafka do this job
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


    session.close()
}
