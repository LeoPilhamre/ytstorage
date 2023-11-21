package ytstorage.utils

trait Pair[T]:
    def x: T
    def y: T
    def tuple: (T, T) = x -> y


case class Dim(width: Int, height: Int) extends Pair[Int]:
    def x: Int = width
    def y: Int = height

    def isInside(p: Pair[Int]): Boolean =
        p.x >= 0 && p.y >= 0 &&
        p.x <= width && p.y <= height

    override def toString: String = s"Dim: { Width: $width, Height: $height }"

object Dim:
    def apply(dim: (Int, Int)) = new Dim(dim._1, dim._2)

case class Pos(x: Int, y: Int) extends Pair[Int]:
    def +(p: Pair[Int]): Pos = copy(x + p.x, y + p.y)
    def -(p: Pair[Int]): Pos = copy(x - p.x, y - p.y)
    def *(p: Pair[Int]): Pos = copy((x * p.x).toInt, (y * p.y).toInt)
    def /(p: Pair[Int]): Pos =
        require(p.x != 0 && p.y != 0, s"Divide By Zero Exception while dividing $toString & ${p.toString}")
        copy((x / p.x.toInt), (y / p.y).toInt)
    
    override def toString: String = s"Pos: ($x, $y)"

object Pos:
    def apply(pos: (Int, Int)) = new Pos(pos._1, pos._2)