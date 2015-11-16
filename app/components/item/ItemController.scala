package components.item

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
  * Created by Sari on 11/15/2015.
  */
trait ItemController extends Controller {
  self: ItemServiceComponent =>

  implicit val itemReads: Reads[ItemResource] = (
    (__ \ "name").read[String] and
    (__ \ "price").read[Long]
  )(ItemResource.apply _)

  implicit val itemWrites = new Writes[Item] {
    override def writes(item: Item): JsValue = {
      Json.obj(
        "id" -> item.id,
        "name" -> item.name,
        "price" -> item.price
      )
    }
  }

  def getItems = Action {
    var users = itemService.all()
    Ok(Json.toJson(users))
  }

  def createItem = Action(parse.json) {request =>
    unmarshalItemResource(request, (resource: ItemResource) => {
      val item = Item(Option.empty, resource.name, resource.price)
      val createdItem = itemService.createItem(item)
      Created(Json.toJson(createdItem))
    })
  }

  def updateItem(id: Long) = Action(parse.json) {request =>
    unmarshalItemResource(request, (resource: ItemResource) => {
      val item = Item(Option(id), resource.name, resource.price)
      itemService.updateItem(item)
      Ok
    })
  }

  def findItemById(id: Long) = Action {
    val item = itemService.tryFindById(id)
    if (item.isDefined) {
      Ok(Json.toJson(item))
    } else {
      NotFound
    }
  }

  def deleteItem(id: Long) = Action {
    itemService.delete(id)
    NoContent
  }

  private def unmarshalItemResource(request: Request[JsValue],
                                    block: (ItemResource) => Result): Result = {
    request.body.validate[ItemResource].fold(
      valid = block,
      invalid = (e => {
        val error = e.mkString
        Logger.error(error)
        BadRequest(error)
      })
    )
  }
}

case class ItemResource(val name: String, val price: Long)