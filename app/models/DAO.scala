package models

import scalikejdbc._

trait DAO[T] extends SQLSyntaxSupport[T] {

  def apply(d: SyntaxProvider[T])(rs: WrappedResultSet): T = apply(d.resultName)(rs)
  def apply(d: ResultName[T])(rs: WrappedResultSet): T

  def find(id: Long)(implicit session: DBSession = autoSession): Option[T] 

  def findAll()(implicit session: DBSession = autoSession): List[T] 

  def countAll()(implicit session: DBSession = autoSession): Long

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[T]

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long

  def create(name: String)(implicit session: DBSession = autoSession): T

  def save(m: T)(implicit session: DBSession = autoSession): T

  def destroy(id: Long)(implicit session: DBSession = autoSession): Unit

}