package controllers

import java.util.UUID

import javax.inject.{Inject, Singleton}
import model.BeerRepository.UnknownBeerId
import model.UserRepository.{User, UserId}
import model.{BeerRepository, UserRepository, UserService}
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import zio.ZIO

@Singleton
class UserController @Inject()
(val controllerComponents: ControllerComponents)
  (implicit val runtime: ZioRuntime) extends ZioController {

  def getUserById(id: String): Action[AnyContent] = Action.zio {
    val userId = UserId(UUID.fromString(id))
    val user = UserRepository.getUser(userId)

    user.flatMap({
      case Some(User(_, name, None)) =>
        ZIO.succeed(Ok(s"$name has no favourite beer"))
      case Some(User(_, name, Some(beerId))) =>
        val beer = BeerRepository.getKnownBeer(beerId)
        beer.map(b => Ok(s"$name's favourite beer is ${b.name}'"))
      case None => ZIO.succeed(NotFound(""))
    }).fold({
      case model.ServiceUnavailable => InternalServerError("Service unavailable")
    }, identity)
  }

  def newFavouriteBeer(uId: String, bId: String): Action[AnyContent] = Action.zio {
    val beerId = UnknownBeerId(bId.toInt)
    val userId = UserId(UUID.fromString(uId))
    val updated = UserService.registerNewFavouriteBeer(userId, beerId)

    updated.fold({
      case model.ServiceUnavailable => InternalServerError("Service unavailable")
    }, {
      case Right(_) => Ok("")
      case Left(error) => NotFound(error)
    })
  }
}