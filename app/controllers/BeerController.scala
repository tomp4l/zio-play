package controllers

import infrastructure.HttpBeerRepository
import javax.inject.{Inject, Singleton}
import model.BeerRepository
import model.BeerRepository.{Beer, BeerId}
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import zio.{Has, Managed, Runtime, ZIO}
import zio.internal.Platform

@Singleton
class BeerController @Inject()
(val controllerComponents: ControllerComponents, val client: WSClient) extends ZioController {

  private val httpBeers = new HttpBeerRepository(client).service

  implicit val runtime = Runtime.default

  def getBeerById(id: String): Action[AnyContent] = Action.zio {
    val beerId = BeerId(id.toInt)
    val beer = BeerRepository.getBeer(beerId)

    val response = beer.fold({
      case BeerRepository.ServiceUnavailable => InternalServerError("Service unavailable")
    }, {
      case Some(Beer(name)) => Ok(name)
      case None => NotFound("")
    })

    response.provideLayer(httpBeers)
  }
}
