package module2.homework


import cats.Applicative.ops.toAllApplicativeOps
import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits.catsSyntaxFlatMapOps

import scala.io.StdIn
import scala.language.{higherKinds, postfixOps}
import scala.util.{Random => ScalaRandom}

object catsEffectHomework{

  /**
   * Тайп класс для генерации псевдо случайных чисел
   * @tparam F
   */
  trait Random[F[_]] {
    /***
     *
     * @param min значение от (включительно)
     * @param max значение до (исключается)
     * @return псевдо случайное число в заданном диапазоне
     */
    def nextIntBetween(min: Int, max: Int): F[Int]
  }



  object Random{
    /**
     * 1. реализовать сумонер метод для класса Random, в последствии он должен позволить
     * использовать Random например вот так для IO:
     * Random[IO].nextIntBetween(1, 10)
     *
     * @return Random[F]
     */
    def apply[F[_]](implicit ev: Random[F]): Random[F] = ev


    /**
     * 2. Реализовать инстанс тайп класса для IO
     */
    implicit val ioRandom = new Random[IO] {
      /**   *
       *
       * @param min значение от (включительно)
       * @param max значение до (исключается)
       * @return псевдо случайное число в заданном диапазоне
       */
      override def nextIntBetween(min: Int, max: Int): IO[Int] = IO.delay(ScalaRandom.nextInt(max - min) + min)
    }
  }

  /**
   * Тайп класс для совершения операций с консолью
   * @tparam F
   */
  trait Console[F[_]]{
    def printLine(str: String): F[Unit]
    def readLine(): F[String]
  }

  object Console{
    /**
     * 3. реализовать сумонер метод для класса Console, в последствии он должен позволить
     * использовать Console например вот так для IO:
     * Console[IO].printLine("Hello")
     *
     * @return Console[F]
     */
    def apply[F[_]](implicit ev: Console[F]): Console[F] = ev

    /**
     * 4. Реализовать инстанс тайп класса для IO
     */
    implicit val ioConsole = new Console[IO] {
      override def printLine(str: String): IO[Unit] = IO.delay(println(str))

      override def readLine(): IO[String] = IO.delay(StdIn.readLine())
    }
  }

  /**
   * 5.
   * Используя Random и Console для IO, напишите консольную программу которая будет предлагать пользователю угадать число от 1 до 3
   * и печатать в когнсоль угадал или нет. Программа должна выполняться до тех пор, пока пользователь не угадает.
   * Подумайте, на какие наиболее простые эффекты ее можно декомпозировать.
   */

  val guessProgram: IO[Boolean] = {
    doWhile(
      for {
        randomized <- Random[IO].nextIntBetween(1, 4)
        _ <- Console[IO].printLine("Угадайте число от 1 до 3")
        result <- Console[IO].readLine()
        isSuccessful <- IO.delay(randomized.toString == result)
        _ <- IO.delay(if (isSuccessful) println("Угадано") else {
          println("Не угадано")
        })
      } yield isSuccessful)((isSuccessful: Boolean) => isSuccessful)
  }



  /**
   * 6. реализовать функцию doWhile (общего назначения) для IO, которая будет выполнять эффект до тех пор, пока его значение в условии не даст true
   * Подумайте над сигнатурой, еам нужно принимать эффект и условие относительно его значения, для того чтобы повторять либо заканчивать выполнение.
   */

  def doWhile[A](fa: IO[A])(f: A => Boolean): IO[A] =
    for {
      x <- fa
      _ <- if (f(x)) IO.delay(x) else doWhile(fa)(f)
    } yield x
}

/**
 * 7. Превратите данный объект в исполняемую cats effect программу, которая будет запускать
 * guessProgram
 */
object HomeworkApp extends IOApp {

  import module2.homework.catsEffectHomework.guessProgram
  override def run(args: List[String]): IO[ExitCode] = {
      guessProgram.as(ExitCode.Success)
  }
}