package webservice

import cassandra.ConnectorCassandra
import io.circe.*
import io.circe.parser.*
import spark.Spark.*
import spark.{Request, Response}

import java.util.UUID
import scala.util.Try


object web {
  val serverPort = 8090

  def main(args: Array[String]): Unit = {

    port(serverPort)
    get(
      "/articles",
      { (request: Request, response: Response) =>
        response.`type`("application/json")
        val tryUserID = Try(
          request.queryParams("user_id")
        )
        if(tryUserID.isFailure){
          response.status(404)
          s"""{"error": "User not found"}"""
        }
        else{
          val userId = tryUserID.get
          val session =  ConnectorCassandra.connect()
          ConnectorCassandra.get10Article(UUID.fromString(userId),session)
        }

      }
    )

    get(
      "/articles/:article_id",
      { (request: Request, response: Response) =>
        response.`type`("application/json")
        val tryActicleId = Try (
        request.params("article_id")
        )
        if(tryActicleId.isFailure){
          response.status(404)
          s"""{"error": "User not found"}"""
        }
        else {
          val articleId = tryActicleId.get
          val session = ConnectorCassandra.connect()
          ConnectorCassandra.getArticleById(UUID.fromString(articleId), session)
        }
      }
    )

    post("/articles",
      { (request: Request, response: Response) =>
        val test = parse(request.body()).getOrElse(Json.Null)
        val articleArray = test.as[List[Map[String,String]]]
        response.`type`("application/json")
        if(articleArray.isLeft){
          response.status(404)
          s"""{"error": "Article not found"}"""
        }
        else{
          s"""{"message": "hello"}"""
        }
      }
    )
  }}