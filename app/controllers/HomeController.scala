package controllers

import javax.inject._
import play.api.mvc._
import zio.{IO, Runtime}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends ZioController {

  private val io = IO.succeed(Ok("zio"))

  implicit private val runtime: Runtime[Unit] = Runtime.default

  def myFirstZio(): Action[AnyContent] = Action.zio(io)
}
