package controllers

import play.api.mvc._
import models._
import org.json4s._
import play.twirl.api.Html

class Depts extends AppController[Dept] {

  override def dao: DAO[Dept] = Dept

  override val renderList: Option[(String) => Html] = Some(views.html.dept.list.render _)

  override def create = Action(json) { req =>
    val o = req.body.extract[Dept]
    val created = Dept.create(o.name);
    Ok(encodeJson(created))
  }

  override def update(id: Long) = Action(json) { req =>
    Dept.find(id) match {
      case Some(d) => {
        val o = req.body.extract[Dept]
        val updated = Dept.save(d.copy(name = o.name))
        Ok(encodeJson(updated))
      }
      case _ => NotFound
    }
  }
}
