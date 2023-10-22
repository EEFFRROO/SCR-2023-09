package module1.homework.futures

import module1.homework.futures.HomeworksUtils.TaskSyntax

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.{Failure, Success}

object task_futures_sequence {

  /**
   * В данном задании Вам предлагается реализовать функцию fullSequence,
   * похожую на Future.sequence, но в отличии от нее,
   * возвращающую все успешные и не успешные результаты.
   * Возвращаемое тип функции - кортеж из двух списков,
   * в левом хранятся результаты успешных выполнений,
   * в правово результаты неуспешных выполнений.
   * Не допускается использование методов объекта Await и мутабельных переменных var
   */
  /**
   * @param futures список асинхронных задач
   * @return асинхронную задачу с кортежом из двух списков
   */
  def fullSequence[A](futures: List[Future[A]])
                     (implicit ex: ExecutionContext): Future[(List[A], List[Throwable])] = {

    val promise = Promise[(List[A], List[Throwable])]()

    def loop(list: List[Future[A]], result: (List[A], List[Throwable])): Unit =
      list match {
        case ::(head, tail) => head.onComplete {
          case Success(v) => loop(tail, (result._1 :+ v, result._2))
          case Failure(e) => loop(tail, (result._1, result._2 :+ e))
        }
        case Nil => promise.success(result)
      }

    loop(futures, (List.empty[A], List.empty[Throwable]))

    promise.future

//    sequence match {
//      case future: Future[(List[A], List[Throwable])] => future
//      case _: Unit => Future((List.empty[A], List.empty[Throwable]))
//    }
  }
}
