package models

import org.joda.time.DateTime
import scalikejdbc._

case class Emp(
  id: Long,
  name: String,
  deptId: Option[Long] = None,
  dept: Option[Dept] = None,
  titleId: Option[Long] = None,
  title: Option[Title] = None
)

object Emp extends DAO[Emp] {

  override def construct(e: SyntaxProvider[Emp])(rs: WrappedResultSet): Emp = {
    Emp(rs.long(e.resultName.id), rs.string(e.resultName.name), rs.get(e.resultName.deptId), None, None)
  }

  def constructWithJoins(e: SyntaxProvider[Emp], d: SyntaxProvider[Dept], t: SyntaxProvider[Title])(rs: WrappedResultSet): Emp = {
    construct(e)(rs).copy(
      dept = rs.longOpt(d.resultName.id).flatMap(_ => Some(Dept.construct(d)(rs))),
      title = rs.longOpt(t.resultName.id).flatMap(_ => Some(Title.construct(t)(rs)))
    )
  }

  override val entity: SQLSyntaxSupport[Emp] = Emp
  override val me = Emp.syntax("e")
  override val mappings: (WrappedResultSet => Emp) = constructWithJoins(me, Dept.me, Title.me)

  override val orderByColumns: Seq[scalikejdbc.SQLSyntax] = List(me.name)

  override val joins = List(joinLeft(Dept, me.deptId, Dept.me.id), joinLeft(Title, me.titleId, Title.me.id))

}
