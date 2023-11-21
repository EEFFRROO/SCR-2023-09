package module2.homework.zio_homework

import zio.clock.Clock
import zio.console.Console
import zio.random.Random
import zio.{ExitCode, URIO, ZIO}

object ZioHomeWorkApp extends zio.App {
  override def run(args: List[String]): URIO[Console with Clock with Random, ExitCode] = {
    ZIO.effect(
      zio.Runtime.default.unsafeRun(
        module2.homework.zio_homework.runApp
      )
    ).orDie.map(_ => ExitCode.success)
  }
}
