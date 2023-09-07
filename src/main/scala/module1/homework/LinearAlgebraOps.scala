package module1.homework

object LinearAlgebraOps{
  def sum(v1: Array[Int], v2: Array[Int]): Array[Int] = {
    if (v1.length != v2.length) {
      throw new Exception("Operation is not supported")
    }
    var result: Array[Int] = Array()
    for (item <- 0 until v1.length) {
      result :+= v1(item) + v2(item)
    }
    result
  }

  def scale(a: Int, v: Array[Int]): Array[Int] = v.map(x => x * a)

  def axpy(a: Int, v1: Array[Int], v2: Array[Int]) = sum(scale(a, v1), v2)

}