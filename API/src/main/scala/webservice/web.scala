package webservice

import io.circe._, io.circe.parser._
import spark.Spark.*
import spark.{Request, Response}


object web {
  val serverPort = 8090

  def main(args: Array[String]): Unit = {

    port(serverPort)
    get(
      "/articles",
      { (request: Request, response: Response) =>
        response.`type`("application/json")
        val userID =
          Option(
            request.queryParams("user_id")
          ).getOrElse("-1").toInt
        if(userID < 0){
          response.status(404)
          s"""{"error": "User not found"}"""
        }
        else{
          s"""{[{"article_id": "id1", "title": "some title 1", "pubDate": "2022-02-03"},
             {"article_id": "id2", "title": "some title 2", "pubDate": "2022-02-03"},
             {"article_id": "id3", "title": "some title 3", "pubDate": "2022-02-03"},
             {"article_id": "id4", "title": "some title 4", "pubDate": "2022-02-03"},
             {"article_id": "id5", "title": "some title 5", "pubDate": "2022-02-03"},
             {"article_id": "id6", "title": "some title 6", "pubDate": "2022-02-03"},
             {"article_id": "id7", "title": "some title 7", "pubDate": "2022-02-03"},
             {"article_id": "id8", "title": "some title 8", "pubDate": "2022-02-03"},
             {"article_id": "id9", "title": "some title 9", "pubDate": "2022-02-03"},
             {"article_id": "id10", "title": "some title 10", "pubDate": "2022-02-03"},]}"""
        }

      }
    )

    get(
      "/articles/:article_id",
      { (request: Request, response: Response) =>
        response.`type`("application/json")

        val articleId = request.params("article_id")

        s"""{"article_id": "$articleId", "title": "some title 1", "pubDate": "2022-02-03"}"""
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