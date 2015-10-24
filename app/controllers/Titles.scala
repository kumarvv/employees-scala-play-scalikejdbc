package controllers

import org.json4s.jackson.Serialization
import play.api.mvc._
import models._
import org.json4s._

class Titles extends AppController {

  def all = Action {
    val json = Serialization.write(Title.findAll)
    Ok(views.html.title.render(json));
  }

  def find(id: Long) = Action {
    Ok(encodeJson(Title.find(id)));
  }

  def create = Action(json) { req =>
    val o = req.body.extract[Title]
    val created = Title.create('name -> o.name);
    Ok(encodeJson(created))
  }

  def update(id: Long) = Action(json) { req =>
    Title.find(id) match {
      case Some(d) => {
        val o = req.body.extract[Title]
        val updated = Title.save(id, 'name -> o.name)
        Ok(encodeJson(updated))
      }
      case _ => NotFound
    }
  }

  def delete(id: Long) = Action {
    Title.find(id) match {
      case Some(d) => Title.remove(id); Ok
      case _ => NotFound
    }
  }
}
