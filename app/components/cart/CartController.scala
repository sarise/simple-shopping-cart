package components.cart

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
  * Created by Sari on 11/15/2015.
  */
trait CartController extends Controller {
  self: CartServiceComponent =>

  implicit val cartReads: Reads[CartResource] = (
    (__ \ "itemId").read[Long] and
      (__ \ "quantity").read[Int]
    )(CartResource.apply _)

  implicit val cartWrites = new Writes[Cart] {
    override def writes(cart: Cart): JsValue = {
      Json.obj(
        "purchaseId" -> cart.purchaseId,
        "itemId" -> cart.itemId,
        "quantity" -> cart.quantity
      )
    }
  }

  def getCarts(userId: Long) = Action {
    val carts = cartService.tryFindById(userId)
    Ok(Json.toJson(carts))
  }

  def addCart(userId: Long) = Action(parse.json) { request =>
    unmarshalCartResource(request, (resource: CartResource) => {
      val newCart = Cart(userId, resource.itemId, resource.quantity)
      cartService.addCart(newCart)
      Created
    })
  }

  def updateCart(userId: Long, itemId: Long) = Action(parse.json) {request =>
    unmarshalCartResource(request, (resource: CartResource) => {
      val updatedCart = Cart(userId, itemId, resource.quantity)
      cartService.updateCart(updatedCart)
      Ok
    })
  }

  def findCartById(userId: Long, itemId: Long) = Action {
    val cart = cartService.tryFindById(userId, itemId)
    if (cart.isDefined) {
      Ok(Json.toJson(cart))
    } else {
      NotFound
    }
  }

  def deleteCart(userId: Long, itemId: Long) = Action {
    cartService.deleteCart(userId, itemId)
    Ok
  }


  private def unmarshalCartResource(request: Request[JsValue],
                                    block: (CartResource) => Result): Result = {
    request.body.validate[CartResource].fold(
      valid = block,
      invalid = (e => {
        val error = e.mkString
        Logger.error(error)
        BadRequest(error)
      })
    )
  }

}

case class CartResource(val itemId: Long, val quantity: Int)
