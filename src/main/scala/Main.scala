import module1.{hof, list, type_system, BallExperiment}


object Main {

  def main(args: Array[String]): Unit = {
    val experiment = new BallExperiment
    println(experiment.run(100000))
    println(experiment.run(100000))
    println(experiment.run(100000))
    println(experiment.run(100000))
    println(experiment.run(100000))
  }
}