package components.purchase

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
  * Created by Sari on 11/15/2015.
  */
trait PurchaseController extends Controller {
  self: PurchaseServiceComponent =>

  implicit val purchaseReads = (__ \ "status").read[String]
    .map(resource => PurchaseResource(resource))

  implicit val purchaseWrites = new Writes[Purchase] {
    override def writes(purchase: Purchase): JsValue = {
      Json.obj(
        "purchaseId" -> purchase.id,
        "userId" -> purchase.userId,
        "status" -> purchase.status
      )
    }
  }

  def createPurchase(userId: Long) = Action(parse.json) { request =>
    unmarshalPurchaseResource(request, (resource: PurchaseResource) => {
      val newPurchase = Purchase(Option.empty, userId, resource.status)
      val createdPurchase = purchaseService.createPurchase(newPurchase)
      Created(Json.toJson(createdPurchase))
    })
  }

  def updatePurchase(purchaseId: Long) = Action(parse.json) {request =>
    unmarshalPurchaseResource(request, (resource: PurchaseResource) => {
      val updatedPurchase = Purchase(Option(purchaseId), 0, resource.status)
      purchaseService.updatePurchase(updatedPurchase)
      Ok
    })
  }

  def findPurchaseById(purchaseId: Long) = Action {
    val purchase = purchaseService.tryFindById(purchaseId)
    if (purchase.isDefined) {
      Ok(Json.toJson(purchase))
    } else {
      NotFound
    }
  }

  def deletePurchase(purchaseId: Long) = Action {
    purchaseService.deletePurchase(purchaseId)
    Ok
  }


  private def unmarshalPurchaseResource(request: Request[JsValue],
                                    block: (PurchaseResource) => Result): Result = {
    request.body.validate[PurchaseResource].fold(
      valid = block,
      invalid = (e => {
        val error = e.mkString
        Logger.error(error)
        BadRequest(error)
      })
    )
  }

}

case class PurchaseResource(val status: String)
