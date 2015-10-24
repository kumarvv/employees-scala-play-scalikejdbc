package models

import scalikejdbc._

case class Summary(
  emps: Long = 0,
  depts: Long = 0,
  titles: Long = 0,
  empsWithoutDept: Long = 0,
  empsWithoutTitle: Long = 0,
  empsWithoutDeptTitle: Long = 0,
  deptWithoutEmps: Long = 0,
  titleWithoutEmps: Long = 0
)

object Summary {

  def all = {
    Summary(
      emps = Emp.countAll(),
      depts = Dept.countAll(),
      titles = Title.countAll(),

      empsWithoutDept = Emp.countBy(SQLSyntax.isNull(Emp.me.deptId)),
      empsWithoutTitle = Emp.countBy(SQLSyntax.isNull(Emp.me.titleId)),
      empsWithoutDeptTitle = Emp.countBy(SQLSyntax.isNull(Emp.me.deptId).and.isNull(Emp.me.titleId)),

      deptWithoutEmps = Dept.countBy(SQLSyntax.notIn(Dept.me.id, sqls"select distinct dept_id from emp where dept_id is not null")),

      titleWithoutEmps = Title.countBy(SQLSyntax.notIn(Title.me.id, sqls"select distinct title_id from emp where title_id is not null"))
    )
  }

}