package models

import scalikejdbc._

case class Dept(id: Long, name: String)

object Dept extends DAO[Dept] {

  override def tableName: String = "dept"

  def apply(d: ResultName[Dept])(rs: WrappedResultSet): Dept = new Dept(
    id = rs.get(d.id),
    name = rs.get(d.name)
  )

  def opt(d: SyntaxProvider[Dept])(rs: WrappedResultSet): Option[Dept] = rs.longOpt(d.resultName.id).map(_ => apply(d.resultName)(rs))

  val d = Dept.syntax("d")

  def find(id: Long)(implicit session: DBSession = autoSession): Option[Dept] = withSQL {
    select.from(Dept as d).where.eq(d.id, id)
  }.map(Dept(d)).single.apply()

  def findAll()(implicit session: DBSession = autoSession): List[Dept] = withSQL {
    select.from(Dept as d)
      .orderBy(d.id)
  }.map(Dept(d)).list.apply()

  def countAll()(implicit session: DBSession = autoSession): Long = withSQL {
    select(sqls.count).from(Dept as d)
  }.map(rs => rs.long(1)).single.apply().get

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Dept] = withSQL {
    select.from(Dept as d)
      .where.append(sqls"${where}")
      .orderBy(d.id)
  }.map(Dept(d)).list.apply()

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = withSQL {
    select(sqls.count).from(Dept as d).where.append(sqls"${where}")
  }.map(_.long(1)).single.apply().get

  def create(name: String)(implicit session: DBSession = autoSession): Dept = {
    val id = withSQL {
      insert.into(Dept).namedValues(column.name -> name)
    }.updateAndReturnGeneratedKey.apply()

    Dept(id = id, name = name)
  }

  def save(m: Dept)(implicit session: DBSession = autoSession): Dept = {
    withSQL {
      update(Dept).set(column.name -> m.name).where.eq(column.id, m.id)
    }.update.apply()
    m
  }

  def destroy(id: Long)(implicit session: DBSession = autoSession): Unit = withSQL {
    delete.from(Dept).where.eq(column.id, id)
  }.update.apply()

}