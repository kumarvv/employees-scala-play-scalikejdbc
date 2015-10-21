package controllers

import models._
import org.json4s.jackson.Serialization
import play.api.mvc._
import play.twirl.api.Html

class Emps extends AppController[Emp] {

  override def dao: DAO[Emp] = Emp

  override val renderList: Option[(String) => Html] = None

  override def all = Action {
    val emps = Serialization.write(dao.findAll)
    val depts = Serialization.write(Dept.findAll)
    Ok(views.html.emp.list.render(emps, depts));
  }

  override def create = Action(json) { req =>
    val o = req.body.extract[Emp]
    val created = Emp.createWithDept(o.name, o.deptId);
    Ok(encodeJson(created))
  }

  override def update(id: Long) = Action(json) { req =>
    Emp.find(id) match {
      case Some(e) => {
        val o = req.body.extract[Emp]
        val updated = Emp.save(e.copy(name = o.name, deptId = o.deptId))
        Ok(encodeJson(updated))
      }
      case _ => NotFound
    }
  }
}
