package models

import scalikejdbc._

case class Title(id: Long, name: String)

object Title extends DAO[Title] {

  override def construct(e: SyntaxProvider[Title])(rs: WrappedResultSet): Title = {
    Title(rs.long(e.resultName.id), rs.string(e.resultName.name))
  }

  override val me = Title.syntax("t")

  override val entity: scalikejdbc.SQLSyntaxSupport[Title] = Title
}