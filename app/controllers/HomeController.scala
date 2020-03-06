package controllers

import javax.inject._
import play.api.mvc._
import zio.{IO, Runtime}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def io = IO.succeed(Ok("zio"))

  val runtime = Runtime.default

  def myFirstZio() = Action.async({
    runtime.unsafeRun(io.toFuture)
  })
}
