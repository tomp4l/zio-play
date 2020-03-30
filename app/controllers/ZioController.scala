package controllers

import play.api.mvc._
import zio.{Runtime, ZIO}

trait ZioController extends BaseController {

  implicit val runtime: ZioRuntime

  implicit class ZioAction(builder: ActionBuilder[Request, AnyContent]) {
    def zio[A](action: ZIO[A, Throwable, Result])(implicit runtime: Runtime[A]): Action[AnyContent] =
      builder.async({
        runtime.unsafeRun(action.toFuture.uninterruptible)
      })
  }

}