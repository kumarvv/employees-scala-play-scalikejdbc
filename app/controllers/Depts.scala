package controllers

import org.json4s.jackson.Serialization
import play.api.mvc._
import models._
import org.json4s._

class Depts extends AppController {

  def all = Action {
    val json = Serialization.write(Dept.findAll)
    Ok(views.html.dept.render(json));
  }

  def find(id: Long) = Action {
    Ok(encodeJson(Dept.find(id)));
  }

  def create = Action(json) { req =>
    val o = req.body.extract[Dept]
    val created = Dept.create('name -> o.name);
    Ok(encodeJson(created))
  }

  def update(id: Long) = Action(json) { req =>
    Dept.find(id) match {
      case Some(d) => {
        val o = req.body.extract[Dept]
        val updated = Dept.save(id, 'name -> o.name)
        Ok(encodeJson(updated))
      }
      case _ => NotFound
    }
  }

  def delete(id: Long) = Action {
    Dept.find(id) match {
      case Some(d) => Dept.remove(id); Ok
      case _ => NotFound
    }
  }
}
