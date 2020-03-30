package model

import model.BeerRepository.BeerId
import model.UserRepository.UserId
import zio.ZIO

object UserService {
  def registerNewFavouriteBeer(
    userId: UserId,
    beerId: BeerId
  ): ZIO[UserRepository with BeerRepository, Error, Either[String, Unit]] = {
    for {
      user <- UserRepository.getUser(userId)
      beer <- BeerRepository.getBeer(beerId)
      saved <- registerBeer(user, beer)
    } yield saved
  }

  private def registerBeer(
    user: Option[UserRepository.User],
    beer: Option[BeerRepository.Beer]
  ) = {
    val maybeSaved = for {
      u <- user.toRight("User missing")
      b <- beer.toRight("Beer missing")
      ub = u.withNewFavouriteBeer(b.id)
    } yield UserRepository.save(ub)
    maybeSaved.fold(e => ZIO.succeed(Left(e)), _.map(Right(_)))
  }
}
