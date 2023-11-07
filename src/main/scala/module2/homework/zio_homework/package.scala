package module2.homework

import module2.{effectConcurrency, zioConcurrency}
import zio.{Has, Task, ULayer, ZIO, ZLayer}
import zio.clock.{Clock, sleep}
import zio.console._
import zio.duration.durationInt
import zio.macros.accessible

//import scala.util.Random
import zio.random._

import java.io.IOException
import java.util.concurrent.TimeUnit
import scala.io.StdIn
import scala.language.postfixOps

package object zio_homework {
  /**
   * 1.
   * Используя сервисы Random и Console, напишите консольную ZIO программу которая будет предлагать пользователю угадать число от 1 до 3
   * и печатать в когнсоль угадал или нет. Подумайте, на какие наиболее простые эффекты ее можно декомпозировать.
   */



  lazy val guessProgram: Task[Unit] = {
    def guessEffect(rand: Int): ZIO[Any, Throwable, Unit] = ZIO.effect(StdIn.readLine().toInt).flatMap(num => ZIO.effect(
      if (num == rand) {
        println("Угадано")
      } else {
        println(s"Не угадано. Загаданное число $rand")
      }
    ))
    for {
      randomized <- Random.Service.live.nextIntBetween(1, 4)
      result <- ZIO.effect(println("Угадайте число от 1 до 3")) andThen guessEffect(randomized)
    } yield result
  }

  /**
   * 2. реализовать функцию doWhile (общего назначения), которая будет выполнять эффект до тех пор, пока его значение в условии не даст true
   * 
   */

  def doWhile[R, E, A](f: ZIO[R, E, A] => Boolean) = {

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
  lazy val eff = {
    ZIO.sleep(zio.duration.Duration.fromMillis(1000))
    Random.Service.live.nextIntBetween(0, 11)
  }

  /**
   * 3.2 Создайте коллукцию из 10 выше описанных эффектов (eff)
   */
  lazy val effects: ZIO[Any, Nothing, List[Int]] = ZIO.collectAll(List.fill(10)(eff))

  
  /**
   * 3.3 Напишите программу которая вычислит сумму элементов коллекци "effects",
   * напечатает ее в консоль и вернет результат, а также залогирует затраченное время на выполнение,
   * можно использовать ф-цию printEffectRunningTime, которую мы разработали на занятиях
   */

  lazy val app = {
    zioConcurrency.printEffectRunningTime(effects)
  }


  /**
   * 3.4 Усовершенствуйте программу 4.3 так, чтобы минимизировать время ее выполнения
   */

  lazy val appSpeedUp = ???


  /**
   * 4. Оформите ф-цию printEffectRunningTime разработанную на занятиях в отдельный сервис, так чтобы ее
   * молжно было использовать аналогично zio.console.putStrLn например
   */


   /**
     * 5.
     * Воспользуйтесь написанным сервисом, чтобы созадть эффект, который будет логировать время выполнения прогаммы из пункта 4.3
     *
     * 
     */

  lazy val appWithTimeLogg = ???

  /**
    * 
    * Подготовьте его к запуску и затем запустите воспользовавшись ZioHomeWorkApp
    */

  lazy val runApp = ???

}
