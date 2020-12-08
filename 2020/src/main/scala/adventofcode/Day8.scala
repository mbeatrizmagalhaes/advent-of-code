package adventofcode

class Day8 extends Problem[Int, Int] {
  case class Command(line: String) {
    val (operation, argument) = {
      val Array(o, a) = line.split(" ")
      (o, a.toInt)
    }

    def changeLine(newOp: String) =
      if (newOp == "nop") line.replace("nop", "jmp")
      else line.replace("jmp", "nop")
  }

  def findLoop(
      lines: Array[String],
      acc: Int = 0,
      currIndex: Int = 0,
      visited: Map[Int, Command] = Map.empty
  ): (Int, Map[Int, Command], Boolean) = {

    if (currIndex == lines.size - 1) (acc, visited, true)
    else if (visited.contains(currIndex)) (acc, visited, false)
    else {
      val command = Command(lines(currIndex))
      command.operation match {
        case "nop" => findLoop(lines, acc, currIndex + 1, visited + (currIndex -> command))
        case "acc" => findLoop(lines, acc + command.argument, currIndex + 1, visited + (currIndex -> command))
        case "jmp" => findLoop(lines, acc, currIndex + command.argument, visited + (currIndex -> command))
        case other =>
          println("invalid instruction: " + other)
          (-1, visited, false)
      }
    }
  }

  def solve2(lines: Array[String]): Int = {
    val (_, initialVisited, _) = findLoop(lines)

    def changeInstructions(notChanged: Set[Int]): Int = {
      val index = notChanged.last
      val commandToChange = initialVisited.get(index).get
      commandToChange.operation match {
        case op @ ("nop" | "jmp") =>
          val newLine = commandToChange.changeLine(op)
          val (acc, _, reachedLastOp) = findLoop(lines.updated(index, newLine))
          if (reachedLastOp) acc
          else changeInstructions(notChanged.init)
        case "acc" =>
          changeInstructions(notChanged.init)
      }
    }

    changeInstructions(initialVisited.keySet)
  }

  def solve(input: String) = {
    val lines = input.split("\n")
    val (res1, _, _) = findLoop(lines)
    (res1, solve2(lines))
  }
}
