import module1.homework6.Monad
import module1.homework6.Monad.MonadFlatMapSyntax
import module1.homework6.Show._


object Main {

  def main(args: Array[String]): Unit = {
    val list: List[Int] = List(1, 2, 3, 4)
    val set: Set[String] = Set("1", "2 3 4")
    println(list.show)
    println(set.show)

    val list1: List[List[Int]] = List(List(1, 2, 3), List(1, 2, 3))
    list1.flatten(Monad[List])
    list.flatMap()(x => List(x + 1, x + 2))(Monad[List])
  }
}

