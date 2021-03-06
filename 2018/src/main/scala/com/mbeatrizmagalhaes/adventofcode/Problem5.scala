package com.mbeatrizmagalhaes.adventofcode

class Problem5 extends Problems[Int, Int] {
  def reactPolymer(polymer: String): String= {
    def removeOpposites(remaining: String, acc: String): String = {
      if (remaining.length <= 1) acc + remaining
      else {
        val a = remaining.charAt(0)
        val b = remaining.charAt(1)
        if (a != b && a.toLower == b.toLower) removeOpposites(remaining.drop(2), acc)
        else removeOpposites(remaining.drop(1), acc + a)
      }
    }

    def removeOppositesCycle(initial: String, afterRemove: String): String =
      if (initial.length == afterRemove.length) afterRemove
      else removeOppositesCycle(afterRemove, removeOpposites(afterRemove, ""))

    removeOppositesCycle("", polymer)
  }

  override def solve(lines: List[String]) = {
    val polymer = lines.head
    val distinct = polymer.toLowerCase.distinct

    val withoutUnitsSizes = distinct.map { c =>
      val withoutUnits = polymer.replace(c.toString, "").replace(c.toUpper.toString, "")
      reactPolymer(withoutUnits).length
    }

    (reactPolymer(polymer).length, withoutUnitsSizes.min)
  }
}