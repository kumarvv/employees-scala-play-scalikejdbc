package controllers

import models._
import play.api.mvc._
import org.json4s.jackson.Serialization

class Summarize extends AppController {

  def all = Action {
    Ok(views.html.summary.render(Serialization.write(Summary.all)))
  }

}
