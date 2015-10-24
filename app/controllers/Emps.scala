package controllers

import models._
import org.json4s.jackson.Serialization
import play.api.mvc._
import play.twirl.api.Html

class Emps extends AppController {

  def all = Action {
    val emps = Serialization.write(Emp.findAll)
    val depts = Serialization.write(Dept.findAll)
    val titles = Serialization.write(Title.findAll)
    Ok(views.html.emp.render(emps, depts, titles));
  }

  def find(id: Long) = Action {
    Ok(encodeJson(Emp.find(id)));
  }

  def create = Action(json) { req =>
    val o = req.body.extract[Emp]
    val created = Emp.create('name -> o.name, 'dept_id -> o.deptId, 'title_id -> o.titleId)
    Ok(encodeJson(created))
  }

  def update(id: Long) = Action(json) { req =>
    Emp.find(id) match {
      case Some(d) => {
        val o = req.body.extract[Emp]
        val updated = Emp.save(id, 'name -> o.name, 'dept_id -> o.deptId, 'title_id -> o.titleId)
        Ok(encodeJson(updated))
      }
      case _ => NotFound
    }
  }

  def delete(id: Long) = Action {
    Emp.find(id) match {
      case Some(d) => Emp.remove(id); Ok
      case _ => NotFound
    }
  }
}
