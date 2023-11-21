package module2.homework

import module2.{effectConcurrency, zioConcurrency}
import zio.{Has, Task, ULayer, ZIO, ZLayer, clock, console, random}
import zio.clock.{Clock, sleep}
import zio.console._
import zio.duration.durationInt
import zio.macros.accessible
import zio.random._

import java.io.IOException
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.MILLISECONDS
import scala.io.StdIn
import scala.language.postfixOps

package object zio_homework {
  /**
   * 1.
   * Используя сервисы Random и Console, напишите консольную ZIO программу которая будет предлагать пользователю угадать число от 1 до 3
   * и печатать в когнсоль угадал или нет. Подумайте, на какие наиболее простые эффекты ее можно декомпозировать.
   */
  lazy val guessProgram = {
    for {
      randomized <- random.nextIntBetween(1, 4)
      _ <- ZIO.effect(println("Угадайте число от 1 до 3"))
      result <- ZIO.effect(StdIn.readLine().toInt)
      isSuccessful <- ZIO.effect(result == randomized)
      _ <- ZIO.effect(if (isSuccessful) println("Угадано") else println("Не угадано"))
    } yield result
  }

  /**
   * 2. реализовать функцию doWhile (общего назначения), которая будет выполнять эффект до тех пор, пока его значение в условии не даст true
   *
   */

  def doWhile[R, E, A](z: ZIO[R, E, A])(f: A => Boolean): ZIO[R, E, A] = {
    for {
      x <- z
      _ <- if (f(x)) ZIO.unit else doWhile(z)(f)
    } yield x
  }


  /**
   * 3. Следуйте инструкциям ниже для написания 2-х ZIO программ,
   * обратите внимание на сигнатуры эффектов, которые будут у вас получаться,
   * на изменение этих сигнатур
   */


  /**
   * 3.1 Создайте эффект, который будет возвращать случайеым образом выбранное число от 0 до 10 спустя 1 секунду
   * Используйте сервис zio Random
   */
  lazy val eff: ZIO[Random with Clock, Nothing, Int] = {
    for {
      _ <- ZIO.sleep(zio.duration.Duration.fromMillis(1000))
      r <- random.nextIntBetween(0, 11)
    } yield r
  }

  /**
   * 3.2 Создайте коллукцию из 10 выше описанных эффектов (eff)
   */
  lazy val effects: ZIO[Random with Clock, Nothing, List[Int]] = ZIO.collectAll(List.fill(10)(eff))
  lazy val effectsPar: ZIO[Random with Clock, Nothing, List[Int]] = ZIO.collectAllPar(List.fill(10)(eff))

  
  /**
   * 3.3 Напишите программу которая вычислит сумму элементов коллекци "effects",
   * напечатает ее в консоль и вернет результат, а также залогирует затраченное время на выполнение,
   * можно использовать ф-цию printEffectRunningTime, которую мы разработали на занятиях
   */

  lazy val app = {
    effects.flatMap(list => ZIO.effect {
      val sum = list.sum
      println(sum)
      sum
    })
  }


  /**
   * 3.4 Усовершенствуйте программу 4.3 так, чтобы минимизировать время ее выполнения
   */

  lazy val appSpeedUp = {
    effectsPar.flatMap(list => ZIO.effect {
      val sum = list.sum
      println(sum)
      sum
    })
  }


  /**
   * 4. Оформите ф-цию printEffectRunningTime разработанную на занятиях в отдельный сервис, так чтобы ее
   * молжно было использовать аналогично zio.console.putStrLn например
   */

  def printEffectRunningTime[R, E, A](zio: ZIO[R, E, A]): ZIO[Console with Clock with R, Throwable, Unit] = {
    for {
      startTime <- clock.currentTime(MILLISECONDS)
      _ <- zio.run
      endTime <- clock.currentTime(MILLISECONDS)
      zioEffect <- console.putStrLn((endTime - startTime) + " milliseconds")
    } yield zioEffect
  }


   /**
     * 5.
     * Воспользуйтесь написанным сервисом, чтобы созадть эффект, который будет логировать время выполнения прогаммы из пункта 4.3
     *
     * 
     */

  lazy val appWithTimeLogg = {
    printEffectRunningTime(app)
  }

  lazy val appSpeedUpWithTimeLogg = {
    printEffectRunningTime(appSpeedUp)
  }

  /**
    * 
    * Подготовьте его к запуску и затем запустите воспользовавшись ZioHomeWorkApp
    */

  lazy val runApp = {
    for {
      _ <- appSpeedUpWithTimeLogg
      _ <- appWithTimeLogg
    } yield ()
  }

}
