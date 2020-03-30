package controllers

import javax.inject.{Inject, Singleton}
import model.BeerRepository
import model.BeerRepository.{Beer, UnknownBeerId}
import play.api.mvc.{Action, AnyContent, ControllerComponents}

@Singleton
class BeerController @Inject()
(val controllerComponents: ControllerComponents)
  (implicit val runtime: ZioRuntime) extends ZioController {

  def getBeerById(id: String): Action[AnyContent] = Action.zio {
    val beerId = UnknownBeerId(id.toInt)
    val beer = BeerRepository.getBeer(beerId)

    beer.fold({
      case model.ServiceUnavailable => InternalServerError("Service unavailable")
    }, {
      case Some(Beer(_, name)) => Ok(name)
      case None => NotFound("")
    })
  }
}
