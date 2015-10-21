package models

import org.joda.time.DateTime
import scalikejdbc._

case class Emp(id: Long, name: String, deptId: Option[Long] = None, dept: Option[Dept] = None)

object Emp extends DAO[Emp] {

  override def apply(e: ResultName[Emp])(rs: WrappedResultSet): Emp = new Emp(
    id = rs.get(e.id),
    name = rs.get(e.name),
    deptId = rs.get(e.deptId)
  )

  def apply(e: SyntaxProvider[Emp], d: SyntaxProvider[Dept])(rs: WrappedResultSet): Emp = {
    println("inside join apply")
    apply(e.resultName)(rs).copy(dept = rs.longOpt(d.resultName.id).flatMap { _ =>
      Some(Dept(d)(rs))
    })
  }

  val e = Emp.syntax("e")
  private val d = Dept.syntax("d")

  def find(id: Long)(implicit session: DBSession = autoSession): Option[Emp] = withSQL {
    select.from(Emp as e)
      .leftJoin(Dept as d).on(e.deptId, d.id)
      .where.eq(e.id, id)
  }.map(Emp(e, d)).single.apply()

  def findAll()(implicit session: DBSession = autoSession): List[Emp] = withSQL {
    select.from(Emp as e)
      .leftJoin(Dept as d).on(e.deptId, d.id)
      .orderBy(e.id)
  }.map(Emp(e, d)).list.apply()

  def countAll()(implicit session: DBSession = autoSession): Long = withSQL {
    select(sqls.count).from(Emp as e)
  }.map(rs => rs.long(1)).single.apply().get

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Emp] = withSQL {
    select.from(Emp as e)
      .leftJoin(Dept as d).on(e.deptId, d.id)
      .where.append(sqls"${where}")
      .orderBy(e.id)
  }.map(Emp(e, d)).list.apply()

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = withSQL {
    select(sqls.count).from(Emp as e).where.append(sqls"${where}")
  }.map(_.long(1)).single.apply().get


  override def create(name: String)(implicit session: DBSession): Emp = createWithDept(name, None)

  def createWithDept(name: String, deptId: Option[Long])(implicit session: DBSession = autoSession): Emp = {
    val id = withSQL {
      insert.into(Emp).namedValues(column.name -> name, column.deptId -> deptId)
    }.updateAndReturnGeneratedKey.apply()

    Emp(id = id, name = name)
  }

  def save(m: Emp)(implicit session: DBSession = autoSession): Emp = {
    withSQL {
      update(Emp).set(column.name -> m.name, column.deptId -> m.deptId).where.eq(column.id, m.id)
    }.update.apply()

    m.deptId match {
      case Some(id) => m.copy(dept = Dept.find(id))
      case _ => m
    }
  }

  def destroy(id: Long)(implicit session: DBSession = autoSession): Unit = withSQL {
    delete.from(Emp).where.eq(column.id, id)
  }.update.apply()

}