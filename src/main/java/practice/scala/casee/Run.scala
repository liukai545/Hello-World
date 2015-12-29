package practice.scala.casee

/**
 * Created by liukai on 2015/10/14.
 */
object Run {

  def main(args: Array[String]) {
    println(simplifyTop(UnOp("-", UnOp("-", Var("x")))))
  }

  def simplifyTop(expr: Expr): Expr =
    expr match {
      case UnOp("-", UnOp("-", e)) => e
      case BinOp("+", e, Number(0)) => e
      case BinOp("*", e, Number(1)) => e
      case _ => expr
    }
}
