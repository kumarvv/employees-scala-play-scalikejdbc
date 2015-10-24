package controllers


import com.github.tototoshi.play2.json4s.native.Json4s
import play.api.mvc._
import org.json4s._
import org.json4s.jackson._

trait AppController extends Controller with Json4s {

  implicit val formats = DefaultFormats

  def encodeJson(src: AnyRef): JValue = {
    implicit val formats = Serialization.formats(NoTypeHints)
    Extraction.decompose(src)
  }
}
