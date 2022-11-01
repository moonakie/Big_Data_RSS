package webservice

import io.circe.*
import io.circe.parser.*
import spark.Spark.*
import spark.{Request, Response}

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
            .toInt
        )
        if(tryUserID.isFailure){
          response.status(404)
          s"""{"error": "User not found"}"""
        }
        else{
          val userId = tryUserID.get
          s"""{"message": "$userId"}"""
        }

      }
    )

    get(
      "/articles/:article_id",
      { (request: Request, response: Response) =>
        response.`type`("application/json")

        val articleId = request.params("article_id")

        s"""{"message": "hello $articleId"}"""
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