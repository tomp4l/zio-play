package model

import java.util.UUID

import model.BeerRepository.{Beer, KnownBeerId}
import model.UserRepository.UserId
import zio.test.Assertion._
import zio.test._
import zio.test.mock.Expectation._

object UserServiceTest extends DefaultRunnableSpec {
  override def spec: ZSpec[zio.test.environment.TestEnvironment, Any] =
    suite("User Service")(
      testM("it loads up the user and beer") {
        val beerId = KnownBeerId(1)
        val userId = UserId(UUID.randomUUID())
        val mockEnv =
          (MockUserRepository.GetUser(equalTo(userId)) returns value(Some(UserRepository.User(userId, "Bob"))))
            .andThen(MockBeerRepository.GetBeer(equalTo(beerId)) returns value(Some(Beer(beerId, "Jim"))))
            .andThen(MockUserRepository.Save(anything) returns value(()))
        val app = for {
          output <- UserService.registerNewFavouriteBeer(userId, beerId)
        } yield assert(output)(isRight(equalTo(())))
        app.provideLayer(mockEnv)
      },
      testM("it handles missing user") {
        val beerId = KnownBeerId(1)
        val userId = UserId(UUID.randomUUID())
        val mockEnv =
          (MockUserRepository.GetUser(equalTo(userId)) returns value(None))
        val app = for {
          output <- UserService.registerNewFavouriteBeer(userId, beerId)
        } yield assert(output)(isLeft(equalTo("User missing")))
        app.provideLayer(BeerRepository.nullService ++ mockEnv)
      },
      testM("it handles missing beer") {
        val beerId = KnownBeerId(1)
        val userId = UserId(UUID.randomUUID())
        val mockEnv =
          (MockUserRepository.GetUser(equalTo(userId)) returns value(Some(UserRepository.User(userId, "Bob"))))
            .andThen(MockBeerRepository.GetBeer(equalTo(beerId)) returns value(None)
            )
        val app = for {
          output <- UserService.registerNewFavouriteBeer(userId, beerId)
        } yield assert(output)(isLeft(equalTo("Beer missing")))
        app.provideLayer(mockEnv)
      }
    )
}
