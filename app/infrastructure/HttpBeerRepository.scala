package infrastructure

import model.BeerRepository._
import model.{BeerRepository, ServiceUnavailable}
import play.api.libs.ws.{WSClient, WSResponse}
import zio.{IO, ZIO, ZLayer}

class HttpBeerRepository(client: WSClient) {

  val service: ZLayer[Any, Nothing, BeerRepository] = ZLayer.succeed(
    new Service {
      private val baseUrl = "https://api.punkapi.com/v2/"

      override def getBeer(beerId: BeerId): IO[model.Error, Option[Beer]] =
        ZIO.fromFuture(_ => client.url(s"${baseUrl}beers/${beerId.id}").get())
          .map(response =>
            response.status match {
              case 404 => Some(None)
              case 200 => Some(processBody(response))
              case _ => None
            }
          )
          .mapError(_ => ServiceUnavailable)
          .someOrFail(ServiceUnavailable)

      override def getBeers: IO[model.Error, Seq[Beer]] =
        ZIO.fromFuture(_ => client.url(baseUrl + "beers").get())
          .map(response => Seq())
          .mapError(t => ServiceUnavailable)

      override def getKnownBeer(beerId: KnownBeerId): ZIO[Any, model.Error, Beer] =
        getBeer(beerId).flatMap({
          case Some(beer) => ZIO.succeed(beer)
          case None => ZIO.fail(ServiceUnavailable)
        })
    }
  )


  private def processBody(response: WSResponse) = {
    val json = response.json
    val beer = json \ 0
    for {
      name <- (beer \ "name").asOpt[String]
      id <- (beer \ "id").asOpt[Int]
    } yield Beer(KnownBeerId(id), name)
  }
}
